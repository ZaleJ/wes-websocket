/*******************************************************************************
 * Copyright (C) 2021 ShangHai Quicktron Intelligent Technology Co.,Ltd All Rights Reserved.
 * 本软件为上海快仓智能科技开发研制。未经本公司正式书面同意，其他任何个人、团体 不得使用、复制、修改或发布本软件. 版权所有 翻版必究
 ******************************************************************************/
package com.quicktron.websocket.client.controller;

import com.alibaba.fastjson.JSON;
import com.kc.evo.wes.common.carry.enums.EventTopicConstants;
import com.kc.evo.wes.common.controller.PageBaseController;
import com.kc.evo.wes.common.event.EventBus;
import com.kc.evo.wes.event.basic.event.WorkbinStationChangeEvent;
import com.kc.evo.wes.event.basic.model.*;
import com.quicktron.websocket.client.listener.WarehouseEvent;
import com.quicktron.websocket.client.service.ReceiveMessageService;
import com.quicktron.websocket.enums.RcsConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

/**
 * 从RCS接受报文转入到WES的mq队列
 *
 * @author LiuBo
 * @since 2018 -12-14
 */
@Api(tags = "接受报文消息")
@RestController
@RequestMapping("/api/wes/warehouse")
@Slf4j
public class ReceiveMessageController extends PageBaseController {

    @Resource
    private ApplicationContext applicationContext;


    @ApiOperation("接受报文")
    @RequestMapping(method = RequestMethod.POST, value = "/receive-message")
    public void receiveMessageFromRcs(@RequestBody String message) {
        ReceiveMessage mes = JSON.parseObject(message, ReceiveMessage.class);
        log.info("receive message from wcs data: {}", message);
        Map<String, ReceiveMessageService> receiveMessageServiceMap = applicationContext.getBeansOfType(ReceiveMessageService.class);
        for (ReceiveMessageService receiveMessageService : receiveMessageServiceMap.values()) {
            receiveMessageService.receiveMessageFromRcs(mes);
        }
        WarehouseEvent messageEvent = new WarehouseEvent();
        messageEvent.setId(mes.getUuid());
        messageEvent.setTs(Long.parseLong(mes.getTs()));
        messageEvent.setWarehouseId(mes.getWarehouseId());
        // 如果是普通的任务作业，如出库、入库、盘点等
        if (StringUtils.equalsIgnoreCase(RcsConstants.RcsJobStateTypeName, mes.getType())) {
            messageEvent.setType("JobReportEvent");
            JobReportEventExt jpee = new JobReportEventExt();
            jpee.setBusinessType(mes.getContent().getBusinessType());
            MessageJobData data = new MessageJobData();
            //这个字段没有
            data.setAgvCode(mes.getContent().getRobotCode());
            data.setBucketCode(mes.getContent().getRackCode());
            data.setEndPoint(mes.getContent().getTargetPoint());
            data.setStartPoint(mes.getContent().getStartPoint());
            data.setWorkFaces(mes.getContent().getWorkFace());
            //对接料箱
            if (StringUtils.equalsIgnoreCase(RcsConstants.QPRcsJobName, mes.getRobotFirstType())) {
                data.setContainerCode(mes.getContent().getContainerCode());
                data.setAgvType(mes.getContent().getRobotTypeCode());
                data.setStartRackCode(mes.getContent().getStartRackCode());
                data.setStartSlotCode(mes.getContent().getStartSlotCode());
                data.setTargetMapCode(mes.getContent().getTargetMapCode());
                data.setTargetRackCode(mes.getContent().getTargetRackCode());
                data.setTargetSlotCode(mes.getContent().getTargetSlotCode());
                data.setPointCode(mes.getContent().getTargetPoint());
                data.setStationCode(mes.getContent().getStationCode());
            }
            jpee.setJobData(data);
            jpee.setJobType(mes.getContent().getOrderType());
            if (mes.getContent().getUpstreamOrderNos().size() > 1) {
                jpee.setRobotJobIds(mes.getContent().getUpstreamOrderNos());
                jpee.setRobotJobId(mes.getContent().getUpstreamOrderNos().get(0));
            } else {
                jpee.setRobotJobIds(mes.getContent().getUpstreamOrderNos());
                jpee.setRobotJobId(mes.getContent().getUpstreamOrderNos().get(0));
            }
            if ("ENTER_STATION".equals(mes.getExecuteState())) {
                jpee.setState("DONE");
            }
            if("RETURN_START".equals(mes.getExecuteState())){
                jpee.setState("LEAVE_STATION");
            }
            if("LIFT_UP_DONE".equals(mes.getExecuteState()) || "QL_LIFT_UP_DONE".equals(mes.getExecuteState())){
                jpee.setState("LIFT_UP_DONE");
            }
            if("RACK_MOVE_START".equals(mes.getExecuteState()) || "QL_CONTAINER_MOVE_START".equals(mes.getExecuteState())){
                jpee.setState("MOVE_BEGIN");
            }
            if("G2P_RACK_MOVE".equals(mes.getContent().getOrderType())){
                jpee.setJobType("BUCKET_MOVE");
            }
            jpee.setJobId(mes.getContent().getOrderNo());
            jpee.setWarehouseId(mes.getWarehouseId());
            messageEvent.setEvent(jpee);
            try {
                EventBus.getEventBus(EventTopicConstants.si2interface).post("JobReportEvent",
                        messageEvent);

            } catch (Exception e) {
                log.error("receive message from wcs" +e.getMessage(),e);
            }
        }
        // 容器变化，如入场、故障出场等
        else if (StringUtils.equalsIgnoreCase(RcsConstants.EVENT_TYPE_CONTAINER_UPDATE, mes.getType())) {
            messageEvent.setType("WorkbinStationChangeEvent");
            WorkbinStationChangeEvent containerChangeEvent = new WorkbinStationChangeEvent();
            containerChangeEvent.setContainerCode(mes.getContent().getContainerCode());
            containerChangeEvent.setFrom(mes.getContent().getSourceSlotCode());
            containerChangeEvent.setTo(mes.getContent().getCurrentSlotCode());
            containerChangeEvent.setType(WorkbinStationChangeEvent.Type.valueOf(mes.getContent().getType()));
            containerChangeEvent.setReason(mes.getContent().getReason());
            messageEvent.setEvent(containerChangeEvent);
            try {
                EventBus.getEventBus(EventTopicConstants.si2interface).post(mes.getContent().getContainerCode(),"WorkbinStationChangeEvent",
                        messageEvent);
            } catch (Exception e) {
                log.error("receive message from wcs" +e.getMessage(),e);
            }
        }else if (StringUtils.equalsIgnoreCase(RcsConstants.EVENT_TYPE_ROBOT_UPDATE, mes.getType())){
            messageEvent.setType(RcsConstants.EVENT_TYPE_ROBOT_UPDATE);
            ChangeRobotReportEvent changeRobotReportEvent = new ChangeRobotReportEvent();
            changeRobotReportEvent.setRobotCode(mes.getContent().getRobotCode());
            changeRobotReportEvent.setBusinessType(mes.getContent().getBusinessType());
            changeRobotReportEvent.setOldRobotCode(mes.getContent().getOldRobotCode());
            changeRobotReportEvent.setJobSn(mes.getContent().getJobSn());
            changeRobotReportEvent.setOrderNo(mes.getContent().getOrderNo());
            changeRobotReportEvent.setUpstreamOrderNo(mes.getContent().getUpstreamOrderNo());
            changeRobotReportEvent.setWarehouseId(mes.getContent().getWarehouseId());
            messageEvent.setEvent(changeRobotReportEvent);
            try {
                EventBus.getEventBus(EventTopicConstants.si2interface).post("ChangeRobotReportEvent",
                        messageEvent);
            } catch (Exception e) {
                log.error("receive message from wcs" + e.getMessage(),e);
            }
        }

    }

    public static ReceiveMessage generateRandomEntity() {
        ReceiveMessage entity = new ReceiveMessage();
        Random random = new Random();
        // serialVersionUID 是静态常量，不需要生成
        entity.setWarehouseId(random.nextLong());
        entity.setUuid(java.util.UUID.randomUUID().toString());
        entity.setTs(Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        entity.setType("type1");
        entity.setRobotFirstType("TYPE2");
        entity.setExecuteState("PENDING");
        entity.setContent(new MessageDetail());
        return entity;
    }
}

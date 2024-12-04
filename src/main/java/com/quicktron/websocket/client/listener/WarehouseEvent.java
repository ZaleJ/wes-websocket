/*******************************************************************************
 * Copyright (C) 2021 ShangHai Quicktron Intelligent Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为上海快仓智能科技开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 * 版权所有 翻版必究
 ******************************************************************************/

package com.quicktron.websocket.client.listener;

import com.kc.evo.wes.common.event.IWesEvent;
import lombok.Data;

/**
 * wcs丢出来的消息的映射类，方便wes mq框架使用
 */
@Data
public class WarehouseEvent implements IWesEvent {
    /**
     * 事件Id,唯一标识
     */
    private String id;

    /**
     * 仓库编码
     */
    private Long warehouseId;

    /**
     * 库区编码
     */
    private String zoneCode;

    /**
     * 事件时间戳
     */
    private Long ts;

    /**
     * 事件类型
     */
    private String type;

    /**
     * 具体事件对象
     */
    private Object event;
}

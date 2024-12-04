package com.quicktron.websocket.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicktron.websocket.client.service.AgvStatusWebSocketServer;
import com.quicktron.websocket.client.service.UserWebSocketServer;
import com.quicktron.websocket.dto.AGVEntity;
import com.quicktron.websocket.dto.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/websocket")
public class CarMoveController {

    @Autowired
    private UserWebSocketServer userWebSocketServer;
    @Autowired
    private AgvStatusWebSocketServer agvStatusWebSocketServer;

    @RequestMapping(value = "/pushAgvStatus", method = RequestMethod.POST)
    public void updateCarLocation(@RequestBody AGVEntity agvEntity) {
        String stationCode = agvEntity.getStationCode();
        String targetURL = agvEntity.getTargetURL();
        Set<WebSocketSession> sessions = agvStatusWebSocketServer.getSessions();
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String agvJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(agvEntity);
                    if (session.getUri().toString().contains(agvEntity.getStationCode())) {
                        session.sendMessage(new TextMessage(agvJson));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "/updateStationStatus", method = RequestMethod.POST)
    public void updateStationStatus(@RequestBody AGVEntity agvEntity) {
        String stationCode = agvEntity.getStationCode();
        String targetURL = agvEntity.getTargetURL();
        Set<WebSocketSession> sessions = agvStatusWebSocketServer.getSessions();
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String agvJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(agvEntity);
                    if (session.getUri().toString().contains(agvEntity.getStationCode())) {
                        session.sendMessage(new TextMessage(agvJson));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    @Scheduled(fixedDelay = 10000)
//    public void sendMessageToAll() {
//        Set<WebSocketSession> sessions = agvStatusWebSocketServer.getSessions();
//        for (WebSocketSession session : sessions) {
//            if (session.isOpen()) {
//                try {
//                    List<AGVEntity> agvList = new ArrayList<>();
//                    for (int i = 0; i < 1; i++) {
//                        AGVEntity agvEntity = AGVEntity.getRandom();
//                        agvList.add(agvEntity);
//                    }
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String agvJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(agvList);
//                    session.sendMessage(new TextMessage(agvJson));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    @Scheduled(fixedDelay = 5000)
//    public void sendAgvMessageToAll() {
//        Set<WebSocketSession> sessions = agvStatusWebSocketServer.getSessions();
//        for (WebSocketSession session : sessions) {
//            if (session.isOpen()) {
//                try {
//                    List<AGVEntity> agvList = new ArrayList<>();
//                    for (int i = 0; i < 1; i++) {
//                        AGVEntity agvEntity = AGVEntity.getRandom();
//                        agvList.add(agvEntity);
//                    }
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String agvJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(agvList);
//                    session.sendMessage(new TextMessage(agvJson));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @Scheduled(fixedDelay = 5000)
//    public void sendMessageToStation2() {
//        Set<WebSocketSession> sessions = agvStatusWebSocketServer.getSessions();
//        for (WebSocketSession session : sessions) {
//            if (session.isOpen()) {
//                try {
//                    List<AGVEntity> agvList = new ArrayList<>();
//                    for (int i = 0; i < 1; i++) {
//                        AGVEntity agvEntity = AGVEntity.getRandom();
//                        agvList.add(agvEntity);
//                    }
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String agvJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(agvList);
//                    session.sendMessage(new TextMessage(agvJson));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}

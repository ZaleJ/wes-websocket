package com.quicktron.websocket.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicktron.websocket.client.service.AgvStatusWebSocketServer;
import com.quicktron.websocket.client.service.UserWebSocketServer;
import com.quicktron.websocket.dto.AGVEntity;
import com.quicktron.websocket.dto.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    public void updateCarLocation() {
        Car car1 = new Car("car1", (new BigDecimal(Math.random() * 1000 + "")).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
        Car car2 = new Car("car2", (new BigDecimal(Math.random() * 1000 + "")).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
        Car car3 = new Car("car3", (new BigDecimal(Math.random() * 1000 + "")).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
        Car car4 = new Car("car4", (new BigDecimal(Math.random() * 1000 + "")).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
        ArrayList<Car> carList = new ArrayList<>();
        carList.add(car1);
        carList.add(car2);
        carList.add(car3);
        carList.add(car4);

        try {

        } catch (Exception e) {
            log.error("car update failed, {}", e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void sendMessageToAll() {
        Set<WebSocketSession> sessions = userWebSocketServer.getSessions();
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    String message = "this is a new message to " + session.getId();
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void sendAgvMessageToAll() {
        Set<WebSocketSession> sessions = agvStatusWebSocketServer.getSessions();
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    List<AGVEntity> agvList = new ArrayList<>();
                    for (int i = 0; i < 100; i++) {
                        AGVEntity agvEntity = AGVEntity.getRandom();
                        agvList.add(agvEntity);
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    String agvJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(agvList);
                    session.sendMessage(new TextMessage(agvJson));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

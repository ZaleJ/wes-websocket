package com.quicktron.websocket.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicktron.websocket.dto.AGVEntity;
import org.apache.tomcat.websocket.server.UriTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.DeploymentException;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/agvStatus//{carId}")
public class AgvStatusWebSocketServer extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("agvStatus Connection opened: " + session.getId());
        sessions.add(session);
        // 获取stationCode
        String stationCode = getStationCode();
        // 发送初始化消息
        String carId = getCarId(session);
        TextMessage textMessage = new TextMessage("this is agv init data for station: " + stationCode + " carId: "+carId);
        AGVEntity agvEntity = AGVEntity.getRandom();

        ObjectMapper objectMapper = new ObjectMapper();
        String agvJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(agvEntity);

        session.sendMessage(textMessage);
        session.sendMessage(new TextMessage(agvJson));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage message) throws Exception {
        // 处理接收到的文本消息
        String payload = message.getPayload().toString();
        System.out.println("接收到消息：" + payload);

        // 发送响应消息
        session.sendMessage(new TextMessage("Hello, client!"));
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        System.out.println("agvStatus handleTransportError: " + webSocketSession.getId());
        System.out.println("agvStatus handleTransportError: " + throwable.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("agvStatus Connection closed: " + webSocketSession.getId());
        sessions.remove(webSocketSession);
    }

    public Set<WebSocketSession> getSessions() {
        return sessions;
    }

    private String getStationCode() {
        return "station101";
    }

    private String getCarId(WebSocketSession session) throws DeploymentException {
        // 定义UriTemplate来匹配WebSocket连接的URI
        UriTemplate uriTemplate = new UriTemplate("/websocket/agvStatus/{carId}");

        // 获取WebSocketSession的URI
        String uri = session.getUri().toString();

        // 解析URI以获取路径参数
        Map<String, String> variables = uriTemplate.match(uriTemplate);
        String carId = variables.get("carId");
        return carId;
    }
}
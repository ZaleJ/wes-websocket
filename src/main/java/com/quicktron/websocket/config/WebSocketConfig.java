package com.quicktron.websocket.config;

import com.quicktron.websocket.client.service.AgvStatusWebSocketServer;
import com.quicktron.websocket.client.service.UserWebSocketServer;
import com.quicktron.websocket.enums.WebSocketEndpintEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new UserWebSocketServer(), WebSocketEndpintEnum.WEBSOCKET.getValue()).setAllowedOrigins("*");
        registry.addHandler(new AgvStatusWebSocketServer(), WebSocketEndpintEnum.AGV_STATUS.getValue()).setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        container.setMaxSessionIdleTimeout(300000L);
        return container;
    }
}
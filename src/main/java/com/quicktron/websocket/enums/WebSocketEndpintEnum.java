package com.quicktron.websocket.enums;

public enum WebSocketEndpintEnum {
    WEBSOCKET("/websocket"),
    AGV_STATUS("/websocket/agvStatus/**");

    private String value;

    WebSocketEndpintEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

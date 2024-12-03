package com.quicktron.websocket.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AGVEntity {
    private String robotId;
    private BigDecimal distanceToWorkstation;
    private String agvStatus;
    private String transportObjectCode;
    private String transportObjectType;
    private String location;


}
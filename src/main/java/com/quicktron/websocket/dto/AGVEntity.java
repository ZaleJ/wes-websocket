package com.quicktron.websocket.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Data
public class AGVEntity {
    private String robotId;
    private BigDecimal distanceToWorkstation;
    private String agvStatus;
    private String transportObjectCode;
    private String transportObjectType;
    private String location;


    public static AGVEntity getRandom() {
        Random random = new Random();

        AGVEntity agvEntity = new AGVEntity();
        agvEntity.setRobotId("ROBOT-" + random.nextInt(1000)); // Random string like ROBOT-42

        // Generate a random BigDecimal value between 0.0 and 100.0 with 2 decimal places
        agvEntity.setDistanceToWorkstation(new BigDecimal(random.nextDouble() * 100).setScale(2, RoundingMode.HALF_UP));

        // Predefined status list
        String[] statuses = {"IDLE", "MOVING", "LOADING", "UNLOADING"};
        agvEntity.setAgvStatus(statuses[random.nextInt(statuses.length)]);

        agvEntity.setTransportObjectCode("TOC-" + random.nextInt(1000)); // Random string like TOC-42

        // Predefined object type list
        String[] objectTypes = {"BOX", "PALLET", "CRATE"};
        agvEntity.setTransportObjectType(objectTypes[random.nextInt(objectTypes.length)]);

        agvEntity.setLocation("LOCATION-" + random.nextInt(1000)); // Random string like LOCATION-42

        return agvEntity;
    }

}
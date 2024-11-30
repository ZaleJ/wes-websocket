package com.quicktron.websocket.client.controller;

import com.quicktron.websocket.client.service.WebSocketSender;
import com.quicktron.websocket.dto.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@Slf4j
@RestController
public class CarMoveController {
    @Autowired
    private WebSocketSender webSocketSender;

    @RequestMapping(value = "/car", method = RequestMethod.POST)
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
            webSocketSender.carUpdateSender(carList);
        } catch (Exception e) {
            log.error("car update failed, {}", e.getMessage());
        }
    }
}

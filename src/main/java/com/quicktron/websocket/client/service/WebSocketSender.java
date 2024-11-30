package com.quicktron.websocket.client.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quicktron.websocket.dto.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

/**
 * Reference: https://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html
 */
@Service
@Slf4j
public class WebSocketSender {

	@Autowired
	private SimpMessagingTemplate messageTemplate;

	public void carUpdateSender(List<Car> carList) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		carList.forEach(car -> {
            String jsonInString = null;
            try {
                jsonInString = mapper.writeValueAsString(Collections.singletonList(car));
            } catch (JsonProcessingException e) {
                log.error("Error in converting object to json", e);
            }
            this.messageTemplate.convertAndSend("/location/"+car.getName(), jsonInString);
		});
	}
    
    @Scheduled(fixedDelay=10000)
    public void car1UpdateSender() throws Exception {
    	double val1 = (new BigDecimal(Math.random() * 1000 + "")).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

    	Car stock1 = new Car("car1", val1);

    	List<Car> list = new ArrayList<>();
    	list.add(stock1);

    	// convert from object to JSON
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonInString = mapper.writeValueAsString(list);
    	
    	this.messageTemplate.convertAndSend("/location/car1", jsonInString);
    }

    @Scheduled(fixedDelay=5000)    
    public void car2UpdateSender() throws Exception {
    	double val1 = (new BigDecimal(Math.random() * 1000 + "")).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

    	Car stock1 = new Car("car2", val1);

    	List<Car> list = new ArrayList<>();
    	list.add(stock1);

    	// convert from object to JSON
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonInString = mapper.writeValueAsString(list);
    	
    	this.messageTemplate.convertAndSend("/location/car2", jsonInString);
    }
}

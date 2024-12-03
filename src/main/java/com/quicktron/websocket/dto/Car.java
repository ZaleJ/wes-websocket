package com.quicktron.websocket.dto;

public class Car {

	private String name;

	private Double power;

	public Car(String name, Double power) {
		this.name = name;
		this.power = power;
	}

	public String getName() {
		return name;
	}

	public Double getPower() {
		return power;
	}

}

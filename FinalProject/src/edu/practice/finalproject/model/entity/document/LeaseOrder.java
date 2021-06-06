package edu.practice.finalproject.model.entity.document;

import java.time.LocalDateTime;

import edu.practice.finalproject.controller.admin.Client;
import edu.practice.finalproject.model.entity.Entity;
import edu.practice.finalproject.model.entity.domain.Car;

public class LeaseOrder extends Entity {
	private Client client;
	private Car car;
	private String passport;
	private boolean driverPresent;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	public Client getClient() { return client;}
	public void setClient(Client client) { this.client = client;}
	
	public Car getCar() { return car;}
	public void setCar(Car car) { this.car= car;}

	public String getPassport() { return passport;}
	public void setPassport(String passport) { this.passport = passport;}

	public boolean getDriverPresent() { return driverPresent;}
	public void setDriverPresent(boolean driverPresent) { this.driverPresent = driverPresent;}

	public LocalDateTime getStartTime() { return startTime;}
	public void setStartTime(LocalDateTime startTime) { this.startTime = startTime;}

	public LocalDateTime getEndTime() { return endTime;}
	public void setEndTime(LocalDateTime endTime) { this.endTime = endTime;}
}

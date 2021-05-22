package edu.practice.finalproject.model.entity.document;

import java.time.LocalDateTime;

import edu.practice.finalproject.controller.admin.Client;
import edu.practice.finalproject.model.entity.Entity;

public class Order extends Entity {
	private Client client;
	private String passport;
	private boolean driverPresent;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	public Client getClient() { return client;}
	public void setClient(Client client) { this.client = client;}

	public String getPassport() { return passport;}
	public void setPassport(String passport) { this.passport = passport;}

	public boolean getDriverPresent() { return driverPresent;}
	public void setDriverPresent(boolean driverPresent) { this.driverPresent = driverPresent;}

	public LocalDateTime getStartTime() { return startTime;}
	public void setStartTime(LocalDateTime startTime) { this.startTime = startTime;}

	public LocalDateTime getEndTime() { return endTime;}
	public void setEndTime(LocalDateTime endTime) { this.endTime = endTime;}
}

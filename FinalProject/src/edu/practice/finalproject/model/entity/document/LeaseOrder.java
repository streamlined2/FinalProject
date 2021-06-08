package edu.practice.finalproject.model.entity.document;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import edu.practice.finalproject.controller.admin.Client;
import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;
import edu.practice.finalproject.model.entity.domain.Car;

public class LeaseOrder extends NaturalKeyEntity {
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
	
	@Override
	public List<Method> keyGetters() {
		try {
			return List.of(
					LeaseOrder.class.getMethod("getClient"),
					LeaseOrder.class.getMethod("getCar"),
					LeaseOrder.class.getMethod("getStartTime")
			);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new EntityException(e);
		}
	}
}

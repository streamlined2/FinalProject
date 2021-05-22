package edu.practice.finalproject.model.entity.document;

import java.time.LocalDateTime;

import edu.practice.finalproject.controller.admin.Manager;
import edu.practice.finalproject.model.entity.Entity;

public class Approvement extends Entity {
	private final Order order;
	private final Manager manager;
	private final LocalDateTime signTime;
	
	public Approvement(final Order order, final Manager manager, final LocalDateTime signTime) {
		this.order = order;
		this.manager = manager;
		this.signTime = signTime;
	}
}

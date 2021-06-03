package edu.practice.finalproject.model.entity.document;

import java.math.BigInteger;
import java.time.LocalDateTime;

import edu.practice.finalproject.model.entity.Entity;

public class Payment extends Entity {
	
	private final LeaseOrder order;
	private final BigInteger number;
	private final LocalDateTime payTime;

	public Payment(LeaseOrder order, BigInteger number, LocalDateTime payTime) {
		this.order = order;
		this.number = number;
		this.payTime = payTime;
	}

}

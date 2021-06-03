package edu.practice.finalproject.model.entity.document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import edu.practice.finalproject.controller.admin.Manager;

public class LeaseInvoice extends Invoice {
	
	private final LeaseOrder order;
	private final Manager manager;
	private final LocalDateTime signTime;
	private final Account account;
	private final BigDecimal sum;
	
	public LeaseInvoice(LeaseOrder order, Manager manager, LocalDateTime signTime, Account account, BigDecimal sum) {
		this.order = order;
		this.manager = manager;
		this.signTime = signTime;
		this.account = account;
		this.sum = sum;
	}

}

package edu.practice.finalproject.model.entity.document;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import edu.practice.finalproject.controller.admin.Manager;
import edu.practice.finalproject.model.analysis.EntityException;

public class LeaseInvoice extends Invoice {
	
	private LeaseOrder leaseOrder;
	
	public LeaseInvoice() {}
	
	public LeaseInvoice(LeaseOrder leaseOrder, Manager manager, LocalDateTime signTime, Account account, BigDecimal sum) {
		this.leaseOrder = leaseOrder;
		this.manager = manager;
		this.signTime = signTime;
		this.account = account;
		this.sum = sum;
	}

	public LeaseOrder getLeaseOrder() { return leaseOrder;}
	public void setLeaseOrder(LeaseOrder leaseOrder) { this.leaseOrder = leaseOrder;}

	@Override
	public List<Method> keyGetters() {
		try {
			return List.of(
					LeaseInvoice.class.getMethod("getLeaseOrder")
			);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new EntityException(e);
		}
	}

}

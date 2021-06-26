package edu.practice.finalproject.model.entity.document;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.entity.userrole.Manager;

/**
 * Entity that represents lease invoice document
 * @author Serhii Pylypenko
 *
 */
public class LeaseInvoice extends Invoice {
	
	private LeaseOrder leaseOrder;
	
	public LeaseInvoice() {}
	
	public LeaseInvoice(LeaseOrder leaseOrder, Manager manager, LocalDateTime signTime, String account, BigDecimal sum) {
		this.leaseOrder = leaseOrder;
		this.manager = manager;
		this.signTime = signTime;
		this.account = account;
		this.sum = sum;
	}

	public LeaseOrder getLeaseOrder() { return leaseOrder;}
	public void setLeaseOrder(LeaseOrder leaseOrder) { this.leaseOrder = leaseOrder;}

	@Override
	public String toString() {
		return new StringJoiner(",","[","]").
				add(leaseOrder.toString()).add(manager.toString()).add(signTime.toString()).
				toString();
	}
	
	@Override
	public List<Method> keyGetters() {
		try {
			return List.of(
					LeaseInvoice.class.getMethod("getLeaseOrder"),
					LeaseInvoice.class.getMethod("getManager"),
					LeaseInvoice.class.getMethod("getSignTime")
			);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new EntityException(e);
		}
	}
}

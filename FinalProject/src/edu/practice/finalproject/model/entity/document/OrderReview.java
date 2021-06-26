package edu.practice.finalproject.model.entity.document;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;
import edu.practice.finalproject.model.entity.userrole.Manager;

/**
 * Entity that represents order review document
 * @author Serhii Pylypenko
 *
 */
public class OrderReview extends NaturalKeyEntity {
	public enum OrderStatus { APPROVED, REJECTED}
	
	private LeaseOrder leaseOrder;
	private Manager manager;
	private LocalDateTime reviewTime;
	private OrderStatus orderStatus;
	private String reasonNote;
	
	public LeaseOrder getLeaseOrder() { return leaseOrder;}
	public void setLeaseOrder(LeaseOrder leaseOrder) { this.leaseOrder = leaseOrder;}
	
	public Manager getManager() { return manager;}
	public void setManager(Manager manager) { this.manager = manager;}
	
	public LocalDateTime getReviewTime() { return reviewTime;}
	public void setReviewTime(final LocalDateTime reviewTime) { this.reviewTime = reviewTime;}
	
	public OrderStatus getOrderStatus() { return orderStatus;}
	public void setOrderStatus(final OrderStatus orderStatus) { this.orderStatus = orderStatus;}
	
	public String getReasonNote() { return reasonNote;}
	public void setReasonNote(final String reason) { this.reasonNote = reason;}

	@Override
	public List<Method> keyGetters() {
		try {
			return List.of(
					OrderReview.class.getMethod("getLeaseOrder")
			);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new EntityException(e);
		}
	}
}

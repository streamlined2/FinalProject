package edu.practice.finalproject.model.entity.document;

import java.time.LocalDateTime;

import edu.practice.finalproject.controller.admin.Manager;
import edu.practice.finalproject.model.entity.Entity;

public class OrderReview extends Entity {
	public enum OrderStatus { APPROVED, REJECTED}
	
	private LeaseOrder leaseOrder;
	private Manager manager;
	private LocalDateTime reviewTime;
	private OrderStatus orderStatus;
	private String reasonNote;
	
	public LeaseOrder getLeaseOrder() { return leaseOrder;}
	public void setLeaseOrder() { this.leaseOrder = leaseOrder;}
	
	public Manager getManager() { return manager;}
	public void setManager() { this.manager = manager;}
	
	public LocalDateTime getReviewTime() { return reviewTime;}
	public void setReviewTime(final LocalDateTime reviewTime) { this.reviewTime = reviewTime;}
	
	public OrderStatus getOrderStatus() { return orderStatus;}
	public void setOrderStatus(final OrderStatus orderStatus) { this.orderStatus = orderStatus;}
	
	public String getReasonNote() { return reasonNote;}
	public void setReasonNote(final String reason) { this.reasonNote = reason;}
}

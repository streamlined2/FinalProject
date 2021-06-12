package edu.practice.finalproject.view.action;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.admin.Manager;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.document.LeaseOrder;
import edu.practice.finalproject.model.entity.document.OrderReview;
import edu.practice.finalproject.model.entity.document.OrderReview.OrderStatus;

public class AcceptOrderAction extends ManagerAction {

	public AcceptOrderAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final Manager manager = (Manager)FCServlet.getUser(req); 
		final LeaseOrder leaseOrder = (LeaseOrder)FCServlet.getAttribute(req, Names.SELECTED_ORDER_ATTRIBUTE);

		final OrderReview orderReview = Inspector.createEntity(OrderReview.class);
		orderReview.setLeaseOrder(leaseOrder);
		orderReview.setManager(manager);
		orderReview.setReviewTime(LocalDateTime.now());
		orderReview.setOrderStatus(OrderStatus.APPROVED);
		entityManager.persist(leaseOrder);

		FCServlet.setAttribute(req, Names.ORDER_REVIEW_ATTRIBUTE, orderReview);
		return true;
	}
}

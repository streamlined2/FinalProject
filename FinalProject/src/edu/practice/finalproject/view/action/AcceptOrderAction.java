package edu.practice.finalproject.view.action;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.DataAccessException;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.document.LeaseOrder;
import edu.practice.finalproject.model.entity.document.OrderReview;
import edu.practice.finalproject.model.entity.document.OrderReview.OrderStatus;
import edu.practice.finalproject.model.entity.userrole.Manager;

public class AcceptOrderAction extends ManagerAction {

	private static final Logger logger = LogManager.getLogger();
	private static final String CANT_SAVE_ORDER_MSG = "Cannot save order";

	public AcceptOrderAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final Manager manager = (Manager)FCServlet.getUser(req); 
		final LeaseOrder leaseOrder = (LeaseOrder)FCServlet.getAttribute(req, Names.SELECTED_ORDER_ATTRIBUTE);

		try {
			final OrderReview orderReview = Inspector.createEntity(OrderReview.class);
			orderReview.setLeaseOrder(leaseOrder);
			orderReview.setManager(manager);
			orderReview.setReviewTime(LocalDateTime.now());
			orderReview.setOrderStatus(OrderStatus.APPROVED);
			entityManager.persist(orderReview);

			FCServlet.setAttribute(req, Names.ORDER_REVIEW_ATTRIBUTE, orderReview);
			return true;
		} catch(EntityException | DataAccessException e) {
			logger.error(CANT_SAVE_ORDER_MSG, e);
			FCServlet.setError(req, CANT_SAVE_ORDER_MSG);
			return false;
		}
	}
}

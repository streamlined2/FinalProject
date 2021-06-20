package edu.practice.finalproject.view.action;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.document.CarReview;
import edu.practice.finalproject.model.entity.document.LeaseOrder;
import edu.practice.finalproject.model.entity.document.CarReview.CarCondition;
import edu.practice.finalproject.model.entity.userrole.Manager;

public class CarNeedsMaintenanceAction extends ManagerAction {

	public CarNeedsMaintenanceAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final Manager manager = (Manager)FCServlet.getUser(req); 
		final LeaseOrder leaseOrder = (LeaseOrder)FCServlet.getAttribute(req, Names.SELECTED_ORDER_ATTRIBUTE);
		
		final CarReview carReview = Inspector.createEntity(CarReview.class);
		carReview.setLeaseOrder(leaseOrder);
		carReview.setManager(manager);
		carReview.setReviewTime(LocalDateTime.now());
		carReview.setCarCondition(CarCondition.NEEDS_MAINTENANCE);
		entityManager.persist(carReview);

		FCServlet.setAttribute(req, Names.CAR_REVIEW_ATTRIBUTE, carReview);
		return true;
	}

}

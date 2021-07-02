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
import edu.practice.finalproject.model.entity.document.CarReview;
import edu.practice.finalproject.model.entity.document.CarReview.CarCondition;
import edu.practice.finalproject.model.entity.userrole.Manager;
import edu.practice.finalproject.model.entity.document.LeaseOrder;

public class CarInPerfectConditionAction extends ManagerAction {
	
	private static final Logger logger = LogManager.getLogger();

	private static final String CAR_REVIEWED_AS_BEING_IN_PERFECT_CONDITION = "car.in.perfect.condition.action.perfect-condition";
	private static final String CANT_SAVE_CAR_REVIEW_MSG = "car.in.perfect.condition.action.cant-save-car-review";

	public CarInPerfectConditionAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final Manager manager = (Manager)FCServlet.getUser(req); 
		final LeaseOrder leaseOrder = (LeaseOrder)FCServlet.getAttribute(req, Names.SELECTED_ORDER_ATTRIBUTE);
		
		try {
			final CarReview carReview = Inspector.createEntity(CarReview.class);
			carReview.setLeaseOrder(leaseOrder);
			carReview.setManager(manager);
			carReview.setReviewTime(LocalDateTime.now());
			carReview.setCarCondition(CarCondition.PERFECT);
			entityManager.persist(carReview);

			FCServlet.setAttribute(req, Names.CAR_REVIEW_ATTRIBUTE, carReview);
			FCServlet.setMessage(req, String.format(FCServlet.localize(CAR_REVIEWED_AS_BEING_IN_PERFECT_CONDITION), carReview.getLeaseOrder().getCar()));
			return true;
		} catch(EntityException | DataAccessException e) {
			logger.error(CANT_SAVE_CAR_REVIEW_MSG, e);
			FCServlet.setError(req, FCServlet.localize(CANT_SAVE_CAR_REVIEW_MSG));
			return false;
		}
	}

}

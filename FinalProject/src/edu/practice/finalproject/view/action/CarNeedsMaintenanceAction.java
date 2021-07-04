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
import edu.practice.finalproject.model.entity.document.LeaseOrder;
import edu.practice.finalproject.model.entity.document.CarReview.CarCondition;
import edu.practice.finalproject.model.entity.userrole.Manager;
import edu.practice.finalproject.utilities.Utils;

public class CarNeedsMaintenanceAction extends ManagerAction {

	private static final Logger logger = LogManager.getLogger();
	private static final String CANT_SAVE_CAR_REVIEW_MSG = "car.needs.maintenance.action.cant-save-car-review";

	public CarNeedsMaintenanceAction(String name) {
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
			carReview.setCarCondition(CarCondition.NEEDS_MAINTENANCE);
			entityManager.persist(carReview);

			FCServlet.setAttribute(req, Names.CAR_REVIEW_ATTRIBUTE, carReview);
			return true;
		} catch(EntityException | DataAccessException e) {
			logger.error(Utils.message(CANT_SAVE_CAR_REVIEW_MSG), e);
			FCServlet.setError(req, CANT_SAVE_CAR_REVIEW_MSG);
			return false;
		}
	}

}

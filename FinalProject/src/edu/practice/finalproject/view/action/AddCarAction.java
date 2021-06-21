package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car;

public class AddCarAction extends AdminAction {
	
	private static final Logger logger = LogManager.getLogger();
	private static final String CANT_CREATE_ENTITY_MSG = "Cannot create entity";

	public AddCarAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		try {
			final Car car = Inspector.createEntity(Car.class);
			FCServlet.setAttribute(req, Names.SELECTED_CAR_ATTRIBUTE, car);
			FCServlet.setAttribute(req, Names.NEW_CAR_ATTRIBUTE, Boolean.TRUE);
			return true;
		} catch(EntityException e) {
			logger.error(CANT_CREATE_ENTITY_MSG, e);
			FCServlet.setError(req, CANT_CREATE_ENTITY_MSG);
			return false;
		}
	}

}

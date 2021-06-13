package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car;

public class AddCarAction extends AdminAction {
	
	public AddCarAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final Car car = Inspector.createEntity(Car.class);
		FCServlet.setAttribute(req, Names.SELECTED_CAR_ATTRIBUTE, car);
		FCServlet.setAttribute(req, Names.NEW_CAR_ATTRIBUTE, Boolean.TRUE);
		return true;
	}

}

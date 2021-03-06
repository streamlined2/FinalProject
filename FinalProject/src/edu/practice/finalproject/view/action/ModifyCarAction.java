package edu.practice.finalproject.view.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car;

public class ModifyCarAction extends AdminAction {

	private static final String INCORRECT_CAR_MSG = "modify.car.action.incorrect-car";

	public ModifyCarAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final List<Car> queryData=(List<Car>)FCServlet.getAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE);
		final int number=Integer.parseInt(FCServlet.getParameterValue(req, Names.CAR_NUMBER_PARAMETER));
		if(number>=0 && number<queryData.size()) {
			final Car car = queryData.get(number);
			FCServlet.setAttribute(req, Names.SELECTED_CAR_ATTRIBUTE, car);
			FCServlet.setAttribute(req, Names.NEW_CAR_ATTRIBUTE, Boolean.FALSE);
			return true;
		}
		FCServlet.setError(req, INCORRECT_CAR_MSG);
		return false;
	}

}

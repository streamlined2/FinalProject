package edu.practice.finalproject.view.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car;

public class DropCarAction extends AdminAction {

	private static final String CAR_DROPPED_MSG = "Car %s deleted successfully";
	private static final String INCORRECT_CAR_MSG = "Incorrect car number";
	private static final String CANT_DROP_CAR_MSG = "Can't delete car";

	public DropCarAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final List<Car> queryData=(List<Car>)FCServlet.getAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE);
		final int number=Integer.parseInt(FCServlet.getParameterValue(req, Names.CAR_NUMBER_PARAMETER));
		if(number>=0 && number<queryData.size()) {
			final Car car = queryData.get(number);
			if(entityManager.remove(car)) {
				FCServlet.setMessage(req, String.format(CAR_DROPPED_MSG,car.toString()));
				return true;
			} else {
				FCServlet.setError(req, CANT_DROP_CAR_MSG);
				return false;
			}
		}
		FCServlet.setError(req, INCORRECT_CAR_MSG);
		return false;
	}
}

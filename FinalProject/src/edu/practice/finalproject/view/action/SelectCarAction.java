package edu.practice.finalproject.view.action;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car;

public class SelectCarAction extends ClientAction {
	
	public static final String INCORRECT_CAR_MSG = "Incorrect car selected!";

	public SelectCarAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) throws ServletException {
		final List<Car> queryData=(List<Car>)FCServlet.getAttribute(req, Names.PAGE_CARS_ATTRIBUTE);
		final int number=Integer.parseInt(FCServlet.getParameterValue(req,Names.CAR_NUMBER_PARAMETER));
		if(number>=0 && number<queryData.size()) {
			FCServlet.setAttribute(req, Names.SELECTED_CAR_ATTRIBUTE, queryData.get(number));
			return true;
		}
		FCServlet.setError(req, INCORRECT_CAR_MSG);
		return false;
	}

}

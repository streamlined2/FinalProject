package edu.practice.finalproject.view.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.DataAccessException;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car;

public class DropCarAction extends AdminAction {

	private static final Logger logger = LogManager.getLogger();

	private static final String CAR_DROPPED_MSG = "drop.car.action.car-dropeed";
	private static final String INCORRECT_CAR_MSG = "drop.car.action.incorrect-car";
	private static final String CANT_DROP_CAR_MSG = "drop.car.action.cant-delete-car";

	public DropCarAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final List<Car> queryData=(List<Car>)FCServlet.getAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE);
		final int number=Integer.parseInt(FCServlet.getParameterValue(req, Names.CAR_NUMBER_PARAMETER));
		if(number>=0 && number<queryData.size()) {
			final Car car = queryData.get(number);
			try {
				if(entityManager.remove(car)) {
					FCServlet.setMessage(req, CAR_DROPPED_MSG,car.toString());
					return true;
				} else {
					FCServlet.setError(req, CANT_DROP_CAR_MSG);
					return false;
				}
			} catch(DataAccessException e) {
				logger.error(CANT_DROP_CAR_MSG, e);
				FCServlet.setError(req, CANT_DROP_CAR_MSG);
				return false;
			}
		}
		FCServlet.setError(req, INCORRECT_CAR_MSG);
		return false;
	}
}

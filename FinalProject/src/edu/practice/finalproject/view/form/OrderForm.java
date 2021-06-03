package edu.practice.finalproject.view.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.view.action.Action;

public class OrderForm extends Form {

	public OrderForm(String name) {
		super(name);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.BACK_ACTION;
	}

	@Override
	public void init(HttpServletRequest req, EntityManager entityManager) {
		super.init(req, entityManager);
		req.setAttribute(Names.PASSPORT_PATTERN_ATTRIBUTE, Names.PASSPORT_PATTERN);
	}

	@Override
	public Action getAction(Map<String, String[]> parameters) {
		if(FCServlet.isActionPresent(parameters,Names.ORDER_CAR_PARAMETER))
			return FormDispatcher.ORDER_CAR_ACTION;
		return super.getAction(parameters);
	}

}

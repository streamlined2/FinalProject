package edu.practice.finalproject.view.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.view.action.Action;

public class OrderForm extends ActionMapForm {

	private static final Map<String,Action> ACTION_MAP = Map.of(
			Names.ORDER_CAR_PARAMETER,FormDispatcher.ORDER_CAR_ACTION
	);

	public OrderForm(String name) {
		super(name);
	}

	@Override
	protected Map<String, Action> getActionMap() {
		return ACTION_MAP;
	}

	@Override
	public void init(HttpServletRequest req, EntityManager entityManager) {
		super.init(req, entityManager);
		req.setAttribute(Names.PASSPORT_PATTERN_ATTRIBUTE, Names.PASSPORT_PATTERN);
	}
}

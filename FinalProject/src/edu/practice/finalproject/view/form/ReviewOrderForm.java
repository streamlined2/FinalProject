package edu.practice.finalproject.view.form;

import java.util.Map;

import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class ReviewOrderForm extends ActionMapForm {

	private static final Map<String,Action> ACTION_MAP = Map.of(
			Names.ACCEPT_ORDER_PARAMETER,FormDispatcher.ACCEPT_ORDER_ACTION,
			Names.REJECT_ORDER_PARAMETER,FormDispatcher.REJECT_ORDER_ACTION
	);

	public ReviewOrderForm(String name) {
		super(name);
	}

	@Override
	protected Map<String, Action> getActionMap() {
		return ACTION_MAP;
	}
}

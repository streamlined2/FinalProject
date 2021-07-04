package edu.practice.finalproject.view.form;

import java.util.Map;

import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class CarOrderStatusForm extends ActionMapForm {

	private static final Map<String,Action> ACTION_MAP = Map.of(
			Names.CAR_CRITERIA_SELECTION_PARAMETER,FormDispatcher.BACK_ACTION
	);

	public CarOrderStatusForm(String name) {
		super(name);
	}

	@Override
	protected Map<String, Action> getActionMap() {
		return ACTION_MAP;
	}
}

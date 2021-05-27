package edu.practice.finalproject.view.form;

import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class CarSelectionCriteriaForm extends Form {

	public CarSelectionCriteriaForm(String name) {
		super(name);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.CONFIRM_CAR_CRITERIA_ACTION;
	}

}

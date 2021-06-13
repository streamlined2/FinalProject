package edu.practice.finalproject.view.form;

import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class CarManagementForm extends Form {

	public CarManagementForm(String name) {
		super(name);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.NEXT_ACTION;
	}

}

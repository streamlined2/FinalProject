package edu.practice.finalproject.view.form;

import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class UserBlockingForm extends Form {

	public UserBlockingForm(String name) {
		super(name);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.NEXT_ACTION;
	}

}

package edu.practice.finalproject.view.form;

import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class LeaseInvoiceDemoForm extends Form {

	public LeaseInvoiceDemoForm(String name) {
		super(name);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.BACK_ACTION;
	}

}

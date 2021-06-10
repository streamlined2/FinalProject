package edu.practice.finalproject.view.form;

import java.util.Map;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class ReviewOrderForm extends Form {

	public ReviewOrderForm(String name) {
		super(name);
	}

	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.isActionPresent(parameters,Names.ACCEPT_ORDER_PARAMETER)) return FormDispatcher.CREATE_LEASE_INVOICE_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.REJECT_ORDER_PARAMETER)) return FormDispatcher.REJECT_ORDER_ACTION;
		return super.getAction(parameters);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.BACK_ACTION;
	}
}

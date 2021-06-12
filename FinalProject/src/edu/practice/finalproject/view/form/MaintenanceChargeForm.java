package edu.practice.finalproject.view.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.view.action.Action;

public class MaintenanceChargeForm extends Form {

	public MaintenanceChargeForm(String name) {
		super(name);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.BACK_ACTION;
	}

	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.isActionPresent(parameters,Names.SEND_MAINTENANCE_INVOICE_PARAMETER)) return FormDispatcher.MAINTENANCE_INVOICE_SUBMIT_ACTION;
		return super.getAction(parameters);
	}

	@Override
	public void init(HttpServletRequest req, EntityManager entityManager) {
		req.setAttribute(Names.ACCOUNT_PATTERN_ATTRIBUTE, Names.ACCOUNT_PATTERN);
	}

}

package edu.practice.finalproject.view.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.view.action.Action;

public class MaintenanceChargeForm extends ActionMapForm {
	private static final Map<String,Action> ACTION_MAP = Map.of(
			Names.SEND_MAINTENANCE_INVOICE_PARAMETER,FormDispatcher.MAINTENANCE_INVOICE_SUBMIT_ACTION
	);

	public MaintenanceChargeForm(String name) {
		super(name);
	}

	@Override
	protected Map<String, Action> getActionMap() {
		return ACTION_MAP;
	}

	@Override
	public void init(HttpServletRequest req, EntityManager entityManager) {
		req.setAttribute(Names.ACCOUNT_PATTERN_ATTRIBUTE, Names.ACCOUNT_PATTERN);
	}
}

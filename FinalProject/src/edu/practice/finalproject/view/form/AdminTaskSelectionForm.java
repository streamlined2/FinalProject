package edu.practice.finalproject.view.form;

import java.util.Map;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class AdminTaskSelectionForm extends Form {

	public AdminTaskSelectionForm(String name) {
		super(name);
	}

	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.ifParameterEquals(parameters,Names.ADMIN_TASK_PARAMETER,Names.CAR_MANAGEMENT_PARAMETER)) return FormDispatcher.CAR_MANAGEMENT_ACTION;
		if(FCServlet.ifParameterEquals(parameters,Names.ADMIN_TASK_PARAMETER,Names.USER_BLOCKING_PARAMETER)) return FormDispatcher.USER_BLOCKING_ACTION;
		if(FCServlet.ifParameterEquals(parameters,Names.ADMIN_TASK_PARAMETER,Names.MANAGER_REGISTRATION_PARAMETER)) return FormDispatcher.MANAGER_REGISTRATION_ACTION;
		return super.getAction(parameters);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.NEXT_ACTION;
	}
}

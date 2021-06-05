package edu.practice.finalproject.view.form;

import java.util.Map;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class ManagerTaskSelectionForm extends Form {

	public ManagerTaskSelectionForm(String name) {
		super(name);
	}

	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.ifParameterEquals(parameters,Names.MANAGER_TASK_PARAMETER,Names.ORDER_APPROVEMENT_PARAMETER)) return FormDispatcher.APPROVE_ORDER_ACTION;
		if(FCServlet.ifParameterEquals(parameters,Names.MANAGER_TASK_PARAMETER,Names.CAR_RECEPTION_PARAMETER)) return FormDispatcher.RECEIVE_CAR_ACTION;
		return super.getAction(parameters);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.NEXT_ACTION;
	}
}

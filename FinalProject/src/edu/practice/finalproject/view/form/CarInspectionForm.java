package edu.practice.finalproject.view.form;

import java.util.Map;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class CarInspectionForm extends Form {

	public CarInspectionForm(String name) {
		super(name);
	}

	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.isActionPresent(parameters,Names.BACK_PARAMETER)) return FormDispatcher.BACK_ACTION;
		if(FCServlet.ifParameterEquals(parameters,Names.CAR_INSPECTION_RESULT_PARAMETER,Names.CAR_IN_PERFECT_CONDITION_PARAMETER)) return FormDispatcher.CAR_IN_PERFECT_CONDITION_ACTION;
		if(FCServlet.ifParameterEquals(parameters,Names.CAR_INSPECTION_RESULT_PARAMETER,Names.CAR_NEEDS_MAINTENANCE_PARAMETER)) return FormDispatcher.CAR_NEEDS_MAINTENANCE_ACTION;
		return super.getAction(parameters);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.BACK_ACTION;
	}
}

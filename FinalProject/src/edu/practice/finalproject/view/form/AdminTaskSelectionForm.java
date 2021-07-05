package edu.practice.finalproject.view.form;

import java.util.Map;

import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class AdminTaskSelectionForm extends ActionMapForm {

	private static final Map<ParameterValue,Action> PARAMETER_MAP = Map.of(
			new ParameterValue(Names.ADMIN_TASK_PARAMETER,Names.CAR_MANAGEMENT_PARAMETER),FormDispatcher.CAR_MANAGEMENT_ACTION,
			new ParameterValue(Names.ADMIN_TASK_PARAMETER,Names.USER_BLOCKING_PARAMETER),FormDispatcher.USER_BLOCKING_ACTION,
			new ParameterValue(Names.ADMIN_TASK_PARAMETER,Names.MANAGER_REGISTRATION_PARAMETER),FormDispatcher.REGISTER_ACTION
	);

	public AdminTaskSelectionForm(String name) {
		super(name);
	}

	@Override
	protected Map<ParameterValue, Action> getParameterValueMap() {
		return PARAMETER_MAP;
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.NEXT_ACTION;
	}
}

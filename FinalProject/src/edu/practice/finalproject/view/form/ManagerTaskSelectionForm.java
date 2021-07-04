package edu.practice.finalproject.view.form;

import java.util.Map;

import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class ManagerTaskSelectionForm extends ActionMapForm {

	private static final Map<ParameterValue,Action> PARAMETER_MAP = Map.of(
			new ParameterValue(Names.MANAGER_TASK_PARAMETER,Names.ORDER_APPROVEMENT_PARAMETER),FormDispatcher.REVIEW_ORDER_ACTION,
			new ParameterValue(Names.MANAGER_TASK_PARAMETER,Names.CAR_RECEPTION_PARAMETER),FormDispatcher.RECEIVE_CAR_ACTION
	);

	public ManagerTaskSelectionForm(String name) {
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

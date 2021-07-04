package edu.practice.finalproject.view.form;

import java.util.Map;

import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class CarInspectionForm extends ActionMapForm {
	
	private static final Map<String,Action> ACTION_MAP = Map.of(
			Names.BACK_PARAMETER,FormDispatcher.BACK_ACTION
	);
	
	private static final Map<ParameterValue,Action> PARAMETER_MAP = Map.of(
			new ParameterValue(Names.CAR_INSPECTION_RESULT_PARAMETER,Names.CAR_IN_PERFECT_CONDITION_PARAMETER),FormDispatcher.CAR_IN_PERFECT_CONDITION_ACTION,
			new ParameterValue(Names.CAR_INSPECTION_RESULT_PARAMETER,Names.CAR_NEEDS_MAINTENANCE_PARAMETER),FormDispatcher.CAR_NEEDS_MAINTENANCE_ACTION
	);

	public CarInspectionForm(String name) {
		super(name);
	}

	@Override
	protected Map<ParameterValue, Action> getParameterValueMap() {
		return PARAMETER_MAP;
	}

	@Override
	protected Map<String, Action> getActionMap() {
		return ACTION_MAP;
	}
}

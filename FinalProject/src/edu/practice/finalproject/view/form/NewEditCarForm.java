package edu.practice.finalproject.view.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car;
import edu.practice.finalproject.view.action.Action;

public class NewEditCarForm extends ActionMapForm {
	private static final Map<String,Action> ACTION_MAP = Map.of(
			Names.CREATE_MODIFY_CAR_PARAMETER,FormDispatcher.SAVE_CAR_ACTION
	);

	public NewEditCarForm(String name) {
		super(name);
	}

	@Override
	protected Map<String, Action> getActionMap() {
		return ACTION_MAP;
	}

	@Override
	public void init(HttpServletRequest req, EntityManager entityManager) {
		req.setAttribute(Names.MANUFACTURER_VALUES_ATTRIBUTE, Inspector.getLabels(Car.Manufacturer.class));
		req.setAttribute(Names.QUALITY_GRADE_VALUES_ATTRIBUTE, Inspector.getLabels(Car.QualityGrade.class));
		req.setAttribute(Names.COLOR_VALUES_ATTRIBUTE, Inspector.getLabels(Car.Color.class));
		req.setAttribute(Names.STYLE_VALUES_ATTRIBUTE, Inspector.getLabels(Car.Style.class));

		req.setAttribute(Names.CAR_MODEL_PATTERN_ATTRIBUTE, Names.CAR_MODEL_PATTERN);
	}
}

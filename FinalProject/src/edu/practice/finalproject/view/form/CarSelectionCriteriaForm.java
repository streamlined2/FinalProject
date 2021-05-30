package edu.practice.finalproject.view.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.entity.domain.Car;
import edu.practice.finalproject.view.action.Action;
import utilities.Utils;

public class CarSelectionCriteriaForm extends Form {

	public CarSelectionCriteriaForm(String name) {
		super(name);
	}

	@Override
	public void init(HttpServletRequest req) {
		super.init(req);
		req.setAttribute(Names.MANUFACTURER_VALUES_ATTRIBUTE, Utils.getLabels(Car.Manufacturer.class));
		req.setAttribute(Names.QUALITY_GRADE_VALUES_ATTRIBUTE, Utils.getLabels(Car.QualityGrade.class));
		req.setAttribute(Names.COLOR_VALUES_ATTRIBUTE, Utils.getLabels(Car.Color.class));
		req.setAttribute(Names.STYLE_VALUES_ATTRIBUTE, Utils.getLabels(Car.Style.class));
	}

	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.isActionPresent(parameters,Names.CONFIRM_CAR_CRITERIA_PARAMETER))
			return FormDispatcher.CONFIRM_CAR_CRITERIA_ACTION;
		return super.getAction(parameters);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.CONFIRM_CAR_CRITERIA_ACTION;
	}
}

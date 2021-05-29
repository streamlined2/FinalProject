package edu.practice.finalproject.view.form;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.entity.domain.Car;
import edu.practice.finalproject.view.action.Action;
import utilities.Utilities;

public class CarSelectionCriteriaForm extends Form {

	public CarSelectionCriteriaForm(String name) {
		super(name);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.CONFIRM_CAR_CRITERIA_ACTION;
	}

	@Override
	public void init(HttpServletRequest req) {
		super.init(req);
		req.setAttribute(Names.MANUFACTURER_VALUES_ATTRIBUTE, Utilities.getLabels(Car.Manufacturer.class));
		req.setAttribute(Names.QUALITY_GRADES_VALUES_ATTRIBUTE, Utilities.getLabels(Car.QualityGrade.class));
		req.setAttribute(Names.COLOR_VALUES_ATTRIBUTE, Utilities.getLabels(Car.Color.class));
		req.setAttribute(Names.STYLE_VALUES_ATTRIBUTE, Utilities.getLabels(Car.Style.class));
	}

}

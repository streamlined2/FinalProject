package edu.practice.finalproject.view.action;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car;
import edu.practice.finalproject.model.entity.domain.Car.Color;
import edu.practice.finalproject.model.entity.domain.Car.Manufacturer;
import edu.practice.finalproject.model.entity.domain.Car.QualityGrade;
import edu.practice.finalproject.model.entity.domain.Car.Style;
import edu.practice.finalproject.utilities.Utils;

public class SaveCarAction extends AdminAction {

	private static final String WRONG_CAR_MODEL_MSG = "Wrong car model";
	private static final String WRONG_RENT_COST_MSG = "Wrong rent cost value";
	private static final String WRONG_MANUFACTURER_MSG = "Wrong manufacturer";
	private static final String WRONG_QUALITY_GRADE_MSG = "Wrong quality grade";
	private static final String WRONG_COLOR_MSG = "Wrong color";
	private static final String WRONG_STYLE_MSG = "Wrong style";
	private static final String CANT_SAVE_CAR_MSG = "Can't save car";
	private static final String COST_SHOULD_BE_POSITIVE_MSG = "Lease cost should be positive value";
	private static final String WRONG_PRODUCTION_DATE_FORMAT_MSG = "Wrong car production date";
	private static final String WRONG_PRODUCTION_DATE_VALUE_MSG = "Production date %tF should succeed current date %tF";
	
	public SaveCarAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		if(!Utils.checkIfValid(req,Names.CAR_MODEL_PARAMETER,Utils::checkCarModel)) {
			FCServlet.setError(req, WRONG_CAR_MODEL_MSG);
			return false;
		}

		final Car car = (Car)FCServlet.getAttribute(req, Names.SELECTED_CAR_ATTRIBUTE);
		final Boolean createNew = (Boolean)FCServlet.getAttribute(req, Names.NEW_CAR_ATTRIBUTE);
		
		final String model=FCServlet.getParameterValue(req,Names.CAR_MODEL_PARAMETER);		
		car.setModel(model);

		final String manufacturer=FCServlet.getParameterValue(req,Names.MANUFACTURER_PARAMETER);		
		final Optional<Manufacturer> manufacturerValue = Inspector.getByLabel(Manufacturer.class, manufacturer);
		if(manufacturerValue.isEmpty()) {
			FCServlet.setError(req, WRONG_MANUFACTURER_MSG);
			return false;
		}
		car.setManufacturer(manufacturerValue.get());
		
		final String qualityGrade=FCServlet.getParameterValue(req,Names.QUALITY_GRADE_PARAMETER);
		final Optional<QualityGrade> qualityGradeValue = Inspector.getByLabel(QualityGrade.class, qualityGrade);
		if(qualityGradeValue.isEmpty()) {
			FCServlet.setError(req, WRONG_QUALITY_GRADE_MSG);
			return false;			
		}
		car.setQualityGrade(qualityGradeValue.get());

		final String color=FCServlet.getParameterValue(req,Names.COLOR_PARAMETER);		
		final Optional<Color> colorValue = Inspector.getByLabel(Color.class, color);
		if(colorValue.isEmpty()) {
			FCServlet.setError(req, WRONG_COLOR_MSG);
			return false;
		}
		car.setColor(colorValue.get());

		final String style=FCServlet.getParameterValue(req,Names.STYLE_PARAMETER);
		final Optional<Style> styleValue = Inspector.getByLabel(Style.class, style);
		if(styleValue.isEmpty()) {
			FCServlet.setError(req, WRONG_STYLE_MSG);
			return false;
		}
		car.setStyle(styleValue.get());
		
		try {
			final String rentCost=FCServlet.getParameterValue(req,Names.CAR_RENT_COST_PARAMETER);
			final BigDecimal costValue = new BigDecimal(rentCost);
			if(costValue.compareTo(BigDecimal.ZERO)<=0) {
				FCServlet.setError(req, COST_SHOULD_BE_POSITIVE_MSG);
				return false;
			}
			car.setCost(costValue);
			
			final String productionDate=FCServlet.getParameterValue(req,Names.CAR_PRODUCTION_DATE_PARAMETER);
			final LocalDate prodDateValue = LocalDate.parse(productionDate);
			final LocalDate now = LocalDate.now();
			if(prodDateValue.isBefore(now)) {
				FCServlet.setError(req, String.format(WRONG_PRODUCTION_DATE_VALUE_MSG, prodDateValue, now));
				return false;
			}
			car.setProductionDate(prodDateValue);
			
			boolean success;
			if(createNew.booleanValue()) {
				success = entityManager.persist(car);
			} else {
				success = entityManager.merge(car);
			}
			if(!success) {
				FCServlet.setError(req, CANT_SAVE_CAR_MSG);
				return false;
			}
			return true;
		} catch(NumberFormatException e) {
			FCServlet.setError(req, WRONG_RENT_COST_MSG);
		} catch(DateTimeParseException e) {
			FCServlet.setError(req, WRONG_PRODUCTION_DATE_FORMAT_MSG);
		}
		return false;
	}
}

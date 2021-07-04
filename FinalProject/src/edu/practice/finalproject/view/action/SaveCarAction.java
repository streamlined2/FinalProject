package edu.practice.finalproject.view.action;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.DataAccessException;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car;
import edu.practice.finalproject.model.entity.domain.Car.Color;
import edu.practice.finalproject.model.entity.domain.Car.Manufacturer;
import edu.practice.finalproject.model.entity.domain.Car.QualityGrade;
import edu.practice.finalproject.model.entity.domain.Car.Style;
import edu.practice.finalproject.utilities.Utils;

public class SaveCarAction extends AdminAction {

	private static final Logger logger = LogManager.getLogger();

	private static final String WRONG_CAR_MODEL_MSG = "save.car.action.wrong-car-model";
	private static final String WRONG_RENT_COST_MSG = "save.car.action.wrong-rent-cost";
	private static final String WRONG_MANUFACTURER_MSG = "save.car.action.wrong-manufacturer";
	private static final String WRONG_QUALITY_GRADE_MSG = "save.car.action.wrong-quality-grade";
	private static final String WRONG_COLOR_MSG = "save.car.action.wrong-color";
	private static final String WRONG_STYLE_MSG = "save.car.action.wrong-style";
	private static final String CANT_SAVE_CAR_MSG = "save.car.action.cant-save-car";
	private static final String COST_SHOULD_BE_POSITIVE_MSG = "save.car.action.negative-lease-cost";
	private static final String WRONG_PRODUCTION_DATE_FORMAT_MSG = "save.car.action.wrong-car-production-date";
	private static final String WRONG_PRODUCTION_DATE_VALUE_MSG = "save.car.action.production-before-current-date";
	private static final String CAR_SAVED_MSG = "save.car.action.car-saved";
	
	public SaveCarAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		if(!Utils.checkIfValid(req,Names.CAR_MODEL_PARAMETER,Utils::checkCarModel)) {
			FCServlet.setError(req, WRONG_CAR_MODEL_MSG);
			return false;
		}
		final String model=FCServlet.getParameterValue(req,Names.CAR_MODEL_PARAMETER);		

		final String manufacturer=FCServlet.getParameterValue(req,Names.MANUFACTURER_PARAMETER);		
		final Optional<Manufacturer> manufacturerValue = Inspector.getByLabel(Manufacturer.class, manufacturer);
		if(!manufacturerValue.isPresent()) {
			FCServlet.setError(req, WRONG_MANUFACTURER_MSG);
			return false;
		}
		
		final String qualityGrade=FCServlet.getParameterValue(req,Names.QUALITY_GRADE_PARAMETER);
		final Optional<QualityGrade> qualityGradeValue = Inspector.getByLabel(QualityGrade.class, qualityGrade);
		if(!qualityGradeValue.isPresent()) {
			FCServlet.setError(req, WRONG_QUALITY_GRADE_MSG);
			return false;			
		}

		final String color=FCServlet.getParameterValue(req,Names.COLOR_PARAMETER);		
		final Optional<Color> colorValue = Inspector.getByLabel(Color.class, color);
		if(!colorValue.isPresent()) {
			FCServlet.setError(req, WRONG_COLOR_MSG);
			return false;
		}

		final String style=FCServlet.getParameterValue(req,Names.STYLE_PARAMETER);
		final Optional<Style> styleValue = Inspector.getByLabel(Style.class, style);
		if(!styleValue.isPresent()) {
			FCServlet.setError(req, WRONG_STYLE_MSG);
			return false;
		}
		
		try {
			final String rentCost=FCServlet.getParameterValue(req,Names.CAR_RENT_COST_PARAMETER);
			final BigDecimal costValue = new BigDecimal(rentCost);
			if(costValue.compareTo(BigDecimal.ZERO)<=0) {
				FCServlet.setError(req, COST_SHOULD_BE_POSITIVE_MSG);
				return false;
			}
			
			final Boolean createNew = (Boolean)FCServlet.getAttribute(req, Names.NEW_CAR_ATTRIBUTE, Boolean.FALSE);

			final String productionDate = FCServlet.getParameterValue(req,Names.CAR_PRODUCTION_DATE_PARAMETER);
			final LocalDate prodDateValue = LocalDate.parse(productionDate);
			final LocalDate now = LocalDate.now();
			if(createNew.booleanValue() && now.isBefore(prodDateValue)) {
				FCServlet.setError(req, WRONG_PRODUCTION_DATE_VALUE_MSG, prodDateValue, now);
				return false;
			}
			
			final Car car = (Car)FCServlet.getAttribute(req, Names.SELECTED_CAR_ATTRIBUTE);
			car.setModel(model);
			car.setManufacturer(manufacturerValue.get());
			car.setQualityGrade(qualityGradeValue.get());
			car.setColor(colorValue.get());
			car.setStyle(styleValue.get());
			car.setCost(costValue);
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
			FCServlet.setMessage(req, CAR_SAVED_MSG);
			return true;
		} catch(NumberFormatException e) {
			logger.error(Utils.message(WRONG_RENT_COST_MSG), e);
			FCServlet.setError(req, WRONG_RENT_COST_MSG);
		} catch(DateTimeParseException e) {
			logger.error(Utils.message(WRONG_PRODUCTION_DATE_FORMAT_MSG), e);
			FCServlet.setError(req, WRONG_PRODUCTION_DATE_FORMAT_MSG);
		} catch(DataAccessException e) {
			logger.error(Utils.message(CANT_SAVE_CAR_MSG), e);
			FCServlet.setError(req, CANT_SAVE_CAR_MSG);			
		}
		return false;
	}
}

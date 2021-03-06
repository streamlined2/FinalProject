package edu.practice.finalproject.view.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car.Color;
import edu.practice.finalproject.model.entity.domain.Car.Manufacturer;
import edu.practice.finalproject.model.entity.domain.Car.QualityGrade;
import edu.practice.finalproject.model.entity.domain.Car.Style;
import edu.practice.finalproject.utilities.Utils;

public class ConfirmCarCriteriaAction extends ClientAction {

	public ConfirmCarCriteriaAction(String name) {
		super(name);
	}
	
	private static void addFilterKeyValue(final Map<String,Object> keyPairs,final String flag,final String key,final Optional<?> value) {
		if(Objects.nonNull(flag) && !flag.isEmpty() && value.isPresent()) {
			keyPairs.put(key, value.get());
		}
	}
	
	private static void addOrderKey(final Map<String,Boolean> orderKeys,final String flag,final String key,final boolean ascending) {
		if(Objects.nonNull(flag) && !flag.isEmpty()) {
			orderKeys.put(key, ascending);
		}
	}

	@Override
	public boolean execute(final HttpServletRequest req, final EntityManager entityManager) {
		final String selectByManufacturer=FCServlet.getParameterValue(req,Names.SELECT_BY_MANUFACTURER_PARAMETER);
		final String manufacturer=FCServlet.getParameterValue(req,Names.MANUFACTURER_PARAMETER);		
		final String selectByQualityGrade=FCServlet.getParameterValue(req,Names.SELECT_BY_QUALITY_GRADE_PARAMETER);
		final String qualityGrade=FCServlet.getParameterValue(req,Names.QUALITY_GRADE_PARAMETER);
		final String selectByColor=FCServlet.getParameterValue(req,Names.SELECT_BY_COLOR_PARAMETER);
		final String color=FCServlet.getParameterValue(req,Names.COLOR_PARAMETER);
		final String selectByStyle=FCServlet.getParameterValue(req,Names.SELECT_BY_STYLE_PARAMETER);
		final String style=FCServlet.getParameterValue(req,Names.STYLE_PARAMETER);
		
		final Map<String,Object> filterKeyPairs=new HashMap<>();
		addFilterKeyValue(filterKeyPairs,selectByManufacturer,Names.MANUFACTURER_PARAMETER,Inspector.getByLabel(Manufacturer.class,manufacturer));
		addFilterKeyValue(filterKeyPairs,selectByQualityGrade,Names.QUALITY_GRADE_PARAMETER,Inspector.getByLabel(QualityGrade.class,qualityGrade));
		addFilterKeyValue(filterKeyPairs,selectByColor,Names.COLOR_PARAMETER,Inspector.getByLabel(Color.class,color));
		addFilterKeyValue(filterKeyPairs,selectByStyle,Names.STYLE_PARAMETER,Inspector.getByLabel(Style.class,style));
		
		final String orderByRentCost=FCServlet.getParameterValue(req,Names.ORDER_BY_RENT_COST_PARAMETER);
		final String costSort=FCServlet.getParameterValue(req,Names.RENT_COST_ORDER_PARAMETER);
		final String orderByModel=FCServlet.getParameterValue(req,Names.ORDER_BY_MODEL_PARAMETER);
		final String modelSort=FCServlet.getParameterValue(req,Names.MODEL_PARAMETER);
		final String orderByProductionDate=FCServlet.getParameterValue(req,Names.ORDER_BY_PRODUCTION_DATE);
		final String productionDateSort=FCServlet.getParameterValue(req,Names.PRODUCTION_DATE_PARAMETER);
		
		final Map<String,Boolean> orderKeys=new HashMap<>();
		addOrderKey(orderKeys,orderByRentCost,Names.CAR_RENT_COST_PARAMETER,Utils.isAscendingOrder(costSort));
		addOrderKey(orderKeys,orderByModel,Names.CAR_MODEL_PARAMETER,Utils.isAscendingOrder(modelSort));
		addOrderKey(orderKeys,orderByProductionDate,Names.CAR_PRODUCTION_DATE_PARAMETER,Utils.isAscendingOrder(productionDateSort));
		
		FCServlet.setAttribute(req, Names.FILTER_KEY_PAIRS_ATTRIBUTE, filterKeyPairs);
		FCServlet.setAttribute(req, Names.ORDER_KEYS_ATTRIBUTE, orderKeys);

		final Integer numberOfElements=FCServlet.getPageElements(req);
		FCServlet.setAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,0L);
		FCServlet.setAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE,Long.valueOf(numberOfElements-1));

		return true;
	}
}

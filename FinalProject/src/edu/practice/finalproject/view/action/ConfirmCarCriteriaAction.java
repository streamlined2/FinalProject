package edu.practice.finalproject.view.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car;
import edu.practice.finalproject.model.entity.domain.Car.Color;
import edu.practice.finalproject.model.entity.domain.Car.Manufacturer;
import edu.practice.finalproject.model.entity.domain.Car.QualityGrade;
import edu.practice.finalproject.model.entity.domain.Car.Style;
import utilities.Utils;

public class ConfirmCarCriteriaAction extends ClientAction {

	public ConfirmCarCriteriaAction(String name) {
		super(name);
	}
	
	private static <V> void addKeyPair(final Map<String,V> keyPairs,final String flag,final String key,final V value) {
		if(Objects.nonNull(flag) && !flag.isEmpty()) {
			keyPairs.put(key, value);
		}
	}
	
	private static void addOrderKey(final Map<String,Boolean> orderKeys,final String flag,final String key,final boolean ascending) {
		if(Objects.nonNull(flag) && !flag.isEmpty()) {
			orderKeys.put(key, ascending);
		}
	}

	@Override
	public boolean execute(final HttpServletRequest req, final EntityManager entityManager) throws ServletException {
		final String selectByManufacturer=FCServlet.getParameterValue(req,Names.SELECT_BY_MANUFACTURER_PARAMETER);
		final String manufacturer=FCServlet.getParameterValue(req,Names.MANUFACTURER_PARAMETER);		
		final String selectByQualityGrade=FCServlet.getParameterValue(req,Names.SELECT_BY_QUALITY_GRADE_PARAMETER);
		final String qualityGrade=FCServlet.getParameterValue(req,Names.QUALITY_GRADE_PARAMETER);
		final String selectByColor=FCServlet.getParameterValue(req,Names.SELECT_BY_COLOR_PARAMETER);
		final String color=FCServlet.getParameterValue(req,Names.COLOR_PARAMETER);
		final String selectByStyle=FCServlet.getParameterValue(req,Names.SELECT_BY_STYLE_PARAMETER);
		final String style=FCServlet.getParameterValue(req,Names.STYLE_PARAMETER);
		
		final Map<String,Object> keyPairs=new HashMap<>();
		addKeyPair(keyPairs,selectByManufacturer,"manufacturer",Utils.getByLabel(Manufacturer.class,manufacturer));
		addKeyPair(keyPairs,selectByQualityGrade,"qualityGrade",Utils.getByLabel(QualityGrade.class,qualityGrade));
		addKeyPair(keyPairs,selectByColor,"color",Utils.getByLabel(Color.class,color));
		addKeyPair(keyPairs,selectByStyle,"style",Utils.getByLabel(Style.class,style));
		
		final String orderByRentCost=FCServlet.getParameterValue(req,Names.ORDER_BY_RENT_COST_PARAMETER);
		final String costSort=FCServlet.getParameterValue(req,Names.RENT_COST_ORDER_PARAMETER);
		final String orderByModel=FCServlet.getParameterValue(req,Names.ORDER_BY_MODEL_PARAMETER);
		final String modelSort=FCServlet.getParameterValue(req,Names.MODEL_PARAMETER);
		final String orderByProductionDate=FCServlet.getParameterValue(req,Names.ORDER_BY_PRODUCTION_DATE);
		final String productionDateSort=FCServlet.getParameterValue(req,Names.PRODUCTION_DATE_PARAMETER);
		
		final Map<String,Boolean> orderKeys=new HashMap<>();
		addOrderKey(orderKeys,orderByRentCost,"cost",Utils.isAscendingOrder(costSort));
		addOrderKey(orderKeys,orderByModel,"model",Utils.isAscendingOrder(modelSort));
		addOrderKey(orderKeys,orderByProductionDate,"productionDate",Utils.isAscendingOrder(productionDateSort));
		
		final List<Car> entities=entityManager.fetchByCompositeKeyOrdered(Car.class,keyPairs,orderKeys);
		FCServlet.setAttribute(req, "query", entities);

		return true;
	}
}

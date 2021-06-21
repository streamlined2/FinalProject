package edu.practice.finalproject.view.form;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.domain.Car;
import edu.practice.finalproject.view.action.Action;

public class CarManagementForm extends Form {

	public CarManagementForm(String name) {
		super(name);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.BACK_ACTION;
	}

	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.isActionPresent(parameters,Names.MODIFY_CAR_PARAMETER)) return FormDispatcher.MODIFY_CAR_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.DELETE_CAR_PARAMETER)) return FormDispatcher.DROP_CAR_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.NEW_CAR_PARAMETER)) return FormDispatcher.ADD_CAR_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.NEXT_PAGE_PARAMETER)) return FormDispatcher.NEXT_PAGE_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.PREVIOUS_PAGE_PARAMETER)) return FormDispatcher.PREVIOUS_PAGE_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.FIRST_PAGE_PARAMETER)) return FormDispatcher.FIRST_PAGE_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.LAST_PAGE_PARAMETER)) return FormDispatcher.LAST_PAGE_ACTION;
		return super.getAction(parameters);
	}

	@Override
	public void init(HttpServletRequest req, EntityManager entityManager) {
		final Integer pageElements=FCServlet.getPageElements(req);
		final Long firstElement=(Long)FCServlet.getAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,0L);
		final Long lastElement=(Long)FCServlet.getAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE,firstElement+pageElements-1);

		final List<Car> queryData=entityManager.fetchEntities(Car.class,false,firstElement,lastElement);

		FCServlet.setAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE,queryData);
		Map<String, String> buttons = new LinkedHashMap<>();
		buttons.put("modifyCar","Edit");
		buttons.put("deleteCar","Delete");
		FCServlet.setAttribute(req, Names.QUERY_BUTTONS_MAP_ATTRIBUTE, buttons);
		FCServlet.setAttribute(req, Names.QUERY_DATA_ATTRIBUTE, Inspector.getValuesForEntities(Car.class, queryData));
		FCServlet.setAttribute(req, Names.QUERY_HEADER_ATTRIBUTE, Inspector.getCaptions(Car.class));
		
		super.init(req,entityManager);
	}

	@Override
	public void destroy(HttpServletRequest req) {
		FCServlet.removeAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_BUTTONS_MAP_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_DATA_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_HEADER_ATTRIBUTE);
		super.destroy(req);
	}
}

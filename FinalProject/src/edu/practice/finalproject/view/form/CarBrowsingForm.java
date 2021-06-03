package edu.practice.finalproject.view.form;

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

public class CarBrowsingForm extends Form {

	public CarBrowsingForm(String name) {
		super(name);
	}

	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.isActionPresent(parameters,Names.SELECT_CAR_PARAMETER)) return FormDispatcher.SELECT_CAR_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.NEXT_PAGE_PARAMETER)) return FormDispatcher.NEXT_PAGE_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.PREVIOUS_PAGE_PARAMETER)) return FormDispatcher.PREVIOUS_PAGE_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.FIRST_PAGE_PARAMETER)) return FormDispatcher.FIRST_PAGE_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.LAST_PAGE_PARAMETER)) return FormDispatcher.LAST_PAGE_ACTION;
		return super.getAction(parameters);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.BACK_ACTION;
	}
	
	@Override
	public void init(final HttpServletRequest req, final EntityManager entityManager) {
		final Map<String,Object> filterKeyPairs = (Map<String, Object>) FCServlet.getAttribute(req, Names.FILTER_KEY_PAIRS_ATTRIBUTE);
		final Map<String,Boolean> orderKeys=(Map<String, Boolean>) FCServlet.getAttribute(req, Names.ORDER_KEYS_ATTRIBUTE);
		
		final Integer pageElements=(Integer)FCServlet.getAttribute(req, Names.PAGE_ELEMENTS_NUMBER_ATTRIBUTE,5);
		final Long firstElement=(Long)FCServlet.getAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,0L);
		final Long lastElement=(Long)FCServlet.getAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE,firstElement+pageElements-1);

		final List<Car> queryData=entityManager.fetchByCompositeKeyOrdered(Car.class,filterKeyPairs,orderKeys,firstElement,lastElement);

		FCServlet.setAttribute(req, Names.PAGE_CARS_ATTRIBUTE,queryData);
		FCServlet.setAttribute(req, Names.QUERY_DATA_ATTRIBUTE, Inspector.getValuesForEntities(Car.class, queryData));
		FCServlet.setAttribute(req, Names.QUERY_HEADER_ATTRIBUTE, Inspector.getCaptions(Car.class));
		
		super.init(req,entityManager);
	}

	@Override
	public void destroy(final HttpServletRequest req) {
		FCServlet.removeAttribute(req, Names.PAGE_CARS_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_DATA_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_HEADER_ATTRIBUTE);
		super.destroy(req);
	}
}

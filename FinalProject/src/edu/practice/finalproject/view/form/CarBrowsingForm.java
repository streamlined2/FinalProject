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

public class CarBrowsingForm extends ActionMapForm {
	
	private static final Map<String,Action> ACTION_MAP = Map.of(
			Names.SELECT_CAR_PARAMETER, FormDispatcher.SELECT_CAR_ACTION,
			Names.NEXT_PAGE_PARAMETER, FormDispatcher.NEXT_PAGE_ACTION,
			Names.PREVIOUS_PAGE_PARAMETER, FormDispatcher.PREVIOUS_PAGE_ACTION,
			Names.FIRST_PAGE_PARAMETER, FormDispatcher.FIRST_PAGE_ACTION,
			Names.LAST_PAGE_PARAMETER, FormDispatcher.LAST_PAGE_ACTION
	);

	public CarBrowsingForm(String name) {
		super(name);
	}

	@Override
	protected Map<String, Action> getActionMap() {
		return ACTION_MAP;
	}
	
	@Override
	public void init(final HttpServletRequest req, final EntityManager entityManager) {
		final Map<String,Object> filterKeyPairs = (Map<String, Object>) FCServlet.getAttribute(req, Names.FILTER_KEY_PAIRS_ATTRIBUTE);
		final Map<String,Boolean> orderKeys=(Map<String, Boolean>) FCServlet.getAttribute(req, Names.ORDER_KEYS_ATTRIBUTE);
		
		final Integer pageElements=FCServlet.getPageElements(req);
		final Long firstElement=(Long)FCServlet.getAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,0L);
		final Long lastElement=(Long)FCServlet.getAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE,firstElement+pageElements-1);

		final List<Car> queryData=entityManager.fetchByCompositeKeyOrdered(Car.class,filterKeyPairs,orderKeys,firstElement,lastElement);
		final Long queryCount=entityManager.countByCompositeKey(Car.class,filterKeyPairs);

		FCServlet.setQueryElements(req, queryCount);
		FCServlet.setAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE,queryData);
		FCServlet.setAttribute(req, Names.QUERY_BUTTONS_MAP_ATTRIBUTE, Map.of(Names.SELECT_CAR_PARAMETER,"Get"));
		FCServlet.setAttribute(req, Names.QUERY_DATA_ATTRIBUTE, Inspector.getValuesForEntities(Car.class, queryData));
		FCServlet.setAttribute(req, Names.QUERY_HEADER_ATTRIBUTE, Inspector.getCaptions(Car.class));
		
		super.init(req,entityManager);
	}

	@Override
	public void destroy(final HttpServletRequest req) {
		FCServlet.removeQueryElements(req);
		FCServlet.removeAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_BUTTONS_MAP_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_DATA_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_HEADER_ATTRIBUTE);
		super.destroy(req);
	}
}

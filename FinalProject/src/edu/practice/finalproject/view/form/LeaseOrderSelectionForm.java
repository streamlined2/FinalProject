package edu.practice.finalproject.view.form;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.document.CarReview;
import edu.practice.finalproject.model.entity.document.LeaseOrder;
import edu.practice.finalproject.model.entity.document.OrderReview;
import edu.practice.finalproject.model.entity.document.OrderReview.OrderStatus;
import edu.practice.finalproject.view.action.Action;

public class LeaseOrderSelectionForm extends ActionMapForm {

	private static final Map<String,Action> ACTION_MAP = Map.of(
			Names.REVIEW_ORDER_PARAMETER,FormDispatcher.SELECT_LEASE_ORDER_ACTION,
			Names.NEXT_PAGE_PARAMETER,FormDispatcher.NEXT_PAGE_ACTION,
			Names.PREVIOUS_PAGE_PARAMETER,FormDispatcher.PREVIOUS_PAGE_ACTION,
			Names.FIRST_PAGE_PARAMETER,FormDispatcher.FIRST_PAGE_ACTION,
			Names.LAST_PAGE_PARAMETER,FormDispatcher.LAST_PAGE_ACTION
	);

	public LeaseOrderSelectionForm(String name) {
		super(name);
	}

	@Override
	protected Map<String, Action> getActionMap() {
		return ACTION_MAP;
	}

	@Override
	public void init(HttpServletRequest req, EntityManager entityManager) {
		final Integer pageElements=FCServlet.getPageElements(req);
		final Long firstElement=(Long)FCServlet.getAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,0L);
		final Long lastElement=(Long)FCServlet.getAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE,firstElement+pageElements-1);

		final Map<String,?> keyPairs = Map.of("orderStatus",OrderStatus.APPROVED);
		final List<LeaseOrder> queryData=entityManager.fetchLinkedMissingEntities(LeaseOrder.class,OrderReview.class,keyPairs,CarReview.class,firstElement,lastElement);
		final Long queryCount=entityManager.countLinkedMissingEntities(LeaseOrder.class,OrderReview.class,keyPairs,CarReview.class);

		FCServlet.setQueryElements(req, queryCount);
		FCServlet.setAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE,queryData);
		FCServlet.setAttribute(req, Names.QUERY_BUTTONS_MAP_ATTRIBUTE, Map.of(Names.REVIEW_ORDER_PARAMETER,"Select"));
		FCServlet.setAttribute(req, Names.QUERY_DATA_ATTRIBUTE, Inspector.getValuesForEntities(LeaseOrder.class, queryData));
		FCServlet.setAttribute(req, Names.QUERY_HEADER_ATTRIBUTE, Inspector.getCaptions(LeaseOrder.class));
		
		super.init(req,entityManager);
	}

	@Override
	public void destroy(HttpServletRequest req) {
		FCServlet.removeQueryElements(req);
		FCServlet.removeAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_BUTTONS_MAP_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_DATA_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_HEADER_ATTRIBUTE);
		super.destroy(req);
	}
}

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

public class LeaseOrderSelectionForm extends Form {

	public LeaseOrderSelectionForm(String name) {
		super(name);
	}

	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.isActionPresent(parameters,Names.REVIEW_ORDER_PARAMETER)) return FormDispatcher.SELECT_LEASE_ORDER_ACTION;
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
	public void init(HttpServletRequest req, EntityManager entityManager) {
		final Integer pageElements=(Integer)FCServlet.getAttribute(req, Names.PAGE_ELEMENTS_NUMBER_ATTRIBUTE,5);
		final Long firstElement=(Long)FCServlet.getAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,0L);
		final Long lastElement=(Long)FCServlet.getAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE,firstElement+pageElements-1);

		final Map<String,?> keyPairs = Map.of("orderStatus",OrderStatus.APPROVED);
		final List<LeaseOrder> queryData=entityManager.fetchLinkedMissingEntities(LeaseOrder.class,OrderReview.class,keyPairs,CarReview.class,firstElement,lastElement);

		FCServlet.setAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE,queryData);
		FCServlet.setAttribute(req, Names.QUERY_DATA_ATTRIBUTE, Inspector.getValuesForEntities(LeaseOrder.class, queryData));
		FCServlet.setAttribute(req, Names.QUERY_HEADER_ATTRIBUTE, Inspector.getCaptions(LeaseOrder.class));
		
		super.init(req,entityManager);
	}

	@Override
	public void destroy(HttpServletRequest req) {
		FCServlet.removeAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_DATA_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_HEADER_ATTRIBUTE);
		super.destroy(req);
	}
}

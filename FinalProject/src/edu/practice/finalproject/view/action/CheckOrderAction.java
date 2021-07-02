package edu.practice.finalproject.view.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.document.LeaseOrder;

public class CheckOrderAction extends ManagerAction {
	
	private static final String INCORRECT_ORDER_MSG = "check.order.action.wrong-order";

	public CheckOrderAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final List<LeaseOrder> queryData=(List<LeaseOrder>)FCServlet.getAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE);
		final int number=Integer.parseInt(FCServlet.getParameterValue(req,Names.ORDER_NUMBER_PARAMETER));
		if(number>=0 && number<queryData.size()) {
			FCServlet.setAttribute(req, Names.SELECTED_ORDER_ATTRIBUTE, queryData.get(number));
			return true;
		}
		FCServlet.setError(req, FCServlet.localize(INCORRECT_ORDER_MSG));
		return false;
	}
}

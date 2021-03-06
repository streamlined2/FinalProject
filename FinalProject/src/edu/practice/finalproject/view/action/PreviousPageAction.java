package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class PreviousPageAction extends Action {

	public PreviousPageAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(final HttpServletRequest req, final EntityManager entityManager) {
		final Integer numberOfElements = FCServlet.getPageElements(req);
		final Long firstElement = (Long)FCServlet.getAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,Long.valueOf(numberOfElements));
		final Long nextLastElement = Math.max(numberOfElements-1,firstElement-1);
		final Long nextFirstElement = Math.max(nextLastElement-numberOfElements+1, 0L);
		FCServlet.setAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE, nextLastElement);
		FCServlet.setAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE, nextFirstElement);
		return true;
	}
}

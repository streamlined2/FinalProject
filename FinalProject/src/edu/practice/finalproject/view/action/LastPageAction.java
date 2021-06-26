package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class LastPageAction extends Action {

	public LastPageAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(final HttpServletRequest req, final EntityManager entityManager) {
		final Integer numberOfElements = FCServlet.getPageElements(req);
		final Long numberOfQueryElements = FCServlet.getQueryElements(req);
		final Long nextLastElement = Math.max(numberOfQueryElements-1, 0L);
		final Long nextFirstElement = Math.max(nextLastElement-numberOfElements+1, 0L);
		FCServlet.setAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE, nextFirstElement);
		FCServlet.setAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE, nextLastElement);
		return true;
	}
}

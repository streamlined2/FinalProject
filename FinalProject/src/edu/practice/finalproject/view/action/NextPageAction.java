package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class NextPageAction extends Action {

	public NextPageAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(final HttpServletRequest req, final EntityManager entityManager) {
		final Integer numberOfElements = FCServlet.getPageElements(req);
		final Long numberOfQueryElements = FCServlet.getQueryElements(req);
		final Long firstElement = (Long)FCServlet.getAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,0L);
		final Long lastElement = Math.min(numberOfQueryElements,(Long)FCServlet.getAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE,firstElement+numberOfElements-1));
		final Long nextFirstElement = lastElement+1;
		final Long nextLastElement = Math.min(numberOfQueryElements-1, lastElement+numberOfElements);
		if(nextLastElement >= nextFirstElement) {
			FCServlet.setAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE, nextFirstElement);
			FCServlet.setAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE, nextLastElement);
			return true;
		}
		return false;
	}
}

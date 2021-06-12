package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.model.dataaccess.EntityManager;

public class LastPageAction extends PageNavigationAction {

	public LastPageAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(final HttpServletRequest req, final EntityManager entityManager) {//TODO
/*		final Integer numberOfElements=(Integer)FCServlet.getAttribute(req, Names.PAGE_ELEMENTS_NUMBER_ATTRIBUTE,5);
		final Long firstElement=(Long)FCServlet.getAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,0L);
		final Long lastElement=(Long)FCServlet.getAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE,firstElement+numberOfElements-1);
		FCServlet.setAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,lastElement+1);
		FCServlet.setAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE,lastElement+numberOfElements);*/
		return true;
	}
}

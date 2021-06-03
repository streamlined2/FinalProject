package edu.practice.finalproject.view.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class FirstPageAction extends PageNavigationAction {

	public FirstPageAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(final HttpServletRequest req, final EntityManager entityManager) throws ServletException {
		final Integer numberOfElements=(Integer)FCServlet.getAttribute(req, Names.PAGE_ELEMENTS_NUMBER_ATTRIBUTE,5);
		FCServlet.setAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,0L);
		FCServlet.setAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE,Long.valueOf(numberOfElements-1));
		return true;
	}

}

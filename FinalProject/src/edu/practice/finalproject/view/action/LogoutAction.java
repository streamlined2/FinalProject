package edu.practice.finalproject.view.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class LogoutAction extends Action {

	public LogoutAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) throws ServletException {
		FCServlet.removeAttribute(req, Names.USER_ATTRIBUTE);
		return true;
	}

}

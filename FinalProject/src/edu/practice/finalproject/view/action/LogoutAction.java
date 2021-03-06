package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class LogoutAction extends Action {

	public LogoutAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		FCServlet.invalidateSession(req);
		return true;
	}

}

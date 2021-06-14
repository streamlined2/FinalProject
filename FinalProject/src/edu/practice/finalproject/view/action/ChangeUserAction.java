package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class ChangeUserAction extends AdminAction {

	public ChangeUserAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final String role=FCServlet.getParameterValue(req, Names.ROLE_PARAMETER);
		FCServlet.setAttribute(req, Names.SELECTED_ROLE_ATTRIBUTE, role);			
		return true;
	}

}

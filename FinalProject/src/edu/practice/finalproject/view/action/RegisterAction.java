package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.userrole.Admin;

public class RegisterAction extends Action {

	public RegisterAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(final HttpServletRequest req,final EntityManager entityManager) {
		FCServlet.setAttribute(req, Names.ADMINISTRATIVE_TASKS_ATTRIBUTE, FCServlet.getUser(req) instanceof Admin);
		return true;
	}

}

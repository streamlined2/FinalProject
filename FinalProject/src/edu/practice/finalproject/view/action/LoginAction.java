package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FrontControllerServlet;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class LoginAction extends Action {
	
	public LoginAction(String name) {
		super(name);
	}

	private static final String ERROR_MSG="Error occured while trying to login. Please try again";

	@Override
	public boolean execute(final HttpServletRequest req,final EntityManager entityManager) {
		final String login=FrontControllerServlet.getParameterValue(req.getParameterMap(),FrontControllerServlet.USER_PARAMETER);
		if(login!=null) {
			final User user=entityManager.findByKey(User.class,login);
			if(user!=null) {
				FrontControllerServlet.setAttribute(req, FrontControllerServlet.USER_ATTRIBUTE, user);
				return true;
			}
		}
		FrontControllerServlet.setAttribute(req, FrontControllerServlet.ERROR_ATTRIBUTE, ERROR_MSG);
		return false;
	}

}

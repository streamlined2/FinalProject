package edu.practice.finalproject.view.action;

import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.admin.Client;
import edu.practice.finalproject.controller.admin.Admin;
import edu.practice.finalproject.controller.admin.Manager;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class LoginAction extends Action {
	
	public LoginAction(String name) {
		super(name);
	}

	private static final String ERROR_MSG="Error occured while trying to login. Please try again";
	
	private static Class<? extends User> mapNameToUserRoleClass(final String name){
		switch(name) {
			case FCServlet.CLIENT_ROLE_PARAMETER: return Client.class;
			case FCServlet.MANAGER_ROLE_PARAMETER: return Manager.class;
			case FCServlet.ADMIN_ROLE_PARAMETER: return Admin.class;
			default: throw new IllegalArgumentException("wrong user role");
		}
	}

	@Override
	public boolean execute(final HttpServletRequest req,final EntityManager entityManager) throws ServletException {
		final String role=FCServlet.getParameterValue(req, FCServlet.ROLE_PARAMETER);
		final String login=FCServlet.getParameterValue(req,FCServlet.USER_PARAMETER);
		if(FCServlet.checkPattern(login,FCServlet.LOGIN_PATTERN)) {
			try {
				final String password=FCServlet.getParameterValue(req, FCServlet.PASSWORD_PARAMETER);
				if(FCServlet.checkPattern(password,FCServlet.PASSWORD_PATTERN)) {
					final byte[] passwordDigest=FCServlet.getDigest(password.getBytes());
					final User user=entityManager.findByCompositeKey(
							mapNameToUserRoleClass(role),
							new String[] {"login","passwordDigest"},
							new Object[] {login,passwordDigest});
					if(user!=null) {
						FCServlet.setAttribute(req, FCServlet.USER_ATTRIBUTE, user);
						return true;
					}
				}
			} catch (NoSuchAlgorithmException e) {
				throw new ServletException(e);
			}
		}
		FCServlet.setAttribute(req, FCServlet.ERROR_ATTRIBUTE, ERROR_MSG);
		return false;
	}

}

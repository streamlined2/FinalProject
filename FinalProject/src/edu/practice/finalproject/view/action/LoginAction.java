package edu.practice.finalproject.view.action;

import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import utilities.Utilities;

public class LoginAction extends Action {
	
	public LoginAction(String name) {
		super(name);
	}

	private static final String ERROR_MSG="Error occured while trying to login. Please try again";
	
	@Override
	public boolean execute(final HttpServletRequest req,final EntityManager entityManager) throws ServletException {
		try {
			Utilities.checkIfValid(req,Names.USER_PARAMETER,Utilities::checkLogin);
			Utilities.checkIfValid(req,Names.PASSWORD_PARAMETER,Utilities::checkPassword);

			final String role=FCServlet.getParameterValue(req,Names.ROLE_PARAMETER);
			final String login=FCServlet.getParameterValue(req,Names.USER_PARAMETER);
			final byte[] passwordDigest=Utilities.getDigest(FCServlet.getParameterValue(req, Names.PASSWORD_PARAMETER).getBytes());

			final Optional<? extends User> user=entityManager.findByCompositeKey(
					Utilities.mapUserRoleToClass(role),
					new String[] {"login","passwordDigest"},
					new Object[] {login, passwordDigest});
			
			if(!user.isEmpty()) {
				FCServlet.setUser(req, user.get());
				return true;
			}
		}catch (Exception e) {
			FCServlet.setError(req, String.format("%s: %s",ERROR_MSG,e.getMessage()));
		}
		return false;
	}

}

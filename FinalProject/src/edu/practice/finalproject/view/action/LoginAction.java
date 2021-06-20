package edu.practice.finalproject.view.action;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.userrole.Admin;
import edu.practice.finalproject.model.entity.userrole.User;
import edu.practice.finalproject.utilities.Utils;

public class LoginAction extends Action {
	
	public LoginAction(String name) {
		super(name);
	}

	private static final String HELLO_EN = "Hello";
	private static final String HELLO_UK = "Добрий день";
	private static final String ERROR_MSG="Error occured while trying to login. Please try again";
	private static final String USER_BLOCKED_MSG = "Please talk to administrator to resolve login issue";
	
	@Override
	public boolean execute(final HttpServletRequest req,final EntityManager entityManager) {
		try {
			if(!Utils.checkIfValid(req,Names.USER_PARAMETER,Utils::checkLogin)) return false;
			if(!Utils.checkIfValid(req,Names.PASSWORD_PARAMETER,Utils::checkPassword)) return false;

			final String role=FCServlet.getParameterValue(req,Names.ROLE_PARAMETER);
			final String login=FCServlet.getParameterValue(req,Names.USER_PARAMETER);
			final byte[] passwordDigest=Utils.getDigest(FCServlet.getParameterValue(req, Names.PASSWORD_PARAMETER).getBytes());
			
			Optional<? extends User> user;
			final Admin primaryAdmin = (Admin)req.getServletContext().getAttribute(Names.PRIMARY_ADMIN_ATTRIBUTE);
			if(primaryAdmin.same(login, passwordDigest)) {
				user = Optional.of(primaryAdmin);
			}else {
				final Map<String,Object> keyPairs=new HashMap<>();
				keyPairs.put("login", login);
				keyPairs.put("passwordDigest", passwordDigest);
				user=entityManager.findByCompositeKey(Utils.mapUserRoleToClass(role),keyPairs);
			}
			
			if(!user.isEmpty()) {
				if(user.get().getBlocked()) {
					FCServlet.setError(req, USER_BLOCKED_MSG);
					return false;
				}
				FCServlet.setUser(req, user.get());
				FCServlet.setMessage(req, String.format("%s, %s!", FCServlet.getLocale(req)==Locale.ENGLISH?HELLO_EN:HELLO_UK,user.get().toString()));
				return true;
			}else {
				FCServlet.setError(req, ERROR_MSG);
			}
		}catch (Exception e) {
			FCServlet.setError(req, String.format("%s: %s",ERROR_MSG,e.getMessage()));
		}
		return false;
	}
}

package edu.practice.finalproject.view.action;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.DataAccessException;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.userrole.Admin;
import edu.practice.finalproject.model.entity.userrole.User;
import edu.practice.finalproject.utilities.Utils;

public class LoginAction extends Action {
	
	private static final Logger logger = LogManager.getLogger();

	public LoginAction(String name) {
		super(name);
	}

	private static final String HELLO_EN = "Hi";
	private static final String HELLO_UK = "�����";
	private static final String ERROR_MSG="Incorrect login or password. Please try again";
	private static final String USER_BLOCKED_MSG = "Your account is blocked by administrator";
	private static final String CAN_FIND_USER_MSG = "Cannot find user";
	
	@Override
	public boolean execute(final HttpServletRequest req,final EntityManager entityManager) {
		try {
			if(!Utils.checkIfValid(req,Names.USER_PARAMETER,Utils::checkLogin)) return false;
			if(!Utils.checkIfValid(req,Names.PASSWORD_PARAMETER,Utils::checkPassword)) return false;

			//final String role=FCServlet.getParameterValue(req,Names.ROLE_PARAMETER);
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
				user=entityManager.findByCompositeKey(User.class,keyPairs);//Utils.mapUserRoleToClass(role)
			}
			
			if(user.isPresent()) {
				if(user.get().getBlocked()) {
					FCServlet.setError(req, USER_BLOCKED_MSG);
					return false;
				}
				FCServlet.setUser(req, user.get());
				FCServlet.setMessage(req, String.format("%s, %s %s!", FCServlet.getLocale(req)==Locale.ENGLISH?HELLO_EN:HELLO_UK,user.get().getFirstName(),user.get().getLastName()));
				return true;
			}else {
				FCServlet.setError(req, ERROR_MSG);
				return false;
			}
		} catch(DataAccessException e) {
			logger.error(CAN_FIND_USER_MSG, e);
			FCServlet.setError(req, CAN_FIND_USER_MSG);
		} catch (Exception e) {
			logger.error(String.format("%s: %s",ERROR_MSG,e.getMessage()), e);
			FCServlet.setError(req, String.format("%s: %s",ERROR_MSG,e.getMessage()));
		}
		return false;
	}
}

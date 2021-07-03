package edu.practice.finalproject.view.action;

import java.util.HashMap;
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

	private static final String HELLO_MSG = "login.hello.message";
	private static final String ERROR_MSG="login.error.message";
	private static final String USER_BLOCKED_MSG = "login.user-blocked.message";
	private static final String CANT_FIND_USER_MSG = "login.cant-find-user.message";
	
	@Override
	public boolean execute(final HttpServletRequest req,final EntityManager entityManager) {
		try {
			if(!Utils.checkIfValid(req,Names.USER_PARAMETER,Utils::checkLogin)) return false;
			if(!Utils.checkIfValid(req,Names.PASSWORD_PARAMETER,Utils::checkPassword)) return false;

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
				user=entityManager.findByCompositeKey(User.class,keyPairs);
			}
			
			if(user.isPresent()) {
				if(user.get().getBlocked()) {
					FCServlet.setError(req, USER_BLOCKED_MSG);
					return false;
				}
				FCServlet.setUser(req, user.get());
				FCServlet.setMessage(req, HELLO_MSG,user.get().getFirstName(),user.get().getLastName());
				return true;
			}else {
				FCServlet.setError(req, ERROR_MSG);
				return false;
			}
		} catch(DataAccessException e) {
			logger.error(Utils.message(CANT_FIND_USER_MSG), e);
			FCServlet.setError(req, CANT_FIND_USER_MSG);
		} catch (Exception e) {
			logger.error(Utils.format(ERROR_MSG,e.getMessage()), e);
			FCServlet.setError(req, ERROR_MSG);
		}
		return false;
	}
}

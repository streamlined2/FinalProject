package edu.practice.finalproject.view.action;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.DataAccessException;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.userrole.Admin;
import edu.practice.finalproject.model.entity.userrole.User;
import edu.practice.finalproject.utilities.Utils;

public class RegisterNewAction extends Action {

	private static final Logger logger = LogManager.getLogger();

	public RegisterNewAction(String name) {
		super(name);
	}
	
	private static final String DIFFERENT_PASSWORDS = "register.new.action.different-passwords";
	private static final String USER_SUCCESSFULLY_REGISTERED = "register.new.action.successfully-registered";
	private static final String USER_ALREADY_REGISTERED = "register.new.action.login-taken";
	private static final String WRONG_FIRSTNAME_MSG = "register.new.action.wrong-first-name";
	private static final String WRONG_LASTNAME_MSG = "register.new.action.wrong-last-name";
	private static final String WRONG_USER_MSG = "register.new.action.wrong-user-name";
	private static final String WRONG_PASSWORD_MSG = "register.new.action.wrong-first-password";
	private static final String WRONG_PASSWORD2_MSG = "register.new.action.wrong-second-password";
	private static final String CANT_SAVE_NEW_USER_MSG = "register.new.action.cant-register-new-user";

	@Override
	public boolean execute(final HttpServletRequest req, final EntityManager entityManager) {
		try {
			if(!Utils.checkIfValid(req,Names.FIRSTNAME_PARAMETER,Utils::checkName)) {
				FCServlet.setError(req, FCServlet.localize(WRONG_FIRSTNAME_MSG));
				return false;
			}
			if(!Utils.checkIfValid(req,Names.LASTNAME_PARAMETER,Utils::checkName)) {
				FCServlet.setError(req, FCServlet.localize(WRONG_LASTNAME_MSG));
				return false;
			}
			if(!Utils.checkIfValid(req,Names.USER_PARAMETER,Utils::checkLogin)) {
				FCServlet.setError(req, FCServlet.localize(WRONG_USER_MSG));
				return false;
			}
			if(!Utils.checkIfValid(req,Names.PASSWORD_PARAMETER,Utils::checkPassword)) {
				FCServlet.setError(req, FCServlet.localize(WRONG_PASSWORD_MSG));
				return false;
			}
			if(!Utils.checkIfValid(req,Names.PASSWORD2_PARAMETER,Utils::checkPassword)) {
				FCServlet.setError(req, FCServlet.localize(WRONG_PASSWORD2_MSG));
				return false;
			}

			final String firstName=FCServlet.getParameterValue(req,Names.FIRSTNAME_PARAMETER);
			final String lastName=FCServlet.getParameterValue(req,Names.LASTNAME_PARAMETER);
			String role=Names.CLIENT_ROLE_PARAMETER;
			if(FCServlet.getUser(req) instanceof Admin) {
				role=FCServlet.getParameterValue(req,Names.ROLE_PARAMETER);
			}
			final String login=FCServlet.getParameterValue(req,Names.USER_PARAMETER);
			final byte[] password=FCServlet.getParameterValue(req,Names.PASSWORD_PARAMETER).getBytes();
			final byte[] password2=FCServlet.getParameterValue(req,Names.PASSWORD2_PARAMETER).getBytes();
			
			if(!Arrays.equals(password, password2)) {
				FCServlet.setError(req, FCServlet.localize(DIFFERENT_PASSWORDS));
				return false;
			}
			
			final Optional<? extends User> checkUser=entityManager.findByKey(Utils.mapUserRoleToClass(role),login);
			if(checkUser.isPresent()) {
				FCServlet.setError(req, FCServlet.localize(USER_ALREADY_REGISTERED));
				return false;
			}
			
			final User user=Inspector.createEntity(Utils.mapUserRoleToClass(role));
			user.setLogin(login);
			user.setPasswordDigest(Utils.getDigest(password));
			user.setFirstName(firstName);
			user.setLastName(lastName);
			entityManager.persist(user);
			
			if(!(FCServlet.getUser(req) instanceof Admin)) {
				FCServlet.setUser(req, user);
			}
			FCServlet.setMessage(req, FCServlet.localize(USER_SUCCESSFULLY_REGISTERED));
			return true;				
		} catch(EntityException | DataAccessException e) {
			logger.error(CANT_SAVE_NEW_USER_MSG, e);
			FCServlet.setError(req, FCServlet.localize(CANT_SAVE_NEW_USER_MSG));				
		} catch(Exception e) {
			logger.error(CANT_SAVE_NEW_USER_MSG, e);
			FCServlet.setError(req, String.format("%s: %s",FCServlet.localize(CANT_SAVE_NEW_USER_MSG),e.getMessage()));
		}
		return false;
	}
}

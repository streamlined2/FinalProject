package edu.practice.finalproject.view.action;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import utilities.Utils;

public class RegisterNewAction extends Action {

	public RegisterNewAction(String name) {
		super(name);
	}
	
	private static final String INTERNAL_ERROR = "Can't register new user";
	private static final String DIFFERENT_PASSWORDS = "Passwords should coincide";
	private static final String USER_SUCCESSFULLY_REGISTERED = "User successfully registered";
	private static final String USER_ALREADY_REGISTERED = "This login has been taken already";

	@Override
	public boolean execute(final HttpServletRequest req, final EntityManager entityManager) {
		try {
			if(!Utils.checkIfValid(req,Names.FIRSTNAME_PARAMETER,Utils::checkName)) return false;
			if(!Utils.checkIfValid(req,Names.LASTNAME_PARAMETER,Utils::checkName)) return false;
			if(!Utils.checkIfValid(req,Names.USER_PARAMETER,Utils::checkLogin)) return false;
			if(!Utils.checkIfValid(req,Names.PASSWORD_PARAMETER,Utils::checkPassword)) return false;
			if(!Utils.checkIfValid(req,Names.PASSWORD2_PARAMETER,Utils::checkPassword)) return false;

			final String firstName=FCServlet.getParameterValue(req,Names.FIRSTNAME_PARAMETER);
			final String lastName=FCServlet.getParameterValue(req,Names.LASTNAME_PARAMETER);
			final String role=Names.CLIENT_ROLE_PARAMETER;//FCServlet.getParameterValue(req,Names.ROLE_PARAMETER);
			final String login=FCServlet.getParameterValue(req,Names.USER_PARAMETER);
			final byte[] password=FCServlet.getParameterValue(req,Names.PASSWORD_PARAMETER).getBytes();
			final byte[] password2=FCServlet.getParameterValue(req,Names.PASSWORD2_PARAMETER).getBytes();
			
			if(!Arrays.equals(password, password2)) {
				FCServlet.setError(req, DIFFERENT_PASSWORDS);
				return false;
			}
			
			final Optional<? extends User> checkUser=entityManager.findByKey(Utils.mapUserRoleToClass(role),login);
			if(checkUser.isPresent()) {
				FCServlet.setError(req, USER_ALREADY_REGISTERED);
				return false;
			}
			
			final User user=Inspector.createEntity(Utils.mapUserRoleToClass(role));
			user.setLogin(login);
			user.setPasswordDigest(Utils.getDigest(password));
			user.setFirstName(firstName);
			user.setLastName(lastName);
			entityManager.persist(user);
			FCServlet.setUser(req, user);
			FCServlet.setMessage(req, USER_SUCCESSFULLY_REGISTERED);
			return true;
		}catch(Exception e) {
			FCServlet.setError(req, String.format("%s: %s",INTERNAL_ERROR,e.getMessage()));
		}
		return false;
	}
}

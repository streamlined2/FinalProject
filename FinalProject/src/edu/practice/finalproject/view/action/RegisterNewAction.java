package edu.practice.finalproject.view.action;

import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.admin.Client;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import utilities.Utils;

public class RegisterNewAction extends Action {

	public RegisterNewAction(String name) {
		super(name);
	}
	
	private static final String ERROR_MSG = "Can't register new user";
	private static final String DIFFERENT_PASSWORDS_ERROR = "Passwords should coincide!";
	private static final String USER_SUCCESSFULLY_REGISTERED_MSG = "User successfully registered";

	@Override
	public boolean execute(final HttpServletRequest req, final EntityManager entityManager) throws ServletException {
		try {
			if(!Utils.checkIfValid(req,Names.FIRSTNAME_PARAMETER,Utils::checkName)) return false;
			if(!Utils.checkIfValid(req,Names.LASTNAME_PARAMETER,Utils::checkName)) return false;
			if(!Utils.checkIfValid(req,Names.USER_PARAMETER,Utils::checkLogin)) return false;
			if(!Utils.checkIfValid(req,Names.PASSWORD_PARAMETER,Utils::checkPassword)) return false;
			if(!Utils.checkIfValid(req,Names.PASSWORD2_PARAMETER,Utils::checkPassword)) return false;

			final String firstName=FCServlet.getParameterValue(req,Names.FIRSTNAME_PARAMETER);
			final String lastName=FCServlet.getParameterValue(req,Names.LASTNAME_PARAMETER);
			final String role=FCServlet.getParameterValue(req,Names.ROLE_PARAMETER);
			final String login=FCServlet.getParameterValue(req,Names.USER_PARAMETER);
			final byte[] password=FCServlet.getParameterValue(req,Names.PASSWORD_PARAMETER).getBytes();
			final byte[] password2=FCServlet.getParameterValue(req,Names.PASSWORD2_PARAMETER).getBytes();
			
			if(!Arrays.equals(password, password2)) {
				FCServlet.setError(req, DIFFERENT_PASSWORDS_ERROR);
				return false;
			}
			
			final User user=Inspector.createEntity(Utils.mapUserRoleToClass(role));
			user.setLogin(login);
			user.setPasswordDigest(Utils.getDigest(password));
			if(user instanceof Client) {
				((Client)user).setFirstName(firstName);
				((Client)user).setLastName(lastName);
			}
			entityManager.persist(user);
			FCServlet.setUser(req, user);
			FCServlet.setMessage(req, USER_SUCCESSFULLY_REGISTERED_MSG);
			return true;
		
		}catch(Exception e) {
			FCServlet.setError(req, String.format("%s: %s",ERROR_MSG,e.getMessage()));
		}
		return false;
	}
}

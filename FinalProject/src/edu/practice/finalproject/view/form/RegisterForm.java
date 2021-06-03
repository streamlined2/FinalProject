package edu.practice.finalproject.view.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.view.action.Action;

public class RegisterForm extends Form {

	public RegisterForm(String name) {
		super(name);
	}

	@Override
	public void init(final HttpServletRequest req, final EntityManager entityManager) {
		req.setAttribute(Names.LOGIN_PATTERN_ATTRIBUTE, Names.LOGIN_PATTERN);
		req.setAttribute(Names.PASSWORD_PATTERN_ATTRIBUTE, Names.PASSWORD_PATTERN);
		req.setAttribute(Names.NAME_PATTERN_ATTRIBUTE, Names.NAME_PATTERN);
	}
	
	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.isActionPresent(parameters,Names.REGISTER_NEW_PARAMETER))
			return FormDispatcher.REGISTER_NEW_ACTION;
		return super.getAction(parameters);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.BACK_ACTION;
	}
}

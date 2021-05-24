package edu.practice.finalproject.view.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class LoginForm extends Form {

	public LoginForm(final String name) {
		super(name);
	}
	
	@Override
	public void init(final HttpServletRequest req) {
		req.setAttribute(FCServlet.LOGIN_PATTERN_ATTRIBUTE, FCServlet.LOGIN_PATTERN);
		req.setAttribute(FCServlet.PASSWORD_PATTERN_ATTRIBUTE, FCServlet.PASSWORD_PATTERN);
	}
	
	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.isActionPresent(parameters,FCServlet.REGISTER_PARAMETER))
			return FormDispatcher.REGISTER_ACTION;
		return getDefaultAction();
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.LOGIN_ACTION;
	}
	
}

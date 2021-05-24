package edu.practice.finalproject.view.form;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class RegisterForm extends Form {

	public RegisterForm(String name) {
		super(name);
	}

	@Override
	public void init(final HttpServletRequest req) {
		req.setAttribute(FCServlet.LOGIN_PATTERN_ATTRIBUTE, FCServlet.LOGIN_PATTERN);
		req.setAttribute(FCServlet.PASSWORD_PATTERN_ATTRIBUTE, FCServlet.PASSWORD_PATTERN);
		req.setAttribute(FCServlet.NAME_PATTERN_ATTRIBUTE, FCServlet.NAME_PATTERN);
		req.setAttribute(FCServlet.PASSPORT_PATTERN_ATTRIBUTE, FCServlet.PASSPORT_PATTERN);
	}
	
	@Override
	public Action getDefaultAction() {
		return FormDispatcher.REGISTER_ACTION;
	}

}

package edu.practice.finalproject.view.form;

import java.util.Map;

import edu.practice.finalproject.controller.FrontControllerServlet;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

public class LoginForm extends Form {

	public LoginForm(final String name) {
		super(name);
	}
	
	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FrontControllerServlet.isActionPresent(parameters,"register")) return FormDispatcher.REGISTER_ACTION;
		return getDefaultAction();
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.LOGIN_ACTION;
	}
	
}

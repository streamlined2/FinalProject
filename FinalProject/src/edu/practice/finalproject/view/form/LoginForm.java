package edu.practice.finalproject.view.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.view.action.Action;

public class LoginForm extends ActionMapForm {
	private static final Map<String,Action> ACTION_MAP = Map.of(
			Names.REGISTER_PARAMETER,FormDispatcher.REGISTER_ACTION
	);

	public LoginForm(final String name) {
		super(name);
	}
	
	@Override
	protected Map<String, Action> getActionMap() {
		return ACTION_MAP;
	}

	@Override
	public void init(final HttpServletRequest req, final EntityManager entityManager) {
		req.setAttribute(Names.LOGIN_PATTERN_ATTRIBUTE, Names.LOGIN_PATTERN);
		req.setAttribute(Names.PASSWORD_PATTERN_ATTRIBUTE, Names.PASSWORD_PATTERN);
	}
	
	@Override
	public Action getDefaultAction() {
		return FormDispatcher.LOGIN_ACTION;
	}	
}

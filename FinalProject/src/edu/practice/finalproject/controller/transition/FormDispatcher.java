package edu.practice.finalproject.controller.transition;

import edu.practice.finalproject.controller.admin.Admin;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.LoginAction;
import edu.practice.finalproject.view.action.RegisterAction;
import edu.practice.finalproject.view.form.Form;
import edu.practice.finalproject.view.form.LoginForm;
import edu.practice.finalproject.view.form.RegisterForm;

public class FormDispatcher {

	private final Admin admin;
	private final Form initialForm;
	
	public FormDispatcher(final String initialUserName,final byte[] initialUserPassword) {
		admin=new Admin(initialUserName,initialUserPassword);
		initialForm=LOGIN_FORM;
	}
	
	public static final LoginAction LOGIN_ACTION=new LoginAction("login");
	public static final RegisterAction REGISTER_ACTION=new RegisterAction("register");

	public static final LoginForm LOGIN_FORM=new LoginForm("/login.jsp");
	public static final RegisterForm REGISTER_FORM=new RegisterForm("/register.jsp");
	
	private final TransitionRuleMap transitions=new TransitionRuleMap();
	{
		transitions.addRule(null, LOGIN_FORM, LOGIN_ACTION, null);
		transitions.addRule(null, LOGIN_FORM, REGISTER_ACTION, REGISTER_FORM);
		//TODO fill up rest of the transition rule map
	}

	public Form getInitialForm() { return initialForm;}

	public Form getNextForm(final User user,final Form form,final Action action,final boolean actionSucceeded) {
		if(!actionSucceeded) return form;
		final Form nextForm=transitions.getNextForm(user, form, action);
		if(nextForm!=null) return nextForm;
		return initialForm;
	}
	
}

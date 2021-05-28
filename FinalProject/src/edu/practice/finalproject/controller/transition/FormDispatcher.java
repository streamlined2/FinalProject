package edu.practice.finalproject.controller.transition;

import edu.practice.finalproject.controller.admin.Admin;
import edu.practice.finalproject.controller.admin.Client;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.BackAction;
import edu.practice.finalproject.view.action.ConfirmCarCriteriaAction;
import edu.practice.finalproject.view.action.SwitchLocaleAction;
import edu.practice.finalproject.view.action.LoginAction;
import edu.practice.finalproject.view.action.LogoutAction;
import edu.practice.finalproject.view.action.RegisterAction;
import edu.practice.finalproject.view.action.RegisterNewAction;
import edu.practice.finalproject.view.form.CarSelectionCriteriaForm;
import edu.practice.finalproject.view.form.Form;
import edu.practice.finalproject.view.form.LoginForm;
import edu.practice.finalproject.view.form.RegisterForm;

public class FormDispatcher {

	private final Admin admin;
	
	public FormDispatcher(final String initialUserName,final byte[] initPwdDigest) {
		admin=new Admin(initialUserName,initPwdDigest);
	}
	
	public static final LoginAction LOGIN_ACTION=new LoginAction("login");
	public static final RegisterAction REGISTER_ACTION=new RegisterAction("register");
	public static final RegisterNewAction REGISTER_NEW_ACTION=new RegisterNewAction("register_new");
	public static final BackAction BACK_ACTION=new BackAction("back");
	public static final ConfirmCarCriteriaAction CONFIRM_CAR_CRITERIA_ACTION=new ConfirmCarCriteriaAction("confirm_car_criteria");
	public static final SwitchLocaleAction SWITCH_LOCALE_ACTION=new SwitchLocaleAction("change_locale");
	public static final LogoutAction LOGOUT_ACTION=new LogoutAction("logout");

	public static final LoginForm LOGIN_FORM=new LoginForm("/login.jsp");
	public static final RegisterForm REGISTER_FORM=new RegisterForm("/register.jsp");
	public static final CarSelectionCriteriaForm CAR_SELECTION_CRITERIA_FORM=new CarSelectionCriteriaForm("/car-selection.jsp");
	
	private final TransitionRuleMap transitions=new TransitionRuleMap();
	{
		transitions.addRule(null, LOGIN_FORM, REGISTER_ACTION, REGISTER_FORM);
		transitions.addRule(null, REGISTER_FORM, BACK_ACTION, LOGIN_FORM);
		//last rule for Client
		transitions.addRule(Client.class, null, null, CAR_SELECTION_CRITERIA_FORM);
		//TODO fill up rest of the transition rule map
	}

	public Form getInitialForm() { return LOGIN_FORM;}

	public Form getNextForm(final User user,final Form form,final Action action,final boolean actionSucceeded) {
		if(!actionSucceeded) return form;
		if(action==SWITCH_LOCALE_ACTION) return form;
		if(action==LOGOUT_ACTION) return getInitialForm();
		return transitions.getNextForm(user, form, action).orElse(getInitialForm());
	}
	
}

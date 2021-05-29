package edu.practice.finalproject.controller;

import java.util.Locale;

public abstract class Names {
	
	private Names() {}

	public static final String USER_ATTRIBUTE = "user";
	public static final String FORM_ATTRIBUTE = "form";
	public static final String ERROR_ATTRIBUTE = "error";
	public static final String MESSAGE_ATTRIBUTE = "message";
	public static final String LOCALE_ATTRIBUTE = "locale";
	public static final String LOGIN_PATTERN_ATTRIBUTE = "loginPattern"; 
	public static final String PASSWORD_PATTERN_ATTRIBUTE = "passwordPattern"; 
	public static final String NAME_PATTERN_ATTRIBUTE = "namePattern"; 
	public static final String PASSPORT_PATTERN_ATTRIBUTE = "passportPattern";
	public static final String AVAILABLE_LOCALES_ATTRIBUTE = "availableLocales";
	public static final String MANUFACTURER_VALUES_ATTRIBUTE = "manufacturerValues";
	public static final String QUALITY_GRADES_VALUES_ATTRIBUTE = "qualityGradeValues";
	public static final String COLOR_VALUES_ATTRIBUTE = "colorValues";
	public static final String STYLE_VALUES_ATTRIBUTE = "styleValues";

	public static final String ACTION_PARAMETER = "action";
	public static final String USER_PARAMETER = "user";
	public static final String ROLE_PARAMETER = "role";
	public static final String CLIENT_ROLE_PARAMETER = "client";
	public static final String ADMIN_ROLE_PARAMETER = "admin";
	public static final String LOCALE_PARAMETER = "locale";
	public static final String LOGOUT_PARAMETER = "logout";
	public static final String MANAGER_ROLE_PARAMETER = "manager";
	public static final String PASSWORD_PARAMETER = "password";
	public static final String PASSWORD2_PARAMETER = "password2";
	public static final String PASSPORT_PARAMETER = "passport";
	public static final String REGISTER_PARAMETER = "register";
	public static final String REGISTER_NEW_PARAMETER = "register_new";
	public static final String FIRSTNAME_PARAMETER = "firstname";
	public static final String LASTNAME_PARAMETER = "lastname";
	
	public static final String LOGIN_PATTERN = "[0-9A-Za-z�-��-���������]+";
	public static final String PASSWORD_PATTERN = "[0-9A-Za-z�-��-���������]+";
	public static final String NAME_PATTERN = "[0-9A-Za-z�-��-���������]+";
	public static final String PASSPORT_PATTERN = "[ 0-9A-Za-z�-��-���������]+";

}

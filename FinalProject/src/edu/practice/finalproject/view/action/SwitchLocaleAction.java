package edu.practice.finalproject.view.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class SwitchLocaleAction extends Action {
	
	public SwitchLocaleAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final String language=FCServlet.getParameterValue(req,Names.LOCALE_PARAMETER);
		final Locale locale = FCServlet.getAcceptableLocale(language);
		Locale.setDefault(locale);
		FCServlet.setLocale(req,locale);
		return true;
	}

}

package edu.practice.finalproject.view.action;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class SwitchLocaleAction extends Action {
	
	public static final String LOCALE_SWITCHED_TO_ENGLISH_MESSAGE = "Locale switched to English";
	public static final String LOCALE_SWITCHED_TO_UKRAINIAN_MESSAGE = "Локаль змінено на Українську";

	public SwitchLocaleAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) throws ServletException {
		final String language=FCServlet.getParameterValue(req,Names.LOCALE_PARAMETER);
		final Locale locale = FCServlet.getAcceptableLocale(language);
		Locale.setDefault(locale);
		FCServlet.setLocale(req,locale);
		FCServlet.setMessage(req, //TODO replace with resource bundle message
				locale==Locale.ENGLISH?
						LOCALE_SWITCHED_TO_ENGLISH_MESSAGE:
							LOCALE_SWITCHED_TO_UKRAINIAN_MESSAGE);
		return true;
	}

}

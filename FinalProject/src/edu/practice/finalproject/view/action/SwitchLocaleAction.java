package edu.practice.finalproject.view.action;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class SwitchLocaleAction extends Action {

	public SwitchLocaleAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) throws ServletException {
		final String language=FCServlet.getParameterValue(req,Names.LOCALE_PARAMETER,Names.ENGLISH_LOCALE);
		switch(language) {
		case Names.ENGLISH_LOCALE:
		case Names.UKRAINIAN_LOCALE:
			final Locale locale = Locale.forLanguageTag(language);
			FCServlet.setLocale(req,locale);
			FCServlet.setMessage(req, locale==Locale.ENGLISH?"Locale switched to English":"������ ������ �� ���������");
			return true;
		default:
			throw new ServletException("wrong locale");
		}
	}

}
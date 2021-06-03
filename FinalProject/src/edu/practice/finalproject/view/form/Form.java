package edu.practice.finalproject.view.form;

import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.view.action.Action;

/**
 * Represents interface form with basic functionality
 * @author Serhii Pylypenko
 *
 */
public abstract class Form {
	
	protected final String name;
	
	protected Form(final String name) {
		this.name=name;
	}
	
	public void init(HttpServletRequest req, final EntityManager entityManager) {}
	public void destroy(HttpServletRequest req) {}
	
	public String getName() { return name;}
	
	public Action getAction(Map<String,String[]> parameters) {	
		if(FCServlet.isActionPresent(parameters,Names.LOCALE_PARAMETER)) return FormDispatcher.SWITCH_LOCALE_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.LOGOUT_PARAMETER)) return FormDispatcher.LOGOUT_ACTION;
		return getDefaultAction();	
	}
	
	public abstract Action getDefaultAction();

	@Override
	public boolean equals(final Object o) {
		if(o instanceof Form) {
			final Form form=(Form)o;
			return name.equals(form.name);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	
}

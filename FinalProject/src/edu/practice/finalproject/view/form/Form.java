package edu.practice.finalproject.view.form;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.view.action.Action;

/**
 * Base abstract class which represents interface form with basic functionality
 * @author Serhii Pylypenko
 *
 */
public abstract class Form implements Serializable {
	
	protected final String name;
	
	protected Form(final String name) {
		this.name=name;
	}
	
	/**
	 * Post construction method to initialize form before it gets displayed to user. Necessary resources may be allocated, request/session attributes may be set. 
	 * @param req servlet request
	 * @param entityManager an EntityManager instance to operate data with
	 */
	public void init(HttpServletRequest req, final EntityManager entityManager) {}
	
	/**
	 * Pre destruction method to release resources and clean unnecessary data before form gets destroyed
	 * @param req servlet request
	 */
	public void destroy(HttpServletRequest req) {}
	
	public String getName() { return name;}

	/**
	 * Basic action mapping behavior for every form
	 * @param parameters request parameters
	 * @return Action object determined by analysis of given {@code parameters}
	 */
	public Action getAction(Map<String,String[]> parameters) {	
		if(FCServlet.isActionPresent(parameters,Names.LOCALE_PARAMETER)) return FormDispatcher.SWITCH_LOCALE_ACTION;
		if(FCServlet.isActionPresent(parameters,Names.LOGOUT_PARAMETER)) return FormDispatcher.LOGOUT_ACTION;
		return getDefaultAction();	
	}
	
	/**
	 * Default action object for this form class.
	 * @return default action object
	 */
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

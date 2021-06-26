package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.model.dataaccess.EntityManager;

/**
 * Base abstract class for any action of user administrator role
 * @author Serhii Pylypenko
 *
 */
public abstract class AdminAction extends Action {
	
	protected AdminAction(final String name) {
		super(name);
	}

	@Override
	public boolean execute(final HttpServletRequest req,final EntityManager entityManager) {
		return false;
	}

}

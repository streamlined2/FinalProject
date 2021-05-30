package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.model.dataaccess.EntityManager;

public abstract class AdminAction extends Action {
	
	public AdminAction(final String name) {
		super(name);
	}

	@Override
	public boolean execute(final HttpServletRequest req,final EntityManager entityManager) {
		return false;
	}

}

package edu.practice.finalproject.view.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.model.dataaccess.EntityManager;

public abstract class ClientAction extends Action {

	public ClientAction(final String name) {
		super(name);
	}

	@Override
	public boolean execute(final HttpServletRequest req,final EntityManager entityManager) throws ServletException {
		return false;
	}

}

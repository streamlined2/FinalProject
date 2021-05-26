package edu.practice.finalproject.view.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.model.dataaccess.EntityManager;

public class BackAction extends Action {

	public BackAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) throws ServletException {
		return true;
	}

}

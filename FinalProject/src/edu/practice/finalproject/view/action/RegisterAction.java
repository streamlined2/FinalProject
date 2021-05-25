package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.model.dataaccess.EntityManager;

public class RegisterAction extends Action {

	public RegisterAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(final HttpServletRequest req,final EntityManager entityManager) {
		return true;
	}

}

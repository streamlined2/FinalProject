package edu.practice.finalproject.view.action;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.model.dataaccess.EntityManager;

public class CarManagementAction extends AdminAction {

	public CarManagementAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		return true;
	}

}

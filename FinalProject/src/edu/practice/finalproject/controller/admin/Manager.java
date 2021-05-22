package edu.practice.finalproject.controller.admin;

import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.ManagerAction;

public class Manager extends User {

	public Manager(final String login,final byte[] password) {
		super(login, password);
	}
	
	public Manager() {}

	@Override
	public void checkPermission(Action action) throws SecurityException {
		if(!(action instanceof ManagerAction)) throw new SecurityException("Manager may perform appropriate actions only!");
	}

}

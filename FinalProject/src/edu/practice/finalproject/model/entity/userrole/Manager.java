package edu.practice.finalproject.model.entity.userrole;

import edu.practice.finalproject.model.entity.userrole.User.Role;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.ManagerAction;

public class Manager extends User {

	public Manager(final String login,final byte[] passwordDigest, String firstName, String lastName)  {
		super(login, passwordDigest, firstName, lastName);
	}
	
	public Manager() {}
	
	@Override
	public void checkPermission(Action action) throws SecurityException {
		if(!(action instanceof ManagerAction)) throw new SecurityException("Manager may perform appropriate actions only!");
	}

	@Override
	public Role role() { return Role.MANAGER;	}
}

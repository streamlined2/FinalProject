package edu.practice.finalproject.model.entity.userrole;

import edu.practice.finalproject.model.entity.userrole.User.Role;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.AdminAction;

public class Admin extends User {

	public Admin(final String login,final byte[] passwordDigest, String firstName, String lastName)  {
		super(login, passwordDigest, firstName, lastName);
	}
	
	public Admin() {}

	@Override
	public void checkPermission(Action action) throws SecurityException {
		if(!(action instanceof AdminAction)) throw new SecurityException("Administrator may perform appropriate actions only!");
	}

	@Override
	public Role role() { return Role.ADMIN;}
}

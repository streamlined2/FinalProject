package edu.practice.finalproject.controller.admin;

import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.ClientAction;

public class Client extends User {
	
	public Client(final String login,final byte[] passwordDigest, String firstName, String lastName)  {
		super(login, passwordDigest, firstName, lastName);
	}
	
	public Client() {}
	
	@Override
	public void checkPermission(Action action) throws SecurityException {
		if(!(action instanceof ClientAction)) throw new SecurityException("Client may perform appropriate actions only!");
	}
}

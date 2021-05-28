package edu.practice.finalproject.controller.admin;

import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.ClientAction;

public class Client extends User {
	
	private String firstName;
	private String lastName;

	public Client(String login, byte[] passwordDigest, String firstName, String lastName) {
		super(login, passwordDigest);
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Client() {}
	
	public String getFirstName() { return firstName;}
	public void setFirstName(final String firstName) { this.firstName=firstName;}
	
	public String getLastName() { return lastName;}
	public void setLastName(final String lastName) { this.lastName=lastName;}
	
	@Override
	public void checkPermission(Action action) throws SecurityException {
		if(!(action instanceof ClientAction)) throw new SecurityException("Client may perform appropriate actions only!");
	}
	
	@Override public String toString() {
		return new StringBuilder(firstName).append(" ").append(lastName).toString();
	}

}

package edu.practice.finalproject.controller.admin;

import java.security.NoSuchAlgorithmException;

import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.ClientAction;

public class Client extends User {
	
	private String firstName;
	private String lastName;
	private String passport;

	public Client(String login, byte[] password, String firstName, String lastName, String passport) throws NoSuchAlgorithmException {
		super(login, password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.passport = passport;
	}
	
	public Client() {}
	
	public String getFirstName() { return firstName;}
	public void setFirstName(final String firstName) { this.firstName=firstName;}
	
	public String getLastName() { return lastName;}
	public void setLastName(final String lastName) { this.lastName=lastName;}
	
	public String getPassport() { return passport;}
	public void setPassport(final String passport) { this.passport=passport;}

	@Override
	public void checkPermission(Action action) throws SecurityException {
		if(!(action instanceof ClientAction)) throw new SecurityException("Client may perform appropriate actions only!");
	}

}

package edu.practice.finalproject.controller.admin;

import edu.practice.finalproject.model.entity.NaturalKeyEntity;
import edu.practice.finalproject.view.action.Action;

public abstract class User extends NaturalKeyEntity<String> {
	protected String login;
	protected byte[] passwordDigest;
	protected String firstName;
	protected String lastName;
	
	protected User() {}
	
	protected User(final String login,final byte[] passwordDigest, String firstName, String lastName)  {
		this.login=login;
		this.passwordDigest=passwordDigest;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getLogin() { return login;}
	public void setLogin(final String login) { this.login=login;}
	
	public byte[] getPasswordDigest() { return passwordDigest;}
	public void setPasswordDigest(final byte[] passwordDigest) { this.passwordDigest=passwordDigest;}

	@Override
	public boolean equals(final Object o) {
		if(o instanceof User) {
			final User user=(User)o;
			return login.equals(user.login);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return login.hashCode();
	}
	
	public abstract void checkPermission(final Action action) throws SecurityException;

	@Override
	protected String getKey() {
		return getLogin();
	}

	@Override
	protected String keyFieldGetter() {
		return "getLogin";
	}
	
	@Override public String toString() {
			return new StringBuilder(firstName).append(" ").append(lastName).toString();
	}

	public String getFirstName() { return firstName;}
	public void setFirstName(final String firstName) { this.firstName=firstName;}

	public String getLastName() { return lastName;}
	public void setLastName(final String lastName) { this.lastName=lastName;}

}

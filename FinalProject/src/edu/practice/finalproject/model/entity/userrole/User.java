package edu.practice.finalproject.model.entity.userrole;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;
import edu.practice.finalproject.view.action.Action;

public abstract class User extends NaturalKeyEntity {
	public enum Role {
		CLIENT("client"),
		MANAGER("manager"),
		ADMIN("admin");
		
		private final String label;
		Role(final String label){ this.label = label;}
		public String getLabel() { return label;}
		@Override public String toString() { return label;}
	}
	
	protected String login;
	protected byte[] passwordDigest;
	protected String firstName;
	protected String lastName;
	protected boolean blocked = false;
	
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

	public String getFirstName() { return firstName;}
	public void setFirstName(final String firstName) { this.firstName=firstName;}

	public String getLastName() { return lastName;}
	public void setLastName(final String lastName) { this.lastName=lastName;}
	
	public boolean getBlocked() { return blocked;}
	public void setBlocked(final boolean blocked) { this.blocked = blocked;}

	@Override
	public boolean equals(final Object o) {
		if(o instanceof User) {
			final User user=(User)o;
			return login.equals(user.login);
		}
		return false;
	}
	
	public boolean same(final String login,final byte[] passwordDigest) {
		if(Objects.isNull(login) || Objects.isNull(passwordDigest)) return false;
		return this.login.equals(login) && Arrays.equals(this.passwordDigest, passwordDigest);
	}
	
	@Override
	public int hashCode() {
		return login.hashCode();
	}
	
	public abstract void checkPermission(final Action action) throws SecurityException;
	public abstract Role role();

	@Override public String toString() {
			return new StringBuilder(firstName).append(" ").append(lastName).toString();
	}
	
	public String getDescription() {
		return new StringBuilder(firstName).append(" ").append(lastName).append(" (").append(role().getLabel()).append(")").toString();
	}

	@Override
	public List<Method> keyGetters() {
		try {
			return List.of(User.class.getMethod("getLogin"));
		} catch (NoSuchMethodException | SecurityException e) {
			throw new EntityException(e);
		}
	}
}

package edu.practice.finalproject.controller.admin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.entity.NaturalKeyEntity;
import edu.practice.finalproject.view.action.Action;

public abstract class User extends NaturalKeyEntity<String> {
	
	protected String login;
	protected byte[] passwordDigest;
	
	protected User() {}
	
	protected User(final String login,final byte[] password)  {
		this.login=login;
		try {
			this.passwordDigest=getDigest(password);
		} catch (NoSuchAlgorithmException e) {
			throw new EntityException(e);
		}
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

	private static final String SHA_256="SHA-256";

	public static byte[] getDigest(final byte[] input) throws NoSuchAlgorithmException {
    	final MessageDigest md=MessageDigest.getInstance(SHA_256);
    	md.update(input);
        return md.digest();
    }
	
    private static String byteArray2String(final byte[] bytes) {
    	final StringBuilder sb=new StringBuilder();
    	for(final byte b:bytes) {
    		sb.append(String.format("%02X", b));
    	}
    	return sb.toString();
    }

}

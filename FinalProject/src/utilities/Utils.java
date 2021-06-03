package utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.admin.Admin;
import edu.practice.finalproject.controller.admin.Client;
import edu.practice.finalproject.controller.admin.Manager;
import edu.practice.finalproject.controller.admin.User;

public abstract class Utils {
	
	private Utils() {}

	public static final String SHA_256="SHA-256";

	public static byte[] getDigest(final byte[] input) throws NoSuchAlgorithmException {
		return getDigest(input,SHA_256);
	}
	
	public static byte[] getDigest(final byte[] input,final String algorithm) throws NoSuchAlgorithmException {
		final MessageDigest md=MessageDigest.getInstance(algorithm);
		md.update(input);
	    return md.digest();
	}
	
	public static boolean isAscendingOrder(final String flag) {
		return Objects.nonNull(flag) && flag.equals("asc");
	}

	public static boolean checkPattern(final String value, final String pattern) {
		if(value==null || pattern==null) return false;
		return Pattern.matches(pattern, value);
	}
	
	public static boolean checkLogin(final String login) {
		return Utils.checkPattern(login,Names.LOGIN_PATTERN);
	}
	
	public static boolean checkPassword(final String password) {
		return Utils.checkPattern(password,Names.PASSWORD_PATTERN);
	}

	public static boolean checkName(final String name) {
		return Utils.checkPattern(name,Names.NAME_PATTERN);
	}

	public static boolean checkPassport(final String name) {
		return Utils.checkPattern(name,Names.PASSPORT_PATTERN);
	}

	public static void checkIfValid(final HttpServletRequest req,final String parameter,final Predicate<String> checker) {
		if(parameter==null) throw new IllegalArgumentException("parameter "+parameter+" shouldn't be null");
		final String value=FCServlet.getParameterValue(req,parameter);
		if(!checker.test(value)) {
			throw new IllegalArgumentException(String.format("wrong value %s of parameter %s", value, parameter));
		}
	}

	public static Class<? extends User> mapUserRoleToClass(final String name){
		switch(name) {
			case Names.CLIENT_ROLE_PARAMETER: return Client.class;
			case Names.MANAGER_ROLE_PARAMETER: return Manager.class;
			case Names.ADMIN_ROLE_PARAMETER: return Admin.class;
			default: throw new IllegalArgumentException("wrong user role");
		}
	}

	public static String byteArray2String(final byte[] bytes) {
		final StringBuilder sb=new StringBuilder();
		for(final byte b:bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
	
}

package edu.practice.finalproject.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.entity.userrole.Admin;
import edu.practice.finalproject.model.entity.userrole.Client;
import edu.practice.finalproject.model.entity.userrole.Manager;
import edu.practice.finalproject.model.entity.userrole.User;

/**
 * Set of common purpose utility methods
 * @author Serhii Pylypenko
 *
 */
public final class Utils {
	
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
		if(Objects.isNull(value) || Objects.isNull(pattern)) return false;
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
	
	public static boolean checkAccount(final String name) {
		return Utils.checkPattern(name,Names.ACCOUNT_PATTERN);
	}
	
	public static boolean checkCarModel(final String name) {
		return Utils.checkPattern(name,Names.CAR_MODEL_PATTERN);
	}
	
	public static boolean checkTime(final String dueTime) {
		try {
			LocalDateTime.parse(dueTime);
			return true;
		}catch(DateTimeParseException e) {
			return false;
		}
	}
	
	public static Optional<LocalDateTime> getTime(final String time) {
		try {
			return Optional.of(LocalDateTime.parse(time));		
		}catch(DateTimeParseException e) {
			return Optional.empty();
		}
	}

	public static boolean checkIfValid(final HttpServletRequest req,final String parameter,final Predicate<String> checker) {
		if(Objects.isNull(parameter)) throw new IllegalArgumentException("parameter "+parameter+" shouldn't be null");
		final String value=FCServlet.getParameterValue(req,parameter);
		if(Objects.isNull(value)) return false;
		if(!checker.test(value)) {
			FCServlet.setError(req, String.format("wrong value %s of parameter %s", value, parameter));
			return false;	
		}
		return true;
	}

	public static Class<? extends User> mapUserRoleToClass(final String name){
		if(Objects.isNull(name)) throw new IllegalArgumentException("wrong user role value: null");
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
	
	public static String escapeQuote(final String str) {
		return str.replace("'", "''");
	}
}

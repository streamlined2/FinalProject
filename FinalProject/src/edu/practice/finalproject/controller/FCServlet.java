package edu.practice.finalproject.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.form.Form;

/**
 * Main servlet class that implements Front Controller patterm
 * @author Serhii Pylypenko
 */
public class FCServlet extends HttpServlet {
	
	private static final int NO_APPROPRIATE_FORM_MAPPING=1;
	
	public static final String USER_ATTRIBUTE = "user";
	public static final String FORM_ATTRIBUTE = "form";
	public static final String ERROR_ATTRIBUTE = "error";
	public static final String LOGIN_PATTERN_ATTRIBUTE = "loginPattern"; 
	public static final String PASSWORD_PATTERN_ATTRIBUTE = "passwordPattern"; 
	public static final String NAME_PATTERN_ATTRIBUTE = "namePattern"; 
	public static final String PASSPORT_PATTERN_ATTRIBUTE = "passportPattern";
	public static final String ACTION_PARAMETER = "action";
	public static final String USER_PARAMETER = "user";
	public static final String ROLE_PARAMETER = "role";
	public static final String CLIENT_ROLE_PARAMETER = "client";
	public static final String ADMIN_ROLE_PARAMETER = "admin";
	public static final String MANAGER_ROLE_PARAMETER = "manager";
	public static final String PASSWORD_PARAMETER = "password";
	public static final String REGISTER_PARAMETER = "register";
	
	public static final String LOGIN_PATTERN = "[0-9A-Za-zÀ-ßà-ÿ¨¸ªº²³¯¿]+";
	public static final String PASSWORD_PATTERN = "[0-9A-Za-zÀ-ßà-ÿ¨¸ªº²³¯¿]+";
	public static final String NAME_PATTERN = "[0-9A-Za-zÀ-ßà-ÿ¨¸ªº²³¯¿]+";
	public static final String PASSPORT_PATTERN = "[ 0-9A-Za-zÀ-ßà-ÿ¨¸ªº²³¯¿]+";

	private EntityManager entityManager;
	private FormDispatcher formDispatcher;
	
	public FCServlet() {
		super();
	}
	
    @Override
	public void init() throws ServletException {
		try{
			final Context envContext=(Context)new InitialContext().lookup("java:/comp/env");
			entityManager=new EntityManager((DataSource)envContext.lookup("jdbc/autoleasing"));
			
			formDispatcher=new FormDispatcher(
					getServletContext().getInitParameter("adminUserName"),
					getDigest(getServletContext().getInitParameter("adminPassword").getBytes()));
		
		}catch(NamingException | NoSuchAlgorithmException e) {
			throw new ServletException(e);
		}
	}

    private void process(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {
		setAttribute(req,ERROR_ATTRIBUTE,null);
		Form currentForm=(Form)getAttribute(req,FORM_ATTRIBUTE);
		if(currentForm==null) {
			currentForm=formDispatcher.getInitialForm();
		}else {
			final Action action=currentForm.getAction(req.getParameterMap());
			final boolean actionSucceeded=action.execute(req,entityManager);
			final User user=(User)getAttribute(req,USER_ATTRIBUTE);
			final Form nextForm=formDispatcher.getNextForm(user, currentForm, action, actionSucceeded);
			if(nextForm!=null) {
				currentForm=nextForm;
			}else {
				try {
					resp.sendError(NO_APPROPRIATE_FORM_MAPPING);
				} catch (IOException e) {
					throw new ServletException(e);
				}
			}
		}
		setAttribute(req, FORM_ATTRIBUTE, currentForm);
		render(currentForm, req, resp);
	}
	
	private void render(final Form form,final HttpServletRequest req,final HttpServletResponse resp) throws ServletException {
		try {
			form.init(req);
			getServletContext().getRequestDispatcher(form.getName()).forward(req,resp);
		} catch (IOException e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {
		process(req,resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {
		process(req,resp);
	}

	public static boolean setAttribute(final HttpServletRequest req,final String name,final Object value) {
		final HttpSession session=req.getSession();
		if(session!=null) {
			session.setAttribute(name, value);
			return true;
		}
		return false;
	}
	
	public static Object getAttribute(final HttpServletRequest req,final String name) {
		final HttpSession session=req.getSession();
		if(session!=null) {
			return session.getAttribute(name);
		}
		return null;
	}
	
	public static String getParameterValue(final HttpServletRequest req,final String parameter) {
		return getParameterValue(req.getParameterMap(),parameter);
	}

	public static String getParameterValue(final Map<String,String[]> parameters,final String parameter) {
		final String[] values=parameters.get(parameter);
		if(values!=null && values.length>0) {
			return values[0];
		}
		return null;
	}
	
	public static boolean isActionPresent(final Map<String,String[]> parameters,final String actionName) {
		final String[] values=parameters.get(ACTION_PARAMETER);
		return values!=null && Arrays.asList(values).contains(actionName); 
	}
	
	private static final String SHA_256="SHA-256";

	public static byte[] getDigest(final byte[] input) throws NoSuchAlgorithmException {
    	final MessageDigest md=MessageDigest.getInstance(SHA_256);
    	md.update(input);
        return md.digest();
    }
	
	public static boolean checkPattern(final String value, final String pattern) {
		if(value==null) return false;
		return Pattern.matches(pattern, value);
	}

}

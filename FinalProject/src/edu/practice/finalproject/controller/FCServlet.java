package edu.practice.finalproject.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.practice.finalproject.controller.admin.Admin;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.form.Form;
import utilities.Utils;

/**
 * Main servlet class that implements Front Controller patterm
 * @author Serhii Pylypenko
 */
public class FCServlet extends HttpServlet {
	
	private EntityManager entityManager;
	private FormDispatcher formDispatcher;
	
	private Admin primaryAdmin;
	
	public static final Locale UKRAINIAN_LOCALE = Locale.forLanguageTag("uk");
	private static final List<Locale> availableLocales= List.of(Locale.ENGLISH,UKRAINIAN_LOCALE);
	
	public FCServlet() {
		super();
	}
	
    @Override
	public void init() throws ServletException {
		try{
			final Context envContext=(Context)new InitialContext().lookup("java:/comp/env");
			entityManager=new EntityManager((DataSource)envContext.lookup("jdbc/autoleasing"));

			formDispatcher=new FormDispatcher();
			
			primaryAdmin=new Admin(
					getServletContext().getInitParameter("adminUserName"),
					Utils.getDigest(getServletContext().getInitParameter("adminPassword").getBytes()),
					"Primary","User");

			getServletContext().setAttribute(Names.PRIMARY_ADMIN_ATTRIBUTE, primaryAdmin);
			
			getServletContext().setAttribute(Names.AVAILABLE_LOCALES_ATTRIBUTE,availableLocales);
			
			getServletContext().setAttribute(
					Names.PAGE_ELEMENTS_NUMBER_ATTRIBUTE, 
					Integer.parseInt(getServletContext().getInitParameter("pageElements")));

			Locale.setDefault(getDefaultLocale());
			
		}catch(NamingException | NoSuchAlgorithmException e) {
			throw new ServletException(e);
		}
	}

	private static final int NO_APPROPRIATE_FORM_MAPPING_CODE=1;
	private static final String NO_APPROPRIATE_FORM_MAPPING_MSG="No appropriate mapping for given user, form and action!"; 
	
    private void process(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {	
    	initLocale(req);
		clearError(req);
		clearMessage(req);
		Form currentForm=getForm(req);
		if(currentForm==null) {
			currentForm=formDispatcher.getInitialForm();
		}else {
			final Action action=currentForm.getAction(req.getParameterMap());
			final boolean actionSucceeded=action.execute(req,entityManager);
			currentForm.destroy(req);
			final User user=getUser(req);
			final Form nextForm=formDispatcher.getNextForm(user, currentForm, action, actionSucceeded);
			if(nextForm!=null) {
				currentForm=nextForm;
			}else {
				try {
					setError(req,NO_APPROPRIATE_FORM_MAPPING_MSG);
					resp.sendError(NO_APPROPRIATE_FORM_MAPPING_CODE,NO_APPROPRIATE_FORM_MAPPING_MSG);
				} catch (IOException e) {
					throw new ServletException(e);
				}
			}
		}
		setForm(req, currentForm);
		currentForm.init(req,entityManager);
		try {
			getServletContext().getRequestDispatcher(currentForm.getName()).forward(req,resp);
			//resp.sendRedirect(getServletContext().getContextPath()+currentForm.getName());
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
	
	public static Form getForm(final HttpServletRequest req) {
		return (Form)getAttribute(req,Names.FORM_ATTRIBUTE);
	}
	
	public static void setForm(final HttpServletRequest req, final Form form) {
		setAttribute(req, Names.FORM_ATTRIBUTE, form);
	}
	
	public static void clearForm(final HttpServletRequest req) {
		removeAttribute(req, Names.FORM_ATTRIBUTE);
	}

	public static User getUser(final HttpServletRequest req) {
		return (User)getAttribute(req,Names.USER_ATTRIBUTE);
	}
	
	public static void setUser(final HttpServletRequest req, final User user) {
		setAttribute(req, Names.USER_ATTRIBUTE, user);
	}
	
	public static void clearUser(final HttpServletRequest req) {
		removeAttribute(req, Names.USER_ATTRIBUTE);
	}

	public static void setError(final HttpServletRequest req,final String message) {
		setAttribute(req,Names.ERROR_ATTRIBUTE,message);
	}
	
	public static void clearError(final HttpServletRequest req) {
		removeAttribute(req,Names.ERROR_ATTRIBUTE);
	}
	
	public static void setMessage(final HttpServletRequest req,final String message) {
		setAttribute(req,Names.MESSAGE_ATTRIBUTE,message);
	}
	
	public static void clearMessage(final HttpServletRequest req) {
		removeAttribute(req,Names.MESSAGE_ATTRIBUTE);
	}
	
	public static void initLocale(final HttpServletRequest req) {
		if(getAttribute(req, Names.LOCALE_ATTRIBUTE)==null) {
			setAttribute(req, Names.LOCALE_ATTRIBUTE, getDefaultLocale());
		}
	}
	
	public static Locale getDefaultLocale() {
		return Locale.ENGLISH;
	}
	
	public static Locale getAcceptableLocale(final String language){
		if(Objects.isNull(language)) return getDefaultLocale();
		final Locale locale=Locale.forLanguageTag(language);
		if(availableLocales.contains(locale)) return locale; 
		return getDefaultLocale();
	}
	
	public static void setLocale(final HttpServletRequest req,final Locale locale) {
		setAttribute(req, Names.LOCALE_ATTRIBUTE, locale);
	}
	
	public static Locale getLocale(final HttpServletRequest req) {
		return (Locale)getAttribute(req, Names.LOCALE_ATTRIBUTE);
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
	
	public static Object getAttribute(final HttpServletRequest req,final String name,final Object defValue) {
		final HttpSession session=req.getSession();
		if(session!=null) {
			final Object value=session.getAttribute(name);
			if(value!=null) return value;
		}
		return defValue;
	}
	
	public static void removeAttribute(final HttpServletRequest req,final String name) {
		final HttpSession session=req.getSession();
		if(session!=null) {
			session.removeAttribute(name);
		}
	}
	
	public static String getParameterValue(final HttpServletRequest req,final String parameter) {
		return getParameterValue(req.getParameterMap(),parameter);
	}

	public static String getParameterValue(final HttpServletRequest req,final String parameter,final String defaultValue) {
		final String value=getParameterValue(req.getParameterMap(),parameter);
		return Objects.isNull(value)?defaultValue:value;
	}

	public static String getParameterValue(final Map<String,String[]> parameters,final String parameter) {
		final String[] values=parameters.get(parameter);
		if(values!=null && values.length>0) {
			return values[0];
		}
		return null;
	}
	
	public static boolean ifParameterEquals(final Map<String,String[]> parameters,final String parameter,final String value) {
		final String[] values=parameters.get(parameter);
		return values!=null && Arrays.asList(values).contains(value); 
	}

	public static boolean isActionPresent(final Map<String,String[]> parameters,final String actionName) {
		final String[] values=parameters.get(Names.ACTION_PARAMETER);
		return values!=null && Arrays.asList(values).contains(actionName); 
	}
	
	public static void invalidateSession(final HttpServletRequest req) {
		final HttpSession session=req.getSession(false);
		if(session!=null) {
			session.invalidate();
		}
	}
}

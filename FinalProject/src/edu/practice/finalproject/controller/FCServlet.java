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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.userrole.Admin;
import edu.practice.finalproject.model.entity.userrole.User;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.form.Form;
import edu.practice.finalproject.utilities.Utils;

/**
 * Main servlet class that implements Front Controller pattern
 * @author Serhii Pylypenko
 */
public class FCServlet extends HttpServlet {
	
	private EntityManager entityManager;
	private FormDispatcher formDispatcher;
	
	private static final Locale UKRAINIAN_LOCALE = Locale.forLanguageTag("uk");
	private static final List<Locale> availableLocales= List.of(Locale.ENGLISH,UKRAINIAN_LOCALE);
	
	private static final Logger logger = LogManager.getLogger();
	
	private static final int NO_APPROPRIATE_FORM_MAPPING_CODE=1;
	private static final String NO_APPROPRIATE_FORM_MAPPING_MSG="No appropriate mapping for given user, form and action!";
	private static final String CANT_TRANSFER_TO_ERROR_PAGE_MSG = "Can't transfer to error page"; 
	private static final String SERVLET_INITIALIZATION_ERROR_MSG = "Servlet cannot be initialized properly";
	private static final String CANT_TRANSFER_TO_NEXT_FORM_MSG = "Can't transfer to next form page";
	private static final String ACCESS_VIOLATION_MSG = "Access violation";
	
	public static final String MAPPING_PATTERN = "main";
	
	public FCServlet() {
		super();
	}
	
	/**
	 * Locates and injects external resource references, reads and initializes servlet's parameters, sets application attributes
	 */
    @Override
	public void init() throws ServletException {
		try{
			final Context envContext=(Context)new InitialContext().lookup("java:/comp/env");
			entityManager = new EntityManager((DataSource)envContext.lookup("jdbc/autoleasing"));
			
			formDispatcher = FormDispatcher.getInstance();

			final Admin primaryAdmin = new Admin(
					getServletContext().getInitParameter("adminUserName"),
					Utils.getDigest(getServletContext().getInitParameter("adminPassword").getBytes()),
					"Primary","User");

			getServletContext().setAttribute(Names.PRIMARY_ADMIN_ATTRIBUTE, primaryAdmin);
			
			getServletContext().setAttribute(Names.AVAILABLE_LOCALES_ATTRIBUTE,availableLocales);
			
			getServletContext().setAttribute(Names.ENTITY_MANAGER_ATTRIBUTE, entityManager);
			
			getServletContext().setAttribute(
					Names.PAGE_ELEMENTS_NUMBER_ATTRIBUTE, 
					Integer.parseInt(getServletContext().getInitParameter("pageElements")));

			Locale.setDefault(getDefaultLocale());
			
		}catch(NamingException | NoSuchAlgorithmException e) {
			logger.fatal(SERVLET_INITIALIZATION_ERROR_MSG, e);
			throw new ServletException(e);
		}
	}

    /**
     * Main servlet method which processes request by selecting and initializing next user interface form, 
     * checking permissions, getting and performing action, and forwarding to selected form  
     * @param req request
     * @param resp response
     * @throws ServletException wraps underlying layer exceptions such as SecurityException, IOException
     */
    private void process(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {
    	initLocale(req);
		clearError(req);
		clearMessage(req);
		
		Form currentForm=getForm(req);
		if(currentForm==null) {
			currentForm=getFormDispatcher().getInitialForm();
		}else {
			Action action=currentForm.getAction(req.getParameterMap());
			try{
				checkAccess(req, action);
				boolean succeeded=action.execute(req, entityManager);
				currentForm.destroy(req);
				Form nextForm=getFormDispatcher().getNextForm(getUser(req), currentForm, action, succeeded);
				if(nextForm!=null) {
					currentForm=nextForm;
				} else {
					setError(req,NO_APPROPRIATE_FORM_MAPPING_MSG);
					logger.fatal(NO_APPROPRIATE_FORM_MAPPING_MSG);
					resp.sendError(NO_APPROPRIATE_FORM_MAPPING_CODE,NO_APPROPRIATE_FORM_MAPPING_MSG);
				}
			} catch(SecurityException e) {
				logger.fatal(ACCESS_VIOLATION_MSG, e);
				throw new ServletException(e);
			} catch(IOException e) {
				logger.fatal(CANT_TRANSFER_TO_ERROR_PAGE_MSG, e);
				throw new ServletException(e);
			}
		}
		
		setForm(req, currentForm);
		currentForm.init(req, entityManager);
		try {
			getServletContext().getRequestDispatcher(currentForm.getName()).forward(req,resp);
		} catch (IOException e) {
			logger.fatal(CANT_TRANSFER_TO_NEXT_FORM_MSG, e);
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
	
	public FormDispatcher getFormDispatcher() { return formDispatcher;}
	
	private static void checkAccess(HttpServletRequest req, Action action) throws SecurityException {
		User user=getUser(req);
		if(Objects.nonNull(user)) {
			user.checkPermission(action);
		}
	}
	
	public Form getForm(final HttpServletRequest req) {
		return (Form)getAttribute(req,Names.FORM_ATTRIBUTE);
	}
	
	private void setForm(final HttpServletRequest req, final Form form) {
		setAttribute(req, Names.FORM_ATTRIBUTE, form);
	}
	
	private void clearForm(final HttpServletRequest req) {
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
	
	public static Integer getPageElements(final HttpServletRequest req) {
		return Objects.requireNonNullElse((Integer)req.getServletContext().getAttribute(Names.PAGE_ELEMENTS_NUMBER_ATTRIBUTE), Integer.valueOf(7));
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
		return Objects.requireNonNullElse(getParameterValue(req.getParameterMap(),parameter),defaultValue);
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

	public static boolean isActionPresent(final HttpServletRequest req,final String actionName) {
		return isActionPresent(req.getParameterMap(), actionName);
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

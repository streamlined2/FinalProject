package edu.practice.finalproject.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

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
public class FrontControllerServlet extends HttpServlet {
	
	private static final int NO_APPROPRIATE_FORM_MAPPING=1;
	
	public static final String USER_ATTRIBUTE = "user";
	public static final String FORM_ATTRIBUTE = "form";
	public static final String ERROR_ATTRIBUTE = "error";
	public static final String ACTION_PARAMETER = "action";
	public static final String USER_PARAMETER = "user";
	
	private EntityManager entityManager;
	private FormDispatcher formDispatcher;
	
	public FrontControllerServlet() {
		super();
	}
	
    @Override
	public void init() throws ServletException {
		try{
			final Context envContext=(Context)new InitialContext().lookup("java:/comp/env");
			entityManager=new EntityManager((DataSource)envContext.lookup("jdbc/autoleasing"));
			
			formDispatcher=new FormDispatcher(
					getServletContext().getInitParameter("adminUserName"),
					getServletContext().getInitParameter("adminPassword").getBytes());
		
		}catch(NamingException e) {
			throw new ServletException(e);
		}
	}

    private void process(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {
		User user=(User)getAttribute(req,USER_ATTRIBUTE);
		Form currentForm=(Form)getAttribute(req,FORM_ATTRIBUTE);
		setAttribute(req,ERROR_ATTRIBUTE,null);
		if(currentForm==null) {
			currentForm=formDispatcher.getInitialForm();
		}else {
			final Action action=currentForm.getAction(req.getParameterMap());
			final boolean actionSucceeded=action.execute(req,entityManager);
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
	
}

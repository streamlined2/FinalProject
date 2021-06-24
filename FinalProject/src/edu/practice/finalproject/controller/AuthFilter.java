package edu.practice.finalproject.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet filter which checks for incorrect references within JSP pages 
 * that do not refer to front controller servlet and prevents unauthorized access
 * @author Serhii Pylypenko
 */
public class AuthFilter implements Filter {
	
	private static final Logger logger = LogManager.getLogger();
	private static final String WRONG_REQUEST_MSG = "Access to the page %s is prohibited";
	
	private static final String WELCOME_PAGE_PARAMETER = "welcomePage";
	private static final String SERVLET_PATH_PARAMETER = "servletPath";
	private static final String CONTEXT_PATH_PARAMETER = "contextPath";

	private final List<String> allowedPages = new ArrayList<>();

	private boolean check;
	private String contextPath;
	
	private boolean correctServletPath(HttpServletRequest req) {
		String path = req.getServletPath();
		return allowedPages.contains(path);
	}
	
	private boolean correctContextPath(HttpServletRequest req) {
		return req.getContextPath().equals(contextPath);
	}
	
	private boolean mayPass(HttpServletRequest req) {
		return 
				!check ||
				correctServletPath(req) && correctContextPath(req);
	}

	/**
	 * Checks if reference to follow refers to front controller servlet or other allowed pages, proceeds to next filter/servlet or error page by throwing exception
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response; 
		
		if(mayPass(req)) {
			chain.doFilter(req, resp);
		} else {
			FCServlet.invalidateSession(req);
			logger.error(WRONG_REQUEST_MSG,req.getRequestURI());
			throw new ServletException(String.format(WRONG_REQUEST_MSG,req.getRequestURI()));
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		check = "true".equalsIgnoreCase(fConfig.getInitParameter("check"));
		contextPath = fConfig.getInitParameter(CONTEXT_PATH_PARAMETER);
		allowedPages.add("/styles.css");
		allowedPages.add("/logoutbutton.png");
		allowedPages.add(fConfig.getInitParameter(SERVLET_PATH_PARAMETER));
		allowedPages.add(fConfig.getInitParameter(WELCOME_PAGE_PARAMETER));
	}
}
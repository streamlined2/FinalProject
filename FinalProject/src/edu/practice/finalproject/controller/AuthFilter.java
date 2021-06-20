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

/**
 * Servlet Filter implementation class AuthFilter
 */
public class AuthFilter implements Filter {
	
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

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response; 
		
		if(mayPass(req)) {
			chain.doFilter(req, resp);
		} else {
			FCServlet.invalidateSession(req);
			throw new ServletException(String.format(WRONG_REQUEST_MSG,req.getRequestURI()));
			//resp.sendError(1, String.format(WRONG_REQUEST_MSG,req.getRequestURI()));
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
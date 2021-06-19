package edu.practice.finalproject.controller;

import java.io.IOException;
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
	
	private static final String WELCOME_PAGE_PARAMETER = "welcomePage";
	private static final String SERVLET_PATH_PARAMETER = "servletPath";
	private static final String CONTEXT_PATH_PARAMETER = "contextPath";

	private static final String WRONG_REQUEST_MSG = "Access to the page %s is prohibited";
	
	private boolean check;
	private String welcomePage;
	private String servletPath;
	private String contextPath;
	
	private boolean correctServletPath(HttpServletRequest req) {
		String path = req.getServletPath();
		return path.equals(welcomePage) || path.equals(servletPath);
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
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		check = "true".equalsIgnoreCase(fConfig.getInitParameter("check"));
		servletPath = fConfig.getInitParameter(SERVLET_PATH_PARAMETER);
		welcomePage = fConfig.getInitParameter(WELCOME_PAGE_PARAMETER);
		contextPath = fConfig.getInitParameter(CONTEXT_PATH_PARAMETER);
	}
}

package edu.practice.finalproject.controller;

import java.util.Enumeration;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Session listener which removes and clears every related attribute
 * @author Serhii Pylypenko
 */
public class CleanSessionListener implements HttpSessionListener {

	@Override
    public void sessionDestroyed(HttpSessionEvent se)  {
		HttpSession session = se.getSession();
    	Enumeration<String> i = session.getAttributeNames();
    	while(i.hasMoreElements()) {
    		session.removeAttribute(i.nextElement());
    	}
    }
}

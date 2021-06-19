package edu.practice.finalproject.controller;

import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class CleanSessionListener
 *
 */
public class CleanSessionListener implements HttpSessionListener {

	@Override
    public void sessionDestroyed(HttpSessionEvent se)  {
    	HttpSession session = se.getSession();
    	Iterator<String> i = session.getAttributeNames().asIterator();
    	while(i.hasNext()) {
    		session.removeAttribute(i.next());
    	}
    }
}

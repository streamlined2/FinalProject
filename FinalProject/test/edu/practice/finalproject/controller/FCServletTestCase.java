package edu.practice.finalproject.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.form.Form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

//@ExtendWith(MockitoExtension.class)
public class FCServletTestCase {

	private HttpServletRequest req = mock(HttpServletRequest.class);
	private HttpServletResponse resp = mock(HttpServletResponse.class);
	private RequestDispatcher dispatcher = mock(RequestDispatcher.class);
	
	private FCServlet servlet = new FCServlet();
	
	//private AutoCloseable mocks;
	
	@BeforeEach
	public void setUp() {
		//mocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	public void tearDown() {
		//mocks.close();
	}

	@Test
	public void testForInitLocale() throws ServletException {
		HttpSession session = mock(HttpSession.class);
		when(req.getSession()).thenReturn(session);
		when(req.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		servlet.doPost(req, resp);
		verify(session).setAttribute(Names.LOCALE_ATTRIBUTE, FCServlet.getDefaultLocale());
	}
	
	@Test
	public void testForClearErrorMessage() throws ServletException {
		HttpSession session = mock(HttpSession.class);
		when(req.getSession()).thenReturn(session);
		when(req.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		servlet.doGet(req, resp);
		verify(session).removeAttribute(Names.ERROR_ATTRIBUTE);
		verify(session).removeAttribute(Names.MESSAGE_ATTRIBUTE);
	}
	
	@Test
	public void testForNoFormAttribute() throws ServletException, IOException {
		HttpSession session = mock(HttpSession.class);
		when(req.getRequestDispatcher(FormDispatcher.LOGIN_FORM.getName())).thenReturn(dispatcher);
		when(req.getSession()).thenReturn(session);
		servlet.doGet(req, resp);
		verify(req, atLeastOnce()).getSession();
		verify(dispatcher).forward(req, resp);
		verify(session).setAttribute(Names.FORM_ATTRIBUTE, FormDispatcher.LOGIN_FORM);
	}

	@Test
	public void testForFormAttributePresentActionSucceeded() throws ServletException, IOException {
		HttpSession session = mock(HttpSession.class);
		when(req.getSession()).thenReturn(session);
		Form form = mock(Form.class);
		when(session.getAttribute(Names.FORM_ATTRIBUTE)).thenReturn(form);
		Action action  = mock(Action.class);
		when(form.getAction(any())).thenReturn(action);
		Form newForm = mock(Form.class);
		FCServlet servletMock = mock(FCServlet.class);
		when(servletMock.getNextForm(req, form, action, true)).thenReturn(newForm);
		when(req.getRequestDispatcher(newForm.getName())).thenReturn(dispatcher);
		servlet.doGet(req, resp);
		verify(req, atLeastOnce()).getSession();
		verify(dispatcher).forward(req, resp);
		verify(session).setAttribute(eq(Names.FORM_ATTRIBUTE), eq(newForm));
	}

	@Test
	public void testForFormAttributePresentActionFailed() throws ServletException, IOException {
		HttpSession session = mock(HttpSession.class);
		when(req.getSession()).thenReturn(session);
		Form form = mock(Form.class);
		when(session.getAttribute(Names.FORM_ATTRIBUTE)).thenReturn(form);
		Action action  = mock(Action.class);
		when(form.getAction(any())).thenReturn(action);
		Form newForm = mock(Form.class);
		FCServlet servletMock = mock(FCServlet.class);
		when(servletMock.getNextForm(req, form, action, true)).thenReturn(newForm);
		when(req.getRequestDispatcher(newForm.getName())).thenReturn(dispatcher);
		servlet.doGet(req, resp);
		verify(req, atLeastOnce()).getSession();
		verify(dispatcher).forward(req, resp);
		assertEquals(form.getName(),newForm.getName());
	}
}

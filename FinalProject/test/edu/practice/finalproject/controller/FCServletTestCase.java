package edu.practice.finalproject.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class FCServletTestCase {

	@Mock private HttpServletRequest req;
	@Mock private HttpServletResponse resp;
	
	private FCServlet servlet = new FCServlet();
	
	AutoCloseable mocks;
	
	@BeforeAll
	void setUp() throws Exception {
		mocks = MockitoAnnotations.openMocks(FCServletTestCase.class);
	}

	@AfterAll
	void tearDown() throws Exception {
		mocks.close();
	}

	@Test
	void testForInitialPage() throws ServletException {
		when(servlet.getForm(req)).thenReturn(null);
		servlet.doGet(req, resp);
		verify(req,never()).getSession();
	}
}

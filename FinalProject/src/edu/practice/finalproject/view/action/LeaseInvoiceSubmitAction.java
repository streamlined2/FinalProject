package edu.practice.finalproject.view.action;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.document.LeaseInvoice;
import edu.practice.finalproject.model.entity.document.OrderReview;
import edu.practice.finalproject.model.entity.userrole.Manager;
import edu.practice.finalproject.utilities.Utils;

public class LeaseInvoiceSubmitAction extends ManagerAction {

	private static final String INCORRECT_INVOICE_SUM_VALUE_MSG = "The value of invoice sum is incorrect";
	private static final String INCORRECT_ACCOUNT_VALUE_MSG = "You entered incorrect bank account";

	public LeaseInvoiceSubmitAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final Manager manager = (Manager)FCServlet.getUser(req);
		final OrderReview orderReview = (OrderReview)FCServlet.getAttribute(req, Names.ORDER_REVIEW_ATTRIBUTE);

		if(!Utils.checkIfValid(req,Names.ACCOUNT_PARAMETER,Utils::checkAccount)) {
			FCServlet.setError(req, INCORRECT_ACCOUNT_VALUE_MSG);
			return false;
		}

		final String account = FCServlet.getParameterValue(req,Names.ACCOUNT_PARAMETER);
		final String sumValue = FCServlet.getParameterValue(req,Names.INVOICE_SUM_PARAMETER);

		try {
			final BigDecimal sum = new BigDecimal(sumValue);
			final LeaseInvoice invoice = Inspector.createEntity(LeaseInvoice.class);
			invoice.setAccount(account);
			invoice.setLeaseOrder(orderReview.getLeaseOrder());
			invoice.setSum(sum);
			invoice.setSignTime(LocalDateTime.now());
			invoice.setManager(manager);
			entityManager.persist(invoice);
			FCServlet.setAttribute(req, Names.LEASE_INVOICE_ATTRIBUTE, invoice);
			return true;
		}catch(NumberFormatException e) {
			FCServlet.setError(req, INCORRECT_INVOICE_SUM_VALUE_MSG);
			return false;
		}
	}

}

package edu.practice.finalproject.view.form;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.document.OrderReview;
import edu.practice.finalproject.view.action.Action;

public class NewLeaseInvoiceForm extends Form {

	public NewLeaseInvoiceForm(String name) {
		super(name);
	}

	@Override
	public Action getAction(final Map<String,String[]> parameters) {
		if(FCServlet.isActionPresent(parameters,Names.SEND_LEASE_INVOICE_PARAMETER)) return FormDispatcher.LEASE_INVOICE_SUBMISSION_ACTION;
		return super.getAction(parameters);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.BACK_ACTION;
	}

	@Override
	public void init(HttpServletRequest req, EntityManager entityManager) {
		final OrderReview orderReview = (OrderReview)FCServlet.getAttribute(req, Names.ORDER_REVIEW_ATTRIBUTE);
		final BigDecimal cost = orderReview.getLeaseOrder().getCar().getCost();
		final long hours = orderReview.getLeaseOrder().getStartTime().until(orderReview.getLeaseOrder().getEndTime(), ChronoUnit.HOURS);
		final BigDecimal invoiceSum = cost.multiply(BigDecimal.valueOf(hours));
		req.setAttribute(Names.INVOICE_SUM_ATTRIBUTE, invoiceSum);
		req.setAttribute(Names.ACCOUNT_PATTERN_ATTRIBUTE, Names.ACCOUNT_PATTERN);
	}

}

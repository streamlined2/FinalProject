package edu.practice.finalproject.view.action;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.DataAccessException;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.document.CarReview;
import edu.practice.finalproject.model.entity.document.MaintenanceInvoice;
import edu.practice.finalproject.model.entity.userrole.Manager;
import edu.practice.finalproject.utilities.Utils;

public class MaintenanceInvoiceSubmitAction extends ManagerAction {

	private static final Logger logger = LogManager.getLogger();

	private static final String INCORRECT_INVOICE_SUM_VALUE_MSG = "The value of invoice sum is incorrect";
	private static final String INCORRECT_ACCOUNT_VALUE_MSG = "You entered incorrect bank account";
	private static final String CANT_SAVE_MAINTENANCE_INVOICE_MSG = "Cannot save maintenance invoice";
	private static final String MAINTENANCE_INVOICE_SAVED_MSG = "Maintenance invoice saved";

	public MaintenanceInvoiceSubmitAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final Manager manager = (Manager)FCServlet.getUser(req);
		final CarReview carReview = (CarReview)FCServlet.getAttribute(req, Names.CAR_REVIEW_ATTRIBUTE);

		if(!Utils.checkIfValid(req,Names.ACCOUNT_PARAMETER,Utils::checkAccount)) {
			FCServlet.setError(req, INCORRECT_ACCOUNT_VALUE_MSG);
			return false;
		}

		final String account = FCServlet.getParameterValue(req,Names.ACCOUNT_PARAMETER);
		final String sumValue = FCServlet.getParameterValue(req,Names.INVOICE_SUM_PARAMETER);
		final String repairs = FCServlet.getParameterValue(req,Names.REPAIRS_PARAMETER);

		try {
			final BigDecimal sum = new BigDecimal(sumValue);
			final MaintenanceInvoice invoice = Inspector.createEntity(MaintenanceInvoice.class);
			invoice.setLeaseOrder(carReview.getLeaseOrder());
			invoice.setRepairs(repairs);
			invoice.setManager(manager);
			invoice.setSignTime(LocalDateTime.now());
			invoice.setAccount(account);
			invoice.setSum(sum);
			entityManager.persist(invoice);
			FCServlet.setAttribute(req, Names.MAINTENANCE_INVOICE_ATTRIBUTE, invoice);
			FCServlet.setMessage(req, MAINTENANCE_INVOICE_SAVED_MSG);
			return true;
		} catch(NumberFormatException e) {
			logger.error(INCORRECT_INVOICE_SUM_VALUE_MSG, e);
			FCServlet.setError(req, INCORRECT_INVOICE_SUM_VALUE_MSG);
			return false;
		} catch(EntityException | DataAccessException e) {
			logger.error(CANT_SAVE_MAINTENANCE_INVOICE_MSG, e);
			FCServlet.setError(req, CANT_SAVE_MAINTENANCE_INVOICE_MSG);
			return false;
		}
	}
}

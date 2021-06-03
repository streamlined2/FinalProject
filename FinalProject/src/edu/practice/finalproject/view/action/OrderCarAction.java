package edu.practice.finalproject.view.action;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.admin.Client;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.document.LeaseOrder;
import edu.practice.finalproject.model.entity.domain.Car;
import utilities.Utils;

public class OrderCarAction extends ClientAction {

	public OrderCarAction(String name) {
		super(name);
	}
	
	private static final String WRONG_START_OR_DUE_TIME_MSG = "Lease start time %c should precede due time %c";
	private static final String WRONG_START_TIME_MSG = "Lease start time %c should follow current time %c";
	private static final String WRONG_USER_MSG = "You should be logged as a client";
	private static final String WRONG_CAR_MSG = "You should select car first";

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) throws ServletException {
		final User user=FCServlet.getUser(req);
		if(!(user instanceof Client)) {
			FCServlet.setError(req, WRONG_USER_MSG);
			return false;
		}
		
		final Client client = (Client) user;

		final Car car = (Car)FCServlet.getAttribute(req, Names.SELECTED_CAR_ATTRIBUTE);
		if(Objects.isNull(car)) {
			FCServlet.setError(req, WRONG_CAR_MSG);
			return false;
		}

		if(!Utils.checkIfValid(req,Names.PASSPORT_PARAMETER,Utils::checkPassport)) return false;
		if(!Utils.checkIfValid(req,Names.DUE_TIME_PARAMETER,Utils::checkTime)) return false;
		if(!Utils.checkIfValid(req,Names.LEASE_START_TIME_PARAMETER,Utils::checkTime)) return false;
		
		final String passport=FCServlet.getParameterValue(req,Names.PASSPORT_PARAMETER);
		final String driver=FCServlet.getParameterValue(req,Names.DRIVER_OPTION_PARAMETER);
		final Optional<LocalDateTime> startTime=Utils.getTime(FCServlet.getParameterValue(req,Names.LEASE_START_TIME_PARAMETER));
		final Optional<LocalDateTime> dueTime=Utils.getTime(FCServlet.getParameterValue(req,Names.DUE_TIME_PARAMETER));
		
		if(startTime.get().isBefore(LocalDateTime.now())) {
			FCServlet.setError(req, String.format(WRONG_START_TIME_MSG, startTime.get(), dueTime.get()) );
			return false;
		}
		
		if(dueTime.get().isBefore(startTime.get())) {
			FCServlet.setError(req, String.format(WRONG_START_OR_DUE_TIME_MSG, startTime.get(), dueTime.get()) );
			return false;
		}
		
		final LeaseOrder order=Inspector.createEntity(LeaseOrder.class);
		order.setClient(client);
		order.setCar(car);
		order.setPassport(passport);
		order.setDriverPresent(Objects.nonNull(driver));
		order.setStartTime(startTime.get());
		order.setEndTime(dueTime.get());
		entityManager.persist(order);

		return true;
	}

}
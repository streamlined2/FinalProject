package edu.practice.finalproject.view.action;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.analysis.EntityException;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.DataAccessException;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.document.LeaseOrder;
import edu.practice.finalproject.model.entity.domain.Car;
import edu.practice.finalproject.model.entity.userrole.Client;
import edu.practice.finalproject.model.entity.userrole.User;
import edu.practice.finalproject.utilities.Utils;

public class OrderCarAction extends ClientAction {

	private static final Logger logger = LogManager.getLogger();

	public OrderCarAction(String name) {
		super(name);
	}
	
	private static final String WRONG_DUE_TIME = "orderCarAction.wrong-due-time";
	private static final String WRONG_START_TIME = "orderCarAction.wrong-start-time";
	private static final String WRONG_USER = "orderCarAction.wrong-user";
	private static final String WRONG_CAR = "orderCarAction.wrong-car";
	private static final String CANT_SAVE_LEASE_ORDER_MSG = "orderCarAction.cannot-save-lease-order";
	private static final String LEASE_ORDER_SAVED_MSG = "orderCarAction.lease-order-saved";
	private static final String AT_LEAST_ONE_ORDER_ALREADY_BEEN_CONFIRMED_FOR_THIS_CAR_AND_TIME_PERIOD = "orderCarAction.already-confirmed-lease-order";

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		try {
			final User user=FCServlet.getUser(req);
			if(!(user instanceof Client)) {
				FCServlet.setError(req, WRONG_USER);
				return false;
			}
			
			final Client client = (Client) user;

			final Car car = (Car)FCServlet.getAttribute(req, Names.SELECTED_CAR_ATTRIBUTE);
			if(Objects.isNull(car)) {
				FCServlet.setError(req, WRONG_CAR);
				return false;
			}

			if(!Utils.checkIfValid(req,Names.PASSPORT_PARAMETER,Utils::checkPassport)) return false;
			if(!Utils.checkIfValid(req,Names.LEASE_DUE_TIME_PARAMETER,Utils::checkTime)) return false;
			if(!Utils.checkIfValid(req,Names.LEASE_START_TIME_PARAMETER,Utils::checkTime)) return false;
			
			final String passport=FCServlet.getParameterValue(req,Names.PASSPORT_PARAMETER);
			final String driver=FCServlet.getParameterValue(req,Names.DRIVER_OPTION_PARAMETER);
			final Optional<LocalDateTime> startTime=Utils.getTime(FCServlet.getParameterValue(req,Names.LEASE_START_TIME_PARAMETER));
			final Optional<LocalDateTime> dueTime=Utils.getTime(FCServlet.getParameterValue(req,Names.LEASE_DUE_TIME_PARAMETER));
			
			final LocalDateTime now = LocalDateTime.now();
			if(startTime.get().isBefore(now)) {
				FCServlet.setError(req, WRONG_START_TIME, startTime.get(), now);
				return false;
			}
			
			if(dueTime.get().isBefore(startTime.get())) {
				FCServlet.setError(req, WRONG_DUE_TIME, startTime.get(), dueTime.get());
				return false;
			}
			
			Long count = entityManager.countConfirmedCarOrders(car,startTime.get(),dueTime.get());
			if(count>0) {
				FCServlet.setError(req, AT_LEAST_ONE_ORDER_ALREADY_BEEN_CONFIRMED_FOR_THIS_CAR_AND_TIME_PERIOD, car.getModel(), startTime.get(), dueTime.get());
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

			FCServlet.setAttribute(req, Names.SELECTED_ORDER_ATTRIBUTE, order);
			FCServlet.setMessage(req, LEASE_ORDER_SAVED_MSG);
			return true;
		} catch(EntityException | DataAccessException e) {
			logger.error(Utils.message(CANT_SAVE_LEASE_ORDER_MSG), e);
			FCServlet.setError(req, CANT_SAVE_LEASE_ORDER_MSG);
		}
		return false;
	}
}

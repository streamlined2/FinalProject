package edu.practice.finalproject.view.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.userrole.User;

public class BlockUserAction extends AdminAction {

	private static final String INCORRECT_USER_MSG = "Wrong user selected";
	private static final String CANT_UPDATE_USER_STATUS_MSG = "Can't update user status";

	public BlockUserAction(String name) {
		super(name);
	}

	@Override
	public boolean execute(HttpServletRequest req, EntityManager entityManager) {
		final List<User> queryData=(List<User>)FCServlet.getAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE);
		final int number=Integer.parseInt(FCServlet.getParameterValue(req, Names.USER_NUMBER_PARAMETER));
		if(number>=0 && number<queryData.size()) {
			final User user = queryData.get(number);
			user.setBlocked(!user.getBlocked());
			boolean success = entityManager.merge(user);
			if(!success) {
				FCServlet.setError(req, CANT_UPDATE_USER_STATUS_MSG);
				return false;
			}
			return true;
		}
		FCServlet.setError(req, INCORRECT_USER_MSG);
		return false;
	}
}

package edu.practice.finalproject.view.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.model.dataaccess.EntityManager;

public class BlockUserAction extends AdminAction {

	private static final String INCORRECT_USER_MSG = "Wrong user selected";

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
			return true;
		}
		FCServlet.setError(req, INCORRECT_USER_MSG);
		return false;
	}
}

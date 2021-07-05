package edu.practice.finalproject.view.form;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.Names;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.model.analysis.Inspector;
import edu.practice.finalproject.model.dataaccess.EntityManager;
import edu.practice.finalproject.model.entity.userrole.User;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.utilities.Utils;

public class UserBlockingForm extends ActionMapForm {
	private static final Map<String,Action> ACTION_MAP = Map.of(
			Names.SELECT_USER_PARAMETER,FormDispatcher.CHANGE_USER_ACTION,
			Names.BLOCK_USER_PARAMETER,FormDispatcher.BLOCK_USER_ACTION,
			Names.NEXT_PAGE_PARAMETER,FormDispatcher.NEXT_PAGE_ACTION,
			Names.PREVIOUS_PAGE_PARAMETER,FormDispatcher.PREVIOUS_PAGE_ACTION,
			Names.FIRST_PAGE_PARAMETER,FormDispatcher.FIRST_PAGE_ACTION,
			Names.LAST_PAGE_PARAMETER,FormDispatcher.LAST_PAGE_ACTION
	);
	
	public UserBlockingForm(String name) {
		super(name);
	}

	@Override
	protected Map<String, Action> getActionMap() {
		return ACTION_MAP;
	}

	private <U extends User> void updateUserList(HttpServletRequest req, EntityManager entityManager,final Long firstElement,final Long lastElement){
		final String userType = (String)FCServlet.getAttribute(req, Names.SELECTED_ROLE_ATTRIBUTE, User.Role.CLIENT.getLabel());			
		final Class<U> userClass = (Class<U>)Utils.mapUserRoleToClass(userType);
		final List<U> queryData=entityManager.fetchEntities(userClass,false,firstElement,lastElement);
		final Long queryCount=entityManager.countEntities(userClass);

		FCServlet.setQueryElements(req, queryCount);
		FCServlet.setAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE,queryData);
		FCServlet.setAttribute(req, Names.QUERY_DATA_ATTRIBUTE, Inspector.getValuesForEntities(userClass, queryData));
		FCServlet.setAttribute(req, Names.QUERY_HEADER_ATTRIBUTE, Inspector.getCaptions(userClass));
	}

	@Override
	public void init(HttpServletRequest req, EntityManager entityManager) {
		final Integer pageElements=FCServlet.getPageElements(req);
		final Long firstElement=(Long)FCServlet.getAttribute(req, Names.FIRST_PAGE_ELEMENT_ATTRIBUTE,0L);
		final Long lastElement=(Long)FCServlet.getAttribute(req, Names.LAST_PAGE_ELEMENT_ATTRIBUTE,firstElement+pageElements-1);

		updateUserList(req, entityManager, firstElement, lastElement);

		req.setAttribute(Names.ROLE_VALUES_ATTRIBUTE, Inspector.getLabels(User.Role.class));

		super.init(req,entityManager);
	}

	@Override
	public void destroy(HttpServletRequest req) {
		FCServlet.removeQueryElements(req);
		FCServlet.removeAttribute(req, Names.PAGE_ITEMS_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_DATA_ATTRIBUTE);
		FCServlet.removeAttribute(req, Names.QUERY_HEADER_ATTRIBUTE);
		super.destroy(req);
	}
}

package edu.practice.finalproject.model.entity.userrole;

import java.util.Objects;

import edu.practice.finalproject.utilities.Utils;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.AdminAction;

/**
 * Entity that represents user role of administrator
 * @author Serhii Pylypenko
 *
 */
public class Admin extends User {

	public Admin(String login, byte[] passwordDigest, String firstName, String lastName)  {
		super(login, passwordDigest, firstName, lastName);
	}
	
	public Admin() {}

	@Override
	public void checkPermission(Action action) throws SecurityException {
		Objects.requireNonNull(action, Utils.message(PASSED_ACTION_SHOULDNT_BE_NULL));
		if(action.getClass().getSuperclass()!=Action.class && !(action instanceof AdminAction)) throw new SecurityException(String.format("Administrator can't perform this action %s!",action));
	}

	@Override
	public Role role() { return Role.ADMIN;}
}

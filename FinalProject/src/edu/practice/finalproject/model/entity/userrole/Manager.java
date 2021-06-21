package edu.practice.finalproject.model.entity.userrole;

import java.util.Objects;

import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.ManagerAction;

public class Manager extends User {

	public Manager(String login, byte[] passwordDigest, String firstName, String lastName)  {
		super(login, passwordDigest, firstName, lastName);
	}
	
	public Manager() {}
	
	@Override
	public void checkPermission(Action action) throws SecurityException {
		Objects.requireNonNull(action, PASSED_ACTION_SHOULDNT_BE_NULL);
		if(action.getClass().getSuperclass()!=Action.class && !(action instanceof ManagerAction)) throw new SecurityException(String.format("Manager can't perform this action %s!",action));
	}

	@Override
	public Role role() { return Role.MANAGER;	}
}

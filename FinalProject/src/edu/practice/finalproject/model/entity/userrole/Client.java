package edu.practice.finalproject.model.entity.userrole;

import java.util.Objects;

import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.action.ClientAction;

public class Client extends User {
	
	public Client(String login, byte[] passwordDigest, String firstName, String lastName)  {
		super(login, passwordDigest, firstName, lastName);
	}
	
	public Client() {}
	
	@Override
	public void checkPermission(Action action) throws SecurityException {
		Objects.requireNonNull(action, PASSED_ACTION_SHOULDNT_BE_NULL);
		if(action.getClass().getSuperclass()!=Action.class && !(action instanceof ClientAction)) throw new SecurityException(String.format("Client can't perform this action %s!",action));
	}

	@Override
	public Role role() { return Role.CLIENT;	}
}

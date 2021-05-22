package edu.practice.finalproject.view.action;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import edu.practice.finalproject.model.dataaccess.EntityManager;

public abstract class Action {
	
	private final String name;
	
	protected Action(final String name) {
		this.name=name;
	}
	
	public String getName() { return name;}
	
	public abstract boolean execute(HttpServletRequest req,EntityManager entityManager);

	@Override
	public boolean equals(final Object o) {
		if(o instanceof Action) {
			final Action action=(Action)o;
			return name.equals(action.name);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	
}

package edu.practice.finalproject.model.analysis;

public class EntityException extends RuntimeException {

	public EntityException(final Exception cause) {
		super(cause);
	}

	public EntityException(final String message, final Exception cause) {
		super(message, cause);
	}

}

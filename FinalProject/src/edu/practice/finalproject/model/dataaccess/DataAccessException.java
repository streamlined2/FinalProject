package edu.practice.finalproject.model.dataaccess;

public class DataAccessException extends RuntimeException {

	public DataAccessException(final Exception cause) {
		super(cause);
	}

	public DataAccessException(final String message) {
		super(message);
	}

	public DataAccessException(final String message, final Exception cause) {
		super(message, cause);
	}
}

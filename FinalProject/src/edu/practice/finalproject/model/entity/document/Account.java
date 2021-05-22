package edu.practice.finalproject.model.entity.document;

public class Account {
	
	private final String countryCode;//2 letters
	private final String checkDigits;//2 digits
	private final String BBAN;//up to 30 chars
	
	public Account(String countryCode, String checkDigits, String bBAN) {
		this.countryCode = countryCode;
		this.checkDigits = checkDigits;
		BBAN = bBAN;
	}

}

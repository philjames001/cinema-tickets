package uk.gov.dwp.uc.pairtest.exception;

public class InvalidAccountException extends InvalidPurchaseException {
	private static final long serialVersionUID = 1L;
	
	public InvalidAccountException(String message) {
		super(message);
	}

}

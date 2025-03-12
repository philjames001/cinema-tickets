package uk.gov.dwp.uc.pairtest.exception;

public class InvalidNumTicketsException extends InvalidPurchaseException {
	private static final long serialVersionUID = 1L;
	
	public InvalidNumTicketsException(String message) {
		super(message);
	}
}

package uk.gov.dwp.uc.pairtest.domain;

public class ChildTicket extends Ticket {

	public ChildTicket() {
		price = TicketPrice.CHILD_PRICE.getPrice();
		numSeats = 1;
	}

}

package uk.gov.dwp.uc.pairtest.domain;

public class AdultTicket extends Ticket {

	public AdultTicket() {
		price = TicketPrice.ADULT_PRICE.getPrice();
		numSeats = 1;
	}

}

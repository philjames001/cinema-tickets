package uk.gov.dwp.uc.pairtest.domain;

public class AdultTicket extends Ticket {

	@Override
	public int getPrice() {
		return TicketPrice.ADULT.getPrice();
	}

	@Override
	public int getSeats() {
		return 1;
	}

}

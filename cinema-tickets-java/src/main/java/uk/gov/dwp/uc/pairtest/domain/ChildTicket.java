package uk.gov.dwp.uc.pairtest.domain;

public class ChildTicket extends Ticket {

	@Override
	public int getPrice() {
		return TicketPrice.CHILD.getPrice();
	}

	@Override
	public int getSeats() {
		return 1;
	}

}

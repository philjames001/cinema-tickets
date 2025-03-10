package uk.gov.dwp.uc.pairtest.domain;

public class InfantTicket extends Ticket {

	@Override
	public int getPrice() {
		return TicketPrice.INFANT.getPrice();
	}

	
	@Override
	public int getSeats() {
		return 0;      // Infants are not allocated a seat. They will be sitting on an Adult's lap.
	}

}

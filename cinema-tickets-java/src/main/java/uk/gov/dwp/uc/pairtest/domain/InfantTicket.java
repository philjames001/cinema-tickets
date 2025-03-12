package uk.gov.dwp.uc.pairtest.domain;

public class InfantTicket extends Ticket {

	public InfantTicket() {
		price = TicketPrice.INFANT_PRICE.getPrice();
		
		// Infants are not allocated a seat. They will be sitting on an Adult's lap.	
		numSeats = 0; 
	}

}

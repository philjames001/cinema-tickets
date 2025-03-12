package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.Ticket;
import uk.gov.dwp.uc.pairtest.domain.TicketFactory;
import uk.gov.dwp.uc.pairtest.domain.TicketList;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.*;

public class TicketServiceImpl implements TicketService {
	static private int MAX_TICKETS = 25;
	
    private final TicketPaymentService 		payment;
    private final SeatReservationService 	seatReservation;

    public TicketServiceImpl(TicketPaymentService 	payment, 
    						 SeatReservationService seatReservation) {
    	this.payment = payment;
        this.seatReservation = seatReservation;
    }
    
    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) 
    		throws InvalidPurchaseException {

        validateAccount(accountId);
           
        // create a list of tickets
        TicketList tickets = new TicketList(MAX_TICKETS);
        
        for(TicketTypeRequest request : ticketTypeRequests) {
            Ticket ticket = TicketFactory.createTicket(request.getTicketType());
            
            for(int i=1; i<= request.getNoOfTickets(); i++) {
            	tickets.add(ticket);
            	
            }
        }
        
        // Business Rule: Child and Infant tickets
        // cannot be purchased without purchasing an Adult ticket.
        // i.e. request always needs to have at least one Adult in it
        if(! tickets.containsAdultTicket() ) {
            throw new MissingAdultException();
        }
 
        int totalAmountToPay     = tickets.calculateTotalPrice();
        int totalSeatsToAllocate = tickets.calculateTotalSeats();

        seatReservation.reserveSeat(accountId, totalSeatsToAllocate);
        payment.makePayment(accountId, totalAmountToPay);

    }

	private void validateAccount(Long accountId) {
		// Only accounts with an id greater than zero are valid.
        if(accountId <= 0L) {
            throw new InvalidAccountException("account " + accountId);
        }
	}
	
}

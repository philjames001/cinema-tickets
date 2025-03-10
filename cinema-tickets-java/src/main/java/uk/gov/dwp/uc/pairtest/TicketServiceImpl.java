package uk.gov.dwp.uc.pairtest;

import java.util.ArrayList;
import java.util.List;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.AdultTicket;
import uk.gov.dwp.uc.pairtest.domain.Ticket;
import uk.gov.dwp.uc.pairtest.domain.TicketFactory;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.*;

public class TicketServiceImpl implements TicketService {
	static public int MAX_TICKETS = 25;
	
    public TicketPaymentService payment;
    public SeatReservationService seatReservation;

    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        // Only accounts with an id greater than zero are valid.
        if(accountId <= 0L) {
            throw new InvalidAccountException();
        }
           
        // create a list of tickets
        List<Ticket> tickets = new ArrayList<Ticket>();
        
        for(TicketTypeRequest request : ticketTypeRequests) {
            Ticket ticket = TicketFactory.createTicket(request.getTicketType());
            
            for(int i=1; i<= request.getNoOfTickets(); i++) {
            	tickets.add(ticket);
            	
                // Business Rule: Only a maximum num of tickets can be purchased at a time
                if(tickets.size() > MAX_TICKETS) {
                    throw new InvalidNumTicketsException();
                }
            }
        }
        

        // Business Rule: Child and Infant tickets
        // cannot be purchased without purchasing an Adult ticket.
        
        if(! containsAdultTicket(tickets) ) {
            throw new MissingAdultException();
        }
 
        int totalAmountToPay     = calculateTotalPrice(tickets);
        int totalSeatsToAllocate = calculateTotalSeats(tickets);

        seatReservation.reserveSeat(accountId, totalSeatsToAllocate);
        payment.makePayment(accountId, totalAmountToPay);

    }
    
    
    private static int calculateTotalPrice(List<Ticket> tickets) {
        return tickets.stream()
                      .mapToInt(Ticket::getPrice)
                      .sum();
    }
    
    
    private static int calculateTotalSeats(List<Ticket> tickets) {
        return tickets.stream()
                      .mapToInt(Ticket::getSeats)
                      .sum();
    }
    
    private static boolean containsAdultTicket(List<Ticket> tickets) {
        return tickets.stream()
        		.anyMatch(t -> t instanceof AdultTicket);
    }

}

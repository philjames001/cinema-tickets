package uk.gov.dwp.uc.pairtest.domain;

import java.util.ArrayList;

import uk.gov.dwp.uc.pairtest.exception.InvalidNumTicketsException;

public class TicketList extends ArrayList<Ticket> {
	private static final long serialVersionUID = 1L;
	
	private int maxTickets;

    public TicketList(int maxTickets) {
        super(); 
    	this.maxTickets = maxTickets;
    }
    
    @Override
    public boolean add(Ticket ticket) {
        // Business Rule: A maximum number of tickets can be purchased at a time
        if(this.size() >= maxTickets) {
            throw new InvalidNumTicketsException("num =" + this.size());
        }
        return super.add(ticket);
    }
    
	public int calculateTotalPrice() {
        return this.stream()
                      .mapToInt(Ticket::getPrice)
                      .sum();
    }
    
    public int calculateTotalSeats() {
        return this.stream()
                      .mapToInt(Ticket::getNumSeats)
                      .sum();
    }
    
    public boolean containsAdultTicket() {
        return this.stream()
        		.anyMatch(t -> t instanceof AdultTicket);
    }

}

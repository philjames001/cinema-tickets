package uk.gov.dwp.uc.pairtest.domain;

import java.util.HashMap;
import java.util.Map;

public class TicketFactory {
	private static Map<TicketTypeRequest.Type, Class<? extends Ticket>> ticketMap = new HashMap<>();

	static {
		addTicketType(TicketTypeRequest.Type.ADULT, AdultTicket.class);
		addTicketType(TicketTypeRequest.Type.CHILD, ChildTicket.class);
		addTicketType(TicketTypeRequest.Type.INFANT, InfantTicket.class);
	}

	public static void addTicketType(TicketTypeRequest.Type type, Class<? extends Ticket> clazz) {
		ticketMap.put(type, clazz);
	}

	public static Ticket createTicket(TicketTypeRequest.Type type) {
		Class<? extends Ticket> ticketClass = ticketMap.get(type); // class to create

		try {
			return ticketClass.getConstructor().newInstance();

		} catch (Exception e) {
			throw new RuntimeException("Error creating ticket instance for " + type, e);
		}

	}

}



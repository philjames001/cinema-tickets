package uk.gov.dwp.uc.pairtest.domain;

	import java.util.HashMap;
	import java.util.Map;

	public class TicketFactory {
		private static final Map<TicketTypeRequest.Type, Class<? extends Ticket>> ticketMap = new HashMap<>();

		static {
			ticketMap.put(TicketTypeRequest.Type.ADULT, AdultTicket.class);
			ticketMap.put(TicketTypeRequest.Type.CHILD, ChildTicket.class);
			ticketMap.put(TicketTypeRequest.Type.INFANT, InfantTicket.class);
		}

		public static Ticket createTicket(TicketTypeRequest.Type type) {
			Class<? extends Ticket> ticketClass = ticketMap.get(type);

			try {
				return ticketClass.getConstructor().newInstance();

			} catch (Exception e) {
				throw new RuntimeException("Error creating ticket instance for " + type, e);
			}
		}

	}



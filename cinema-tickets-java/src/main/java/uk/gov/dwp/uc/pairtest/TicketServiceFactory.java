package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;

public class TicketServiceFactory {

    public static TicketService createTicketService(
            TicketPaymentService ticketPaymentService,
            SeatReservationService seatReservationService) {

        TicketServiceImpl impl = new TicketServiceImpl();

        impl.payment = ticketPaymentService;
        impl.seatReservation = seatReservationService;

        return impl;
    }
}

package uk.gov.dwp.uc.pairtest;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidAccountException;
import uk.gov.dwp.uc.pairtest.exception.InvalidNumTicketsException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.MissingAdultException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TicketServiceTest extends TestCase {

    private TicketService ticketService;


    @Mock
    private TicketPaymentService paymentService;
    @Mock
    private SeatReservationService reservationService;

    @Before
    public void setUp() {
        paymentService = Mockito.mock(TicketPaymentService.class);
        reservationService = Mockito.mock(SeatReservationService.class);

        ticketService = TicketServiceFactory.createTicketService(paymentService, reservationService);
    }

    @Test
    public void testPurchaseOneAdult() {
        long accountId = 1L;

        TicketTypeRequest request = new TicketTypeRequest(Type.ADULT, 1);

        try {
            ticketService.purchaseTickets(accountId, request);
        } catch (Exception e) {
            fail();
        }

        verify(paymentService, times(1)).makePayment(1L, 20);
        verify(reservationService, times(1)).reserveSeat(1, 1);

        assertTrue(true);
    }


    @Test
    public void testPurchaseMultipleTickets() {
        long accountId = 1L;

        // total = £90 for 6 seats

        // 3 adults = £60, 3 seats
        TicketTypeRequest r1 = new TicketTypeRequest(Type.ADULT, 1);
        TicketTypeRequest r2 = new TicketTypeRequest(Type.ADULT, 2);

        // 3 children = £30, 3 seats
        TicketTypeRequest r3 = new TicketTypeRequest(Type.CHILD, 1);
        TicketTypeRequest r4 = new TicketTypeRequest(Type.CHILD, 2);

        // 3 infants = £0, 0 seats
        TicketTypeRequest r5 = new TicketTypeRequest(Type.INFANT, 1);
        TicketTypeRequest r6 = new TicketTypeRequest(Type.INFANT, 2);

        try {
            ticketService.purchaseTickets(accountId, r1,r2,r3,r4,r5,r6);
        } catch (Exception e) {
            fail();
        }

        verify(paymentService, times(1)).makePayment(1L, 90);
        verify(reservationService, times(1)).reserveSeat(1L, 6);

        assertTrue(true);
    }

    @Test
    public void testInvalidAccount() {
        long accountId = 0L;

        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);

        try {
            ticketService.purchaseTickets(accountId, request);
        } catch (InvalidPurchaseException e) {
            assertThat(e, instanceOf(InvalidAccountException.class));
        }
    }

    @Test
    public void testChildMissingAdult() {
        long accountId = 1L;

        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        try {
            ticketService.purchaseTickets(accountId, request);
        } catch (InvalidPurchaseException e) {
            assertThat(e, instanceOf(MissingAdultException.class));
        }
    }

    @Test
    public void testInfantMissingAdult() {
        long accountId = 1L;

        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        try {
            ticketService.purchaseTickets(accountId, request);
        } catch (InvalidPurchaseException e) {
            assertThat(e, instanceOf(MissingAdultException.class));
        }
    }

    @Test
    public void testTooManyTickets() {
        long accountId = 1L;

        List<TicketTypeRequest> requests = new ArrayList<TicketTypeRequest>();

        requests.add(new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21));
 
        try {
            ticketService.purchaseTickets(accountId, requests.toArray(new TicketTypeRequest[0]));
        } catch (InvalidPurchaseException e) {
            assertThat(e, instanceOf(InvalidNumTicketsException.class));
        }
    }
}
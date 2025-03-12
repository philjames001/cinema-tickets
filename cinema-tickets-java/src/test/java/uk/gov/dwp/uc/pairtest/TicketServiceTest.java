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

        // check the mocked services were invoked as expected
        // 1 adult = £20, 1 seat
        verify(paymentService, times(1)).makePayment(accountId, 20);
        verify(reservationService, times(1)).reserveSeat(accountId, 1);

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

        // check the mocked services were invoked as expected
        verify(paymentService,     times(1)).makePayment(accountId, 90);
        verify(reservationService, times(1)).reserveSeat(accountId, 6);

        assertTrue(true);
    }
    

    @Test
    public void testInvalidAccount() {
        long accountId = 0L;
        Object exception = null;

        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);

        try {
            ticketService.purchaseTickets(accountId, request);
        } catch (Exception e) {
        	exception = e;  
        }
        assertThat(exception, instanceOf(InvalidAccountException.class));
    }

    @Test
    public void testChildMissingAdult() {
        long accountId = 1L;
        Object exception = null;

        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        try {
            ticketService.purchaseTickets(accountId, request);
        } catch (Exception e) {
        	exception = e;  
        }
        assertThat(exception, instanceOf(MissingAdultException.class));
    }

    @Test
    public void testInfantMissingAdult() {
        long accountId = 1L;
        Object exception = null;

        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        try {
            ticketService.purchaseTickets(accountId, request);
        } catch (Exception e) {
        	exception = e;
        }
        assertThat(exception, instanceOf(MissingAdultException.class));
    }

    @Test
    public void testPurchaseMaxNumTickets() {
        long accountId = 1L;
        
        // max is 25 tickets per request
        // 25 adults = £500, 25 seats
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 25);
 
        try {
        	ticketService.purchaseTickets(accountId, request);
        } catch (Exception e) {
            fail();
        }

        // check the mocked services were invoked as expected
        verify(paymentService,     times(1)).makePayment(accountId, 500);
        verify(reservationService, times(1)).reserveSeat(accountId, 25);

        assertTrue(true);
    }

    
    @Test
    public void testTooManyTicketsForAdult() {
        long accountId = 1L;
        Object exception = null;

        // max is 25 tickets per request
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 26);
 
        try {
        	ticketService.purchaseTickets(accountId, request);
        } catch (InvalidPurchaseException e) {
        	exception = e;
        }
        
        assertThat(exception, instanceOf(InvalidNumTicketsException.class));
    }
    
    
    @Test
    public void testTooManyTicketsInTotal() {
        long accountId = 1L;
        Object exception = null;

        // max is 25 tickets per request
        // 10 adult + 10 child + 6 infant = 26 tickets
        TicketTypeRequest r1 = new TicketTypeRequest(Type.ADULT, 10);
        TicketTypeRequest r2 = new TicketTypeRequest(Type.CHILD, 10);
        TicketTypeRequest r3 = new TicketTypeRequest(Type.INFANT, 6);

        try {
            ticketService.purchaseTickets(accountId, r1,r2,r3);
            
        } catch (InvalidPurchaseException e) {
        	exception = e;
        }
        
        assertThat(exception, instanceOf(InvalidNumTicketsException.class));
    }
}
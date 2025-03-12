package uk.gov.dwp.uc.pairtest.domain;

// Likely that more than one price will rise at same time, so group them here
public enum TicketPrice {
	// Prices are in £'s
    ADULT_PRICE(20),
    CHILD_PRICE(10),
    INFANT_PRICE(0);

    private final int price;

    TicketPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

}

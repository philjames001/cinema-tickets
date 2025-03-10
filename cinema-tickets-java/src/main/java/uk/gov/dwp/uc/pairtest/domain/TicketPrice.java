package uk.gov.dwp.uc.pairtest.domain;

public enum TicketPrice { 
	// Prices are in £'s
    ADULT(20),
    CHILD(10),
    INFANT(0);

    private final int price;

    TicketPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

}

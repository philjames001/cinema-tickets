package uk.gov.dwp.uc.pairtest.domain;

public abstract class Ticket {
	int price;
	int numSeats;
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setNumSeats(int numSeats) {
		this.numSeats = numSeats;
	}
	
	public int getNumSeats() {
		return numSeats;
	}
}

package org.lajcik.kociolek.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lajcik
 */
public class Rental {
    private long id;
    private int ticketNumber;
    private boolean rented;
    private Date rentDate;
    private Date returnDate;
    private List<Item> rentedItems = new ArrayList<Item>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public List<Item> getRentedItems() {
        return rentedItems;
    }

    public void setRentedItems(List<Item> rentedItems) {
        this.rentedItems = rentedItems;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }
}

package org.lajcik.kociolek.domain;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lajcik
 */
@Entity
@Table(name = "RENTAL")
public class Rental {
    private Long id;

    private Integer ticketNumber;
    private Date rentDate;
    private Date returnDate;
    private Integer minutesRented;
    private List<Item> rentedItems = new ArrayList<Item>();

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Index(name = "TICKET_NUMBER_IDX")
    @Column(name = "TICKET_NUMBER", nullable = true)
    public Integer getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @Column(name = "RENT_DATE", nullable = false)
    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    @Column(name = "RETURN_DATE", nullable = true)
    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Column(name = "MINUTES_RENTED", nullable = true)
    public Integer getMinutesRented() {
        return minutesRented;
    }

    public void setMinutesRented(Integer minutesRented) {
        this.minutesRented = minutesRented;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "RENTED_ITEM",
            joinColumns = {
                    @JoinColumn(name = "RENTAL_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "ITEM_ID", nullable = false, updatable = false)})
    public List<Item> getRentedItems() {
        return rentedItems;
    }

    public void setRentedItems(List<Item> rentedItems) {
        this.rentedItems = rentedItems;
    }
}

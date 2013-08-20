package org.lajcik.kociolek.service;

import org.lajcik.kociolek.domain.Rental;

import java.util.List;

/**
 * User: sienkom
 */
public interface RentalService {
    void addListener(RentalListener listener);

    void removeListener(RentalListener listener);

    int getNextTicketNumber();

    Rental rentItem(int ticketNumber, String... items);

    void returnTicket(int ticketNumber);

    void returnItem(int ticketNumber);

    List<Rental> getActiveRentals();

    Rental getRental(Integer ticketNumber);

    void updateItem(int ticketNumber, String... items);
}

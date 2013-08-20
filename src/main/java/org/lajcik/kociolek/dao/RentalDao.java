package org.lajcik.kociolek.dao;

import org.lajcik.kociolek.domain.Rental;

import java.util.List;

/**
 * User: sienkom
 */
public interface RentalDao extends Dao<Rental> {

    Rental getByTicket(int ticketNumber);

    List<Rental> getAllActiveRentals();
}

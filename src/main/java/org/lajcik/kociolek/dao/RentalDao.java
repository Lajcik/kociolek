package org.lajcik.kociolek.dao;

import org.lajcik.kociolek.domain.Rental;

/**
 * User: sienkom
 */
public interface RentalDao extends Dao<Rental> {

    Rental getByTicket(int ticketNumber);
}

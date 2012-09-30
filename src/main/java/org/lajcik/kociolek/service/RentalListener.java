package org.lajcik.kociolek.service;

import org.lajcik.kociolek.domain.Rental;

/**
 * @author lajcik
 */
public interface RentalListener {

    public void itemRented(Rental rental);

    public void itemReturned(Rental rental);

    public void itemsChanged(Rental before, Rental after);
}

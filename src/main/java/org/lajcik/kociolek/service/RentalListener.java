package org.lajcik.kociolek.service;

import org.lajcik.kociolek.domain.Rental;

/**
 * @author lajcik
 */
public interface RentalListener {

    /**
     * Called when a Rental is created
     * @param rental new rental object
     */
    public void itemRented(Rental rental);

    /**
     * Called when items are returned
     * @param rental returned Rental object
     */
    public void itemReturned(Rental rental);

    public void itemsChanged(Rental before, Rental after);
}

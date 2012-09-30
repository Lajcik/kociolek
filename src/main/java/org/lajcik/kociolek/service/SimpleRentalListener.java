package org.lajcik.kociolek.service;

import org.lajcik.kociolek.domain.Rental;

/**
 * @author lajcik
 */
public abstract class SimpleRentalListener implements RentalListener {

    @Override
    public void itemsChanged(Rental before, Rental after) {
        itemReturned(before);
        itemRented(after);
    }
}

package org.lajcik.kociolek.service;

import org.lajcik.kociolek.dao.ItemDao;
import org.lajcik.kociolek.dao.RentalDao;
import org.lajcik.kociolek.domain.Item;
import org.lajcik.kociolek.domain.Rental;
import org.lajcik.kociolek.util.TicketDispenser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author lajcik
 */
@Component
public class RentalServiceImpl implements RentalService {

    @Autowired
    private RentalDao rentalDao;
    @Autowired
    private ItemDao itemDao;

    private TicketDispenser ticketDispenser = new TicketDispenser();

    private List<RentalListener> listeners = new ArrayList<RentalListener>();

    public void addListener(RentalListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RentalListener listener) {
        listeners.remove(listener);
    }

    public int getNextTicketNumber() {
        return ticketDispenser.getNextAvailableTicket();
    }

    @Transactional
    public void rentItem(int ticketNumber, String... items) {

        Rental rental = createRental(ticketNumber, items);

        for (RentalListener listener : listeners) {
            listener.itemRented(rental);
        }
    }

    private Rental createRental(int ticketNumber, String[] items) {
        Rental rental = new Rental();
        rental.setRentDate(new Date());
        rental.setTicketNumber(ticketNumber);

        for (String itemName : items) {
            // TODO: find or create item
            Item item = new Item();
            item.setName(itemName);
            rental.getRentedItems().add(item);
        }

        rentalDao.save(rental);

        return rental;
    }

    public void returnTicket(int ticketNumber) {
        ticketDispenser.returnTicket(ticketNumber);
    }

    @Transactional
    public void returnItem(int ticketNumber) {
        Rental rental = rentalDao.getByTicket(ticketNumber);

        rental.setReturnDate(new Date());

        for (RentalListener listener : listeners) {
            listener.itemReturned(rental);
        }

        rental.setTicketNumber(null);
        returnTicket(ticketNumber);
    }

    public void updateItem(int ticketNumber, String... items) {
        Rental before = rentalDao.getByTicket(ticketNumber);
        before.setReturnDate(new Date());
        Rental after = createRental(ticketNumber, items);

        for (RentalListener listener : listeners) {
            listener.itemsChanged(before, after);
        }
        before.setTicketNumber(null);
    }
}

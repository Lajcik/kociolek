package org.lajcik.kociolek.service;

import org.lajcik.kociolek.domain.Item;
import org.lajcik.kociolek.domain.Rental;
import org.lajcik.kociolek.util.TicketDispenser;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author lajcik
 */
@Component
public class RentalServiceImpl implements RentalService {
    private Map<Integer, Rental> TMP = new HashMap<Integer, Rental>();
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
        rental.setRented(true);

        for (String itemName : items) {
            // TODO: find or create item
            Item item = new Item();
            item.setName(itemName);
            rental.getRentedItems().add(item);
        }

        // TODO: save

        TMP.put(ticketNumber, rental);
        return rental;
    }

    public void returnTicket(int ticketNumber) {
        ticketDispenser.returnTicket(ticketNumber);
    }

    public void returnItem(int ticketNumber) {
        Rental rental = returnRental(ticketNumber);

        for (RentalListener listener : listeners) {
            listener.itemReturned(rental);
        }

        returnTicket(ticketNumber);
    }

    private Rental returnRental(int ticketNumber) {
        // TODO: get
        Rental rental = TMP.remove(ticketNumber);
        rental.setRented(false);
        rental.setReturnDate(new Date());

        // TODO: update
        return rental;
    }

    public void updateItem(int ticketNumber, String... items) {
        Rental before = returnRental(ticketNumber);
        Rental after = createRental(ticketNumber, items);

        for (RentalListener listener : listeners) {
            listener.itemsChanged(before, after);
        }
    }
}

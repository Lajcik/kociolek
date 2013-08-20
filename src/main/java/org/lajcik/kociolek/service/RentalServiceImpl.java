package org.lajcik.kociolek.service;

import org.lajcik.kociolek.dao.ItemDao;
import org.lajcik.kociolek.dao.RentalDao;
import org.lajcik.kociolek.domain.Item;
import org.lajcik.kociolek.domain.Rental;
import org.lajcik.kociolek.util.TicketDispenser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lajcik
 */
@Component
@Transactional
public class RentalServiceImpl implements RentalService {

    @Autowired
    private RentalDao rentalDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private TicketDispenser ticketDispenser;

    private List<RentalListener> listeners = new ArrayList<RentalListener>();

    @Transactional(propagation = Propagation.SUPPORTS)
    public void addListener(RentalListener listener) {
        listeners.add(listener);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void removeListener(RentalListener listener) {
        listeners.remove(listener);
    }

    @PostConstruct
    public void init() {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                List<Rental> activeRentals = getActiveRentals();
                if (activeRentals.size() > 0) {
                    List<Integer> activeTickets = new ArrayList<Integer>();
                    for (Rental rental : activeRentals) {
                        activeTickets.add(rental.getTicketNumber());
                    }
                    ticketDispenser = new TicketDispenser(activeTickets);
                } else {
                    ticketDispenser = new TicketDispenser();
                }
            }
        });
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public int getNextTicketNumber() {
        return ticketDispenser.getNextAvailableTicket();
    }

    public Rental rentItem(int ticketNumber, String... items) {

        Rental rental = createRental(ticketNumber, items);

        for (RentalListener listener : listeners) {
            listener.itemRented(rental);
        }
        return rental;
    }

    private Rental createRental(int ticketNumber, String[] items) {
        Rental rental = new Rental();
        rental.setRentDate(new Date());
        rental.setTicketNumber(ticketNumber);

        for (String itemName : items) {

            Item item = itemDao.findItem(itemName);
            if (item == null) {
                item = new Item();
                item.setName(itemName);
                itemDao.save(item);
            }
            rental.getRentedItems().add(item);
        }

        rentalDao.save(rental);

        return rental;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void returnTicket(int ticketNumber) {
        ticketDispenser.returnTicket(ticketNumber);
    }

    public void returnItem(int ticketNumber) {
        Rental rental = rentalDao.getByTicket(ticketNumber);

        rental.setReturnDate(new Date());

        for (RentalListener listener : listeners) {
            listener.itemReturned(rental);
        }

        rental.setTicketNumber(null);
        returnTicket(ticketNumber);
    }

    @Override
    public void updateItem(int ticketNumber, String... items) {
        Rental before = rentalDao.getByTicket(ticketNumber);
        before.setReturnDate(new Date());
        Rental after = createRental(ticketNumber, items);

        for (RentalListener listener : listeners) {
            listener.itemsChanged(before, after);
        }
        before.setTicketNumber(null);
    }

    public List<Rental> getActiveRentals() {
        return rentalDao.getAllActiveRentals();
    }

    @Override
    public Rental getRental(Integer ticketNumber) {
        return rentalDao.getByTicket(ticketNumber);
    }
}

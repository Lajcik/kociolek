package org.lajcik.kociolek.util;

import java.util.*;

/**
 * @author lajcik
 */
public class TicketDispenser {
    private Set<Integer> usedNumbers = new HashSet<Integer>();
    private Set<Integer> unusedNumbers = new TreeSet<Integer>();

    public int getNextAvailableTicket() {
        int ticket;
        if (unusedNumbers.size() == 0) {
            ticket = usedNumbers.size() + 1;
        } else {
            Iterator<Integer> iterator = unusedNumbers.iterator();
            ticket = iterator.next();
            iterator.remove();
        }
        if (!usedNumbers.add(ticket)) {
            throw new WtfException("Trying to dispense ticket " + " but it's already in [used]");
        }
        return ticket;
    }

    public void returnTicket(int ticketNumber) {
        if (!usedNumbers.remove(ticketNumber)) {
            throw new WtfException("Trying to return ticket number " + ticketNumber + " but it's not in [used]");
        }
        if (!unusedNumbers.add(ticketNumber)) {
            throw new WtfException("Trying to return ticket number " + ticketNumber + " but it was already in [unused]");
        }
    }
}

package org.lajcik.kociolek.gui.components;

import net.miginfocom.swing.MigLayout;
import org.lajcik.kociolek.domain.Rental;
import org.lajcik.kociolek.service.RentalListener;
import org.lajcik.kociolek.util.ScrollablePanel;
import org.lajcik.kociolek.util.WrapLayout;
import org.lajcik.kociolek.util.WtfException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lajcik
 */
public class TicketPanel extends ScrollablePanel implements RentalListener {
    private final int BUTTON_SIZE = 60;

    private Map<Integer, TicketButton> buttonMap = new HashMap<Integer, TicketButton>();

    public TicketPanel() {
    }

    @Override
    public void itemRented(Rental rental) {
        TicketButton ticketButton = new TicketButton(rental);
        add(ticketButton);
        buttonMap.put(rental.getTicketNumber(), ticketButton);
        revalidate();
    }

    @Override
    public void itemReturned(Rental rental) {
        TicketButton ticketButton = buttonMap.get(rental.getTicketNumber());
        if(ticketButton == null) {
            throw new WtfException("No button assigned to ticket " + rental.getTicketNumber());
        }
        remove(ticketButton);
        revalidate();
    }

    @Override
    public void itemsChanged(Rental before, Rental after) {
        // noop
    }

    private class TicketButton extends JButton {

        private TicketButton(Rental rental) {
            setText(Integer.toString(rental.getTicketNumber()));
            setFont(getFont().deriveFont(Font.BOLD, 20.0f));
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            setMinimumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO
                }
            });
        }
    }
}

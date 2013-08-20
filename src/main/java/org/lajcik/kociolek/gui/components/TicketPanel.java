package org.lajcik.kociolek.gui.components;

import org.lajcik.kociolek.domain.Rental;
import org.lajcik.kociolek.gui.RentalDialog;
import org.lajcik.kociolek.service.RentalListener;
import org.lajcik.kociolek.util.ScrollablePanel;
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

        int insertIndex = -1;
        // find a place to insert this button in an orderly fashon
        TicketButton[] buttons = getButtons();
        for(int i = buttons.length -1 ; i >= 0; i--) {
            TicketButton otherButton = buttons[i];
            if(rental.getTicketNumber() < otherButton.ticketNumber) {
                insertIndex = i;
                break;
            }
        }

        add(ticketButton, insertIndex);
        buttonMap.put(rental.getTicketNumber(), ticketButton);
        revalidate();
    }

    public TicketButton[] getButtons() {
        Component[] components = getComponents();
        TicketButton[] result = new TicketButton[components.length];
        int i = 0;
        for(Component component : components) {
            result[i++] = (TicketButton) component;
        }

        return result;
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

        private int ticketNumber;

        private TicketButton(final Rental rental) {
            setText(Integer.toString(rental.getTicketNumber()));
            setFont(getFont().deriveFont(Font.BOLD, 20.0f));
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            setMinimumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            ticketNumber = rental.getTicketNumber();
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new RentalDialog(null, rental);
                }
            });
        }

        private int getTicketNumber() {
            return ticketNumber;
        }
    }
}

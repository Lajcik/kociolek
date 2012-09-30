package org.lajcik.kociolek.gui;

import org.lajcik.kociolek.service.RentalService;
import org.lajcik.kociolek.util.TicketDispenser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author lajcik
 */
public class MainPanel extends JPanel {

    private JPanel buttonPanel = new JPanel();
    private RentedItemsPanel rentedItemsPanel = new RentedItemsPanel();

    //TODO: shouldn't be here
    private TicketDispenser ticketDispenser = new TicketDispenser();
    private int i = 0;

    public MainPanel() {
        setLayout(new BorderLayout());

        JButton rentButton = new JButton("Wypożycz");
        rentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RentalService.rentItem(
                        ticketDispenser.getNextAvailableTicket(), "Item #" + (++i)
                );
            }
        });

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(rentButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(new JButton("Raport"));
        buttonPanel.add(new JButton("Zamknij"));
        add(buttonPanel, BorderLayout.PAGE_START);

        add(rentedItemsPanel, BorderLayout.CENTER);
    }
}

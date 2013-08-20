package org.lajcik.kociolek.gui;

import org.springframework.context.annotation.Scope;

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

    public MainPanel() {
        setLayout(new BorderLayout());

        JButton rentButton = new JButton("Wypożycz");
        rentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RentalDialog();
            }
        });

        JButton closeButton = new JButton("Zamknij");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); //TODO: proper exit
            }
        });

        JButton raportButton = new JButton("Raport");

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(rentButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(raportButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.PAGE_START);

        add(rentedItemsPanel, BorderLayout.CENTER);
    }
}

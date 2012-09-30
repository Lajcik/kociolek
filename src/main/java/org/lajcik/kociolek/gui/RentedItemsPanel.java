package org.lajcik.kociolek.gui;

import org.lajcik.kociolek.gui.components.RentedItemsList;
import org.lajcik.kociolek.service.RentalService;

import javax.swing.*;
import java.awt.*;

/**
 * @author lajcik
 */
public class RentedItemsPanel extends JPanel  {
    private RentedItemsList itemList = new RentedItemsList();

    public RentedItemsPanel() {
        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(450, 400));

        JPanel ticketPanel = new JPanel();
        ticketPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Numerki"));
        ticketPanel.setMinimumSize(new Dimension(300, 400));

        JPanel itemsPanel = new JPanel();
        itemsPanel.setMinimumSize(new Dimension(150, 400));
        itemsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Wypożyczone"));
        JScrollPane listScroller = new JScrollPane(itemList);
        itemsPanel.add(listScroller);

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0; c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        add(ticketPanel, c);

        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.7;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        add(itemsPanel, c);

        RentalService.addListener(itemList);
    }
}

package org.lajcik.kociolek.gui;

import org.lajcik.kociolek.gui.components.RentedItemsList;
import org.lajcik.kociolek.gui.components.TicketPanel;
import org.lajcik.kociolek.service.RentalService;
import org.lajcik.kociolek.util.ScrollablePanel;
import org.lajcik.kociolek.util.SpringHelper;
import org.lajcik.kociolek.util.WrapLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author lajcik
 */
public class RentedItemsPanel extends JPanel {
    private RentedItemsList itemList = new RentedItemsList();
    private TicketPanel ticketPanel = new TicketPanel();
    private RentalService rentalService = SpringHelper.getBean(RentalService.class);

    public RentedItemsPanel() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(600, 400));
        setMinimumSize(new Dimension(600, 400));

        JPanel ticketPanelHolder = new JPanel();
        ticketPanelHolder.setLayout(new BorderLayout());
        ticketPanelHolder.setPreferredSize(new Dimension(300, 400));
        ticketPanelHolder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Numerki"));

        ticketPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 5, 5));
//        ticketPanel.setLayout(new MigLayout("nogrid fillx"));
//        setSize(new Dimension(300, 1));
        ticketPanel.setScrollableHeight(ScrollablePanel.ScrollableSizeHint.FIT);
        ticketPanel.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.NONE);
//        ticketPanel.setScrollableBlockIncrement(HORIZONTAL, ScrollablePanel.IncrementType.PIXELS, BUTTON_SIZE);

        JScrollPane ticketScroller = new JScrollPane(ticketPanel);
        ticketScroller.setBorder(BorderFactory.createEmptyBorder());
        ticketPanelHolder.add(ticketScroller);

        JPanel itemsPanelHolder = new JPanel();
        itemsPanelHolder.setLayout(new BorderLayout());
        itemsPanelHolder.setPreferredSize(new Dimension(250, 400));
        itemsPanelHolder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Wypożyczone"));
        JScrollPane listScroller = new JScrollPane(itemList);
        itemsPanelHolder.add(listScroller);

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        add(ticketPanelHolder, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.7;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        add(itemsPanelHolder, c);

        rentalService.addListener(ticketPanel);
        rentalService.addListener(itemList);
    }
}

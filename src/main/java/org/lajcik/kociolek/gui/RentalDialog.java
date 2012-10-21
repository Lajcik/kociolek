package org.lajcik.kociolek.gui;

import net.miginfocom.swing.MigLayout;
import org.lajcik.kociolek.service.RentalService;
import org.lajcik.kociolek.util.WtfException;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author lajcik
 */
public class RentalDialog extends JDialog implements ActionListener {

    private JPanel itemPanel;
    private JButton saveButton;
    private JButton updateButton;
    private JButton cancelButton;
    private int ticketNumber;

    private final Mode mode;
    private JButton addItemButton;

    public RentalDialog(Frame frame, int ticketNumber, String... rentedItems) {
        super(frame, "", true);
        mode = rentedItems.length > 0 ? Mode.EDIT : Mode.CREATE;
        this.ticketNumber = ticketNumber;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        mainPanel.add(createTopPanel(ticketNumber), BorderLayout.PAGE_START);
        mainPanel.add(createCenterPanel(rentedItems), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(), BorderLayout.PAGE_END);


        pack();

        setLocationRelativeTo(frame);
        setVisible(true);
        setResizable(false);
    }

    private JPanel createTopPanel(int ticketNumber) {
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Numerek "));
        JLabel numberLabel = new JLabel("#" + Integer.toString(ticketNumber));
        numberLabel.setFont(numberLabel.getFont().deriveFont(Font.BOLD));
        topPanel.add(numberLabel);
        return topPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        saveButton = new JButton(mode == Mode.CREATE ? "Zapisz" : "Zwróć");
        saveButton.addActionListener(this);
        bottomPanel.add(saveButton);

        if (mode == Mode.EDIT) {
            updateButton = new JButton("Aktualizuj");
            updateButton.addActionListener(this);
            bottomPanel.add(updateButton);
        }

        bottomPanel.add(Box.createHorizontalGlue());

        cancelButton = new JButton("Anuluj");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);
        return bottomPanel;
    }

    private JPanel createCenterPanel(String... rentedItems) {
        itemPanel = new JPanel();
        itemPanel.setLayout(new MigLayout("wrap 3, debug", "[7]7[fill]7[]"));

        addItemButton = new JButton("+");
        addItemButton.addActionListener(this);
        itemPanel.add(Box.createHorizontalGlue(), "south");
        itemPanel.add(addItemButton, "south");

        for (String item : rentedItems) {
            addItem(item);
        }

        return itemPanel;
    }

    private void addItem(String item) {
        final JLabel lpLabel = new JLabel("x.");
        lpLabel.setName("LP");

        final JTextField itemName = new JTextField();
        itemName.setName("ITEM");
        itemName.setPreferredSize(new Dimension(200, itemName.getHeight()));
        if (item != null) {
            itemName.setText(item);
            itemName.setEditable(false);
        }
        final JButton removeThis = new JButton("-");
        itemPanel.add(lpLabel);
        itemPanel.add(itemName);
        itemPanel.add(removeThis);
        removeThis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemPanel.remove(lpLabel);
                itemPanel.remove(itemName);
                itemPanel.remove(removeThis);
                refresh();
            }
        });
    }

    private void refresh() {
        int i = 1;
        for (Component component : itemPanel.getComponents()) {
            if ("LP".equals(component.getName())) {
                JLabel lpLabel = (JLabel) component;
                lpLabel.setText(i++ + ".");
            }
        }
        itemPanel.validate();
        itemPanel.repaint();
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            List<String> items = new ArrayList<String>();
            for (Component component : itemPanel.getComponents()) {
                if ("ITEM".equals(component.getName())) {
                    JTextField textField = (JTextField) component;
                    String item = textField.getText().trim();
                    if (!item.equals("")) {
                        items.add(item);
                    }
                }
            }
            RentalService.rentItem(ticketNumber, items.toArray(new String[items.size()]));
            setVisible(false);
            return;
        }

        if (e.getSource() == addItemButton) {
            addItem(null);
            refresh();
            return;
        }

        if (mode == Mode.EDIT && e.getSource() == updateButton) {

            return;
        }

        if (e.getSource() == cancelButton) {
            setVisible(false);
            return;
        }
        throw new WtfException("Unexpected source: " + e.getSource() + " of event " + e);
    }

    enum Mode {
        CREATE, EDIT
    }
}

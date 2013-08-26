package org.lajcik.kociolek.gui;

import net.miginfocom.swing.MigLayout;
import org.lajcik.kociolek.domain.Item;
import org.lajcik.kociolek.domain.Rental;
import org.lajcik.kociolek.service.NameService;
import org.lajcik.kociolek.service.RentalService;
import org.lajcik.kociolek.util.AutoCompleteComboBox;
import org.lajcik.kociolek.util.SpringHelper;
import org.lajcik.kociolek.util.WtfException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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

    private RentalService rentalService;
    private NameService nameService;
    private List<String> itemDictionary = null;

    public RentalDialog() {
        super((JFrame)null, "", true);
        mode = Mode.CREATE;
        init(null);
    }

    public RentalDialog(int ticketNumber) {
        super((JFrame)null, "", true);
        mode = Mode.EDIT;
        init(ticketNumber);
    }

    private synchronized List<String> getItemDictionary() {
        if(itemDictionary == null) {
            itemDictionary = nameService.getItemDictionary();
        }
        return itemDictionary;
    }

    private void init(Integer ticketNumber) {
        rentalService = SpringHelper.getBean(RentalService.class);
        nameService = SpringHelper.getBean(NameService.class);
        Rental rental = null;
        if (mode == Mode.CREATE) {
            this.ticketNumber = rentalService.getNextTicketNumber();
        } else {
            this.ticketNumber = ticketNumber;
            rental = rentalService.getRental(ticketNumber);
        }


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        mainPanel.add(createTopPanel(), BorderLayout.PAGE_START);
        mainPanel.add(createCenterPanel(rental), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(), BorderLayout.PAGE_END);

        if (mode == Mode.CREATE) {
            addItem(null);
        } else {
            for (Item item : rental.getRentedItems()) {
                addItem(item.getName());
            }
        }
        refresh();

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    private JPanel createTopPanel() {
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

    private JPanel createCenterPanel(Rental rental) {
        itemPanel = new JPanel();
        itemPanel.setLayout(new MigLayout("wrap 3", "[7]7[fill]7[]"));
//        itemPanel.setLayout(new MigLayout("wrap 3, debug", "[7]7[fill]7[]"));

        addItemButton = new JButton("+");
        addItemButton.addActionListener(this);
        itemPanel.add(Box.createHorizontalGlue(), "south");
        itemPanel.add(addItemButton, "south");

        return itemPanel;
    }

    private String getItemName(Component component) {
        if(component instanceof JTextField) {
            return ((JTextField) component).getText();
        }
        if(component instanceof AutoCompleteComboBox) {
            AutoCompleteComboBox combo = (AutoCompleteComboBox) component;
            Object selectedItem = combo.getSelectedItem();
            return selectedItem.toString();
        }
        throw new IllegalStateException("Unexpected component instance: " + component);
    }

    private void addItem(String item) {
        final JLabel lpLabel = new JLabel("x.");
        lpLabel.setName("LP");

        final Component itemName;
        if(item == null) {
            itemName = new AutoCompleteComboBox(getItemDictionary());
        } else {
            JTextField jTextField = new JTextField();
            jTextField.setText(item);
            jTextField.setEditable(false);
            itemName = jTextField;
        }
        itemName.setName("ITEM");
        itemName.setPreferredSize(new Dimension(200, itemName.getHeight()));

        final JButton removeThis = new JButton("-");
        itemPanel.add(lpLabel);
        itemPanel.add(itemName);
        itemPanel.add(removeThis);
        removeThis.addActionListener(new ActionListener() {
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

    public void actionPerformed(ActionEvent e) {
        Action action = getAction(e);
        switch (action) {
            case RENT:
                List<String> items = getItems();
                rentalService.rentItem(ticketNumber, items.toArray(new String[items.size()]));
                setVisible(false);
                dispose();
                break;

            case RETURN:
                rentalService.returnItem(ticketNumber);
                setVisible(false);
                dispose();
                break;

            case UPDATE:
                List<String> newItems = getItems();
                rentalService.updateItem(ticketNumber, newItems.toArray(new String[newItems.size()]));
                setVisible(false);
                dispose();
                break;

            case CANCEL:
                if (mode == Mode.CREATE) {
                    rentalService.returnTicket(ticketNumber);
                }
                setVisible(false);
                dispose();
                break;

            case ADD_ITEM:
                addItem(null);
                refresh();
                break;
        }
    }

    private List<String> getItems() {
        List<String> items = new ArrayList<String>();
        for (Component component : itemPanel.getComponents()) {
            if ("ITEM".equals(component.getName())) {
                String item = getItemName(component);
                if (!item.equals("")) {
                    items.add(item);
                }
            }
        }
        return items;
    }

    private Action getAction(ActionEvent e) {
        if (e.getSource() == saveButton) {
            return mode == Mode.CREATE ? Action.RENT : Action.RETURN;
        }
        if (e.getSource() == addItemButton) {
            return Action.ADD_ITEM;
        }

        if (mode == Mode.EDIT && e.getSource() == updateButton) {
            return Action.UPDATE;
        }

        if (e.getSource() == cancelButton) {
            return Action.CANCEL;
        }
        throw new WtfException("Unexpected source: " + e.getSource() + " of event " + e);
    }

    enum Mode {
        CREATE, EDIT
    }

    enum Action {
        RENT, RETURN, UPDATE, CANCEL, ADD_ITEM
    }
}

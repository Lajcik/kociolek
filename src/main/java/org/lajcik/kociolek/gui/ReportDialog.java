package org.lajcik.kociolek.gui;

import net.miginfocom.swing.MigLayout;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * User: sienkom
 */
public class ReportDialog extends JDialog implements ActionListener {
    private JButton saveButton;
    private JButton cancelButton;

    private JDatePicker dateFrom;
    private JDatePicker dateTo;

    private JFileChooser fileChooser = new JFileChooser();

    public ReportDialog() {
        super((JFrame) null, "Raport", true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(), BorderLayout.PAGE_END);

        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    private Component createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        dateFrom = JDateComponentFactory.createJDatePicker();
        dateTo = JDateComponentFactory.createJDatePicker();

        panel.add(new JLabel("Data od (włącznie)"));
        panel.add((Component) dateFrom, "wrap");
        panel.add(new JLabel("Data do (włącznie)"));
        panel.add((Component) dateTo);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        saveButton = new JButton("Zapisz");
        saveButton.addActionListener(this);

        bottomPanel.add(saveButton);
        bottomPanel.add(Box.createHorizontalGlue());

        cancelButton = new JButton("Anuluj");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);

        return bottomPanel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            setVisible(false);
            dispose();
            return;
        }
        if (e.getSource() == saveButton) {
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
            }
            return;
        }

    }
}

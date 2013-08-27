package org.lajcik.kociolek.gui;

import net.miginfocom.swing.MigLayout;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import org.lajcik.kociolek.service.ReportService;
import org.lajcik.kociolek.util.SpringHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * User: sienkom
 */
public class ReportDialog extends JDialog implements ActionListener {
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel errorLabel;

    private JDatePicker dateFromField;
    private JDatePicker dateToField;

    private JFileChooser fileChooser = new JFileChooser();

    private ReportService reportService;

    public ReportDialog() {
        super((JFrame) null, "Raport", true);

        reportService = SpringHelper.getBean(ReportService.class);

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
        dateFromField = JDateComponentFactory.createJDatePicker();
        dateToField = JDateComponentFactory.createJDatePicker();
        errorLabel = new JLabel(" ");

        panel.add(new JLabel("Data od (włącznie)"));
        panel.add((Component) dateFromField, "wrap");
        panel.add(new JLabel("Data do (włącznie)"));
        panel.add((Component) dateToField, "wrap");

        panel.add(errorLabel, "alignx center");
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
            Calendar dateFrom = getDate(dateFromField);
            Calendar dateTo = getDate(dateToField);
            if(dateFrom == null || dateTo == null) {
                errorLabel.setText("Wypełnij obydwie daty!");
                errorLabel.setForeground(Color.RED);
                return;
            }

            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if(file.exists()) {
                    file.delete();
                }
                try {
                    reportService.generateReport(file, dateFrom, dateTo);
                    errorLabel.setText("Raport zapisany");
                    errorLabel.setForeground(Color.GREEN);
                } catch (Exception e1) {
                    errorLabel.setText("Nie udało się wygenerować raportu: " + e1.getMessage());
                    errorLabel.setForeground(Color.RED);
                }
            }
            return;
        }

    }

    private Calendar getDate(JDatePicker datePicker) {
        Calendar calendar = (Calendar) datePicker.getModel().getValue();
        return calendar;
    }
}

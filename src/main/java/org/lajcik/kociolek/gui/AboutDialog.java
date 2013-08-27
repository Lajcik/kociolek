package org.lajcik.kociolek.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: sienkom
 */
public class AboutDialog extends JDialog {

    public AboutDialog() {

        initUI();
        setVisible(true);
    }

    public final void initUI() {

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 10)));

/*
        ImageIcon icon = new ImageIcon("notes.png");
        JLabel label = new JLabel(icon);
        label.setAlignmentX(0.5f);
        add(label);
*/

        addLine("Kociolek 1.0", true);
        add(Box.createRigidArea(new Dimension(0, 10)));

        addLine("Autor: Michał Sieńko", false);
        addLine("email: mich.sienko@gmail.com", false);

        add(Box.createRigidArea(new Dimension(0, 50)));

        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });

        close.setAlignmentX(0.5f);
        add(close);

        setModalityType(ModalityType.APPLICATION_MODAL);

        setTitle("O Programie");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300, 200);
    }

    private void addLine(String text, boolean bold) {
        JLabel name = new JLabel(text);
        if (bold) {
            name.setFont(new Font(name.getFont().getName(), Font.BOLD, 13));
        }
        name.setAlignmentX(0.5f);
        add(name);

    }
}

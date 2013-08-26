package org.lajcik.kociolek.util;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Autocomplete combobox with filtering and
 * text inserting of new text
 *
 * @author Exterminator13
 */
public class AutoCompleteComboBox extends JComboBox {

    private static final Logger logger = Logger.getLogger(AutoCompleteComboBox.class.getName());
    private Model model;
    private final JTextComponent textComponent = (JTextComponent) getEditor().getEditorComponent();
    private boolean modelFilling = false;

    private boolean updatePopup;

    public AutoCompleteComboBox() {
        this(null);
    }

    public AutoCompleteComboBox(List<String> initialData) {
        if(initialData == null) {
            this.model = new Model();
        } else {
            this.model = new Model(initialData);
        }
        setEditable(true);

        logger.fine("setPattern() called from constructor");
        setPattern(null);
        updatePopup = false;

        textComponent.setDocument(new AutoCompleteDocument());
        setModel(model);
        setSelectedItem(null);

        new Timer(20, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (updatePopup && isDisplayable()) {
                    setPopupVisible(false);
                    if (model.getSize() > 0) {
                        setPopupVisible(true);
                    }
                    updatePopup = false;
                }
            }
        }).start();
    }

    private class AutoCompleteDocument extends PlainDocument {

        boolean arrowKeyPressed = false;

        public AutoCompleteDocument() {
            textComponent.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) {
                        logger.fine("[key listener] enter key pressed");
                        //there is no such element in the model for now
                        String text = textComponent.getText();
                        if (!model.data.contains(text)) {
                            logger.fine("addToTop() called from keyPressed()");
                            addToTop(text);
                        }
                    } else if (key == KeyEvent.VK_UP
                            || key == KeyEvent.VK_DOWN) {
                        arrowKeyPressed = true;
                        logger.fine("arrow key pressed");
                    }
                }
            });
        }

        void updateModel() throws BadLocationException {
            String textToMatch = getText(0, getLength());
            logger.fine("setPattern() called from updateModel()");
            setPattern(textToMatch);
        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {

            if (modelFilling) {
                logger.fine("[remove] model is being filled now");
                return;
            }

            super.remove(offs, len);
            if (arrowKeyPressed) {
                arrowKeyPressed = false;
                logger.fine("[remove] arrow key was pressed, updateModel() was NOT called");
            } else {
                logger.fine("[remove] calling updateModel()");
                updateModel();
            }
            clearSelection();
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

            if (modelFilling) {
                logger.fine("[insert] model is being filled now");
                return;
            }

            // insert the string into the document
            super.insertString(offs, str, a);

//            if (enterKeyPressed) {
//                logger.fine("[insertString] enter key was pressed");
//                enterKeyPressed = false;
//                return;
//            }

            String text = getText(0, getLength());
            if (arrowKeyPressed) {
                logger.fine("[insert] arrow key was pressed, updateModel() was NOT called");
                model.setSelectedItem(text);
                logger.fine(String.format("[insert] model.setSelectedItem(%s)", text));
                arrowKeyPressed = false;
            } else if (!text.equals(getSelectedItem())) {
                logger.fine("[insert] calling updateModel()");
                updateModel();
            }

            clearSelection();
        }

    }


    public void setText(String text) {
        if (model.data.contains(text)) {
            setSelectedItem(text);
        } else {
            addToTop(text);
            setSelectedIndex(0);
        }
    }

    public String getText() {
        return getEditor().getItem().toString();
    }

    private String previousPattern = null;

    private void setPattern(String pattern) {

        if (pattern != null && pattern.trim().isEmpty()) {
            pattern = null;
        }

        if (previousPattern == null && pattern == null ||
                pattern != null && pattern.equals(previousPattern)) {
            logger.fine("[setPatter] pattern is the same as previous: " + previousPattern);
            return;
        }

        previousPattern = pattern;

        modelFilling = true;
//        logger.fine("setPattern(): start");

        model.setPattern(pattern);

        if (logger.isLoggable(Level.FINE)) {
            StringBuilder b = new StringBuilder(100);
            b.append("pattern filter '").append(pattern == null ? "null" : pattern).append("' set:\n");
            for (int i = 0; i < model.getSize(); i++) {
                b.append(", ").append('[').append(model.getElementAt(i)).append(']');
            }
            int ind = b.indexOf(", ");
            if (ind != -1) {
                b.delete(ind, ind + 2);
            }
//            b.append('\n');
            logger.fine(b.toString());
        }
//        logger.fine("setPattern(): end");
        modelFilling = false;
        if (pattern != null) {
            updatePopup = true;
        }
    }


    private void clearSelection() {
        int i = getText().length();
        textComponent.setSelectionStart(i);
        textComponent.setSelectionEnd(i);
    }

//    @Override
//    public void setSelectedItem(Object anObject) {
//        super.setSelectedItem(anObject);
//        clearSelection();
//    }


    public synchronized void addToTop(String aString) {
        model.addToTop(aString);
    }

    private class Model extends AbstractListModel implements ComboBoxModel {

        //        String pattern;
        String selected;
        final int limit = 20;

        class Data {

            private List<String> list = new ArrayList<String>(limit);
            private List<String> lowercase = new ArrayList<String>(limit);
            private List<String> filtered;

            void add(String s) {
                list.add(s);
                lowercase.add(s.toLowerCase());
            }

            void addToTop(String s) {
                list.add(0, s);
                lowercase.add(0, s.toLowerCase());
            }

            void remove(int index) {
                list.remove(index);
                lowercase.remove(index);
            }

            List<String> getList() {
                return list;
            }

            List<String> getFiltered() {
                if (filtered == null)
                    filtered = list;
                return filtered;
            }

            int size() {
                return list.size();
            }

            void setPattern(String pattern) {
                if (pattern == null || pattern.isEmpty()) {
                    filtered = list;
                    AutoCompleteComboBox.this.setSelectedItem(model.getElementAt(0));
                    logger.fine(String.format("[setPattern] combo.setSelectedItem(null)"));
                } else {
                    filtered = new ArrayList<String>(limit);
                    pattern = pattern.toLowerCase();
                    for (int i = 0; i < lowercase.size(); i++) {
                        //case insensitive search
                        if (lowercase.get(i).contains(pattern)) {
                            filtered.add(list.get(i));
                        }
                    }
                    AutoCompleteComboBox.this.setSelectedItem(pattern);
                    logger.fine(String.format("[setPattern] combo.setSelectedItem(%s)", pattern));
                }
                logger.fine(String.format("pattern:'%s', filtered: %s", pattern, filtered));
            }

            boolean contains(String s) {
                if (s == null || s.trim().isEmpty()) {
                    return true;
                }
                s = s.toLowerCase();
                for (String item : lowercase) {
                    if (item.equals(s)) {
                        return true;
                    }
                }
                return false;
            }
        }

        Data data = new Data();

        public Model() {
        }

        public Model(List<String> initialData) {
            for(String str : initialData) {
                data.add(str);
            }
        }

        public void setPattern(String pattern) {

            int size1 = getSize();

            data.setPattern(pattern);

            int size2 = getSize();

            if (size1 < size2) {
                fireIntervalAdded(this, size1, size2 - 1);
                fireContentsChanged(this, 0, size1 - 1);
            } else if (size1 > size2) {
                fireIntervalRemoved(this, size2, size1 - 1);
                fireContentsChanged(this, 0, size2 - 1);
            }
        }

        public void addToTop(String aString) {
            if (aString == null || data.contains(aString))
                return;
            if (data.size() == 0)
                data.add(aString);
            else
                data.addToTop(aString);

            while (data.size() > limit) {
                int index = data.size() - 1;
                data.remove(index);
            }

            setPattern(null);
            model.setSelectedItem(aString);
            logger.fine(String.format("[addToTop] model.setSelectedItem(%s)", aString));
        }

        @Override
        public Object getSelectedItem() {
            return selected;
        }

        @Override
        public void setSelectedItem(Object anObject) {
            if ((selected != null && !selected.equals(anObject)) ||
                    selected == null && anObject != null) {
                selected = (String) anObject;
                fireContentsChanged(this, -1, -1);
            }
        }

        @Override
        public int getSize() {
            return data.getFiltered().size();
        }

        @Override
        public Object getElementAt(int index) {
            return data.getFiltered().get(index);
        }

    }

}


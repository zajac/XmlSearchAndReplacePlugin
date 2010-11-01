package org.jetbrains.plugins.xml.searchandreplace.ui.view;

import javax.swing.*;
import java.awt.*;

public class TagPredicatePanel extends JPanel {

    private JTextField tagNameField = new JTextField("TAG");

    public TagPredicatePanel() {
        setLayout(new FlowLayout());
        add(tagNameField);
    }

    public String getTagName() {
        return tagNameField.getText();
    }
}

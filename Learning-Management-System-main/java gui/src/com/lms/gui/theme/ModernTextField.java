package com.lms.gui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Consumer;

public class ModernTextField extends JTextField {
    private String placeholder = "";
    private boolean isFocused = false;
    private int cornerRadius = 8;

    public ModernTextField(int columns) {
        super(columns);
        init();
    }

    public ModernTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        init();
    }

    private void init() {
        setFont(UITheme.FONT_BODY);
        setForeground(UITheme.TEXT_MAIN);
        setCaretColor(UITheme.PRIMARY);
        setOpaque(false);
        setBorder(new EmptyBorder(10, 14, 10, 14));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    public void onTextChanged(Consumer<String> callback) {
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { callback.accept(getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) { callback.accept(getText()); }
            @Override
            public void changedUpdate(DocumentEvent e) { callback.accept(getText()); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        UITheme.setupAntiAliasing(g);
        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        // Background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);

        // Border
        if (isFocused) {
            g2.setColor(UITheme.PRIMARY);
            g2.setStroke(new BasicStroke(1.8f));
        } else {
            g2.setColor(UITheme.BORDER);
            g2.setStroke(new BasicStroke(1.0f));
        }
        g2.drawRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);

        super.paintComponent(g);

        // Placeholder text
        if (getText().isEmpty() && !placeholder.isEmpty() && !isFocused) {
            g2.setColor(UITheme.TEXT_LIGHT);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, 14, y);
        }
    }
}

package com.lms.gui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernTextField extends JTextField {
    private String placeholder = "";
    private boolean isFocused = false;
    private int cornerRadius = 10;

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
        setBorder(new EmptyBorder(11, 14, 11, 14));

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

    @Override
    protected void paintComponent(Graphics g) {
        UITheme.setupAntiAliasing(g);
        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        // Subtle focus glow
        if (isFocused) {
            g2.setColor(UITheme.withAlpha(UITheme.PRIMARY, 18));
            g2.fill(new RoundRectangle2D.Float(-2, -2, w + 3, h + 3, cornerRadius + 4, cornerRadius + 4));
        }

        // Background fill
        g2.setColor(isFocused ? Color.WHITE : new Color(249, 250, 251));
        g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, cornerRadius, cornerRadius));

        // Border - thicker and colored when focused
        if (isFocused) {
            g2.setColor(UITheme.PRIMARY);
            g2.setStroke(new BasicStroke(1.6f));
        } else {
            g2.setColor(UITheme.BORDER);
            g2.setStroke(new BasicStroke(1.0f));
        }
        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 2, h - 2, cornerRadius, cornerRadius));

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

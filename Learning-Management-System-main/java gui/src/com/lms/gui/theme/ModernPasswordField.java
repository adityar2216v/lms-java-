package com.lms.gui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernPasswordField extends JPasswordField {
    private String placeholder = "";
    private boolean isFocused = false;
    private int cornerRadius = 8;
    private boolean showPassword = false;
    private char defaultEchoChar;
    private boolean eyeHovered = false;

    public ModernPasswordField(int columns) {
        super(columns);
        init();
    }

    public ModernPasswordField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        init();
    }

    private void init() {
        defaultEchoChar = getEchoChar();
        setFont(UITheme.FONT_BODY);
        setForeground(UITheme.TEXT_MAIN);
        setCaretColor(UITheme.PRIMARY);
        setOpaque(false);
        // Leave room on right for the eye toggle icon
        setBorder(new EmptyBorder(10, 14, 10, 36));

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

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isEyeArea(e.getX(), e.getY())) {
                    togglePasswordVisibility();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                boolean over = isEyeArea(e.getX(), e.getY());
                if (over != eyeHovered) {
                    eyeHovered = over;
                    setCursor(over ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean over = isEyeArea(e.getX(), e.getY());
                if (over != eyeHovered) {
                    eyeHovered = over;
                    setCursor(over ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    repaint();
                }
            }
        });
    }

    private boolean isEyeArea(int x, int y) {
        int right = getWidth() - 10;
        int left = right - 22;
        return x >= left && x <= right && y >= 8 && y <= getHeight() - 8;
    }

    public void togglePasswordVisibility() {
        showPassword = !showPassword;
        if (showPassword) {
            setEchoChar((char) 0);
        } else {
            setEchoChar(defaultEchoChar);
        }
        repaint();
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
        if (getPassword().length == 0 && !placeholder.isEmpty() && !isFocused) {
            g2.setColor(UITheme.TEXT_LIGHT);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, 14, y);
        }

        // Draw Eye Toggle Icon on the right
        int eyeSize = 16;
        int eyeX = w - eyeSize - 12;
        int eyeY = (h - eyeSize) / 2;

        VectorIcon eyeIcon = new VectorIcon(
                showPassword ? VectorIcon.Type.EYE_OFF : VectorIcon.Type.EYE,
                eyeSize,
                eyeHovered ? UITheme.PRIMARY : UITheme.TEXT_MUTED
        );
        eyeIcon.paintIcon(this, g2, eyeX, eyeY);
    }
}

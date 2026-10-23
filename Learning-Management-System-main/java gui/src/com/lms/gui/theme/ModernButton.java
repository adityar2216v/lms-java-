package com.lms.gui.theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    public enum Style {
        PRIMARY, SUCCESS, DANGER, WARNING, SECONDARY, OUTLINE, DARK
    }

    private Style style;
    private int cornerRadius = 10;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public ModernButton(String text) {
        this(text, Style.PRIMARY);
    }

    public ModernButton(String text, Style style) {
        super(text);
        this.style = style;
        setFont(UITheme.FONT_BODY_BOLD);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(10, 18, 10, 18));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        UITheme.setupAntiAliasing(g);
        Graphics2D g2 = (Graphics2D) g;

        Color bg = getBaseBgColor();
        Color fg = getBaseFgColor();

        if (style == Style.OUTLINE) {
            if (isHovered) {
                bg = UITheme.PRIMARY_LIGHT;
                fg = UITheme.PRIMARY;
            } else {
                bg = Color.WHITE;
                fg = UITheme.PRIMARY;
            }
        } else {
            if (isPressed) {
                bg = darken(bg, 0.2f);
            } else if (isHovered) {
                bg = lighten(bg, 0.1f);
            }
        }

        int w = getWidth();
        int h = getHeight();

        // Background
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

        // Border for outline style
        if (style == Style.OUTLINE) {
            g2.setColor(UITheme.PRIMARY);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, w - 2, h - 2, cornerRadius, cornerRadius);
        }

        // Draw text and icon
        setForeground(fg);
        super.paintComponent(g);
    }

    private Color getBaseBgColor() {
        switch (style) {
            case PRIMARY: return UITheme.PRIMARY;
            case SUCCESS: return UITheme.SUCCESS;
            case DANGER: return UITheme.DANGER;
            case WARNING: return UITheme.WARNING;
            case SECONDARY: return new Color(241, 245, 249);
            case DARK: return UITheme.SIDEBAR_BG;
            case OUTLINE: return Color.WHITE;
            default: return UITheme.PRIMARY;
        }
    }

    private Color getBaseFgColor() {
        if (style == Style.SECONDARY) return UITheme.TEXT_MAIN;
        if (style == Style.OUTLINE) return UITheme.PRIMARY;
        return Color.WHITE;
    }

    private Color lighten(Color c, float factor) {
        int r = Math.min(255, (int) (c.getRed() + (255 - c.getRed()) * factor));
        int g = Math.min(255, (int) (c.getGreen() + (255 - c.getGreen()) * factor));
        int b = Math.min(255, (int) (c.getBlue() + (255 - c.getBlue()) * factor));
        return new Color(r, g, b);
    }

    private Color darken(Color c, float factor) {
        int r = Math.max(0, (int) (c.getRed() * (1 - factor)));
        int g = Math.max(0, (int) (c.getGreen() * (1 - factor)));
        int b = Math.max(0, (int) (c.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
}

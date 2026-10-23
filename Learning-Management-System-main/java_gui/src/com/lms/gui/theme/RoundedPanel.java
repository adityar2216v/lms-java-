package com.lms.gui.theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;
    private Color borderColor;
    private int borderWidth;
    private boolean elevated;

    public RoundedPanel(int radius, Color bg) {
        this(radius, bg, null, 0);
    }

    public RoundedPanel(int radius, Color bg, boolean elevated) {
        this(radius, bg, null, 0);
        this.elevated = elevated;
    }

    public RoundedPanel(int radius, Color bg, Color border, int borderWidth) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bg;
        this.borderColor = border;
        this.borderWidth = borderWidth;
        this.elevated = true; // default subtle elevation
        setOpaque(false);
    }

    public void setBackgroundColor(Color bg) {
        this.backgroundColor = bg;
        repaint();
    }

    public void setBorderColor(Color border) {
        this.borderColor = border;
        repaint();
    }

    public void setElevated(boolean elevated) {
        this.elevated = elevated;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        UITheme.setupAntiAliasing(g);
        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();
        int shadowOffset = elevated ? 3 : 0;

        // Subtle drop shadow layers for depth
        if (elevated) {
            UITheme.drawSoftShadow(g2, 1, 1, w - 3, h - shadowOffset - 1, cornerRadius, 4);
        }

        // Main background fill
        g2.setColor(backgroundColor != null ? backgroundColor : getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - shadowOffset - 1, cornerRadius, cornerRadius));

        // Optional border
        if (borderColor != null && borderWidth > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderWidth));
            g2.draw(new RoundRectangle2D.Float(
                borderWidth / 2f, borderWidth / 2f,
                w - borderWidth - 1, h - borderWidth - shadowOffset - 1,
                cornerRadius, cornerRadius
            ));
        } else if (elevated) {
            // Subtle 1px border for definition
            g2.setColor(UITheme.withAlpha(UITheme.BORDER, 100));
            g2.setStroke(new BasicStroke(0.5f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 2f, h - shadowOffset - 2f, cornerRadius, cornerRadius));
        }
    }
}

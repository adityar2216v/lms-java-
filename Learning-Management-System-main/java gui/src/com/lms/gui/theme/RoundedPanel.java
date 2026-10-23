package com.lms.gui.theme;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;
    private Color borderColor;
    private int borderWidth;

    public RoundedPanel(int radius, Color bg) {
        this(radius, bg, UITheme.BORDER, 1);
    }

    public RoundedPanel(int radius, Color bg, Color border, int borderWidth) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bg;
        this.borderColor = border;
        this.borderWidth = borderWidth;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        UITheme.setupAntiAliasing(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        // Draw background
        g2.setColor(backgroundColor != null ? backgroundColor : getBackground());
        g2.fillRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);

        // Draw border if needed
        if (borderColor != null && borderWidth > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderWidth));
            g2.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
        }
    }
}

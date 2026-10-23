package com.lms.gui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * Official Brand Assets & Logo Generator for SmartLearn India.
 * Generates vector-sharp brand emblems, window icons, and header wordmarks.
 */
public class BrandLogo {

    /**
     * Generates a high-resolution BufferedImage of the SmartLearn India logo.
     * Combines an academic graduation mortarboard cap, an open book of knowledge,
     * and a vibrant saffron knowledge accent.
     */
    public static BufferedImage createLogoImage(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        float s = size / 100.0f;
        g2.scale(s, s);

        // Circular background container with subtle gradient
        GradientPaint bgGrad = new GradientPaint(
                0, 0, new Color(30, 27, 75), // Deep Indigo
                100, 100, new Color(15, 23, 42) // Slate 900
        );
        g2.setPaint(bgGrad);
        g2.fill(new RoundRectangle2D.Float(4, 4, 92, 92, 24, 24));

        // Subtle saffron inner border accent
        g2.setColor(new Color(249, 115, 22, 180)); // Saffron 500
        g2.setStroke(new BasicStroke(2.0f));
        g2.draw(new RoundRectangle2D.Float(5, 5, 90, 90, 23, 23));

        // 1. Open Book at the bottom (Knowledge Base)
        Path2D bookLeft = new Path2D.Float();
        bookLeft.moveTo(50, 72);
        bookLeft.curveTo(36, 65, 22, 68, 18, 68);
        bookLeft.lineTo(18, 52);
        bookLeft.curveTo(22, 52, 36, 49, 50, 56);
        bookLeft.closePath();

        GradientPaint bookGrad = new GradientPaint(18, 50, Color.WHITE, 50, 72, new Color(226, 232, 240));
        g2.setPaint(bookGrad);
        g2.fill(bookLeft);

        Path2D bookRight = new Path2D.Float();
        bookRight.moveTo(50, 72);
        bookRight.curveTo(64, 65, 78, 68, 82, 68);
        bookRight.lineTo(82, 52);
        bookRight.curveTo(78, 52, 64, 49, 50, 56);
        bookRight.closePath();
        g2.fill(bookRight);

        // Book spine line
        g2.setColor(new Color(148, 163, 184));
        g2.setStroke(new BasicStroke(1.8f));
        g2.draw(new Line2D.Float(50, 56, 50, 73));

        // Book page lines (subtle text lines)
        g2.setColor(new Color(203, 213, 225));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new Line2D.Float(24, 57, 44, 59));
        g2.draw(new Line2D.Float(24, 62, 42, 64));
        g2.draw(new Line2D.Float(56, 59, 76, 57));
        g2.draw(new Line2D.Float(58, 64, 76, 62));

        // 2. Graduation Mortarboard Cap (Top)
        Path2D capTop = new Path2D.Float();
        capTop.moveTo(50, 22); // Top apex
        capTop.lineTo(82, 33); // Right corner
        capTop.lineTo(50, 44); // Bottom apex
        capTop.lineTo(18, 33); // Left corner
        capTop.closePath();

        GradientPaint capGrad = new GradientPaint(
                18, 22, new Color(37, 99, 235), // Royal Blue
                82, 44, new Color(29, 78, 216)  // Royal Blue Dark
        );
        g2.setPaint(capGrad);
        g2.fill(capTop);

        g2.setColor(new Color(255, 255, 255, 120));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(capTop);

        // Cap Skull Cap Underneath
        Path2D skull = new Path2D.Float();
        skull.moveTo(33, 40);
        skull.curveTo(33, 48, 67, 48, 67, 40);
        skull.lineTo(67, 42);
        skull.curveTo(67, 51, 33, 51, 33, 42);
        skull.closePath();
        g2.setColor(new Color(30, 58, 138));
        g2.fill(skull);

        // 3. Indian Saffron Tassel with Button
        g2.setColor(new Color(249, 115, 22)); // Vibrant Saffron
        g2.fill(new Ellipse2D.Float(47.5f, 31f, 5f, 5f)); // Center button

        Path2D tassel = new Path2D.Float();
        tassel.moveTo(50, 33);
        tassel.curveTo(62, 34, 72, 39, 75, 46);
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(tassel);

        // Hanging tassel fringe
        g2.fill(new RoundRectangle2D.Float(73, 46, 4.5f, 8f, 2f, 2f));

        // 4. Subtle Radiant Glow / Knowledge Sparks
        g2.setColor(new Color(253, 224, 71, 200)); // Golden yellow
        g2.fill(new Ellipse2D.Float(48, 14, 4, 4));
        g2.fill(new Ellipse2D.Float(13, 24, 3, 3));
        g2.fill(new Ellipse2D.Float(84, 24, 3, 3));

        g2.dispose();
        return img;
    }

    /**
     * Creates a branded banner header with the logo icon, wordmark, and tagline.
     */
    public static JPanel createBrandHeader(boolean darkBackground) {
        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setOpaque(false);

        // Logo Icon
        Image logoImg = createLogoImage(52);
        JLabel iconLbl = new JLabel(new ImageIcon(logoImg));

        // Titles
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel brandLbl = new JLabel("SmartLearn India");
        brandLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brandLbl.setForeground(darkBackground ? Color.WHITE : UITheme.TEXT_MAIN);

        JLabel tagLbl = new JLabel("Learn Smarter. Grow Further.");
        tagLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tagLbl.setForeground(UITheme.SAFFRON);

        JLabel catLbl = new JLabel("National Smart LMS Platform");
        catLbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        catLbl.setForeground(darkBackground ? UITheme.SIDEBAR_TEXT : UITheme.TEXT_MUTED);

        titlePanel.add(brandLbl);
        titlePanel.add(Box.createVerticalStrut(2));
        titlePanel.add(tagLbl);
        titlePanel.add(Box.createVerticalStrut(1));
        titlePanel.add(catLbl);

        header.add(iconLbl, BorderLayout.WEST);
        header.add(titlePanel, BorderLayout.CENTER);
        return header;
    }
}

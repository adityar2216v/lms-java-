package com.lms.gui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class UITheme {
    // ── Primary Palette (Warm Indigo - approachable, not sterile) ──────────────
    public static final Color PRIMARY = new Color(88, 80, 236);         // Vivid Indigo
    public static final Color PRIMARY_DARK = new Color(72, 62, 210);    // Deep Indigo
    public static final Color PRIMARY_LIGHT = new Color(237, 233, 254); // Indigo Tint
    public static final Color PRIMARY_GLOW = new Color(129, 120, 248);  // Soft Indigo glow
    public static final Color ACCENT = new Color(6, 182, 212);          // Teal 500

    public static final Color SUCCESS = new Color(34, 197, 94);         // Green 500
    public static final Color SUCCESS_DARK = new Color(22, 163, 74);
    public static final Color SUCCESS_LIGHT = new Color(220, 252, 231);

    public static final Color DANGER = new Color(239, 68, 68);          // Red 500
    public static final Color DANGER_DARK = new Color(220, 38, 38);
    public static final Color DANGER_LIGHT = new Color(254, 226, 226);

    public static final Color WARNING = new Color(234, 179, 8);         // Yellow 500
    public static final Color WARNING_LIGHT = new Color(254, 249, 195);

    public static final Color INFO = new Color(59, 130, 246);           // Blue 500

    // ── Neutral Palette (Carefully balanced warmth) ───────────────────────────
    public static final Color SIDEBAR_BG = new Color(17, 24, 39);       // Gray 900 (rich dark)
    public static final Color SIDEBAR_HOVER = new Color(31, 41, 55);    // Gray 800
    public static final Color SIDEBAR_ACTIVE = new Color(55, 48, 120);  // Subtle indigo undertone
    public static final Color SIDEBAR_TEXT = new Color(156, 163, 175);  // Gray 400
    public static final Color SIDEBAR_TEXT_ACTIVE = new Color(255, 255, 255);

    public static final Color APP_BG = new Color(245, 247, 250);        // Warm off-white
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER = new Color(229, 231, 235);        // Gray 200
    public static final Color DIVIDER = new Color(243, 244, 246);       // Gray 100

    public static final Color TEXT_MAIN = new Color(17, 24, 39);        // Gray 900
    public static final Color TEXT_SECONDARY = new Color(55, 65, 81);   // Gray 700
    public static final Color TEXT_MUTED = new Color(107, 114, 128);    // Gray 500
    public static final Color TEXT_LIGHT = new Color(156, 163, 175);    // Gray 400

    // ── Shadow Colors ─────────────────────────────────────────────────────────
    public static final Color SHADOW_LIGHT = new Color(0, 0, 0, 18);
    public static final Color SHADOW_MEDIUM = new Color(0, 0, 0, 30);
    public static final Color SHADOW_STRONG = new Color(0, 0, 0, 45);

    // ── Typography (Consistent scale, warm fonts) ─────────────────────────────
    public static final Font FONT_HERO = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 21);
    public static final Font FONT_SECTION = new Font("Segoe UI Semibold", Font.BOLD, 17);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_TINY = new Font("Segoe UI", Font.PLAIN, 10);

    // ── Spacing ───────────────────────────────────────────────────────────────
    public static final int SPACING_XS = 4;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 16;
    public static final int SPACING_LG = 24;
    public static final int SPACING_XL = 32;

    // ── Helpers ───────────────────────────────────────────────────────────────
    public static void setupAntiAliasing(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        }
    }

    /** Draw a soft drop shadow behind a rounded rect */
    public static void drawSoftShadow(Graphics2D g2, int x, int y, int w, int h, int radius, int layers) {
        for (int i = layers; i > 0; i--) {
            float alpha = 8f / (i * i + 4);
            g2.setColor(new Color(0, 0, 0, Math.min(255, (int)(alpha * 255))));
            g2.fill(new RoundRectangle2D.Float(
                x - i, y + i, w + i * 2, h + i, radius + i, radius + i
            ));
        }
    }

    /** Lighten a color by a factor (0.0 to 1.0) */
    public static Color lighten(Color c, float factor) {
        int r = Math.min(255, (int)(c.getRed() + (255 - c.getRed()) * factor));
        int g = Math.min(255, (int)(c.getGreen() + (255 - c.getGreen()) * factor));
        int b = Math.min(255, (int)(c.getBlue() + (255 - c.getBlue()) * factor));
        return new Color(r, g, b);
    }

    /** Darken a color by a factor (0.0 to 1.0) */
    public static Color darken(Color c, float factor) {
        int r = Math.max(0, (int)(c.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(c.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(c.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }

    /** Build a translucent version of a color */
    public static Color withAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(0, Math.min(255, alpha)));
    }

    // ── Reusable Component Builders ──────────────────────────────────────────

    public static JPanel createHeader(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout(5, 4));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(20, 24, 16, 24));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(FONT_TITLE);
        titleLbl.setForeground(TEXT_MAIN);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(FONT_BODY);
        subLbl.setForeground(TEXT_MUTED);

        p.add(titleLbl, BorderLayout.NORTH);
        p.add(subLbl, BorderLayout.SOUTH);
        return p;
    }

    public static JPanel createStatCard(String label, String value, String iconText, Color accentColor) {
        // Card with subtle shadow via RoundedPanel
        RoundedPanel card = new RoundedPanel(14, CARD_BG);
        card.setLayout(new BorderLayout(12, 8));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        // Left side: Value + Label
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(FONT_HERO);
        valLbl.setForeground(TEXT_MAIN);

        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(FONT_SMALL);
        lblLbl.setForeground(TEXT_MUTED);

        left.add(valLbl);
        left.add(Box.createVerticalStrut(3));
        left.add(lblLbl);

        // Right side: Icon with subtle colored background
        JPanel iconHolder = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                UITheme.setupAntiAliasing(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(withAlpha(accentColor, 22));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        iconHolder.setOpaque(false);
        iconHolder.setPreferredSize(new Dimension(52, 52));

        JLabel iconLbl = new JLabel(iconText, SwingConstants.CENTER);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLbl.setForeground(accentColor);
        iconHolder.add(iconLbl);

        card.add(left, BorderLayout.CENTER);
        card.add(iconHolder, BorderLayout.EAST);
        return card;
    }

    /** Create a styled separator line */
    public static JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(DIVIDER);
        sep.setBackground(DIVIDER);
        return sep;
    }

    /** Create a badge label with colored background */
    public static JLabel createBadge(String text, Color bgColor, Color fgColor) {
        JLabel badge = new JLabel("  " + text + "  ") {
            @Override
            protected void paintComponent(Graphics g) {
                UITheme.setupAntiAliasing(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        badge.setFont(FONT_TINY);
        badge.setForeground(fgColor);
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));
        return badge;
    }
}

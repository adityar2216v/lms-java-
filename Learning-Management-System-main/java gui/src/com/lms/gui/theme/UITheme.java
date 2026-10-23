package com.lms.gui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UITheme {
    // ── Primary Brand Palette (Slate & Modern Indigo) ──────────────────────────
    public static final Color PRIMARY = new Color(79, 70, 229);        // Indigo 600
    public static final Color PRIMARY_DARK = new Color(67, 56, 202);   // Indigo 700
    public static final Color PRIMARY_LIGHT = new Color(238, 242, 255); // Indigo 50
    public static final Color ACCENT = new Color(14, 165, 233);        // Sky 500
    public static final Color ACCENT_LIGHT = new Color(240, 249, 255);

    public static final Color SUCCESS = new Color(16, 185, 129);       // Emerald 500
    public static final Color SUCCESS_DARK = new Color(5, 150, 105);
    public static final Color SUCCESS_LIGHT = new Color(236, 253, 245);

    public static final Color DANGER = new Color(239, 68, 68);         // Rose 500
    public static final Color DANGER_DARK = new Color(220, 38, 38);
    public static final Color DANGER_LIGHT = new Color(254, 242, 242);

    public static final Color WARNING = new Color(245, 158, 11);       // Amber 500
    public static final Color WARNING_LIGHT = new Color(255, 251, 235);

    public static final Color PURPLE = new Color(139, 92, 246);        // Violet 500
    public static final Color PURPLE_LIGHT = new Color(245, 243, 255);

    // ── Slate Neutrals (Executive Dark & Clean Light) ──────────────────────────
    public static final Color SIDEBAR_BG = new Color(15, 23, 42);      // Slate 900
    public static final Color SIDEBAR_HOVER = new Color(30, 41, 59);   // Slate 800
    public static final Color SIDEBAR_ACTIVE = new Color(51, 65, 85);  // Slate 700
    public static final Color SIDEBAR_TEXT = new Color(148, 163, 184); // Slate 400
    public static final Color SIDEBAR_BORDER = new Color(30, 41, 59);

    public static final Color APP_BG = new Color(248, 250, 252);       // Slate 50
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER = new Color(226, 232, 240);       // Slate 200
    public static final Color BORDER_DARK = new Color(203, 213, 225);  // Slate 300

    public static final Color TEXT_MAIN = new Color(15, 23, 42);       // Slate 900
    public static final Color TEXT_BODY = new Color(51, 65, 85);       // Slate 700
    public static final Color TEXT_MUTED = new Color(100, 116, 139);   // Slate 500
    public static final Color TEXT_LIGHT = new Color(148, 163, 184);   // Slate 400

    // ── Typography ─────────────────────────────────────────────────────────────
    public static final Font FONT_HERO = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 11);

    // ── Anti-Aliasing Helpers ──────────────────────────────────────────────────
    public static void setupAntiAliasing(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
    }

    public static JPanel createHeader(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout(5, 4));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(16, 22, 14, 22));

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

    public static JPanel createSectionHeader(String title, String subtitle, JComponent actionComponent) {
        JPanel p = new JPanel(new BorderLayout(10, 4));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(6, 4, 10, 4));

        JPanel titles = new JPanel(new GridLayout(2, 1, 2, 2));
        titles.setOpaque(false);
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(FONT_SUBTITLE);
        titleLbl.setForeground(TEXT_MAIN);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(FONT_SMALL);
        subLbl.setForeground(TEXT_MUTED);
        titles.add(titleLbl);
        titles.add(subLbl);

        p.add(titles, BorderLayout.WEST);
        if (actionComponent != null) {
            p.add(actionComponent, BorderLayout.EAST);
        }
        return p;
    }

    public static JPanel createStatCard(String label, String value, String iconText, Color accentColor, String trendText) {
        RoundedPanel card = new RoundedPanel(16, CARD_BG);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(FONT_HERO);
        valLbl.setForeground(TEXT_MAIN);

        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(FONT_BODY_BOLD);
        lblLbl.setForeground(TEXT_MUTED);

        JLabel trendLbl = new JLabel(trendText != null ? trendText : "Updated live");
        trendLbl.setFont(FONT_SMALL);
        trendLbl.setForeground(accentColor);

        left.add(valLbl);
        left.add(Box.createVerticalStrut(3));
        left.add(lblLbl);
        left.add(Box.createVerticalStrut(4));
        left.add(trendLbl);

        JPanel iconBox = new JPanel(new GridBagLayout());
        iconBox.setOpaque(false);
        iconBox.setPreferredSize(new Dimension(54, 54));

        JLabel iconLbl = new JLabel(iconText, SwingConstants.CENTER);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));

        iconBox.add(iconLbl);

        card.add(left, BorderLayout.CENTER);
        card.add(iconBox, BorderLayout.EAST);
        return card;
    }

    public static JComponent createStatusBadge(String text, Color bg, Color fg) {
        JLabel badge = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                setupAntiAliasing(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        badge.setFont(FONT_SMALL_BOLD);
        badge.setForeground(fg);
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));
        badge.setOpaque(false);
        return badge;
    }
}

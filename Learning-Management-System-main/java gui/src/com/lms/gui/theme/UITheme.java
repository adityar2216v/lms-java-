package com.lms.gui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern, India-focused Design System for SmartLearn India.
 * Implements the color palette: Deep Navy/Indigo, Royal Blue, Restrained Saffron, Soft Neutrals.
 */
public class UITheme {

    // ── Primary Brand Palette (SmartLearn India) ──────────────────────────────
    public static final Color PRIMARY = new Color(37, 99, 235);          // Royal Blue 600
    public static final Color PRIMARY_DARK = new Color(29, 78, 216);     // Royal Blue 700
    public static final Color PRIMARY_DEEP = new Color(30, 27, 75);      // Deep Indigo 950
    public static final Color PRIMARY_LIGHT = new Color(239, 246, 255);  // Royal Blue 50

    // Indian Saffron Accents (Restrained, Modern, Elegant)
    public static final Color SAFFRON = new Color(249, 115, 22);         // Saffron Orange 500 (#F97316)
    public static final Color SAFFRON_DARK = new Color(194, 65, 12);     // Saffron 700
    public static final Color SAFFRON_LIGHT = new Color(255, 247, 237);  // Saffron 50
    public static final Color ACCENT = SAFFRON;
    public static final Color ACCENT_LIGHT = SAFFRON_LIGHT;

    // Semantic States
    public static final Color SUCCESS = new Color(16, 185, 129);         // Emerald Green 500
    public static final Color SUCCESS_DARK = new Color(5, 150, 105);
    public static final Color SUCCESS_LIGHT = new Color(236, 253, 245);

    public static final Color DANGER = new Color(239, 68, 68);           // Rose Red 500
    public static final Color DANGER_DARK = new Color(220, 38, 38);
    public static final Color DANGER_LIGHT = new Color(254, 242, 242);

    public static final Color WARNING = new Color(245, 158, 11);         // Amber 500
    public static final Color WARNING_LIGHT = new Color(255, 251, 235);

    public static final Color PURPLE = new Color(124, 58, 237);          // Violet 600
    public static final Color PURPLE_LIGHT = new Color(245, 243, 255);

    // ── Slate & Navy Neutrals ──────────────────────────────────────────────────
    public static final Color SIDEBAR_BG = new Color(15, 23, 42);        // Deep Slate 900
    public static final Color SIDEBAR_HOVER = new Color(30, 41, 59);     // Slate 800
    public static final Color SIDEBAR_ACTIVE = new Color(37, 99, 235);   // Royal Blue Active
    public static final Color SIDEBAR_TEXT = new Color(148, 163, 184);   // Slate 400
    public static final Color SIDEBAR_BORDER = new Color(30, 41, 59);

    public static final Color APP_BG = new Color(248, 250, 252);         // Slate 50 Neutral
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER = new Color(226, 232, 240);         // Slate 200
    public static final Color BORDER_DARK = new Color(203, 213, 225);    // Slate 300

    public static final Color TEXT_MAIN = new Color(15, 23, 42);         // Slate 900
    public static final Color TEXT_BODY = new Color(51, 65, 85);         // Slate 700
    public static final Color TEXT_MUTED = new Color(100, 116, 139);     // Slate 500
    public static final Color TEXT_LIGHT = new Color(148, 163, 184);     // Slate 400

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
        p.setBorder(new EmptyBorder(14, 20, 12, 20));

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

    public static JPanel createStatCard(String label, String value, String iconText, Color accentColor) {
        return createStatCard(label, value, iconText, accentColor, "Updated live");
    }

    public static JPanel createStatCard(String label, String value, String iconText, Color accentColor, String trendText) {
        RoundedPanel card = new RoundedPanel(16, CARD_BG);
        card.setLayout(new BorderLayout(12, 10));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

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
        trendLbl.setFont(FONT_SMALL_BOLD);
        trendLbl.setForeground(accentColor != null ? accentColor : PRIMARY);

        left.add(valLbl);
        left.add(Box.createVerticalStrut(2));
        left.add(lblLbl);
        left.add(Box.createVerticalStrut(4));
        left.add(trendLbl);

        // Right circular icon badge
        JPanel iconCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setupAntiAliasing(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 28));
                g2.fill(new Ellipse2D.Float(2, 2, getWidth() - 4, getHeight() - 4));
            }
        };
        iconCircle.setLayout(new GridBagLayout());
        iconCircle.setOpaque(false);
        iconCircle.setPreferredSize(new Dimension(50, 50));

        // Use vector icon if recognized, else fallback to text/symbol
        JLabel iconLbl = createIconLabel(iconText, accentColor);
        iconCircle.add(iconLbl);

        card.add(left, BorderLayout.CENTER);
        card.add(iconCircle, BorderLayout.EAST);
        return card;
    }

    private static JLabel createIconLabel(String iconText, Color color) {
        VectorIcon.Type type = mapIconTextToType(iconText);
        if (type != null) {
            return new JLabel(new VectorIcon(type, 24, color));
        }
        JLabel lbl = new JLabel(iconText, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        lbl.setForeground(color);
        return lbl;
    }

    private static VectorIcon.Type mapIconTextToType(String text) {
        if (text == null) return null;
        switch (text.trim()) {
            case "📚": return VectorIcon.Type.COURSES;
            case "📈": return VectorIcon.Type.CHART;
            case "📑": case "📋": return VectorIcon.Type.ASSIGNMENTS;
            case "🎯": case "🏆": return VectorIcon.Type.EXAMS;
            case "💡": case "❓": case "💬": return VectorIcon.Type.DOUBTS;
            case "📢": return VectorIcon.Type.NOTICES;
            case "👨‍🎓": case "👨‍🏫": case "👤": return VectorIcon.Type.USERS;
            case "⏳": return VectorIcon.Type.REFRESH;
            case "📅": return VectorIcon.Type.ATTENDANCE;
            default: return null;
        }
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

    /**
     * Generates a circular user avatar badge with user initials (e.g., "AS" for Aarav Sharma).
     */
    public static JComponent createAvatarBadge(String fullName, int size) {
        String initials = extractInitials(fullName);
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setupAntiAliasing(g);
                Graphics2D g2 = (Graphics2D) g;

                // Gradient circle background
                GradientPaint grad = new GradientPaint(
                        0, 0, PRIMARY,
                        size, size, PRIMARY_DEEP
                );
                g2.setPaint(grad);
                g2.fill(new Ellipse2D.Float(1, 1, size - 2, size - 2));

                // Saffron accent ring
                g2.setColor(SAFFRON);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new Ellipse2D.Float(1, 1, size - 2, size - 2));

                // Initials text
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(10, size / 2 - 2)));
                FontMetrics fm = g2.getFontMetrics();
                int x = (size - fm.stringWidth(initials)) / 2;
                int y = (size - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(initials, x, y);
            }
        };
        p.setPreferredSize(new Dimension(size, size));
        p.setMaximumSize(new Dimension(size, size));
        p.setOpaque(false);
        return p;
    }

    private static String extractInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "IN";
        String clean = name.replace("Dr.", "").replace("Prof.", "").replace("Mr.", "").replace("Ms.", "").trim();
        String[] parts = clean.split("\\s+");
        if (parts.length >= 2) {
            return ("" + parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
        } else if (parts.length == 1 && parts[0].length() >= 2) {
            return parts[0].substring(0, 2).toUpperCase();
        }
        return "IN";
    }
}

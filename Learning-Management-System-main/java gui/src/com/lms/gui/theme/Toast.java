package com.lms.gui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern floating toast notification system for SmartLearn India.
 * Displays temporary feedback banners (Success, Info, Warning, Error) over application frames.
 */
public class Toast {
    public enum Type {
        SUCCESS, INFO, WARNING, DANGER
    }

    public static void show(Component parent, String message, Type type) {
        SwingUtilities.invokeLater(() -> {
            Window window = SwingUtilities.getWindowAncestor(parent);
            if (window == null && parent instanceof Window) {
                window = (Window) parent;
            }
            if (window == null) return;

            JDialog dialog = new JDialog(window);
            dialog.setUndecorated(true);
            dialog.setFocusableWindowState(false);
            dialog.setBackground(new Color(0, 0, 0, 0));

            Color bg;
            Color border;
            Color text;
            VectorIcon.Type iconType;

            switch (type) {
                case SUCCESS:
                    bg = new Color(236, 253, 245);
                    border = UITheme.SUCCESS;
                    text = UITheme.SUCCESS_DARK;
                    iconType = VectorIcon.Type.CHECK;
                    break;
                case WARNING:
                    bg = new Color(255, 251, 235);
                    border = UITheme.WARNING;
                    text = new Color(180, 83, 9);
                    iconType = VectorIcon.Type.NOTICES;
                    break;
                case DANGER:
                    bg = new Color(254, 242, 242);
                    border = UITheme.DANGER;
                    text = UITheme.DANGER_DARK;
                    iconType = VectorIcon.Type.SHIELD;
                    break;
                case INFO:
                default:
                    bg = new Color(239, 246, 255);
                    border = UITheme.PRIMARY;
                    text = UITheme.PRIMARY_DARK;
                    iconType = VectorIcon.Type.BELL;
                    break;
            }

            JPanel toastPanel = new JPanel(new BorderLayout(10, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    UITheme.setupAntiAliasing(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(bg);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                    g2.setColor(border);
                    g2.setStroke(new BasicStroke(1.6f));
                    g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 12, 12));
                }
            };
            toastPanel.setOpaque(false);
            toastPanel.setBorder(new EmptyBorder(10, 16, 10, 18));

            JLabel iconLbl = new JLabel(new VectorIcon(iconType, 18, border));
            JLabel msgLbl = new JLabel(message);
            msgLbl.setFont(UITheme.FONT_BODY_BOLD);
            msgLbl.setForeground(text);

            toastPanel.add(iconLbl, BorderLayout.WEST);
            toastPanel.add(msgLbl, BorderLayout.CENTER);

            dialog.getContentPane().add(toastPanel);
            dialog.pack();

            // Position at top center of window
            Point loc = window.getLocationOnScreen();
            int x = loc.x + (window.getWidth() - dialog.getWidth()) / 2;
            int y = loc.y + 70;
            dialog.setLocation(x, y);
            dialog.setVisible(true);

            // Auto-dismiss after 2.6 seconds
            Timer timer = new Timer(2600, e -> {
                dialog.setVisible(false);
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
        });
    }
}

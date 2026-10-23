package com.lms.gui.theme;

import com.lms.gui.db.DataStore;
import com.lms.gui.model.Notice;
import com.lms.gui.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.function.Consumer;

/**
 * Modern Application Shell Top Navigation Bar for SmartLearn India.
 * Includes breadcrumbs, live search, notification center with unread badge, and user profile avatar.
 */
public class TopNavBar extends JPanel {
    private final User currentUser;
    private final JLabel breadcrumbLbl;
    private final ModernTextField searchField;
    private final JButton bellBtn;
    private int unreadNoticesCount = 0;

    public TopNavBar(User user, String initialBreadcrumb, Consumer<String> onSearch) {
        this.currentUser = user;
        setLayout(new BorderLayout(16, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                new EmptyBorder(10, 24, 10, 24)
        ));
        setPreferredSize(new Dimension(0, 62));

        // ── Left: Breadcrumb Navigation ────────────────────────────────────────
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        left.setOpaque(false);

        JLabel logoIcon = new JLabel(new ImageIcon(BrandLogo.createLogoImage(32)));
        breadcrumbLbl = new JLabel(initialBreadcrumb);
        breadcrumbLbl.setFont(UITheme.FONT_SUBTITLE);
        breadcrumbLbl.setForeground(UITheme.TEXT_MAIN);

        left.add(logoIcon);
        left.add(breadcrumbLbl);
        add(left, BorderLayout.WEST);

        // ── Right: Search + Notification Bell + Profile Avatar Chip ────────────
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        // Global Search Field
        searchField = new ModernTextField("Search subjects, notices, tasks...", 16);
        searchField.setPreferredSize(new Dimension(240, 38));
        if (onSearch != null) {
            searchField.onTextChanged(onSearch);
        }
        right.add(searchField);

        // Notification Bell Button with Badge
        unreadNoticesCount = DataStore.getInstance().getAllNotices().size();
        bellBtn = createNotificationBellButton();
        right.add(bellBtn);

        // Profile Avatar Chip
        JPanel profileChip = createProfileChip();
        right.add(profileChip);

        add(right, BorderLayout.EAST);
    }

    public void setBreadcrumb(String text) {
        breadcrumbLbl.setText(text);
    }

    private JButton createNotificationBellButton() {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                UITheme.setupAntiAliasing(g);
                Graphics2D g2 = (Graphics2D) g;

                // Bell Icon
                VectorIcon icon = new VectorIcon(VectorIcon.Type.BELL, 20, UITheme.TEXT_MAIN);
                icon.paintIcon(this, g2, 10, 9);

                // Red/Saffron Notification Badge if count > 0
                if (unreadNoticesCount > 0) {
                    g2.setColor(UITheme.SAFFRON);
                    g2.fill(new Ellipse2D.Float(22, 5, 14, 14));
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
                    String cnt = unreadNoticesCount > 9 ? "9+" : String.valueOf(unreadNoticesCount);
                    FontMetrics fm = g2.getFontMetrics();
                    int tx = 29 - (fm.stringWidth(cnt) / 2);
                    int ty = 15;
                    g2.drawString(cnt, tx, ty);
                }
            }
        };
        btn.setPreferredSize(new Dimension(42, 38));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Campus Notices & Announcements");

        btn.addActionListener(e -> showNotificationsPopup(btn));
        return btn;
    }

    private void showNotificationsPopup(Component invoker) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        menu.setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY_LIGHT);
        header.setBorder(new EmptyBorder(8, 12, 8, 12));
        JLabel title = new JLabel("Campus Bulletins (" + unreadNoticesCount + ")");
        title.setFont(UITheme.FONT_BODY_BOLD);
        title.setForeground(UITheme.PRIMARY);
        header.add(title, BorderLayout.WEST);
        menu.add(header);

        List<Notice> notices = DataStore.getInstance().getAllNotices();
        if (notices.isEmpty()) {
            JMenuItem empty = new JMenuItem("No announcements today.");
            empty.setEnabled(false);
            menu.add(empty);
        } else {
            int shown = 0;
            for (Notice n : notices) {
                if (shown++ >= 4) break;
                JMenuItem item = new JMenuItem("<html><b>" + n.getTitle() + "</b><br/><span style='color:#64748b;font-size:10px;'>" + n.getAuthor() + " • " + n.getCategory() + "</span></html>");
                item.setBorder(new EmptyBorder(6, 12, 6, 12));
                item.setFont(UITheme.FONT_SMALL);
                item.addActionListener(e -> {
                    JOptionPane.showMessageDialog(invoker,
                            n.getContent() + "\n\nCategory: " + n.getCategory() + "\nPublished By: " + n.getAuthor(),
                            n.getTitle(), JOptionPane.INFORMATION_MESSAGE);
                });
                menu.add(item);
            }
        }

        menu.show(invoker, -180, invoker.getHeight() + 4);
    }

    private JPanel createProfileChip() {
        JPanel chip = new JPanel(new BorderLayout(8, 0));
        chip.setOpaque(false);
        chip.setBorder(new EmptyBorder(2, 6, 2, 6));

        JComponent avatar = UITheme.createAvatarBadge(currentUser.getName(), 36);
        chip.add(avatar, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel nameLbl = new JLabel(currentUser.getName());
        nameLbl.setFont(UITheme.FONT_BODY_BOLD);
        nameLbl.setForeground(UITheme.TEXT_MAIN);

        Color roleColor;
        switch (currentUser.getRole()) {
            case ADMIN: roleColor = UITheme.PRIMARY_DEEP; break;
            case FACULTY: roleColor = UITheme.SUCCESS_DARK; break;
            default: roleColor = UITheme.PRIMARY; break;
        }

        JLabel roleLbl = new JLabel(currentUser.getRole().name() + " • " + (currentUser.getDepartment() != null ? currentUser.getDepartment() : "Academic"));
        roleLbl.setFont(UITheme.FONT_SMALL);
        roleLbl.setForeground(roleColor);

        textPanel.add(nameLbl);
        textPanel.add(Box.createVerticalStrut(1));
        textPanel.add(roleLbl);

        chip.add(textPanel, BorderLayout.CENTER);
        return chip;
    }
}

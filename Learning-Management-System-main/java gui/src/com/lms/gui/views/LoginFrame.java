package com.lms.gui.views;

import com.lms.gui.db.DataStore;
import com.lms.gui.model.User;
import com.lms.gui.theme.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {
    private final ModernTextField emailField;
    private final ModernPasswordField passwordField;

    public LoginFrame() {
        super("EduPro LMS - University Academic Portal");
        setSize(1020, 660);
        setMinimumSize(new Dimension(920, 580));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Split Main Panel: Left Showcase (45%), Right Login Form (55%)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // ── Left Hero Panel ────────────────────────────────────────────────────
        JPanel leftHero = new JPanel();
        leftHero.setBackground(UITheme.SIDEBAR_BG);
        leftHero.setLayout(new BoxLayout(leftHero, BoxLayout.Y_AXIS));
        leftHero.setBorder(new EmptyBorder(36, 36, 36, 36));

        // University Header
        JLabel uniLbl = new JLabel("🏛️ EDUPRO UNIVERSITY");
        uniLbl.setFont(UITheme.FONT_SMALL_BOLD);
        uniLbl.setForeground(UITheme.ACCENT);

        JLabel logoLbl = new JLabel("Academic LMS Portal");
        logoLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        logoLbl.setForeground(Color.WHITE);

        JLabel tagLbl = new JLabel("Curriculum, Online Examinations & Mentorship");
        tagLbl.setFont(UITheme.FONT_BODY);
        tagLbl.setForeground(UITheme.SIDEBAR_TEXT);

        leftHero.add(uniLbl);
        leftHero.add(Box.createVerticalStrut(4));
        leftHero.add(logoLbl);
        leftHero.add(Box.createVerticalStrut(2));
        leftHero.add(tagLbl);
        leftHero.add(Box.createVerticalStrut(22));

        // Academic Features
        leftHero.add(createFeatureItem("🎓 Role-Based Portals", "Custom tailored views for Students, Faculty & Administrators."));
        leftHero.add(createFeatureItem("📚 Curriculum Hub", "Syllabus outlines, video lectures, downloadable notes & lesson progress."));
        leftHero.add(createFeatureItem("📝 Homework & Rubric Grading", "Assignment submissions with instant feedback and marks."));
        leftHero.add(createFeatureItem("⏱️ Timed MCQ Examinations", "Live countdown timer with question palette and auto-grading."));
        leftHero.add(createFeatureItem("💬 Direct Doubt Resolution", "Direct student inquiry threads with instructor answer tracking."));

        leftHero.add(Box.createVerticalGlue());

        // ── Quick Demo Switcher for Teachers & Evaluators ──────────────────────
        JPanel demoBox = new JPanel();
        demoBox.setLayout(new BoxLayout(demoBox, BoxLayout.Y_AXIS));
        demoBox.setOpaque(false);
        demoBox.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.SIDEBAR_BORDER));

        JLabel quickLbl = new JLabel("⚡ Instant Demo Logins (Click to Auto-Fill):");
        quickLbl.setFont(UITheme.FONT_SMALL_BOLD);
        quickLbl.setForeground(UITheme.WARNING);

        demoBox.add(Box.createVerticalStrut(14));
        demoBox.add(quickLbl);
        demoBox.add(Box.createVerticalStrut(10));

        JPanel quickGrid = new JPanel(new GridLayout(3, 1, 6, 8));
        quickGrid.setOpaque(false);

        ModernButton demoAdminBtn = new ModernButton("👑 Master Admin (admin@lms.com)", ModernButton.Style.PRIMARY);
        demoAdminBtn.setFont(UITheme.FONT_SMALL_BOLD);
        demoAdminBtn.addActionListener(e -> quickLogin("admin@lms.com", "admin123"));

        ModernButton demoFacultyBtn = new ModernButton("👨‍🏫 Faculty: Dr. Smith (faculty@lms.com)", ModernButton.Style.SUCCESS);
        demoFacultyBtn.setFont(UITheme.FONT_SMALL_BOLD);
        demoFacultyBtn.addActionListener(e -> quickLogin("faculty@lms.com", "faculty123"));

        ModernButton demoStudentBtn = new ModernButton("👨‍🎓 Student: Alex Johnson (student@lms.com)", ModernButton.Style.DARK);
        demoStudentBtn.setFont(UITheme.FONT_SMALL_BOLD);
        demoStudentBtn.addActionListener(e -> quickLogin("student@lms.com", "student123"));

        quickGrid.add(demoAdminBtn);
        quickGrid.add(demoFacultyBtn);
        quickGrid.add(demoStudentBtn);
        demoBox.add(quickGrid);

        leftHero.add(demoBox);
        mainPanel.add(leftHero);

        // ── Right Form Panel ───────────────────────────────────────────────────
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(UITheme.APP_BG);

        RoundedPanel formCard = new RoundedPanel(20, Color.WHITE);
        formCard.setPreferredSize(new Dimension(410, 500));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(36, 36, 36, 36));

        JLabel welcomeLbl = new JLabel("Welcome Back");
        welcomeLbl.setFont(UITheme.FONT_HERO);
        welcomeLbl.setForeground(UITheme.TEXT_MAIN);

        JLabel subWelcome = new JLabel("Sign in to your EduPro institutional account");
        subWelcome.setFont(UITheme.FONT_BODY);
        subWelcome.setForeground(UITheme.TEXT_MUTED);

        formCard.add(welcomeLbl);
        formCard.add(Box.createVerticalStrut(4));
        formCard.add(subWelcome);
        formCard.add(Box.createVerticalStrut(28));

        emailField = new ModernTextField("Institutional Email Address", 20);
        emailField.setText("admin@lms.com");
        passwordField = new ModernPasswordField("Account Password", 20);
        passwordField.setText("admin123");

        // Submit on Enter key
        KeyAdapter enterSubmit = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        emailField.addKeyListener(enterSubmit);
        passwordField.addKeyListener(enterSubmit);

        formCard.add(createFieldWrapper("Email Address:", emailField));
        formCard.add(Box.createVerticalStrut(14));
        formCard.add(createFieldWrapper("Password:", passwordField));
        formCard.add(Box.createVerticalStrut(26));

        ModernButton loginBtn = new ModernButton("🚀 Sign In to Portal", ModernButton.Style.PRIMARY);
        loginBtn.setPreferredSize(new Dimension(340, 44));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.addActionListener(e -> handleLogin());
        formCard.add(loginBtn);

        formCard.add(Box.createVerticalStrut(16));

        JPanel registerRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        registerRow.setOpaque(false);
        JLabel noAccLbl = new JLabel("New student or faculty?");
        noAccLbl.setFont(UITheme.FONT_BODY);
        noAccLbl.setForeground(UITheme.TEXT_MUTED);

        JButton createAccBtn = new JButton("<html><u>Register an Account</u></html>");
        createAccBtn.setFont(UITheme.FONT_BODY_BOLD);
        createAccBtn.setForeground(UITheme.PRIMARY);
        createAccBtn.setBorderPainted(false);
        createAccBtn.setContentAreaFilled(false);
        createAccBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccBtn.addActionListener(e -> {
            RegisterDialog dlg = new RegisterDialog(this);
            dlg.setVisible(true);
        });

        registerRow.add(noAccLbl);
        registerRow.add(createAccBtn);
        formCard.add(registerRow);

        formCard.add(Box.createVerticalGlue());

        JLabel statusFooter = new JLabel("🟢 System Online | Term: Fall 2026", SwingConstants.CENTER);
        statusFooter.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusFooter.setFont(UITheme.FONT_SMALL);
        statusFooter.setForeground(UITheme.TEXT_LIGHT);
        formCard.add(statusFooter);

        rightPanel.add(formCard);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createFeatureItem(String title, String desc) {
        JPanel p = new JPanel(new BorderLayout(8, 2));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(4, 0, 8, 0));

        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_BODY_BOLD);
        t.setForeground(Color.WHITE);

        JLabel d = new JLabel(desc);
        d.setFont(UITheme.FONT_SMALL);
        d.setForeground(UITheme.SIDEBAR_TEXT);

        p.add(t, BorderLayout.NORTH);
        p.add(d, BorderLayout.CENTER);
        return p;
    }

    private JPanel createFieldWrapper(String labelText, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(5, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(UITheme.FONT_BODY_BOLD);
        lbl.setForeground(UITheme.TEXT_MAIN);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your email and password.", "Missing Credentials", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User u = DataStore.getInstance().authenticate(email, pass);
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please verify your email and password.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!u.isActive()) {
            JOptionPane.showMessageDialog(this, "This account is currently marked Inactive. Please contact the Master Administrator.", "Account Inactive", JOptionPane.WARNING_MESSAGE);
            return;
        }

        launchPortalForUser(u);
    }

    private void quickLogin(String email, String password) {
        emailField.setText(email);
        passwordField.setText(password);
        handleLogin();
    }

    private void launchPortalForUser(User u) {
        dispose();
        switch (u.getRole()) {
            case ADMIN:
                new AdminDashboardFrame(u).setVisible(true);
                break;
            case FACULTY:
                new FacultyDashboardFrame(u).setVisible(true);
                break;
            case STUDENT:
                new StudentDashboardFrame(u).setVisible(true);
                break;
        }
    }
}

package com.lms.gui.views;

import com.lms.gui.db.DataStore;
import com.lms.gui.model.User;
import com.lms.gui.theme.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final ModernTextField emailField;
    private final ModernPasswordField passwordField;

    public LoginFrame() {
        super("EduPro LMS - Desktop Portal Login");
        setSize(980, 620);
        setMinimumSize(new Dimension(880, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Split Main Panel: Left Hero (45%), Right Form (55%)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // ── Left Hero Panel ────────────────────────────────────────────────────
        JPanel leftHero = new JPanel();
        leftHero.setBackground(UITheme.SIDEBAR_BG);
        leftHero.setLayout(new BoxLayout(leftHero, BoxLayout.Y_AXIS));
        leftHero.setBorder(new EmptyBorder(40, 36, 40, 36));

        JLabel logoLbl = new JLabel("🎓 EduPro LMS");
        logoLbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoLbl.setForeground(Color.WHITE);

        JLabel tagLbl = new JLabel("Enterprise Learning Management System");
        tagLbl.setFont(UITheme.FONT_SUBTITLE);
        tagLbl.setForeground(UITheme.ACCENT);

        leftHero.add(logoLbl);
        leftHero.add(Box.createVerticalStrut(6));
        leftHero.add(tagLbl);
        leftHero.add(Box.createVerticalStrut(24));

        leftHero.add(createFeatureItem("👥 Role-Based Portals", "Admin, Faculty, and Student workflows"));
        leftHero.add(createFeatureItem("📚 Curriculum Hub", "Syllabus, video lectures, and study notes"));
        leftHero.add(createFeatureItem("📝 Homework Grading", "Submission tracking with feedback & scores"));
        leftHero.add(createFeatureItem("🎯 Timed MCQ Exams", "Real-time countdown with instant auto-grading"));
        leftHero.add(createFeatureItem("💬 Academic Doubts", "Direct student-to-instructor Q&A resolutions"));

        leftHero.add(Box.createVerticalGlue());

        // Quick Presentation Switcher for Teachers/Vivas
        JLabel quickLbl = new JLabel("⚡ 1-Click Presentation Demo Logins:");
        quickLbl.setFont(UITheme.FONT_SMALL_BOLD);
        quickLbl.setForeground(UITheme.WARNING);
        leftHero.add(quickLbl);
        leftHero.add(Box.createVerticalStrut(10));

        JPanel quickGrid = new JPanel(new GridLayout(3, 1, 6, 8));
        quickGrid.setOpaque(false);

        ModernButton demoAdminBtn = new ModernButton("👑 Login as Master Admin", ModernButton.Style.PRIMARY);
        demoAdminBtn.setFont(UITheme.FONT_SMALL_BOLD);
        demoAdminBtn.addActionListener(e -> quickLogin("admin@lms.com", "admin123"));

        ModernButton demoFacultyBtn = new ModernButton("👨‍🏫 Login as Faculty (Dr. Smith)", ModernButton.Style.SUCCESS);
        demoFacultyBtn.setFont(UITheme.FONT_SMALL_BOLD);
        demoFacultyBtn.addActionListener(e -> quickLogin("faculty@lms.com", "faculty123"));

        ModernButton demoStudentBtn = new ModernButton("👨‍🎓 Login as Student (Alex Johnson)", ModernButton.Style.DARK);
        demoStudentBtn.setFont(UITheme.FONT_SMALL_BOLD);
        demoStudentBtn.addActionListener(e -> quickLogin("student@lms.com", "student123"));

        quickGrid.add(demoAdminBtn);
        quickGrid.add(demoFacultyBtn);
        quickGrid.add(demoStudentBtn);
        leftHero.add(quickGrid);

        mainPanel.add(leftHero);

        // ── Right Form Panel ───────────────────────────────────────────────────
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(UITheme.APP_BG);

        RoundedPanel formCard = new RoundedPanel(20, Color.WHITE);
        formCard.setPreferredSize(new Dimension(390, 480));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(36, 32, 36, 32));

        JLabel welcomeLbl = new JLabel("Welcome Back");
        welcomeLbl.setFont(UITheme.FONT_HERO);
        welcomeLbl.setForeground(UITheme.TEXT_MAIN);

        JLabel subWelcome = new JLabel("Sign in to your EduPro account");
        subWelcome.setFont(UITheme.FONT_BODY);
        subWelcome.setForeground(UITheme.TEXT_MUTED);

        formCard.add(welcomeLbl);
        formCard.add(Box.createVerticalStrut(4));
        formCard.add(subWelcome);
        formCard.add(Box.createVerticalStrut(28));

        emailField = new ModernTextField("Email Address", 20);
        emailField.setText("admin@lms.com");
        passwordField = new ModernPasswordField("Password", 20);
        passwordField.setText("admin123");

        formCard.add(createFieldWrapper("Email Address:", emailField));
        formCard.add(Box.createVerticalStrut(14));
        formCard.add(createFieldWrapper("Password:", passwordField));
        formCard.add(Box.createVerticalStrut(24));

        ModernButton loginBtn = new ModernButton("🚀 Sign In to Portal", ModernButton.Style.PRIMARY);
        loginBtn.setPreferredSize(new Dimension(320, 44));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.addActionListener(e -> handleLogin());
        formCard.add(loginBtn);

        formCard.add(Box.createVerticalStrut(16));

        JPanel registerRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        registerRow.setOpaque(false);
        JLabel noAccLbl = new JLabel("Don't have an account?");
        noAccLbl.setFont(UITheme.FONT_BODY);
        noAccLbl.setForeground(UITheme.TEXT_MUTED);

        JButton createAccBtn = new JButton("<html><u>Create One</u></html>");
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
            JOptionPane.showMessageDialog(this, "Invalid email or password. Please verify credentials.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!u.isActive()) {
            JOptionPane.showMessageDialog(this, "This account is currently marked Inactive. Please contact the Master Admin.", "Account Inactive", JOptionPane.WARNING_MESSAGE);
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

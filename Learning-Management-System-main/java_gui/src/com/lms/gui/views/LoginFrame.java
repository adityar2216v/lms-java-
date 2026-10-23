package com.lms.gui.views;

import com.lms.gui.db.DataStore;
import com.lms.gui.model.User;
import com.lms.gui.theme.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Modern, branded login gateway for SmartLearn India.
 * Includes password visibility controls, 1-click viva evaluator demo buttons, and validation.
 */
public class LoginFrame extends JFrame {
    private final ModernTextField emailField;
    private final ModernPasswordField passwordField;

    public LoginFrame() {
        super("SmartLearn India — Smart Learning Management System");
        setIconImage(BrandLogo.createLogoImage(64));
        setSize(1040, 680);
        setMinimumSize(new Dimension(940, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Split Main Panel: Left Showcase (46%), Right Login Form (54%)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // ── Left Hero Panel ────────────────────────────────────────────────────
        JPanel leftHero = new JPanel();
        leftHero.setBackground(UITheme.SIDEBAR_BG);
        leftHero.setLayout(new BoxLayout(leftHero, BoxLayout.Y_AXIS));
        leftHero.setBorder(new EmptyBorder(32, 34, 32, 34));

        // Official SmartLearn India Brand Header
        JPanel brandHeader = BrandLogo.createBrandHeader(true);
        brandHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftHero.add(brandHeader);
        leftHero.add(Box.createVerticalStrut(20));

        // Platform Highlights
        leftHero.add(createFeatureItem("National Curriculum & Video Hub", "Syllabus outlines, structured lecture modules, and lesson progress.", VectorIcon.Type.COURSES));
        leftHero.add(createFeatureItem("Assignments & Rubric Grading", "Continuous lab evaluations with numerical scores and faculty remarks.", VectorIcon.Type.ASSIGNMENTS));
        leftHero.add(createFeatureItem("Timed MCQ Quizzes & Assessments", "Live countdown timer, question palette, and instant score reports.", VectorIcon.Type.EXAMS));
        leftHero.add(createFeatureItem("Direct Doubt Resolution Forum", "Two-way mentorship threads between enrolled students and course faculty.", VectorIcon.Type.DOUBTS));
        leftHero.add(createFeatureItem("AICTE Attendance & Verified Certificates", "Real-time 75% attendance criteria checks and authenticated certificates.", VectorIcon.Type.CERTIFICATE));

        leftHero.add(Box.createVerticalGlue());

        // ── 1-Click Evaluator & Teacher Demo Panel ──────────────────────────────
        JPanel demoBox = new JPanel();
        demoBox.setLayout(new BoxLayout(demoBox, BoxLayout.Y_AXIS));
        demoBox.setOpaque(false);
        demoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        demoBox.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.SIDEBAR_BORDER));

        JLabel quickLbl = new JLabel("⚡ Instant Demo Logins (Click to Auto-Fill):");
        quickLbl.setFont(UITheme.FONT_SMALL_BOLD);
        quickLbl.setForeground(UITheme.SAFFRON);

        demoBox.add(Box.createVerticalStrut(12));
        demoBox.add(quickLbl);
        demoBox.add(Box.createVerticalStrut(8));

        JPanel quickGrid = new JPanel(new GridLayout(3, 1, 6, 8));
        quickGrid.setOpaque(false);

        ModernButton demoAdminBtn = new ModernButton(
                "Master Admin (admin@smartlearn.in)",
                new VectorIcon(VectorIcon.Type.SHIELD, 14, Color.WHITE),
                ModernButton.Style.PRIMARY
        );
        demoAdminBtn.setFont(UITheme.FONT_SMALL_BOLD);
        demoAdminBtn.addActionListener(e -> quickLogin("admin@smartlearn.in", "admin123"));

        ModernButton demoFacultyBtn = new ModernButton(
                "Faculty: Dr. Neha Kapoor (neha.kapoor@smartlearn.in)",
                new VectorIcon(VectorIcon.Type.USERS, 14, Color.WHITE),
                ModernButton.Style.SUCCESS
        );
        demoFacultyBtn.setFont(UITheme.FONT_SMALL_BOLD);
        demoFacultyBtn.addActionListener(e -> quickLogin("neha.kapoor@smartlearn.in", "faculty123"));

        ModernButton demoStudentBtn = new ModernButton(
                "Student: Aarav Sharma (aarav.sharma@smartlearn.in)",
                new VectorIcon(VectorIcon.Type.COURSES, 14, UITheme.TEXT_MAIN),
                ModernButton.Style.SECONDARY
        );
        demoStudentBtn.setFont(UITheme.FONT_SMALL_BOLD);
        demoStudentBtn.addActionListener(e -> quickLogin("aarav.sharma@smartlearn.in", "student123"));

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
        formCard.setPreferredSize(new Dimension(420, 520));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(34, 36, 32, 36));

        JLabel welcomeLbl = new JLabel("Welcome Back");
        welcomeLbl.setFont(UITheme.FONT_HERO);
        welcomeLbl.setForeground(UITheme.TEXT_MAIN);

        JLabel subWelcome = new JLabel("Sign in to your SmartLearn India institutional account");
        subWelcome.setFont(UITheme.FONT_BODY);
        subWelcome.setForeground(UITheme.TEXT_MUTED);

        formCard.add(welcomeLbl);
        formCard.add(Box.createVerticalStrut(4));
        formCard.add(subWelcome);
        formCard.add(Box.createVerticalStrut(24));

        emailField = new ModernTextField("Institutional Email Address", 20);
        emailField.setText("aarav.sharma@smartlearn.in");

        passwordField = new ModernPasswordField("Account Password", 20);
        passwordField.setText("student123");

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
        formCard.add(createFieldWrapper("Password (Click eye to view):", passwordField));
        formCard.add(Box.createVerticalStrut(24));

        ModernButton loginBtn = new ModernButton(
                "Sign In to Portal",
                new VectorIcon(VectorIcon.Type.LOGOUT, 16, Color.WHITE),
                ModernButton.Style.PRIMARY
        );
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

        JLabel statusFooter = new JLabel("SmartLearn India | Academic Year 2026-27 | Odd Semester", SwingConstants.CENTER);
        statusFooter.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusFooter.setFont(UITheme.FONT_SMALL);
        statusFooter.setForeground(UITheme.TEXT_LIGHT);
        formCard.add(statusFooter);

        rightPanel.add(formCard);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createFeatureItem(String title, String desc, VectorIcon.Type iconType) {
        JPanel p = new JPanel(new BorderLayout(10, 2));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setBorder(new EmptyBorder(3, 0, 7, 0));

        JLabel iconLbl = new JLabel(new VectorIcon(iconType, 16, UITheme.SAFFRON));

        JPanel textGrp = new JPanel(new BorderLayout(2, 2));
        textGrp.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_BODY_BOLD);
        t.setForeground(Color.WHITE);

        JLabel d = new JLabel(desc);
        d.setFont(UITheme.FONT_SMALL);
        d.setForeground(UITheme.SIDEBAR_TEXT);

        textGrp.add(t, BorderLayout.NORTH);
        textGrp.add(d, BorderLayout.CENTER);

        p.add(iconLbl, BorderLayout.WEST);
        p.add(textGrp, BorderLayout.CENTER);
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
            Toast.show(this, "Please enter your email and password.", Toast.Type.WARNING);
            return;
        }

        User u = DataStore.getInstance().authenticate(email, pass);
        if (u == null) {
            Toast.show(this, "Invalid credentials. Verify your email and password.", Toast.Type.DANGER);
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

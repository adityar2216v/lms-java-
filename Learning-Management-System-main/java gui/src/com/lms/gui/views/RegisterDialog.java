package com.lms.gui.views;

import com.lms.gui.db.DataStore;
import com.lms.gui.model.User;
import com.lms.gui.theme.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterDialog extends JDialog {
    private final ModernTextField nameField;
    private final ModernTextField emailField;
    private final ModernTextField mobileField;
    private final ModernPasswordField passwordField;
    private final JComboBox<String> roleCombo;
    private final JComboBox<String> deptCombo;
    private boolean registered = false;

    public RegisterDialog(Frame parent) {
        super(parent, "Create New LMS Account", true);
        setSize(480, 560);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(UITheme.APP_BG);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UITheme.SIDEBAR_BG);
        headerPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel("Join SmartLearn India");
        titleLbl.setFont(UITheme.FONT_TITLE);
        titleLbl.setForeground(Color.WHITE);

        JLabel subLbl = new JLabel("Create an institutional Student or Faculty Account");
        subLbl.setFont(UITheme.FONT_BODY);
        subLbl.setForeground(UITheme.SAFFRON);

        headerPanel.add(titleLbl);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(subLbl);
        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 28, 20, 28));
        formPanel.setLayout(new GridLayout(6, 1, 10, 10));

        nameField = new ModernTextField("Full Name (e.g. Aarav Sharma)", 20);
        emailField = new ModernTextField("Institutional Email (e.g. aarav@smartlearn.in)", 20);
        mobileField = new ModernTextField("Mobile Number (e.g. 9876543210)", 20);
        passwordField = new ModernPasswordField("Account Password (min 4 characters)", 20);

        roleCombo = new JComboBox<>(new String[]{"STUDENT", "FACULTY"});
        roleCombo.setFont(UITheme.FONT_BODY);
        roleCombo.setBackground(Color.WHITE);

        deptCombo = new JComboBox<>(new String[]{
            "Computer Science & Engineering",
            "Information Technology",
            "Artificial Intelligence & Data Science",
            "Software Engineering",
            "Electronics & Communication Engineering"
        });
        deptCombo.setFont(UITheme.FONT_BODY);
        deptCombo.setBackground(Color.WHITE);

        formPanel.add(createFieldWrapper("Full Name:", nameField));
        formPanel.add(createFieldWrapper("Email Address:", emailField));
        formPanel.add(createFieldWrapper("Mobile Number:", mobileField));
        formPanel.add(createFieldWrapper("Account Password:", passwordField));
        formPanel.add(createFieldWrapper("Account Role:", roleCombo));
        formPanel.add(createFieldWrapper("Department:", deptCombo));

        add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 16));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));

        ModernButton cancelBtn = new ModernButton("Cancel", ModernButton.Style.SECONDARY);
        cancelBtn.addActionListener(e -> dispose());

        ModernButton registerBtn = new ModernButton("Create Account", ModernButton.Style.PRIMARY);
        registerBtn.addActionListener(e -> handleRegister());

        bottomPanel.add(cancelBtn);
        bottomPanel.add(registerBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createFieldWrapper(String labelText, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(5, 4));
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(UITheme.FONT_BODY_BOLD);
        lbl.setForeground(UITheme.TEXT_MAIN);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String mobile = mobileField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        String roleStr = (String) roleCombo.getSelectedItem();
        String dept = (String) deptCombo.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Name, Email, and Password.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User.Role role = User.Role.fromString(roleStr);
            User u = DataStore.getInstance().registerUser(name, email, mobile, pass, role, dept);
            registered = true;
            JOptionPane.showMessageDialog(this, "🎉 Account successfully created for " + u.getName() + "!\nYou can now log in.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isRegistered() {
        return registered;
    }
}

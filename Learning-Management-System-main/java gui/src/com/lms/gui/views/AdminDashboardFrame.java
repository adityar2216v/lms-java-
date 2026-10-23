package com.lms.gui.views;

import com.lms.gui.db.DataStore;
import com.lms.gui.model.*;
import com.lms.gui.theme.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Modern, India-focused Master Administrator Portal for SmartLearn India.
 * Handles user directory governance, course approvals, campus notices broadcast, and system supervision.
 */
public class AdminDashboardFrame extends JFrame {
    private final User adminUser;
    private final CardLayout cardLayout;
    private final JPanel contentCards;
    private final TopNavBar topNavBar;

    private DefaultTableModel usersTableModel;
    private DefaultTableModel coursesTableModel;
    private DefaultTableModel noticesTableModel;
    private DefaultTableModel doubtsTableModel;

    private ModernTable usersTable;
    private ModernTable coursesTable;
    private ModernTable noticesTable;
    private ModernTable doubtsTable;

    private JPanel statsPanel;

    public AdminDashboardFrame(User adminUser) {
        super("SmartLearn India — Master Administrator Portal (" + adminUser.getName() + ")");
        this.adminUser = adminUser;
        setIconImage(BrandLogo.createLogoImage(64));

        setSize(1240, 820);
        setMinimumSize(new Dimension(1040, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.APP_BG);
        setLayout(new BorderLayout());

        // ── Top Navigation Bar ─────────────────────────────────────────────────
        topNavBar = new TopNavBar(adminUser, "SmartLearn India / Admin Portal / System Governance", query -> {
            if (usersTable != null) usersTable.filter(query);
            if (coursesTable != null) coursesTable.filter(query);
            if (noticesTable != null) noticesTable.filter(query);
            if (doubtsTable != null) doubtsTable.filter(query);
        });
        add(topNavBar, BorderLayout.NORTH);

        // ── Sidebar ────────────────────────────────────────────────────────────
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(UITheme.SIDEBAR_BG);

        // Sidebar Header
        JPanel sideTop = new JPanel();
        sideTop.setLayout(new BoxLayout(sideTop, BoxLayout.Y_AXIS));
        sideTop.setOpaque(false);
        sideTop.setBorder(new EmptyBorder(20, 18, 16, 18));

        JPanel brandHeader = BrandLogo.createBrandHeader(true);
        brandHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleBadge = new JLabel("  MASTER ADMINISTRATOR  ");
        roleBadge.setFont(UITheme.FONT_SMALL_BOLD);
        roleBadge.setOpaque(true);
        roleBadge.setBackground(UITheme.PRIMARY);
        roleBadge.setForeground(Color.WHITE);
        roleBadge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        roleBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel adminNameLbl = new JLabel(adminUser.getName());
        adminNameLbl.setFont(UITheme.FONT_BODY_BOLD);
        adminNameLbl.setForeground(Color.WHITE);
        adminNameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel adminEmailLbl = new JLabel(adminUser.getEmail());
        adminEmailLbl.setFont(UITheme.FONT_SMALL);
        adminEmailLbl.setForeground(UITheme.SIDEBAR_TEXT);
        adminEmailLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        sideTop.add(brandHeader);
        sideTop.add(Box.createVerticalStrut(14));
        sideTop.add(roleBadge);
        sideTop.add(Box.createVerticalStrut(12));
        sideTop.add(adminNameLbl);
        sideTop.add(Box.createVerticalStrut(2));
        sideTop.add(adminEmailLbl);
        sidebar.add(sideTop, BorderLayout.NORTH);

        // Sidebar Navigation
        JPanel navPanel = new JPanel(new GridLayout(5, 1, 0, 6));
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(6, 12, 6, 12));

        navPanel.add(createNavButton("System Overview", VectorIcon.Type.DASHBOARD, "OVERVIEW"));
        navPanel.add(createNavButton("User Directory", VectorIcon.Type.USERS, "USERS"));
        navPanel.add(createNavButton("Academic Curriculum", VectorIcon.Type.COURSES, "COURSES"));
        navPanel.add(createNavButton("Campus Bulletins", VectorIcon.Type.NOTICES, "NOTICES"));
        navPanel.add(createNavButton("Doubts Supervision", VectorIcon.Type.DOUBTS, "DOUBTS"));

        sidebar.add(navPanel, BorderLayout.CENTER);

        // Sidebar Footer
        JPanel sideBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 14));
        sideBottom.setOpaque(false);
        ModernButton logoutBtn = new ModernButton(
                "Sign Out",
                new VectorIcon(VectorIcon.Type.LOGOUT, 16, Color.WHITE),
                ModernButton.Style.DANGER
        );
        logoutBtn.setPreferredSize(new Dimension(210, 40));
        logoutBtn.addActionListener(e -> logout());
        sideBottom.add(logoutBtn);
        sidebar.add(sideBottom, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);

        // ── Main Content Area ──────────────────────────────────────────────────
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setOpaque(false);

        contentCards.add(buildOverviewCard(), "OVERVIEW");
        contentCards.add(buildUsersCard(), "USERS");
        contentCards.add(buildCoursesCard(), "COURSES");
        contentCards.add(buildNoticesCard(), "NOTICES");
        contentCards.add(buildDoubtsCard(), "DOUBTS");

        add(contentCards, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text, VectorIcon.Type iconType, String cardName) {
        JButton btn = new JButton(text, new VectorIcon(iconType, 16, UITheme.SIDEBAR_TEXT));
        btn.setFont(UITheme.FONT_BODY_BOLD);
        btn.setForeground(UITheme.SIDEBAR_TEXT);
        btn.setBackground(UITheme.SIDEBAR_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(12);
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(UITheme.SIDEBAR_HOVER);
                btn.setForeground(Color.WHITE);
                btn.setIcon(new VectorIcon(iconType, 16, Color.WHITE));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(UITheme.SIDEBAR_BG);
                btn.setForeground(UITheme.SIDEBAR_TEXT);
                btn.setIcon(new VectorIcon(iconType, 16, UITheme.SIDEBAR_TEXT));
            }
        });

        btn.addActionListener(e -> {
            cardLayout.show(contentCards, cardName);
            topNavBar.setBreadcrumb("SmartLearn India / Admin Portal / " + text);
            refreshAllData();
        });
        return btn;
    }

    // ── 1. Overview ────────────────────────────────────────────────────────────
    private JPanel buildOverviewCard() {
        JPanel card = new JPanel(new BorderLayout(14, 14));
        card.setOpaque(false);

        JPanel headerPanel = new JPanel(new BorderLayout(5, 4));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(16, 22, 10, 22));

        JLabel titleLbl = new JLabel("Institutional Governance & System Analytics");
        titleLbl.setFont(UITheme.FONT_HERO);
        titleLbl.setForeground(UITheme.TEXT_MAIN);

        JLabel subLbl = new JLabel("High-level oversight of institutional student enrollment, faculty allocations, and platform metrics");
        subLbl.setFont(UITheme.FONT_BODY);
        subLbl.setForeground(UITheme.TEXT_MUTED);

        headerPanel.add(titleLbl, BorderLayout.NORTH);
        headerPanel.add(subLbl, BorderLayout.SOUTH);
        card.add(headerPanel, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        statsPanel = new JPanel(new GridLayout(2, 3, 14, 14));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
        refreshStats();
        center.add(statsPanel);
        center.add(Box.createVerticalStrut(18));

        // Quick System Status & Distribution Card
        RoundedPanel distCard = new RoundedPanel(16, Color.WHITE);
        distCard.setLayout(new BorderLayout(8, 8));
        distCard.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel dTop = new JPanel(new BorderLayout());
        dTop.setOpaque(false);
        JLabel dLbl = new JLabel("Departmental Distribution & Academic Infrastructure");
        dLbl.setFont(UITheme.FONT_SUBTITLE);
        JLabel dHealth = new JLabel("🟢 System Engine: Local Synchronized & Serialized | State: Healthy");
        dHealth.setFont(UITheme.FONT_SMALL_BOLD);
        dHealth.setForeground(UITheme.SUCCESS_DARK);
        dTop.add(dLbl, BorderLayout.WEST);
        dTop.add(dHealth, BorderLayout.EAST);
        distCard.add(dTop, BorderLayout.NORTH);

        String[] cols = {"Department / Domain", "Students Enrolled", "Faculty In-Charge", "Active Subjects", "Accreditation Status"};
        DefaultTableModel distModel = new DefaultTableModel(cols, 0);
        distModel.addRow(new Object[]{"Computer Science & Engineering", "124 Students", "Dr. Neha Kapoor", "4 Courses", "✅ AICTE Accredited"});
        distModel.addRow(new Object[]{"Information Technology", "88 Students", "Prof. Vikram Deshmukh", "3 Courses", "✅ NBA Tier-1"});
        distModel.addRow(new Object[]{"Artificial Intelligence & Data Science", "76 Students", "Dr. Meera Iyer", "3 Courses", "✅ AICTE Approved"});
        distModel.addRow(new Object[]{"Electronics & Communication", "64 Students", "Prof. Rajesh Verma", "2 Courses", "✅ University Affiliated"});

        ModernTable distTable = new ModernTable(distModel);
        distCard.add(ModernTable.wrapInScrollPane(distTable), BorderLayout.CENTER);
        center.add(distCard);

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    private void refreshStats() {
        statsPanel.removeAll();
        DataStore ds = DataStore.getInstance();

        int studentCount = ds.getUsersByRole(User.Role.STUDENT).size();
        int facultyCount = ds.getUsersByRole(User.Role.FACULTY).size();
        int courseCount = ds.getAllCourses().size();
        int enrollmentCount = ds.getAllEnrollments().size();
        int pendingDoubts = (int) ds.getAllDoubts().stream().filter(d -> d.getStatus() == Doubt.Status.PENDING).count();
        int noticeCount = ds.getAllNotices().size();

        statsPanel.add(UITheme.createStatCard("Registered Students", String.valueOf(studentCount), "👨‍🎓", UITheme.PRIMARY, "+100% active"));
        statsPanel.add(UITheme.createStatCard("Faculty Members", String.valueOf(facultyCount), "👨‍🏫", UITheme.ACCENT, "Full-time faculty"));
        statsPanel.add(UITheme.createStatCard("Active Courses", String.valueOf(courseCount), "📚", UITheme.SUCCESS, "Odd Semester 2026"));
        statsPanel.add(UITheme.createStatCard("Total Enrollments", String.valueOf(enrollmentCount), "📈", UITheme.PURPLE, "Across engineering"));
        statsPanel.add(UITheme.createStatCard("Unresolved Doubts", String.valueOf(pendingDoubts), "❓", UITheme.WARNING, "Awaiting faculty reply"));
        statsPanel.add(UITheme.createStatCard("Official Bulletins", String.valueOf(noticeCount), "📢", UITheme.DANGER, "Broadcast alerts"));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    // ── 2. Users Management ────────────────────────────────────────────────────
    private JPanel buildUsersCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Institutional User Directory", "Search, approve, toggle status, and govern accounts for students and faculty"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        // Tool bar
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);

        ModernButton addBtn = new ModernButton("➕ Add User", ModernButton.Style.PRIMARY);
        addBtn.addActionListener(e -> showAddUserDialog());

        ModernButton toggleStatusBtn = new ModernButton("⚡ Toggle Status", ModernButton.Style.WARNING);
        ModernButton viewProfileBtn = new ModernButton("👤 View Profile", ModernButton.Style.SECONDARY);
        ModernButton deleteBtn = new ModernButton("🗑 Delete", ModernButton.Style.DANGER);

        toolBar.add(addBtn);
        toolBar.add(toggleStatusBtn);
        toolBar.add(viewProfileBtn);
        toolBar.add(deleteBtn);

        center.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"User ID", "Full Name", "Email Address", "Mobile", "Role", "Department / Batch", "Account Status"};
        usersTableModel = new DefaultTableModel(cols, 0);
        usersTable = new ModernTable(usersTableModel);
        center.add(ModernTable.wrapInScrollPane(usersTable), BorderLayout.CENTER);

        toggleStatusBtn.addActionListener(e -> {
            int row = usersTable.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select a user from the table first.", Toast.Type.WARNING);
                return;
            }
            int modelRow = usersTable.convertRowIndexToModel(row);
            int userId = (int) usersTableModel.getValueAt(modelRow, 0);
            User u = DataStore.getInstance().getUserById(userId);
            if (u != null) {
                u.setStatus(u.getStatus() == 1 ? 0 : 1);
                DataStore.getInstance().saveUser(u);
                loadUsers();
                refreshStats();
                Toast.show(this, "User '" + u.getName() + "' status changed to: " + (u.getStatus() == 1 ? "Active" : "Inactive"), Toast.Type.SUCCESS);
            }
        });

        viewProfileBtn.addActionListener(e -> {
            int row = usersTable.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select a user to view profile.", Toast.Type.WARNING);
                return;
            }
            int modelRow = usersTable.convertRowIndexToModel(row);
            int userId = (int) usersTableModel.getValueAt(modelRow, 0);
            User u = DataStore.getInstance().getUserById(userId);
            if (u != null) {
                showUserProfileDialog(u);
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = usersTable.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select a user to delete.", Toast.Type.WARNING);
                return;
            }
            int modelRow = usersTable.convertRowIndexToModel(row);
            int userId = (int) usersTableModel.getValueAt(modelRow, 0);
            if (userId == adminUser.getId()) {
                JOptionPane.showMessageDialog(this, "You cannot delete your own Master Administrator account!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to permanently delete this user account?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DataStore.getInstance().deleteUser(userId);
                loadUsers();
                refreshStats();
                Toast.show(this, "User account successfully removed.", Toast.Type.SUCCESS);
            }
        });

        loadUsers();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadUsers() {
        usersTableModel.setRowCount(0);
        for (User u : DataStore.getInstance().getAllUsers()) {
            usersTableModel.addRow(new Object[]{
                    u.getId(),
                    u.getName(),
                    u.getEmail(),
                    u.getMobile(),
                    u.getRole().name(),
                    u.getDepartment() != null ? u.getDepartment() : "General",
                    u.isActive() ? "🟢 Active" : "🔴 Inactive"
            });
        }
    }

    private void showAddUserDialog() {
        RegisterDialog dlg = new RegisterDialog(this);
        dlg.setVisible(true);
        if (dlg.isRegistered()) {
            loadUsers();
            refreshStats();
        }
    }

    private void showUserProfileDialog(User u) {
        JDialog dlg = new JDialog(this, "User Profile: " + u.getName(), true);
        dlg.setSize(440, 360);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(6, 1, 8, 8));
        p.setBorder(new EmptyBorder(20, 24, 20, 24));
        p.add(new JLabel("User ID: " + u.getId()));
        p.add(new JLabel("Full Name: " + u.getName()));
        p.add(new JLabel("Email Address: " + u.getEmail()));
        p.add(new JLabel("Mobile Contact: " + u.getMobile()));
        p.add(new JLabel("Role: " + u.getRole()));
        p.add(new JLabel("Department: " + (u.getDepartment() != null ? u.getDepartment() : "General")));

        dlg.add(p, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        ModernButton closeBtn = new ModernButton("Close", ModernButton.Style.SECONDARY);
        closeBtn.addActionListener(e -> dlg.dispose());
        btm.add(closeBtn);
        dlg.add(btm, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    // ── 3. Academic Curriculum Management ──────────────────────────────────────
    private JPanel buildCoursesCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("National Academic Curriculum & Course Governance", "Manage university subjects, allocate faculty in-charge, and inspect enrollments"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);
        ModernButton addCourseBtn = new ModernButton("➕ Add Course", ModernButton.Style.PRIMARY);
        addCourseBtn.addActionListener(e -> showAddCourseDialog());

        ModernButton deleteCourseBtn = new ModernButton("🗑 Delete Course", ModernButton.Style.DANGER);
        deleteCourseBtn.addActionListener(e -> {
            int row = coursesTable.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select a course to delete.", Toast.Type.WARNING);
                return;
            }
            int modelRow = coursesTable.convertRowIndexToModel(row);
            int courseId = (int) coursesTableModel.getValueAt(modelRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this course from the university curriculum?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DataStore.getInstance().deleteCourse(courseId);
                loadCourses();
                refreshStats();
                Toast.show(this, "Course removed from curriculum.", Toast.Type.SUCCESS);
            }
        });

        toolBar.add(addCourseBtn);
        toolBar.add(deleteCourseBtn);
        center.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"Course ID", "Course Title", "Category", "Faculty Instructor", "Enrolled Students", "Curriculum Summary"};
        coursesTableModel = new DefaultTableModel(cols, 0);
        coursesTable = new ModernTable(coursesTableModel);
        center.add(ModernTable.wrapInScrollPane(coursesTable), BorderLayout.CENTER);

        loadCourses();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadCourses() {
        coursesTableModel.setRowCount(0);
        for (Course c : DataStore.getInstance().getAllCourses()) {
            coursesTableModel.addRow(new Object[]{
                    c.getId(), c.getTitle(), c.getCategory(), c.getInstructorName(),
                    c.getEnrolledStudentsCount() + " Students", c.getDescription()
            });
        }
    }

    private void showAddCourseDialog() {
        List<User> facultyList = DataStore.getInstance().getUsersByRole(User.Role.FACULTY);
        if (facultyList.isEmpty()) {
            Toast.show(this, "Please add at least one faculty member first.", Toast.Type.WARNING);
            return;
        }

        JDialog dlg = new JDialog(this, "Add New Academic Course", true);
        dlg.setSize(520, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.setBorder(new EmptyBorder(18, 22, 14, 22));

        ModernTextField titleField = new ModernTextField("Course Title (e.g. Distributed Systems)", 20);
        ModernTextField catField = new ModernTextField("Academic Category (e.g. Computer Science)", 20);
        JComboBox<User> facCombo = new JComboBox<>(facultyList.toArray(new User[0]));
        ModernTextField descField = new ModernTextField("Course Syllabus Description", 20);

        form.add(createFieldWrapper("Course Title:", titleField));
        form.add(createFieldWrapper("Academic Category:", catField));
        form.add(createFieldWrapper("Allocated Faculty In-Charge:", facCombo));
        form.add(createFieldWrapper("Curriculum Summary:", descField));
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Save Course", ModernButton.Style.PRIMARY);
        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Course title cannot be empty.");
                return;
            }
            User f = (User) facCombo.getSelectedItem();
            if (f != null) {
                DataStore.getInstance().addCourse(title,
                        descField.getText().trim().isEmpty() ? "Engineering syllabus module." : descField.getText().trim(),
                        f.getId(), f.getName(),
                        catField.getText().trim().isEmpty() ? "Core Engineering" : catField.getText().trim());
                loadCourses();
                refreshStats();
                dlg.dispose();
                Toast.show(this, "Course '" + title + "' added successfully!", Toast.Type.SUCCESS);
            }
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 4. Campus Bulletins ────────────────────────────────────────────────────
    private JPanel buildNoticesCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Official Campus Notices & Bulletins", "Broadcast announcements to all enrolled students and faculty across departments"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);
        ModernButton addNoticeBtn = new ModernButton("📢 Broadcast New Notice", ModernButton.Style.PRIMARY);
        addNoticeBtn.addActionListener(e -> showAddNoticeDialog());

        ModernButton deleteNoticeBtn = new ModernButton("🗑 Delete Notice", ModernButton.Style.DANGER);
        deleteNoticeBtn.addActionListener(e -> {
            int row = noticesTable.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select a notice to delete.", Toast.Type.WARNING);
                return;
            }
            int modelRow = noticesTable.convertRowIndexToModel(row);
            int nId = (int) noticesTableModel.getValueAt(modelRow, 0);
            DataStore.getInstance().deleteNotice(nId);
            loadNotices();
            refreshStats();
            Toast.show(this, "Notice removed.", Toast.Type.SUCCESS);
        });

        toolBar.add(addNoticeBtn);
        toolBar.add(deleteNoticeBtn);
        center.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"Notice ID", "Title", "Category", "Author / Office", "Date Published", "Priority"};
        noticesTableModel = new DefaultTableModel(cols, 0);
        noticesTable = new ModernTable(noticesTableModel);
        center.add(ModernTable.wrapInScrollPane(noticesTable), BorderLayout.CENTER);

        loadNotices();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadNotices() {
        noticesTableModel.setRowCount(0);
        for (Notice n : DataStore.getInstance().getAllNotices()) {
            noticesTableModel.addRow(new Object[]{
                    n.getId(), n.getTitle(), n.getCategory(), n.getAuthor(), n.getCreatedAt(),
                    n.isPinned() ? "📌 Pinned Priority" : "Standard"
            });
        }
    }

    private void showAddNoticeDialog() {
        JDialog dlg = new JDialog(this, "Broadcast Campus Notice", true);
        dlg.setSize(520, 440);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.setBorder(new EmptyBorder(18, 22, 14, 22));

        ModernTextField titleField = new ModernTextField("Notice Headline", 20);
        ModernTextField catField = new ModernTextField("Category (e.g. Academic, Placement, Event)", 20);
        ModernTextField contentField = new ModernTextField("Detailed Notice Content", 20);
        JCheckBox pinCheck = new JCheckBox("Pin as High-Priority Notice at Top of Portal");
        pinCheck.setFont(UITheme.FONT_BODY_BOLD);
        pinCheck.setOpaque(false);

        form.add(createFieldWrapper("Headline:", titleField));
        form.add(createFieldWrapper("Category:", catField));
        form.add(createFieldWrapper("Content:", contentField));
        form.add(pinCheck);
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Broadcast Notice", ModernButton.Style.PRIMARY);
        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Headline cannot be empty.");
                return;
            }
            DataStore.getInstance().addNotice(title,
                    contentField.getText().trim().isEmpty() ? "Official announcement." : contentField.getText().trim(),
                    catField.getText().trim().isEmpty() ? "General" : catField.getText().trim(),
                    adminUser.getName(),
                    pinCheck.isSelected());
            loadNotices();
            refreshStats();
            dlg.dispose();
            Toast.show(this, "Campus notice broadcast successfully!", Toast.Type.SUCCESS);
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 5. Doubt Supervision ───────────────────────────────────────────────────
    private JPanel buildDoubtsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Institutional Doubt Supervision & SLA Monitor", "Audit student inquiries and verify timely faculty responses"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        String[] cols = {"Doubt ID", "Course", "Student Inquirer", "Question Details", "Faculty Answer", "Resolution Status", "Date"};
        doubtsTableModel = new DefaultTableModel(cols, 0);
        doubtsTable = new ModernTable(doubtsTableModel);
        center.add(ModernTable.wrapInScrollPane(doubtsTable), BorderLayout.CENTER);

        loadDoubts();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadDoubts() {
        doubtsTableModel.setRowCount(0);
        for (Doubt d : DataStore.getInstance().getAllDoubts()) {
            doubtsTableModel.addRow(new Object[]{
                    d.getId(), d.getCourseTitle(), d.getStudentName(), d.getQuestion(),
                    d.getAnswer() != null ? d.getAnswer() : "Pending faculty response...",
                    d.getStatus() == Doubt.Status.RESOLVED ? "✅ Resolved" : "⏳ Pending",
                    d.getCreatedAt()
            });
        }
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

    private void refreshAllData() {
        refreshStats();
        loadUsers();
        loadCourses();
        loadNotices();
        loadDoubts();
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}

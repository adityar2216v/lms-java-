package com.lms.gui.views;

import com.lms.gui.db.DataStore;
import com.lms.gui.model.*;
import com.lms.gui.theme.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardFrame extends JFrame {
    private final User adminUser;
    private final CardLayout cardLayout;
    private final JPanel contentCards;

    private DefaultTableModel usersTableModel;
    private DefaultTableModel coursesTableModel;
    private DefaultTableModel noticesTableModel;
    private DefaultTableModel doubtsTableModel;

    private JPanel statsPanel;

    public AdminDashboardFrame(User adminUser) {
        super("EduPro LMS - Master Admin Dashboard (" + adminUser.getName() + ")");
        this.adminUser = adminUser;

        setSize(1180, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.APP_BG);
        setLayout(new BorderLayout());

        // ── Sidebar ────────────────────────────────────────────────────────────
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(UITheme.SIDEBAR_BG);

        // Sidebar Top: Brand & User profile
        JPanel sideTop = new JPanel();
        sideTop.setLayout(new BoxLayout(sideTop, BoxLayout.Y_AXIS));
        sideTop.setOpaque(false);
        sideTop.setBorder(new EmptyBorder(24, 20, 20, 20));

        JLabel brandLbl = new JLabel("🎓 EduPro LMS");
        brandLbl.setFont(UITheme.FONT_TITLE);
        brandLbl.setForeground(Color.WHITE);

        JLabel roleBadge = new JLabel("  ADMINISTRATOR  ");
        roleBadge.setFont(UITheme.FONT_SMALL_BOLD);
        roleBadge.setOpaque(true);
        roleBadge.setBackground(UITheme.PRIMARY);
        roleBadge.setForeground(Color.WHITE);
        roleBadge.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

        JLabel adminNameLbl = new JLabel(adminUser.getName());
        adminNameLbl.setFont(UITheme.FONT_BODY_BOLD);
        adminNameLbl.setForeground(Color.WHITE);

        JLabel adminEmailLbl = new JLabel(adminUser.getEmail());
        adminEmailLbl.setFont(UITheme.FONT_SMALL);
        adminEmailLbl.setForeground(UITheme.SIDEBAR_TEXT);

        sideTop.add(brandLbl);
        sideTop.add(Box.createVerticalStrut(10));
        sideTop.add(roleBadge);
        sideTop.add(Box.createVerticalStrut(12));
        sideTop.add(adminNameLbl);
        sideTop.add(Box.createVerticalStrut(2));
        sideTop.add(adminEmailLbl);
        sidebar.add(sideTop, BorderLayout.NORTH);

        // Sidebar Nav Buttons
        JPanel navPanel = new JPanel(new GridLayout(5, 1, 0, 8));
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(10, 16, 10, 16));

        navPanel.add(createNavButton("📊 System Overview", "OVERVIEW"));
        navPanel.add(createNavButton("👥 Manage Users", "USERS"));
        navPanel.add(createNavButton("📚 Manage Courses", "COURSES"));
        navPanel.add(createNavButton("📢 Campus Notices", "NOTICES"));
        navPanel.add(createNavButton("💬 Doubts & Q&A", "DOUBTS"));

        sidebar.add(navPanel, BorderLayout.CENTER);

        // Sidebar Bottom: Logout
        JPanel sideBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 16));
        sideBottom.setOpaque(false);
        ModernButton logoutBtn = new ModernButton("🚪 Log Out", ModernButton.Style.DANGER);
        logoutBtn.setPreferredSize(new Dimension(190, 40));
        logoutBtn.addActionListener(e -> logout());
        sideBottom.add(logoutBtn);
        sidebar.add(sideBottom, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);

        // ── Main Content Area (Cards) ──────────────────────────────────────────
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

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_BODY_BOLD);
        btn.setForeground(UITheme.SIDEBAR_TEXT);
        btn.setBackground(UITheme.SIDEBAR_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(UITheme.SIDEBAR_HOVER);
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(UITheme.SIDEBAR_BG);
                btn.setForeground(UITheme.SIDEBAR_TEXT);
            }
        });

        btn.addActionListener(e -> {
            cardLayout.show(contentCards, cardName);
            refreshAllData();
        });
        return btn;
    }

    // ── 1. Overview Card ───────────────────────────────────────────────────────
    private JPanel buildOverviewCard() {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setOpaque(false);
        card.add(UITheme.createHeader("System Overview & Analytics", "Real-time key performance indicators and statistics"), BorderLayout.NORTH);

        statsPanel = new JPanel(new GridLayout(2, 3, 16, 16));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        refreshStats();

        card.add(statsPanel, BorderLayout.CENTER);
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

        statsPanel.add(UITheme.createStatCard("Total Students", String.valueOf(studentCount), "👨‍🎓", UITheme.PRIMARY));
        statsPanel.add(UITheme.createStatCard("Total Faculty", String.valueOf(facultyCount), "👨‍🏫", UITheme.ACCENT));
        statsPanel.add(UITheme.createStatCard("Active Courses", String.valueOf(courseCount), "📚", UITheme.SUCCESS));
        statsPanel.add(UITheme.createStatCard("Total Enrollments", String.valueOf(enrollmentCount), "📈", new Color(139, 92, 246)));
        statsPanel.add(UITheme.createStatCard("Pending Doubts", String.valueOf(pendingDoubts), "❓", UITheme.WARNING));
        statsPanel.add(UITheme.createStatCard("Campus Notices", String.valueOf(noticeCount), "📢", UITheme.DANGER));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    // ── 2. Users Card ──────────────────────────────────────────────────────────
    private JPanel buildUsersCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("User Directory & Roles", "Manage student profiles, faculty credentials, and status approvals"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Tool bar
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);

        ModernTextField searchField = new ModernTextField("Search by name or email...", 20);
        JComboBox<String> roleFilter = new JComboBox<>(new String[]{"ALL ROLES", "STUDENT", "FACULTY", "ADMIN"});
        roleFilter.setFont(UITheme.FONT_BODY);

        ModernButton searchBtn = new ModernButton("🔍 Filter", ModernButton.Style.SECONDARY);
        searchBtn.addActionListener(e -> filterUsers(searchField.getText().trim(), (String) roleFilter.getSelectedItem()));

        ModernButton addBtn = new ModernButton("➕ Add User", ModernButton.Style.PRIMARY);
        addBtn.addActionListener(e -> showAddUserDialog());

        ModernButton toggleStatusBtn = new ModernButton("⚡ Toggle Active/Inactive", ModernButton.Style.WARNING);
        ModernButton deleteBtn = new ModernButton("🗑 Delete", ModernButton.Style.DANGER);

        toolBar.add(searchField);
        toolBar.add(roleFilter);
        toolBar.add(searchBtn);
        toolBar.add(addBtn);
        toolBar.add(toggleStatusBtn);
        toolBar.add(deleteBtn);

        center.add(toolBar, BorderLayout.NORTH);

        // Table
        String[] cols = {"User ID", "Full Name", "Email Address", "Mobile", "Role", "Department", "Account Status"};
        usersTableModel = new DefaultTableModel(cols, 0);
        ModernTable usersTable = new ModernTable(usersTableModel);
        center.add(ModernTable.wrapInScrollPane(usersTable), BorderLayout.CENTER);

        toggleStatusBtn.addActionListener(e -> {
            int row = usersTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a user to toggle status.");
                return;
            }
            int userId = (int) usersTableModel.getValueAt(row, 0);
            User u = DataStore.getInstance().getUserById(userId);
            if (u != null) {
                u.setStatus(u.getStatus() == 1 ? 0 : 1);
                DataStore.getInstance().saveUser(u);
                loadUsers(null, "ALL ROLES");
                JOptionPane.showMessageDialog(this, "User status updated to: " + (u.getStatus() == 1 ? "Active" : "Inactive"));
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = usersTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a user to delete.");
                return;
            }
            int userId = (int) usersTableModel.getValueAt(row, 0);
            int conf = JOptionPane.showConfirmDialog(this, "Delete user ID #" + userId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                DataStore.getInstance().deleteUser(userId);
                loadUsers(null, "ALL ROLES");
            }
        });

        loadUsers(null, "ALL ROLES");
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadUsers(String query, String roleFilter) {
        usersTableModel.setRowCount(0);
        List<User> list = DataStore.getInstance().getAllUsers();
        for (User u : list) {
            boolean matchQuery = query == null || query.isEmpty() ||
                    u.getName().toLowerCase().contains(query.toLowerCase()) ||
                    u.getEmail().toLowerCase().contains(query.toLowerCase());

            boolean matchRole = roleFilter == null || roleFilter.equals("ALL ROLES") ||
                    u.getRole().name().equalsIgnoreCase(roleFilter);

            if (matchQuery && matchRole) {
                usersTableModel.addRow(new Object[]{
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getMobile(),
                        u.getRole().name(),
                        u.getDepartment() != null ? u.getDepartment() : "General",
                        u.getStatus() == 1 ? "🟢 Active" : "🔴 Inactive"
                });
            }
        }
    }

    private void filterUsers(String query, String role) {
        loadUsers(query, role);
    }

    private void showAddUserDialog() {
        RegisterDialog dlg = new RegisterDialog(this);
        dlg.setVisible(true);
        if (dlg.isRegistered()) {
            loadUsers(null, "ALL ROLES");
            refreshStats();
        }
    }

    // ── 3. Courses Card ────────────────────────────────────────────────────────
    private JPanel buildCoursesCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Course Catalog Management", "Create, organize, and allocate faculty to curriculum modules"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);

        ModernButton createBtn = new ModernButton("➕ Create New Course", ModernButton.Style.PRIMARY);
        createBtn.addActionListener(e -> showCreateCourseDialog());

        ModernButton deleteBtn = new ModernButton("🗑 Delete Course", ModernButton.Style.DANGER);

        toolBar.add(createBtn);
        toolBar.add(deleteBtn);
        center.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"Course ID", "Course Title", "Category", "Assigned Faculty", "Enrolled Students", "Description"};
        coursesTableModel = new DefaultTableModel(cols, 0);
        ModernTable table = new ModernTable(coursesTableModel);
        center.add(ModernTable.wrapInScrollPane(table), BorderLayout.CENTER);

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a course to delete.");
                return;
            }
            int id = (int) coursesTableModel.getValueAt(row, 0);
            int conf = JOptionPane.showConfirmDialog(this, "Delete course #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                DataStore.getInstance().deleteCourse(id);
                loadCourses();
                refreshStats();
            }
        });

        loadCourses();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadCourses() {
        coursesTableModel.setRowCount(0);
        for (Course c : DataStore.getInstance().getAllCourses()) {
            coursesTableModel.addRow(new Object[]{
                    c.getId(),
                    c.getTitle(),
                    c.getCategory(),
                    c.getInstructorName(),
                    c.getEnrolledStudentsCount(),
                    c.getDescription()
            });
        }
    }

    private void showCreateCourseDialog() {
        JDialog dlg = new JDialog(this, "Create New Course", true);
        dlg.setSize(440, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        ModernTextField titleField = new ModernTextField("Course Title", 20);
        ModernTextField categoryField = new ModernTextField("Category (e.g. Computer Science)", 20);
        ModernTextField descField = new ModernTextField("Description", 20);

        List<User> faculties = DataStore.getInstance().getUsersByRole(User.Role.FACULTY);
        JComboBox<User> facultyCombo = new JComboBox<>(faculties.toArray(new User[0]));

        form.add(createFieldWrapper("Course Title:", titleField));
        form.add(createFieldWrapper("Category:", categoryField));
        form.add(createFieldWrapper("Course Description:", descField));
        form.add(createFieldWrapper("Assign Faculty:", facultyCombo));
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Save Course", ModernButton.Style.PRIMARY);
        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String cat = categoryField.getText().trim();
            String desc = descField.getText().trim();
            User f = (User) facultyCombo.getSelectedItem();
            if (title.isEmpty() || f == null) {
                JOptionPane.showMessageDialog(dlg, "Course title and faculty are required.");
                return;
            }
            DataStore.getInstance().addCourse(title, desc, f.getId(), f.getName(), cat);
            loadCourses();
            refreshStats();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Course created successfully!");
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 4. Notices Card ────────────────────────────────────────────────────────
    private JPanel buildNoticesCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Campus Notices & Announcements", "Broadcast notices to students and faculty"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);

        ModernButton createBtn = new ModernButton("📢 Publish Notice", ModernButton.Style.PRIMARY);
        createBtn.addActionListener(e -> showPublishNoticeDialog());

        ModernButton deleteBtn = new ModernButton("🗑 Delete Notice", ModernButton.Style.DANGER);
        toolBar.add(createBtn);
        toolBar.add(deleteBtn);
        center.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"Notice ID", "Title", "Category", "Author", "Published Date", "Pinned", "Message Body"};
        noticesTableModel = new DefaultTableModel(cols, 0);
        ModernTable table = new ModernTable(noticesTableModel);
        center.add(ModernTable.wrapInScrollPane(table), BorderLayout.CENTER);

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a notice to delete.");
                return;
            }
            int id = (int) noticesTableModel.getValueAt(row, 0);
            DataStore.getInstance().deleteNotice(id);
            loadNotices();
            refreshStats();
        });

        loadNotices();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadNotices() {
        noticesTableModel.setRowCount(0);
        for (Notice n : DataStore.getInstance().getAllNotices()) {
            noticesTableModel.addRow(new Object[]{
                    n.getId(),
                    n.getTitle(),
                    n.getCategory(),
                    n.getAuthor(),
                    n.getPostDate().toString(),
                    n.isPinned() ? "📌 Pinned" : "Standard",
                    n.getContent()
            });
        }
    }

    private void showPublishNoticeDialog() {
        JDialog dlg = new JDialog(this, "Publish Campus Notice", true);
        dlg.setSize(450, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        ModernTextField titleField = new ModernTextField("Notice Headline", 20);
        JComboBox<String> catCombo = new JComboBox<>(new String[]{"Academic", "Exam", "Placement", "Events", "General"});
        JTextArea contentArea = new JTextArea(4, 20);
        contentArea.setFont(UITheme.FONT_BODY);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JCheckBox pinCheck = new JCheckBox("Pin to top of student dashboard");

        form.add(createFieldWrapper("Notice Title:", titleField));
        form.add(createFieldWrapper("Category:", catCombo));
        form.add(createFieldWrapper("Content:", new JScrollPane(contentArea)));
        form.add(pinCheck);
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton pubBtn = new ModernButton("Broadcast Notice", ModernButton.Style.PRIMARY);
        pubBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String cat = (String) catCombo.getSelectedItem();
            String content = contentArea.getText().trim();
            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Notice title and content are required.");
                return;
            }
            DataStore.getInstance().addNotice(title, content, cat, adminUser.getName(), pinCheck.isSelected());
            loadNotices();
            refreshStats();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Notice broadcasted successfully!");
        });
        btm.add(pubBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 5. Doubts Card ─────────────────────────────────────────────────────────
    private JPanel buildDoubtsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Student Doubt & Resolution Monitor", "Supervise faculty interactions and student inquiry turnaround"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        String[] cols = {"Doubt ID", "Course", "Student Name", "Inquiry Question", "Instructor Answer", "Status", "Created At"};
        doubtsTableModel = new DefaultTableModel(cols, 0);
        ModernTable table = new ModernTable(doubtsTableModel);
        center.add(ModernTable.wrapInScrollPane(table), BorderLayout.CENTER);

        loadDoubts();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadDoubts() {
        doubtsTableModel.setRowCount(0);
        for (Doubt d : DataStore.getInstance().getAllDoubts()) {
            doubtsTableModel.addRow(new Object[]{
                    d.getId(),
                    d.getCourseTitle(),
                    d.getStudentName(),
                    d.getQuestion(),
                    d.getAnswer() != null ? d.getAnswer() : "Awaiting Instructor Reply",
                    d.getStatus() == Doubt.Status.RESOLVED ? "✅ Resolved" : "⏳ Pending",
                    d.getCreatedAt()
            });
        }
    }

    private void refreshAllData() {
        refreshStats();
        loadUsers(null, "ALL ROLES");
        loadCourses();
        loadNotices();
        loadDoubts();
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

    private void logout() {
        int conf = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out of the Admin Portal?", "Log Out", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}

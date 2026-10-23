package com.lms.gui.views;

import com.lms.gui.db.DataStore;
import com.lms.gui.model.*;
import com.lms.gui.theme.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Modern, India-focused Faculty Academic Portal for SmartLearn India.
 * Handles curriculum management, lecture uploads, rubric grading, MCQ quiz builder,
 * daily student attendance recording, and doubt mentoring.
 */
public class FacultyDashboardFrame extends JFrame {
    private final User facultyUser;
    private final CardLayout cardLayout;
    private final JPanel contentCards;
    private final TopNavBar topNavBar;

    private JPanel statsPanel;
    private DefaultTableModel coursesTableModel;
    private DefaultTableModel lecturesTableModel;
    private DefaultTableModel assignmentsTableModel;
    private DefaultTableModel submissionsTableModel;
    private DefaultTableModel examsTableModel;
    private DefaultTableModel resultsTableModel;
    private DefaultTableModel attendanceTableModel;
    private DefaultTableModel doubtsTableModel;

    private ModernTable submissionsTable;
    private ModernTable coursesTable;
    private ModernTable attendanceTable;

    public FacultyDashboardFrame(User facultyUser) {
        super("SmartLearn India — Faculty Academic Portal (" + facultyUser.getName() + ")");
        this.facultyUser = facultyUser;
        setIconImage(BrandLogo.createLogoImage(64));

        setSize(1240, 820);
        setMinimumSize(new Dimension(1040, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.APP_BG);
        setLayout(new BorderLayout());

        // ── Top Navigation Bar ─────────────────────────────────────────────────
        topNavBar = new TopNavBar(facultyUser, "SmartLearn India / Faculty Portal / Academic Overview", query -> {
            if (submissionsTable != null) submissionsTable.filter(query);
            if (coursesTable != null) coursesTable.filter(query);
            if (attendanceTable != null) attendanceTable.filter(query);
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

        JLabel roleBadge = new JLabel("  FACULTY ACADEMIC PORTAL  ");
        roleBadge.setFont(UITheme.FONT_SMALL_BOLD);
        roleBadge.setOpaque(true);
        roleBadge.setBackground(UITheme.SUCCESS_DARK);
        roleBadge.setForeground(Color.WHITE);
        roleBadge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        roleBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel facultyNameLbl = new JLabel(facultyUser.getName());
        facultyNameLbl.setFont(UITheme.FONT_BODY_BOLD);
        facultyNameLbl.setForeground(Color.WHITE);
        facultyNameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel facultyDeptLbl = new JLabel(facultyUser.getDepartment() != null ? facultyUser.getDepartment() : "Computer Science Dept");
        facultyDeptLbl.setFont(UITheme.FONT_SMALL);
        facultyDeptLbl.setForeground(UITheme.SIDEBAR_TEXT);
        facultyDeptLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        sideTop.add(brandHeader);
        sideTop.add(Box.createVerticalStrut(14));
        sideTop.add(roleBadge);
        sideTop.add(Box.createVerticalStrut(12));
        sideTop.add(facultyNameLbl);
        sideTop.add(Box.createVerticalStrut(2));
        sideTop.add(facultyDeptLbl);
        sidebar.add(sideTop, BorderLayout.NORTH);

        // Navigation
        JPanel navPanel = new JPanel(new GridLayout(6, 1, 0, 6));
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(6, 12, 6, 12));

        navPanel.add(createNavButton("Dashboard", VectorIcon.Type.DASHBOARD, "OVERVIEW"));
        navPanel.add(createNavButton("My Courses & Materials", VectorIcon.Type.COURSES, "COURSES"));
        navPanel.add(createNavButton("Assignments & Grading", VectorIcon.Type.ASSIGNMENTS, "ASSIGNMENTS"));
        navPanel.add(createNavButton("Exams & Assessments", VectorIcon.Type.EXAMS, "EXAMS"));
        navPanel.add(createNavButton("Record Attendance", VectorIcon.Type.ATTENDANCE, "ATTENDANCE"));
        navPanel.add(createNavButton("Student Q&A Doubts", VectorIcon.Type.DOUBTS, "DOUBTS"));

        sidebar.add(navPanel, BorderLayout.CENTER);

        // Logout
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
        contentCards.add(buildCoursesCard(), "COURSES");
        contentCards.add(buildAssignmentsCard(), "ASSIGNMENTS");
        contentCards.add(buildExamsCard(), "EXAMS");
        contentCards.add(buildAttendanceCard(), "ATTENDANCE");
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
            topNavBar.setBreadcrumb("SmartLearn India / Faculty Portal / " + text);
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

        JLabel titleLbl = new JLabel("Namaste, " + facultyUser.getName() + "! 🙏");
        titleLbl.setFont(UITheme.FONT_HERO);
        titleLbl.setForeground(UITheme.TEXT_MAIN);

        JLabel subLbl = new JLabel("Faculty Academic Hub | " + (facultyUser.getDepartment() != null ? facultyUser.getDepartment() : "Engineering"));
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

        // Submissions to Grade Quick Action Panel
        RoundedPanel subCard = new RoundedPanel(16, Color.WHITE);
        subCard.setLayout(new BorderLayout(8, 8));
        subCard.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel subTop = new JPanel(new BorderLayout());
        subTop.setOpaque(false);
        JLabel sLbl = new JLabel("Recent Student Submissions Awaiting Evaluation");
        sLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton gradeShortcutBtn = new ModernButton(
                "Grade Selected Submission",
                new VectorIcon(VectorIcon.Type.CHECK, 14, Color.WHITE),
                ModernButton.Style.PRIMARY
        );
        subTop.add(sLbl, BorderLayout.WEST);
        subTop.add(gradeShortcutBtn, BorderLayout.EAST);
        subCard.add(subTop, BorderLayout.NORTH);

        String[] cols = {"Submission ID", "Assignment", "Course", "Student Name", "Submitted At", "Status", "Score", "Faculty Feedback"};
        submissionsTableModel = new DefaultTableModel(cols, 0);
        submissionsTable = new ModernTable(submissionsTableModel);
        subCard.add(ModernTable.wrapInScrollPane(submissionsTable), BorderLayout.CENTER);

        gradeShortcutBtn.addActionListener(e -> {
            int row = submissionsTable.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select a submission from the table to evaluate.", Toast.Type.WARNING);
                return;
            }
            int modelRow = submissionsTable.convertRowIndexToModel(row);
            int subId = (int) submissionsTableModel.getValueAt(modelRow, 0);
            AssignmentSubmission s = DataStore.getInstance().getAllSubmissions().stream().filter(x -> x.getId() == subId).findFirst().orElse(null);
            if (s != null) {
                showGradeSubmissionDialog(s);
            }
        });

        center.add(subCard);

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);

        loadSubmissions();
        return card;
    }

    private void refreshStats() {
        statsPanel.removeAll();
        DataStore ds = DataStore.getInstance();

        List<Course> myCourses = ds.getCoursesByFaculty(facultyUser.getId());
        int myCourseCount = myCourses.size();

        int totalStudents = myCourses.stream().mapToInt(Course::getEnrolledStudentsCount).sum();

        List<Assignment> myAssignments = ds.getAssignmentsByFaculty(facultyUser.getId());
        int pendingSubmissions = 0;
        for (Assignment a : myAssignments) {
            for (AssignmentSubmission s : ds.getSubmissionsByAssignment(a.getId())) {
                if (s.getStatus() == AssignmentSubmission.Status.SUBMITTED) {
                    pendingSubmissions++;
                }
            }
        }

        int liveExams = ds.getExamsByFaculty(facultyUser.getId()).size();
        int pendingDoubts = (int) ds.getAllDoubts().stream().filter(d -> d.getStatus() == Doubt.Status.PENDING).count();

        statsPanel.add(UITheme.createStatCard("My Courses", String.valueOf(myCourseCount), "📚", UITheme.PRIMARY, "Semester IV"));
        statsPanel.add(UITheme.createStatCard("Enrolled Students", String.valueOf(totalStudents), "👨‍🎓", UITheme.ACCENT, "Across my courses"));
        statsPanel.add(UITheme.createStatCard("Submissions to Grade", String.valueOf(pendingSubmissions), "⏳", UITheme.WARNING, "Requires review"));
        statsPanel.add(UITheme.createStatCard("Active Assessments", String.valueOf(liveExams), "🏆", UITheme.SUCCESS, "Timed tests"));
        statsPanel.add(UITheme.createStatCard("Pending Doubts", String.valueOf(pendingDoubts), "💬", UITheme.DANGER, "Awaiting reply"));
        statsPanel.add(UITheme.createStatCard("Course Assignments", String.valueOf(myAssignments.size()), "📋", UITheme.PURPLE, "Published labs"));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    // ── 2. Course Management ───────────────────────────────────────────────────
    private JPanel buildCoursesCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Faculty Course & Curriculum Hub", "Design courses, upload video lectures, and distribute study notes"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 14, 14));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        // Courses Table
        RoundedPanel cSec = new RoundedPanel(16, Color.WHITE);
        cSec.setLayout(new BorderLayout(8, 8));
        cSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel cTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        cTop.setOpaque(false);
        JLabel cLbl = new JLabel("Courses Assigned to Me");
        cLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton addCourseBtn = new ModernButton("➕ Create New Course", ModernButton.Style.PRIMARY);
        addCourseBtn.addActionListener(e -> showAddCourseDialog());
        cTop.add(cLbl);
        cTop.add(addCourseBtn);
        cSec.add(cTop, BorderLayout.NORTH);

        String[] cCols = {"Course ID", "Course Title", "Category", "Enrolled Students", "Curriculum Description"};
        coursesTableModel = new DefaultTableModel(cCols, 0);
        coursesTable = new ModernTable(coursesTableModel);
        cSec.add(ModernTable.wrapInScrollPane(coursesTable), BorderLayout.CENTER);
        center.add(cSec);

        // Lectures Table
        RoundedPanel lSec = new RoundedPanel(16, Color.WHITE);
        lSec.setLayout(new BorderLayout(8, 8));
        lSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel lTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        lTop.setOpaque(false);
        JLabel lLbl = new JLabel("Curriculum Modules & Video Lectures");
        lLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton addLectureBtn = new ModernButton("🎥 Add Lecture Module", ModernButton.Style.SUCCESS);
        addLectureBtn.addActionListener(e -> showAddLectureDialog());
        lTop.add(lLbl);
        lTop.add(addLectureBtn);
        lSec.add(lTop, BorderLayout.NORTH);

        String[] lCols = {"ID", "Course", "Lesson Title", "Duration", "File Resource", "Study Notes"};
        lecturesTableModel = new DefaultTableModel(lCols, 0);
        ModernTable lTable = new ModernTable(lecturesTableModel);
        lSec.add(ModernTable.wrapInScrollPane(lTable), BorderLayout.CENTER);
        center.add(lSec);

        loadCoursesAndLectures();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadCoursesAndLectures() {
        coursesTableModel.setRowCount(0);
        lecturesTableModel.setRowCount(0);

        List<Course> courses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        for (Course c : courses) {
            coursesTableModel.addRow(new Object[]{
                    c.getId(), c.getTitle(), c.getCategory(), c.getEnrolledStudentsCount(), c.getDescription()
            });
            for (VideoLecture v : DataStore.getInstance().getLecturesByCourse(c.getId())) {
                lecturesTableModel.addRow(new Object[]{
                        v.getId(), v.getCourseTitle(), v.getTitle(), v.getDuration(), v.getFileName(), v.getNotesText()
                });
            }
        }
    }

    private void showAddCourseDialog() {
        JDialog dlg = new JDialog(this, "Create New Course", true);
        dlg.setSize(500, 360);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(3, 1, 10, 10));
        form.setBorder(new EmptyBorder(20, 24, 16, 24));

        ModernTextField titleField = new ModernTextField("Course Title (e.g. Advanced Java)", 20);
        ModernTextField catField = new ModernTextField("Category (e.g. Computer Science)", 20);
        ModernTextField descField = new ModernTextField("Description & Objectives", 20);

        form.add(createFieldWrapper("Course Title:", titleField));
        form.add(createFieldWrapper("Academic Category:", catField));
        form.add(createFieldWrapper("Curriculum Summary:", descField));
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Create Course", ModernButton.Style.PRIMARY);
        saveBtn.addActionListener(e -> {
            String t = titleField.getText().trim();
            String c = catField.getText().trim();
            String d = descField.getText().trim();
            if (t.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Course title cannot be empty.");
                return;
            }
            DataStore.getInstance().addCourse(t, d, facultyUser.getId(), facultyUser.getName(), c.isEmpty() ? "Engineering" : c);
            loadCoursesAndLectures();
            refreshStats();
            dlg.dispose();
            Toast.show(this, "Course '" + t + "' created successfully!", Toast.Type.SUCCESS);
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void showAddLectureDialog() {
        List<Course> courses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        if (courses.isEmpty()) {
            Toast.show(this, "Please create at least one course first.", Toast.Type.WARNING);
            return;
        }

        JDialog dlg = new JDialog(this, "Add Lecture Module", true);
        dlg.setSize(520, 480);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 1, 10, 10));
        form.setBorder(new EmptyBorder(16, 22, 12, 22));

        JComboBox<Course> courseCombo = new JComboBox<>(courses.toArray(new Course[0]));
        ModernTextField titleField = new ModernTextField("Lecture Title (e.g. 1. Introduction to Collections)", 20);
        ModernTextField durField = new ModernTextField("Duration (e.g. 45 mins)", 20);
        ModernTextField fileField = new ModernTextField("File Name / Video Source (e.g. lec1_intro.mp4)", 20);
        ModernTextField notesField = new ModernTextField("Study Notes / Key Takeaways", 20);

        form.add(createFieldWrapper("Select Course:", courseCombo));
        form.add(createFieldWrapper("Lesson Title:", titleField));
        form.add(createFieldWrapper("Duration:", durField));
        form.add(createFieldWrapper("Video File Resource:", fileField));
        form.add(createFieldWrapper("Key Study Notes:", notesField));
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Save Module", ModernButton.Style.PRIMARY);
        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Lecture title cannot be empty.");
                return;
            }
            Course c = (Course) courseCombo.getSelectedItem();
            if (c != null) {
                DataStore.getInstance().addLecture(c.getId(), c.getTitle(), title,
                        "Core lecture topic covering fundamental concepts.",
                        durField.getText().trim().isEmpty() ? "45 mins" : durField.getText().trim(),
                        fileField.getText().trim().isEmpty() ? "lecture.mp4" : fileField.getText().trim(),
                        notesField.getText().trim());
                loadCoursesAndLectures();
                dlg.dispose();
                Toast.show(this, "Lecture module added successfully!", Toast.Type.SUCCESS);
            }
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 3. Assignments & Grading ───────────────────────────────────────────────
    private JPanel buildAssignmentsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Homework & Lab Assignment Management", "Publish assignments with maximum marks, review student code, and provide grades"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 14, 14));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        // Assignments List
        RoundedPanel aSec = new RoundedPanel(16, Color.WHITE);
        aSec.setLayout(new BorderLayout(8, 8));
        aSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel aTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        aTop.setOpaque(false);
        JLabel aLbl = new JLabel("My Published Course Homework");
        aLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton addAssignBtn = new ModernButton("➕ Publish New Assignment", ModernButton.Style.PRIMARY);
        addAssignBtn.addActionListener(e -> showAddAssignmentDialog());
        aTop.add(aLbl);
        aTop.add(addAssignBtn);
        aSec.add(aTop, BorderLayout.NORTH);

        String[] aCols = {"ID", "Course", "Assignment Title", "Due Date", "Max Marks", "Problem Statement"};
        assignmentsTableModel = new DefaultTableModel(aCols, 0);
        ModernTable aTable = new ModernTable(assignmentsTableModel);
        aSec.add(ModernTable.wrapInScrollPane(aTable), BorderLayout.CENTER);
        center.add(aSec);

        // Submissions to Grade
        RoundedPanel sSec = new RoundedPanel(16, Color.WHITE);
        sSec.setLayout(new BorderLayout(8, 8));
        sSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel sTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        sTop.setOpaque(false);
        JLabel sLbl = new JLabel("Student Solutions Awaiting Evaluation");
        sLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton gradeBtn = new ModernButton("Evaluate & Assign Marks", ModernButton.Style.SUCCESS);
        gradeBtn.addActionListener(e -> {
            int row = submissionsTable.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select a submission from the table to evaluate.", Toast.Type.WARNING);
                return;
            }
            int modelRow = submissionsTable.convertRowIndexToModel(row);
            int subId = (int) submissionsTableModel.getValueAt(modelRow, 0);
            AssignmentSubmission s = DataStore.getInstance().getAllSubmissions().stream().filter(x -> x.getId() == subId).findFirst().orElse(null);
            if (s != null) {
                showGradeSubmissionDialog(s);
            }
        });
        sTop.add(sLbl);
        sTop.add(gradeBtn);
        sSec.add(sTop, BorderLayout.NORTH);
        sSec.add(ModernTable.wrapInScrollPane(submissionsTable), BorderLayout.CENTER);
        center.add(sSec);

        loadAssignmentsAndSubmissions();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadAssignmentsAndSubmissions() {
        assignmentsTableModel.setRowCount(0);
        loadSubmissions();
        for (Assignment a : DataStore.getInstance().getAssignmentsByFaculty(facultyUser.getId())) {
            assignmentsTableModel.addRow(new Object[]{
                    a.getId(), a.getCourseTitle(), a.getTitle(), a.getDueDate().toString(),
                    a.getMaxMarks(), a.getDescription()
            });
        }
    }

    private void loadSubmissions() {
        submissionsTableModel.setRowCount(0);
        for (Assignment a : DataStore.getInstance().getAssignmentsByFaculty(facultyUser.getId())) {
            for (AssignmentSubmission s : DataStore.getInstance().getSubmissionsByAssignment(a.getId())) {
                submissionsTableModel.addRow(new Object[]{
                        s.getId(), s.getAssignmentTitle(), s.getCourseTitle(), s.getStudentName(),
                        s.getSubmissionTime(),
                        s.getStatus() == AssignmentSubmission.Status.GRADED ? "🟢 Graded" : "⏳ Pending Review",
                        s.getMarks() != null ? s.getMarks() : "-",
                        s.getFeedback() != null ? s.getFeedback() : "Awaiting review"
                });
            }
        }
    }

    private void showAddAssignmentDialog() {
        List<Course> courses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        if (courses.isEmpty()) {
            Toast.show(this, "Please create at least one course first.", Toast.Type.WARNING);
            return;
        }

        JDialog dlg = new JDialog(this, "Publish New Course Assignment", true);
        dlg.setSize(520, 460);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.setBorder(new EmptyBorder(18, 22, 14, 22));

        JComboBox<Course> courseCombo = new JComboBox<>(courses.toArray(new Course[0]));
        ModernTextField titleField = new ModernTextField("Assignment Title", 20);
        ModernTextField marksField = new ModernTextField("Maximum Marks (e.g. 100)", 20);
        marksField.setText("100");
        ModernTextField descField = new ModernTextField("Detailed Problem Description", 20);

        form.add(createFieldWrapper("Course:", courseCombo));
        form.add(createFieldWrapper("Title:", titleField));
        form.add(createFieldWrapper("Maximum Marks:", marksField));
        form.add(createFieldWrapper("Problem Statement:", descField));
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Publish Assignment", ModernButton.Style.PRIMARY);
        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Assignment title cannot be empty.");
                return;
            }
            int marks = 100;
            try {
                marks = Integer.parseInt(marksField.getText().trim());
            } catch (Exception ignored) {}

            Course c = (Course) courseCombo.getSelectedItem();
            if (c != null) {
                DataStore.getInstance().addAssignment(c.getId(), c.getTitle(), title,
                        descField.getText().trim().isEmpty() ? "Complete the programming exercise." : descField.getText().trim(),
                        LocalDate.now().plusDays(7), marks, facultyUser.getId(), facultyUser.getName());
                loadAssignmentsAndSubmissions();
                refreshStats();
                dlg.dispose();
                Toast.show(this, "Assignment published successfully!", Toast.Type.SUCCESS);
            }
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void showGradeSubmissionDialog(AssignmentSubmission s) {
        JDialog dlg = new JDialog(this, "Grade Submission: " + s.getStudentName(), true);
        dlg.setSize(560, 480);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel info = new JLabel("<html><b>Student:</b> " + s.getStudentName() + " | <b>Assignment:</b> " + s.getAssignmentTitle() + "</html>");
        info.setFont(UITheme.FONT_BODY);
        center.add(info, BorderLayout.NORTH);

        JTextArea codeArea = new JTextArea(s.getAnswerText());
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        codeArea.setEditable(false);
        center.add(createFieldWrapper("Student Submitted Code / Solution:", new JScrollPane(codeArea)), BorderLayout.CENTER);

        JPanel gradingForm = new JPanel(new GridLayout(2, 1, 6, 6));
        ModernTextField marksField = new ModernTextField("Marks Awarded (e.g. 95)", 10);
        if (s.getMarks() != null) marksField.setText(String.valueOf(s.getMarks()));

        ModernTextField fbField = new ModernTextField("Constructive Faculty Feedback", 20);
        if (s.getFeedback() != null) fbField.setText(s.getFeedback());

        gradingForm.add(createFieldWrapper("Marks Awarded:", marksField));
        gradingForm.add(createFieldWrapper("Feedback / Remarks:", fbField));
        center.add(gradingForm, BorderLayout.SOUTH);

        dlg.add(center, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Record Grade & Feedback", ModernButton.Style.SUCCESS);
        saveBtn.addActionListener(e -> {
            try {
                int marks = Integer.parseInt(marksField.getText().trim());
                String fb = fbField.getText().trim();
                DataStore.getInstance().gradeSubmission(s.getId(), marks, fb.isEmpty() ? "Good work." : fb);
                loadSubmissions();
                refreshStats();
                dlg.dispose();
                Toast.show(this, "Marks (" + marks + ") and feedback recorded successfully!", Toast.Type.SUCCESS);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Please enter a valid numeric mark.");
            }
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 4. Exams & MCQ Builder ─────────────────────────────────────────────────
    private JPanel buildExamsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Online Examinations & MCQ Question Bank", "Design timed quizzes, configure auto-grading answer keys, and inspect score transcripts"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 14, 14));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        // Exams Table
        RoundedPanel eSec = new RoundedPanel(16, Color.WHITE);
        eSec.setLayout(new BorderLayout(8, 8));
        eSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel eTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        eTop.setOpaque(false);
        JLabel eLbl = new JLabel("Assessments Created by Me");
        eLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton addExamBtn = new ModernButton("➕ Create New Exam", ModernButton.Style.PRIMARY);
        addExamBtn.addActionListener(e -> showAddExamDialog());

        ModernButton addQBtn = new ModernButton("❓ Add MCQ Question", ModernButton.Style.SUCCESS);
        addQBtn.addActionListener(e -> {
            int row = examsTable.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select an exam from the table to add questions to.", Toast.Type.WARNING);
                return;
            }
            int modelRow = examsTable.convertRowIndexToModel(row);
            int examId = (int) examsTableModel.getValueAt(modelRow, 0);
            Exam ex = DataStore.getInstance().getAllExams().stream().filter(x -> x.getId() == examId).findFirst().orElse(null);
            if (ex != null) {
                showAddQuestionDialog(ex);
            }
        });

        eTop.add(eLbl);
        eTop.add(addExamBtn);
        eTop.add(addQBtn);
        eSec.add(eTop, BorderLayout.NORTH);

        String[] eCols = {"Exam ID", "Course", "Exam Title", "Duration", "Total Marks", "Questions Added", "Status"};
        examsTableModel = new DefaultTableModel(eCols, 0);
        examsTable = new ModernTable(examsTableModel);
        eSec.add(ModernTable.wrapInScrollPane(examsTable), BorderLayout.CENTER);
        center.add(eSec);

        // Results Table
        RoundedPanel rSec = new RoundedPanel(16, Color.WHITE);
        rSec.setLayout(new BorderLayout(8, 8));
        rSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel rLbl = new JLabel("Student Exam Performance Transcripts");
        rLbl.setFont(UITheme.FONT_SUBTITLE);
        rSec.add(rLbl, BorderLayout.NORTH);

        String[] rCols = {"Result ID", "Exam Title", "Student Name", "Score Obtained", "Total Marks", "Percentage", "Outcome", "Date"};
        resultsTableModel = new DefaultTableModel(rCols, 0);
        ModernTable rTable = new ModernTable(resultsTableModel);
        rSec.add(ModernTable.wrapInScrollPane(rTable), BorderLayout.CENTER);
        center.add(rSec);

        loadExamsAndResults();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private ModernTable examsTable;

    private void loadExamsAndResults() {
        examsTableModel.setRowCount(0);
        resultsTableModel.setRowCount(0);

        for (Exam ex : DataStore.getInstance().getExamsByFaculty(facultyUser.getId())) {
            examsTableModel.addRow(new Object[]{
                    ex.getId(), ex.getCourseTitle(), ex.getTitle(), ex.getDurationMinutes() + " mins",
                    ex.getTotalMarks(), ex.getQuestions().size(), ex.getStatus()
            });
            for (ExamResult r : DataStore.getInstance().getExamResultsByExam(ex.getId())) {
                resultsTableModel.addRow(new Object[]{
                        r.getId(), r.getExamTitle(), r.getStudentName(), r.getScore(),
                        r.getTotalMarks(), String.format("%.1f%%", r.getPercentage()),
                        r.getStatus().equals("PASSED") ? "🟢 Passed" : "🔴 Failed",
                        r.getAttemptDate()
                });
            }
        }
    }

    private void showAddExamDialog() {
        List<Course> courses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        if (courses.isEmpty()) {
            Toast.show(this, "Please create at least one course first.", Toast.Type.WARNING);
            return;
        }

        JDialog dlg = new JDialog(this, "Create Online Examination", true);
        dlg.setSize(500, 380);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.setBorder(new EmptyBorder(18, 22, 14, 22));

        JComboBox<Course> courseCombo = new JComboBox<>(courses.toArray(new Course[0]));
        ModernTextField titleField = new ModernTextField("Exam Title (e.g. Midterm 2026)", 20);
        ModernTextField durField = new ModernTextField("Duration in Minutes (e.g. 45)", 20);
        durField.setText("45");
        ModernTextField marksField = new ModernTextField("Total Marks (e.g. 40)", 20);
        marksField.setText("40");

        form.add(createFieldWrapper("Course:", courseCombo));
        form.add(createFieldWrapper("Exam Title:", titleField));
        form.add(createFieldWrapper("Duration (Minutes):", durField));
        form.add(createFieldWrapper("Total Marks:", marksField));
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Create Exam", ModernButton.Style.PRIMARY);
        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Exam title cannot be empty.");
                return;
            }
            int dur = 45;
            int marks = 40;
            try {
                dur = Integer.parseInt(durField.getText().trim());
                marks = Integer.parseInt(marksField.getText().trim());
            } catch (Exception ignored) {}

            Course c = (Course) courseCombo.getSelectedItem();
            if (c != null) {
                DataStore.getInstance().addExam(c.getId(), c.getTitle(), title, dur, marks, facultyUser.getId(), facultyUser.getName());
                loadExamsAndResults();
                refreshStats();
                dlg.dispose();
                Toast.show(this, "Exam '" + title + "' created! You can now add questions.", Toast.Type.SUCCESS);
            }
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void showAddQuestionDialog(Exam ex) {
        JDialog dlg = new JDialog(this, "Add Question to: " + ex.getTitle(), true);
        dlg.setSize(560, 520);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(7, 1, 8, 8));
        form.setBorder(new EmptyBorder(16, 22, 12, 22));

        ModernTextField qField = new ModernTextField("Question Text", 20);
        ModernTextField o1 = new ModernTextField("Option A", 20);
        ModernTextField o2 = new ModernTextField("Option B", 20);
        ModernTextField o3 = new ModernTextField("Option C", 20);
        ModernTextField o4 = new ModernTextField("Option D", 20);
        JComboBox<String> correctCombo = new JComboBox<>(new String[]{"Option A (0)", "Option B (1)", "Option C (2)", "Option D (3)"});
        ModernTextField marksField = new ModernTextField("Marks for this question", 10);
        marksField.setText("10");

        form.add(createFieldWrapper("Question Prompt:", qField));
        form.add(createFieldWrapper("Option A:", o1));
        form.add(createFieldWrapper("Option B:", o2));
        form.add(createFieldWrapper("Option C:", o3));
        form.add(createFieldWrapper("Option D:", o4));
        form.add(createFieldWrapper("Correct Answer Key:", correctCombo));
        form.add(createFieldWrapper("Marks:", marksField));
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Save Question", ModernButton.Style.PRIMARY);
        saveBtn.addActionListener(e -> {
            String q = qField.getText().trim();
            if (q.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Question text cannot be empty.");
                return;
            }
            int correctIdx = correctCombo.getSelectedIndex();
            int marks = 10;
            try {
                marks = Integer.parseInt(marksField.getText().trim());
            } catch (Exception ignored) {}

            List<String> options = Arrays.asList(o1.getText().trim(), o2.getText().trim(), o3.getText().trim(), o4.getText().trim());
            DataStore.getInstance().addQuestionToExam(ex.getId(), q, marks, options, correctIdx);
            loadExamsAndResults();
            dlg.dispose();
            Toast.show(this, "Question added to " + ex.getTitle() + "!", Toast.Type.SUCCESS);
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 5. Attendance Marker (NEW) ─────────────────────────────────────────────
    private JPanel buildAttendanceCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Student Attendance Management & AICTE Audits", "Record daily class attendance and monitor student 75% eligibility criteria"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        // Attendance Recording Toolbar
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);

        List<Course> myCourses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        JComboBox<Course> courseCombo = new JComboBox<>(myCourses.toArray(new Course[0]));

        List<User> students = DataStore.getInstance().getUsersByRole(User.Role.STUDENT);
        JComboBox<User> studentCombo = new JComboBox<>(students.toArray(new User[0]));

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"PRESENT", "ABSENT", "LATE"});
        ModernTextField topicField = new ModernTextField("Lecture Topic (e.g. Session 12)", 14);

        ModernButton markBtn = new ModernButton(
                "Record Attendance",
                new VectorIcon(VectorIcon.Type.CHECK, 14, Color.WHITE),
                ModernButton.Style.SUCCESS
        );

        toolBar.add(new JLabel("Course:"));
        toolBar.add(courseCombo);
        toolBar.add(new JLabel("Student:"));
        toolBar.add(studentCombo);
        toolBar.add(new JLabel("Status:"));
        toolBar.add(statusCombo);
        toolBar.add(topicField);
        toolBar.add(markBtn);
        center.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"Attendance ID", "Student Name", "Course Title", "Date", "Topic / Session", "Status"};
        attendanceTableModel = new DefaultTableModel(cols, 0);
        attendanceTable = new ModernTable(attendanceTableModel);
        center.add(ModernTable.wrapInScrollPane(attendanceTable), BorderLayout.CENTER);

        markBtn.addActionListener(e -> {
            Course c = (Course) courseCombo.getSelectedItem();
            User s = (User) studentCombo.getSelectedItem();
            if (c == null || s == null) {
                Toast.show(this, "Select a valid course and student.", Toast.Type.WARNING);
                return;
            }
            String stat = (String) statusCombo.getSelectedItem();
            String topic = topicField.getText().trim().isEmpty() ? "Class Lecture" : topicField.getText().trim();

            DataStore.getInstance().markAttendance(s.getId(), s.getName(), c.getId(), c.getTitle(), LocalDate.now(), stat, topic);
            loadAttendanceRecords();
            Toast.show(this, "Attendance marked " + stat + " for " + s.getName() + "!", Toast.Type.SUCCESS);
        });

        loadAttendanceRecords();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadAttendanceRecords() {
        attendanceTableModel.setRowCount(0);
        for (AttendanceRecord r : DataStore.getInstance().getAllAttendance()) {
            attendanceTableModel.addRow(new Object[]{
                    r.getId(),
                    r.getStudentName(),
                    r.getCourseTitle(),
                    r.getDate().toString(),
                    r.getTopic(),
                    r.isPresent() ? "🟢 " + r.getStatus() : "🔴 " + r.getStatus()
            });
        }
    }

    // ── 6. Student Doubts & Mentorship Q&A ──────────────────────────────────────
    private JPanel buildDoubtsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Student Doubt & Academic Mentorship Resolution", "Direct questions submitted by your students with 1-click answer posting"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);
        ModernButton answerBtn = new ModernButton(
                "Provide Faculty Answer",
                new VectorIcon(VectorIcon.Type.DOUBTS, 14, Color.WHITE),
                ModernButton.Style.PRIMARY
        );
        toolBar.add(answerBtn);
        center.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"Doubt ID", "Course", "Student Name", "Student Inquiry", "My Answer", "Status", "Date"};
        doubtsTableModel = new DefaultTableModel(cols, 0);
        ModernTable table = new ModernTable(doubtsTableModel);
        center.add(ModernTable.wrapInScrollPane(table), BorderLayout.CENTER);

        answerBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select a doubt from the table to answer.", Toast.Type.WARNING);
                return;
            }
            int modelRow = table.convertRowIndexToModel(row);
            int doubtId = (int) doubtsTableModel.getValueAt(modelRow, 0);
            Doubt d = DataStore.getInstance().getAllDoubts().stream().filter(x -> x.getId() == doubtId).findFirst().orElse(null);
            if (d != null) {
                showAnswerDoubtDialog(d);
            }
        });

        loadDoubts();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadDoubts() {
        doubtsTableModel.setRowCount(0);
        for (Doubt d : DataStore.getInstance().getAllDoubts()) {
            doubtsTableModel.addRow(new Object[]{
                    d.getId(), d.getCourseTitle(), d.getStudentName(), d.getQuestion(),
                    d.getAnswer() != null ? d.getAnswer() : "Awaiting your reply...",
                    d.getStatus() == Doubt.Status.RESOLVED ? "✅ Resolved" : "⏳ Pending",
                    d.getCreatedAt()
            });
        }
    }

    private void showAnswerDoubtDialog(Doubt d) {
        JDialog dlg = new JDialog(this, "Answer Doubt from " + d.getStudentName(), true);
        dlg.setSize(520, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel qLbl = new JLabel("<html><b>Student Question:</b><br/>" + d.getQuestion() + "</html>");
        qLbl.setFont(UITheme.FONT_BODY);
        center.add(qLbl, BorderLayout.NORTH);

        JTextArea ansArea = new JTextArea(6, 20);
        ansArea.setFont(UITheme.FONT_BODY);
        ansArea.setLineWrap(true);
        ansArea.setWrapStyleWord(true);
        if (d.getAnswer() != null) ansArea.setText(d.getAnswer());

        center.add(createFieldWrapper("Write Faculty Answer:", new JScrollPane(ansArea)), BorderLayout.CENTER);
        dlg.add(center, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Send Answer to Student", ModernButton.Style.SUCCESS);
        saveBtn.addActionListener(e -> {
            String ans = ansArea.getText().trim();
            if (ans.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Answer text cannot be empty.");
                return;
            }
            DataStore.getInstance().resolveDoubt(d.getId(), ans, facultyUser.getName());
            loadDoubts();
            refreshStats();
            dlg.dispose();
            Toast.show(this, "Answer posted and marked resolved for " + d.getStudentName() + "!", Toast.Type.SUCCESS);
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
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
        loadCoursesAndLectures();
        loadAssignmentsAndSubmissions();
        loadExamsAndResults();
        loadAttendanceRecords();
        loadDoubts();
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}

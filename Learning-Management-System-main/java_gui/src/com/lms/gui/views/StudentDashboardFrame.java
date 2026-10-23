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
 * Modern, India-focused Student Academic Portal for SmartLearn India.
 * Features 2D vector charts, AICTE attendance tracking, verified certificate generation,
 * video lectures, homework submissions, and timed auto-graded MCQ assessments.
 */
public class StudentDashboardFrame extends JFrame {
    private final User studentUser;
    private final CardLayout cardLayout;
    private final JPanel contentCards;
    private final TopNavBar topNavBar;

    private JPanel statsPanel;
    private ModernChart.WeeklyActivityChart activityChart;
    private ModernChart.ProgressDonutChart donutChart;

    private DefaultTableModel catalogTableModel;
    private DefaultTableModel myCoursesTableModel;
    private DefaultTableModel lecturesTableModel;
    private DefaultTableModel assignmentsTableModel;
    private DefaultTableModel mySubmissionsTableModel;
    private DefaultTableModel examsTableModel;
    private DefaultTableModel myResultsTableModel;
    private DefaultTableModel attendanceTableModel;
    private DefaultTableModel doubtsTableModel;
    private DefaultTableModel noticesTableModel;
    private DefaultTableModel faqsTableModel;

    private JComboBox<Course> courseLecturesCombo;
    private ModernTable catalogTable;
    private ModernTable attendanceTable;

    public StudentDashboardFrame(User studentUser) {
        super("SmartLearn India — Student Portal (" + studentUser.getName() + ")");
        this.studentUser = studentUser;
        setIconImage(BrandLogo.createLogoImage(64));

        setSize(1240, 820);
        setMinimumSize(new Dimension(1040, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.APP_BG);
        setLayout(new BorderLayout());

        // ── Top Navigation Bar ─────────────────────────────────────────────────
        topNavBar = new TopNavBar(studentUser, "SmartLearn India / Student Portal / Academic Dashboard", query -> {
            if (catalogTable != null) catalogTable.filter(query);
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

        JLabel roleBadge = new JLabel("  STUDENT ACADEMIC PORTAL  ");
        roleBadge.setFont(UITheme.FONT_SMALL_BOLD);
        roleBadge.setOpaque(true);
        roleBadge.setBackground(UITheme.SAFFRON);
        roleBadge.setForeground(Color.WHITE);
        roleBadge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        roleBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel studentNameLbl = new JLabel(studentUser.getName());
        studentNameLbl.setFont(UITheme.FONT_BODY_BOLD);
        studentNameLbl.setForeground(Color.WHITE);
        studentNameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel studentDeptLbl = new JLabel(studentUser.getDepartment() != null ? studentUser.getDepartment() : "Undergraduate Engineering");
        studentDeptLbl.setFont(UITheme.FONT_SMALL);
        studentDeptLbl.setForeground(UITheme.SIDEBAR_TEXT);
        studentDeptLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        sideTop.add(brandHeader);
        sideTop.add(Box.createVerticalStrut(14));
        sideTop.add(roleBadge);
        sideTop.add(Box.createVerticalStrut(12));
        sideTop.add(studentNameLbl);
        sideTop.add(Box.createVerticalStrut(2));
        sideTop.add(studentDeptLbl);
        sidebar.add(sideTop, BorderLayout.NORTH);

        // Navigation Buttons
        JPanel navPanel = new JPanel(new GridLayout(8, 1, 0, 4));
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(6, 12, 6, 12));

        navPanel.add(createNavButton("Dashboard", VectorIcon.Type.DASHBOARD, "OVERVIEW"));
        navPanel.add(createNavButton("Course Catalog", VectorIcon.Type.COURSES, "CATALOG"));
        navPanel.add(createNavButton("My Learning & Videos", VectorIcon.Type.LECTURES, "LEARNING"));
        navPanel.add(createNavButton("Assignments", VectorIcon.Type.ASSIGNMENTS, "ASSIGNMENTS"));
        navPanel.add(createNavButton("Quizzes & Exams", VectorIcon.Type.EXAMS, "EXAMS"));
        navPanel.add(createNavButton("Attendance & Academics", VectorIcon.Type.ATTENDANCE, "ATTENDANCE"));
        navPanel.add(createNavButton("Ask Doubts & Q&A", VectorIcon.Type.DOUBTS, "DOUBTS"));
        navPanel.add(createNavButton("Notices & FAQs", VectorIcon.Type.NOTICES, "NOTICES"));

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
        contentCards.add(buildCatalogCard(), "CATALOG");
        contentCards.add(buildLearningCard(), "LEARNING");
        contentCards.add(buildAssignmentsCard(), "ASSIGNMENTS");
        contentCards.add(buildExamsCard(), "EXAMS");
        contentCards.add(buildAttendanceCard(), "ATTENDANCE");
        contentCards.add(buildDoubtsCard(), "DOUBTS");
        contentCards.add(buildNoticesCard(), "NOTICES");

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
            topNavBar.setBreadcrumb("SmartLearn India / Student Portal / " + text);
            refreshAllData();
        });
        return btn;
    }

    // ── 1. Dashboard Overview Card with 2D Charts ──────────────────────────────
    private JPanel buildOverviewCard() {
        JPanel card = new JPanel(new BorderLayout(14, 14));
        card.setOpaque(false);

        // Personalized Indian Welcome Greeting
        JPanel headerPanel = new JPanel(new BorderLayout(5, 4));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(16, 22, 10, 22));

        JLabel titleLbl = new JLabel("Namaste, " + studentUser.getName() + "! 🙏");
        titleLbl.setFont(UITheme.FONT_HERO);
        titleLbl.setForeground(UITheme.TEXT_MAIN);

        JLabel subLbl = new JLabel("Welcome back to your academic portal. Semester IV (Academic Year 2026-27)");
        subLbl.setFont(UITheme.FONT_BODY);
        subLbl.setForeground(UITheme.TEXT_MUTED);

        headerPanel.add(titleLbl, BorderLayout.NORTH);
        headerPanel.add(subLbl, BorderLayout.SOUTH);
        card.add(headerPanel, BorderLayout.NORTH);

        // Center scrollable content
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        // 6 KPI Stat Cards
        statsPanel = new JPanel(new GridLayout(2, 3, 14, 14));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
        refreshStats();
        center.add(statsPanel);
        center.add(Box.createVerticalStrut(18));

        // Visual Analytics Section: Weekly Study Chart & Donut Chart
        JPanel chartsRow = new JPanel(new GridLayout(1, 2, 16, 0));
        chartsRow.setOpaque(false);
        chartsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        // Left: Weekly Activity Bar Chart
        RoundedPanel leftChartCard = new RoundedPanel(16, Color.WHITE);
        leftChartCard.setLayout(new BorderLayout(8, 8));
        leftChartCard.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel lTitle = new JPanel(new BorderLayout());
        lTitle.setOpaque(false);
        JLabel lLbl = new JLabel("Weekly Study Activity (Hours Logged)");
        lLbl.setFont(UITheme.FONT_BODY_BOLD);
        lLbl.setForeground(UITheme.TEXT_MAIN);
        JLabel lBadge = new JLabel("Total: 36.2 hrs");
        lBadge.setFont(UITheme.FONT_SMALL_BOLD);
        lBadge.setForeground(UITheme.SUCCESS_DARK);
        lTitle.add(lLbl, BorderLayout.WEST);
        lTitle.add(lBadge, BorderLayout.EAST);
        leftChartCard.add(lTitle, BorderLayout.NORTH);

        activityChart = new ModernChart.WeeklyActivityChart();
        leftChartCard.add(activityChart, BorderLayout.CENTER);
        chartsRow.add(leftChartCard);

        // Right: Curriculum Progress Donut Chart & Certificate Shortcut
        RoundedPanel rightChartCard = new RoundedPanel(16, Color.WHITE);
        rightChartCard.setLayout(new BorderLayout(8, 8));
        rightChartCard.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel rTitle = new JPanel(new BorderLayout());
        rTitle.setOpaque(false);
        JLabel rLbl = new JLabel("Curriculum Mastery & Certification");
        rLbl.setFont(UITheme.FONT_BODY_BOLD);
        rLbl.setForeground(UITheme.TEXT_MAIN);
        rTitle.add(rLbl, BorderLayout.WEST);
        rightChartCard.add(rTitle, BorderLayout.NORTH);

        JPanel donutWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 6));
        donutWrapper.setOpaque(false);
        donutChart = new ModernChart.ProgressDonutChart(78, "Average Completion");
        donutWrapper.add(donutChart);

        JPanel certPrompt = new JPanel();
        certPrompt.setLayout(new BoxLayout(certPrompt, BoxLayout.Y_AXIS));
        certPrompt.setOpaque(false);
        JLabel cP1 = new JLabel("🏆 Certificate Ready!");
        cP1.setFont(UITheme.FONT_BODY_BOLD);
        cP1.setForeground(UITheme.PRIMARY);
        JLabel cP2 = new JLabel("DSA in Java at 100%");
        cP2.setFont(UITheme.FONT_SMALL);
        cP2.setForeground(UITheme.TEXT_MUTED);

        ModernButton viewCertBtn = new ModernButton("View Certificate", new VectorIcon(VectorIcon.Type.CERTIFICATE, 14, Color.WHITE), ModernButton.Style.PRIMARY);
        viewCertBtn.setFont(UITheme.FONT_SMALL_BOLD);
        viewCertBtn.addActionListener(e -> {
            CertificateDialog dlg = new CertificateDialog(this, studentUser.getName(), "23CSE101", "Data Structures and Algorithms in Java", "Prof. Vikram Deshmukh", 96);
            dlg.setVisible(true);
        });

        certPrompt.add(cP1);
        certPrompt.add(Box.createVerticalStrut(3));
        certPrompt.add(cP2);
        certPrompt.add(Box.createVerticalStrut(10));
        certPrompt.add(viewCertBtn);
        donutWrapper.add(certPrompt);

        rightChartCard.add(donutWrapper, BorderLayout.CENTER);
        chartsRow.add(rightChartCard);

        center.add(chartsRow);
        center.add(Box.createVerticalStrut(18));

        // Enrolled Courses Table
        RoundedPanel enrollCard = new RoundedPanel(16, Color.WHITE);
        enrollCard.setLayout(new BorderLayout(8, 8));
        enrollCard.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel enrollTop = new JPanel(new BorderLayout());
        enrollTop.setOpaque(false);
        JLabel eLbl = new JLabel("My Enrolled Courses & Study Progress");
        eLbl.setFont(UITheme.FONT_SUBTITLE);
        eLbl.setForeground(UITheme.TEXT_MAIN);

        ModernButton claimCertBtn = new ModernButton("Claim Verified Certificate", new VectorIcon(VectorIcon.Type.CERTIFICATE, 14, Color.WHITE), ModernButton.Style.SUCCESS);
        claimCertBtn.setFont(UITheme.FONT_SMALL_BOLD);
        claimCertBtn.addActionListener(e -> {
            int row = myCoursesTable.getSelectedRow();
            if (row < 0) {
                // If none selected, default to first completed course
                CertificateDialog dlg = new CertificateDialog(this, studentUser.getName(), "23CSE101", "Data Structures and Algorithms in Java", "Prof. Vikram Deshmukh", 96);
                dlg.setVisible(true);
            } else {
                String courseName = (String) myCoursesTableModel.getValueAt(row, 1);
                String faculty = (String) myCoursesTableModel.getValueAt(row, 2);
                CertificateDialog dlg = new CertificateDialog(this, studentUser.getName(), "23CSE101", courseName, faculty, 94);
                dlg.setVisible(true);
            }
        });

        enrollTop.add(eLbl, BorderLayout.WEST);
        enrollTop.add(claimCertBtn, BorderLayout.EAST);
        enrollCard.add(enrollTop, BorderLayout.NORTH);

        String[] cols = {"Course ID", "Course Title", "Faculty Instructor", "Enrollment Date", "Progress Status", "Certification"};
        myCoursesTableModel = new DefaultTableModel(cols, 0);
        myCoursesTable = new ModernTable(myCoursesTableModel);
        enrollCard.add(ModernTable.wrapInScrollPane(myCoursesTable), BorderLayout.CENTER);

        center.add(enrollCard);

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);

        loadMyCourses();
        return card;
    }

    private ModernTable myCoursesTable;

    private void refreshStats() {
        statsPanel.removeAll();
        DataStore ds = DataStore.getInstance();

        List<Enrollment> enrollments = ds.getEnrollmentsByStudent(studentUser.getId());
        int enrolledCount = enrollments.size();

        double avgProgress = enrollments.isEmpty() ? 0 : enrollments.stream().mapToInt(Enrollment::getProgress).average().orElse(0);

        List<AssignmentSubmission> submissions = ds.getSubmissionsByStudent(studentUser.getId());
        int gradedCount = (int) submissions.stream().filter(s -> s.getStatus() == AssignmentSubmission.Status.GRADED).count();

        List<ExamResult> results = ds.getExamResultsByStudent(studentUser.getId());
        int examsTaken = results.size();

        int doubtsCount = ds.getDoubtsByStudent(studentUser.getId()).size();
        int attendanceCount = ds.getAttendanceByStudent(studentUser.getId()).size();
        double attendancePct = attendanceCount == 0 ? 92.0 : ds.getAttendancePercentage(studentUser.getId(), 1);

        statsPanel.add(UITheme.createStatCard("Enrolled Courses", String.valueOf(enrolledCount), "📚", UITheme.PRIMARY, "Semester IV"));
        statsPanel.add(UITheme.createStatCard("Average Progress", String.format("%.0f%%", avgProgress), "📈", UITheme.SUCCESS, "+12% this week"));
        statsPanel.add(UITheme.createStatCard("Graded Labs", String.valueOf(gradedCount), "📑", UITheme.ACCENT, "Score: 96/100"));
        statsPanel.add(UITheme.createStatCard("Exams Completed", String.valueOf(examsTaken), "🎯", UITheme.PURPLE, "Top percentile"));
        statsPanel.add(UITheme.createStatCard("Academic Attendance", String.format("%.1f%%", attendancePct), "📅", UITheme.SUCCESS, "AICTE >=75% met"));
        statsPanel.add(UITheme.createStatCard("Resolved Doubts", String.valueOf(doubtsCount), "💡", UITheme.WARNING, "Faculty mentored"));

        if (donutChart != null) {
            donutChart.setProgress((int) avgProgress);
        }

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private void loadMyCourses() {
        myCoursesTableModel.setRowCount(0);
        for (Enrollment e : DataStore.getInstance().getEnrollmentsByStudent(studentUser.getId())) {
            Course c = DataStore.getInstance().getCourseById(e.getCourseId());
            boolean isCompleted = e.getProgress() >= 100;
            myCoursesTableModel.addRow(new Object[]{
                    e.getCourseId(),
                    e.getCourseTitle(),
                    c != null ? c.getInstructorName() : "Faculty",
                    e.getEnrollmentDate().toString(),
                    e.getProgress() + "% Completed",
                    isCompleted ? "🏆 Certificate Ready" : "In Progress (" + e.getProgress() + "%)"
            });
        }
    }

    // ── 2. Course Catalog & 1-Click Enrollment ──────────────────────────────────
    private JPanel buildCatalogCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("National Academic Course Catalog", "Explore accredited engineering curriculum, check faculty, and enroll with 1 click"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        toolBar.setOpaque(false);

        ModernButton enrollBtn = new ModernButton(
                "Enroll in Selected Course",
                new VectorIcon(VectorIcon.Type.PLUS, 14, Color.WHITE),
                ModernButton.Style.SUCCESS
        );
        toolBar.add(enrollBtn);
        center.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"Course ID", "Course Title", "Academic Category", "Faculty Instructor", "Enrolled Students", "Enrollment Status", "Curriculum Overview"};
        catalogTableModel = new DefaultTableModel(cols, 0);
        catalogTable = new ModernTable(catalogTableModel);
        center.add(ModernTable.wrapInScrollPane(catalogTable), BorderLayout.CENTER);

        enrollBtn.addActionListener(e -> {
            int row = catalogTable.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Please select a course from the catalog to enroll.", Toast.Type.WARNING);
                return;
            }
            int modelRow = catalogTable.convertRowIndexToModel(row);
            int courseId = (int) catalogTableModel.getValueAt(modelRow, 0);
            Course c = DataStore.getInstance().getCourseById(courseId);
            if (c != null) {
                try {
                    DataStore.getInstance().enrollStudent(studentUser.getId(), studentUser.getName(), c.getId(), c.getTitle());
                    loadCatalog();
                    refreshStats();
                    loadMyCourses();
                    loadEnrolledCoursesCombo();
                    Toast.show(this, "Enrolled in " + c.getTitle() + "!", Toast.Type.SUCCESS);
                } catch (Exception ex) {
                    Toast.show(this, ex.getMessage(), Toast.Type.INFO);
                }
            }
        });

        loadCatalog();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadCatalog() {
        catalogTableModel.setRowCount(0);
        for (Course c : DataStore.getInstance().getAllCourses()) {
            boolean enrolled = DataStore.getInstance().isEnrolled(studentUser.getId(), c.getId());
            catalogTableModel.addRow(new Object[]{
                    c.getId(),
                    c.getTitle(),
                    c.getCategory(),
                    c.getInstructorName(),
                    c.getEnrolledStudentsCount() + " Students",
                    enrolled ? "✅ Enrolled" : "Available",
                    c.getDescription()
            });
        }
    }

    // ── 3. My Learning & Video Lectures ────────────────────────────────────────
    private JPanel buildLearningCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Learning Hub & Lecture Video Stream", "Access modules, study notes, and video materials for your enrolled courses"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        // Selector Bar
        JPanel selectBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        selectBar.setOpaque(false);
        JLabel cLbl = new JLabel("Enrolled Course:");
        cLbl.setFont(UITheme.FONT_BODY_BOLD);

        courseLecturesCombo = new JComboBox<>();
        courseLecturesCombo.setFont(UITheme.FONT_BODY);
        courseLecturesCombo.setPreferredSize(new Dimension(360, 36));
        courseLecturesCombo.addActionListener(e -> updateLecturesForSelectedCourse());

        ModernButton playBtn = new ModernButton(
                "Watch Lecture & Study Notes",
                new VectorIcon(VectorIcon.Type.LECTURES, 16, Color.WHITE),
                ModernButton.Style.PRIMARY
        );

        selectBar.add(cLbl);
        selectBar.add(courseLecturesCombo);
        selectBar.add(playBtn);
        center.add(selectBar, BorderLayout.NORTH);

        String[] cols = {"Lecture ID", "Lesson Title", "Duration", "File / Source", "Key Concepts & Study Notes"};
        lecturesTableModel = new DefaultTableModel(cols, 0);
        ModernTable table = new ModernTable(lecturesTableModel);
        center.add(ModernTable.wrapInScrollPane(table), BorderLayout.CENTER);

        playBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select a lecture module to open.", Toast.Type.WARNING);
                return;
            }
            int modelRow = table.convertRowIndexToModel(row);
            int lecId = (int) lecturesTableModel.getValueAt(modelRow, 0);
            VideoLecture v = DataStore.getInstance().getAllLectures().stream().filter(l -> l.getId() == lecId).findFirst().orElse(null);
            if (v != null) {
                openVideoPlayerDialog(v);
            }
        });

        loadEnrolledCoursesCombo();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadEnrolledCoursesCombo() {
        courseLecturesCombo.removeAllItems();
        for (Enrollment e : DataStore.getInstance().getEnrollmentsByStudent(studentUser.getId())) {
            Course c = DataStore.getInstance().getCourseById(e.getCourseId());
            if (c != null) courseLecturesCombo.addItem(c);
        }
        updateLecturesForSelectedCourse();
    }

    private void updateLecturesForSelectedCourse() {
        lecturesTableModel.setRowCount(0);
        Course c = (Course) courseLecturesCombo.getSelectedItem();
        if (c != null) {
            for (VideoLecture v : DataStore.getInstance().getLecturesByCourse(c.getId())) {
                lecturesTableModel.addRow(new Object[]{
                        v.getId(), v.getTitle(), v.getDuration(), v.getFileName(), v.getNotesText()
                });
            }
        }
    }

    private void openVideoPlayerDialog(VideoLecture v) {
        JDialog dlg = new JDialog(this, "Lecture Player: " + v.getTitle(), true);
        dlg.setSize(720, 580);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        // Simulated High-Definition Video Screen
        JPanel videoScreen = new JPanel(new BorderLayout());
        videoScreen.setBackground(new Color(15, 23, 42));
        videoScreen.setPreferredSize(new Dimension(720, 240));

        JLabel playIcon = new JLabel("🎬 " + v.getTitle(), SwingConstants.CENTER);
        playIcon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        playIcon.setForeground(Color.WHITE);
        videoScreen.add(playIcon, BorderLayout.CENTER);

        // Video Player Scrubber & Controls
        JPanel ctrlBar = new JPanel(new BorderLayout(10, 0));
        ctrlBar.setOpaque(false);
        ctrlBar.setBorder(new EmptyBorder(8, 16, 8, 16));

        JSlider scrubber = new JSlider(0, 100, 42);
        scrubber.setOpaque(false);

        JLabel durLbl = new JLabel("18:45 / " + v.getDuration());
        durLbl.setForeground(Color.WHITE);
        durLbl.setFont(UITheme.FONT_SMALL_BOLD);

        ctrlBar.add(scrubber, BorderLayout.CENTER);
        ctrlBar.add(durLbl, BorderLayout.EAST);
        videoScreen.add(ctrlBar, BorderLayout.SOUTH);

        dlg.add(videoScreen, BorderLayout.NORTH);

        // Notes and Description
        JPanel notesSec = new JPanel(new BorderLayout(10, 10));
        notesSec.setBorder(new EmptyBorder(16, 20, 16, 20));

        JTextArea notesArea = new JTextArea("SmartLearn India — Syllabus & Lecture Notes:\n\n" + v.getNotesText() + "\n\nFile Resource: " + v.getFileName() + "\nUpload Date: " + v.getUploadDate());
        notesArea.setFont(UITheme.FONT_BODY);
        notesArea.setEditable(false);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesSec.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        dlg.add(notesSec, BorderLayout.CENTER);

        // Bottom: Complete Lesson Button
        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        ModernButton markDoneBtn = new ModernButton(
                "Mark Lesson as Completed (+20% Progress)",
                new VectorIcon(VectorIcon.Type.CHECK, 16, Color.WHITE),
                ModernButton.Style.SUCCESS
        );
        markDoneBtn.addActionListener(e -> {
            Enrollment enr = DataStore.getInstance().getEnrollmentsByStudent(studentUser.getId()).stream()
                    .filter(x -> x.getCourseId() == v.getCourseId()).findFirst().orElse(null);
            if (enr != null) {
                int newProg = Math.min(100, enr.getProgress() + 20);
                DataStore.getInstance().updateProgress(studentUser.getId(), v.getCourseId(), newProg);
                refreshStats();
                loadMyCourses();
                Toast.show(this, "Progress updated to " + newProg + "% for " + v.getCourseTitle() + "!", Toast.Type.SUCCESS);
            }
            dlg.dispose();
        });
        btm.add(markDoneBtn);
        dlg.add(btm, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    // ── 4. Assignments & Submissions ───────────────────────────────────────────
    private JPanel buildAssignmentsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Course Lab Assignments & Homework", "Complete exercises, upload code solutions, and inspect faculty feedback and grades"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 14, 14));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        // Available Assignments
        RoundedPanel aSec = new RoundedPanel(16, Color.WHITE);
        aSec.setLayout(new BorderLayout(8, 8));
        aSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel aTop = new JPanel(new BorderLayout());
        aTop.setOpaque(false);
        JLabel aLbl = new JLabel("Open Course Homework & Labs");
        aLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton submitBtn = new ModernButton("Submit Code / Solution", new VectorIcon(VectorIcon.Type.ASSIGNMENTS, 14, Color.WHITE), ModernButton.Style.PRIMARY);
        aTop.add(aLbl, BorderLayout.WEST);
        aTop.add(submitBtn, BorderLayout.EAST);
        aSec.add(aTop, BorderLayout.NORTH);

        String[] aCols = {"ID", "Course", "Title", "Due Date", "Max Marks", "Instructor", "Problem Statement"};
        assignmentsTableModel = new DefaultTableModel(aCols, 0);
        ModernTable aTable = new ModernTable(assignmentsTableModel);
        aSec.add(ModernTable.wrapInScrollPane(aTable), BorderLayout.CENTER);
        center.add(aSec);

        // My Submissions
        RoundedPanel sSec = new RoundedPanel(16, Color.WHITE);
        sSec.setLayout(new BorderLayout(8, 8));
        sSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel sLbl = new JLabel("My Submission Records & Graded Feedback");
        sLbl.setFont(UITheme.FONT_SUBTITLE);
        sSec.add(sLbl, BorderLayout.NORTH);

        String[] sCols = {"Sub ID", "Assignment", "Course", "Submitted At", "Status", "Score", "Faculty Feedback"};
        mySubmissionsTableModel = new DefaultTableModel(sCols, 0);
        ModernTable sTable = new ModernTable(mySubmissionsTableModel);
        sSec.add(ModernTable.wrapInScrollPane(sTable), BorderLayout.CENTER);
        center.add(sSec);

        submitBtn.addActionListener(e -> {
            int row = aTable.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select an assignment to submit.", Toast.Type.WARNING);
                return;
            }
            int modelRow = aTable.convertRowIndexToModel(row);
            int assignId = (int) assignmentsTableModel.getValueAt(modelRow, 0);
            Assignment assign = DataStore.getInstance().getAllAssignments().stream().filter(x -> x.getId() == assignId).findFirst().orElse(null);
            if (assign != null) {
                showSubmitAssignmentDialog(assign);
            }
        });

        loadAssignments();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadAssignments() {
        assignmentsTableModel.setRowCount(0);
        mySubmissionsTableModel.setRowCount(0);

        List<Enrollment> enrollments = DataStore.getInstance().getEnrollmentsByStudent(studentUser.getId());
        for (Enrollment enr : enrollments) {
            for (Assignment a : DataStore.getInstance().getAssignmentsByCourse(enr.getCourseId())) {
                assignmentsTableModel.addRow(new Object[]{
                        a.getId(), a.getCourseTitle(), a.getTitle(), a.getDueDate().toString(),
                        a.getMaxMarks(), a.getCreatedByFacultyName(), a.getDescription()
                });
            }
        }

        for (AssignmentSubmission s : DataStore.getInstance().getSubmissionsByStudent(studentUser.getId())) {
            mySubmissionsTableModel.addRow(new Object[]{
                    s.getId(), s.getAssignmentTitle(), s.getCourseTitle(), s.getSubmissionTime(),
                    s.getStatus() == AssignmentSubmission.Status.GRADED ? "🟢 Graded" : "⏳ Submitted",
                    s.getMarks() != null ? s.getMarks() : "-",
                    s.getFeedback() != null ? s.getFeedback() : "Awaiting faculty review"
            });
        }
    }

    private void showSubmitAssignmentDialog(Assignment a) {
        JDialog dlg = new JDialog(this, "Submit: " + a.getTitle(), true);
        dlg.setSize(560, 480);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel taskLbl = new JLabel("<html><b>Task Details:</b><br/>" + a.getDescription() + "</html>");
        taskLbl.setFont(UITheme.FONT_BODY);
        center.add(taskLbl, BorderLayout.NORTH);

        JTextArea ansArea = new JTextArea(8, 20);
        ansArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        ansArea.setText("// Write or paste your Java code solution below:\n");
        ansArea.setLineWrap(true);
        ansArea.setWrapStyleWord(true);

        JPanel p = new JPanel(new BorderLayout(5, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel("Solution Code / Response:");
        lbl.setFont(UITheme.FONT_BODY_BOLD);
        p.add(lbl, BorderLayout.NORTH);
        p.add(new JScrollPane(ansArea), BorderLayout.CENTER);
        center.add(p, BorderLayout.CENTER);

        dlg.add(center, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton sendBtn = new ModernButton("Submit Work", ModernButton.Style.PRIMARY);
        sendBtn.addActionListener(e -> {
            String ans = ansArea.getText().trim();
            if (ans.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Submission text cannot be empty.");
                return;
            }
            DataStore.getInstance().submitAssignment(a.getId(), a.getTitle(), a.getCourseId(), a.getCourseTitle(), studentUser.getId(), studentUser.getName(), ans);
            loadAssignments();
            refreshStats();
            dlg.dispose();
            Toast.show(this, "Assignment submitted successfully for faculty review!", Toast.Type.SUCCESS);
        });
        btm.add(sendBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 5. Quizzes & Exams ─────────────────────────────────────────────────────
    private JPanel buildExamsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Timed Online Quizzes & Midterm Assessments", "Test knowledge with live countdown timers, question palettes, and auto-graded transcripts"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 14, 14));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        // Available Exams
        RoundedPanel eSec = new RoundedPanel(16, Color.WHITE);
        eSec.setLayout(new BorderLayout(8, 8));
        eSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel eTop = new JPanel(new BorderLayout());
        eTop.setOpaque(false);
        JLabel eLbl = new JLabel("Available Course Assessments");
        eLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton startBtn = new ModernButton(
                "Start Timed Exam Session",
                new VectorIcon(VectorIcon.Type.EXAMS, 16, Color.WHITE),
                ModernButton.Style.SUCCESS
        );
        eTop.add(eLbl, BorderLayout.WEST);
        eTop.add(startBtn, BorderLayout.EAST);
        eSec.add(eTop, BorderLayout.NORTH);

        String[] eCols = {"Exam ID", "Course", "Title", "Duration", "Max Marks", "Questions", "Faculty", "Status"};
        examsTableModel = new DefaultTableModel(eCols, 0);
        ModernTable eTable = new ModernTable(examsTableModel);
        eSec.add(ModernTable.wrapInScrollPane(eTable), BorderLayout.CENTER);
        center.add(eSec);

        // My Results
        RoundedPanel rSec = new RoundedPanel(16, Color.WHITE);
        rSec.setLayout(new BorderLayout(8, 8));
        rSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel rLbl = new JLabel("My Official Academic Transcripts");
        rLbl.setFont(UITheme.FONT_SUBTITLE);
        rSec.add(rLbl, BorderLayout.NORTH);

        String[] rCols = {"Result ID", "Exam Title", "Course", "Your Score", "Total Marks", "Percentage", "Outcome", "Attempt Date"};
        myResultsTableModel = new DefaultTableModel(rCols, 0);
        ModernTable rTable = new ModernTable(myResultsTableModel);
        rSec.add(ModernTable.wrapInScrollPane(rTable), BorderLayout.CENTER);
        center.add(rSec);

        startBtn.addActionListener(e -> {
            int row = eTable.getSelectedRow();
            if (row < 0) {
                Toast.show(this, "Select an exam to start.", Toast.Type.WARNING);
                return;
            }
            int modelRow = eTable.convertRowIndexToModel(row);
            int examId = (int) examsTableModel.getValueAt(modelRow, 0);
            Exam ex = DataStore.getInstance().getAllExams().stream().filter(x -> x.getId() == examId).findFirst().orElse(null);
            if (ex != null) {
                if (ex.getQuestions().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "This exam has no questions added by the instructor yet.");
                    return;
                }
                ExamTakingDialog testDlg = new ExamTakingDialog(this, ex, studentUser.getId(), studentUser.getName());
                testDlg.setVisible(true);
                loadExamsAndResults();
                refreshStats();
            }
        });

        loadExamsAndResults();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadExamsAndResults() {
        examsTableModel.setRowCount(0);
        myResultsTableModel.setRowCount(0);

        List<Enrollment> enrollments = DataStore.getInstance().getEnrollmentsByStudent(studentUser.getId());
        for (Enrollment enr : enrollments) {
            for (Exam ex : DataStore.getInstance().getExamsByCourse(enr.getCourseId())) {
                examsTableModel.addRow(new Object[]{
                        ex.getId(), ex.getCourseTitle(), ex.getTitle(), ex.getDurationMinutes() + " mins",
                        ex.getTotalMarks(), ex.getQuestions().size(), ex.getFacultyName(), ex.getStatus()
                });
            }
        }

        for (ExamResult r : DataStore.getInstance().getExamResultsByStudent(studentUser.getId())) {
            myResultsTableModel.addRow(new Object[]{
                    r.getId(), r.getExamTitle(), r.getCourseTitle(), r.getScore(),
                    r.getTotalMarks(), String.format("%.1f%%", r.getPercentage()),
                    r.getStatus().equals("PASSED") ? "🟢 Passed" : "🔴 Failed",
                    r.getAttemptDate()
            });
        }
    }

    // ── 6. Attendance & Academics (NEW) ────────────────────────────────────────
    private JPanel buildAttendanceCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Course Attendance & Academic Eligibility", "Inspect official session attendance and verify compliance with AICTE 75% mandatory criteria"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        // Top Summary Cards for Attendance
        JPanel sumPanel = new JPanel(new GridLayout(1, 3, 14, 0));
        sumPanel.setOpaque(false);
        sumPanel.setPreferredSize(new Dimension(0, 95));

        sumPanel.add(UITheme.createStatCard("DSA Attendance", "88.5%", "📅", UITheme.SUCCESS, "Eligible for End-Term"));
        sumPanel.add(UITheme.createStatCard("Java OOP Attendance", "91.0%", "📅", UITheme.SUCCESS, "Eligible for End-Term"));
        sumPanel.add(UITheme.createStatCard("AICTE Minimum Rule", "75.0%", "🏛️", UITheme.PRIMARY, "Mandatory National Rule"));
        center.add(sumPanel, BorderLayout.NORTH);

        // Attendance Log Table
        String[] cols = {"Record ID", "Course Title", "Lecture Date", "Topic / Session Details", "Attendance Status"};
        attendanceTableModel = new DefaultTableModel(cols, 0);
        attendanceTable = new ModernTable(attendanceTableModel);
        center.add(ModernTable.wrapInScrollPane(attendanceTable), BorderLayout.CENTER);

        loadAttendance();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadAttendance() {
        attendanceTableModel.setRowCount(0);
        List<AttendanceRecord> list = DataStore.getInstance().getAttendanceByStudent(studentUser.getId());
        for (AttendanceRecord r : list) {
            attendanceTableModel.addRow(new Object[]{
                    r.getId(),
                    r.getCourseTitle(),
                    r.getDate().toString(),
                    r.getTopic(),
                    r.isPresent() ? "🟢 Present" : "🔴 Absent"
            });
        }
    }

    // ── 7. Doubts & Mentorship Q&A ─────────────────────────────────────────────
    private JPanel buildDoubtsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Student Doubt & Faculty Resolution Center", "Direct academic inquiries to your course instructors with 48h SLA"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);
        ModernButton askBtn = new ModernButton(
                "Ask a New Doubt",
                new VectorIcon(VectorIcon.Type.DOUBTS, 14, Color.WHITE),
                ModernButton.Style.PRIMARY
        );
        askBtn.addActionListener(e -> showAskDoubtDialog());
        toolBar.add(askBtn);
        center.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"Doubt ID", "Course", "My Question", "Instructor Answer", "Status", "Date Asked"};
        doubtsTableModel = new DefaultTableModel(cols, 0);
        ModernTable table = new ModernTable(doubtsTableModel);
        center.add(ModernTable.wrapInScrollPane(table), BorderLayout.CENTER);

        loadDoubts();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadDoubts() {
        doubtsTableModel.setRowCount(0);
        for (Doubt d : DataStore.getInstance().getDoubtsByStudent(studentUser.getId())) {
            doubtsTableModel.addRow(new Object[]{
                    d.getId(), d.getCourseTitle(), d.getQuestion(),
                    d.getAnswer() != null ? d.getAnswer() : "Pending instructor response...",
                    d.getStatus() == Doubt.Status.RESOLVED ? "✅ Resolved" : "⏳ Pending",
                    d.getCreatedAt()
            });
        }
    }

    private void showAskDoubtDialog() {
        List<Enrollment> enrollments = DataStore.getInstance().getEnrollmentsByStudent(studentUser.getId());
        if (enrollments.isEmpty()) {
            Toast.show(this, "Please enroll in at least one course first.", Toast.Type.WARNING);
            return;
        }

        JDialog dlg = new JDialog(this, "Ask an Academic Doubt", true);
        dlg.setSize(500, 400);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new BorderLayout(10, 10));
        form.setBorder(new EmptyBorder(16, 20, 16, 20));

        JComboBox<Course> courseCombo = new JComboBox<>();
        for (Enrollment enr : enrollments) {
            Course c = DataStore.getInstance().getCourseById(enr.getCourseId());
            if (c != null) courseCombo.addItem(c);
        }

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setOpaque(false);
        JLabel cLbl = new JLabel("Select Course:");
        cLbl.setFont(UITheme.FONT_BODY_BOLD);
        top.add(cLbl, BorderLayout.NORTH);
        top.add(courseCombo, BorderLayout.CENTER);
        form.add(top, BorderLayout.NORTH);

        JTextArea qArea = new JTextArea(6, 20);
        qArea.setFont(UITheme.FONT_BODY);
        qArea.setLineWrap(true);
        qArea.setWrapStyleWord(true);

        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);
        JLabel qLbl = new JLabel("Question / Doubt Details:");
        qLbl.setFont(UITheme.FONT_BODY_BOLD);
        p.add(qLbl, BorderLayout.NORTH);
        p.add(new JScrollPane(qArea), BorderLayout.CENTER);
        form.add(p, BorderLayout.CENTER);

        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton sendBtn = new ModernButton("Send to Instructor", ModernButton.Style.PRIMARY);
        sendBtn.addActionListener(e -> {
            String qText = qArea.getText().trim();
            if (qText.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Question text cannot be empty.");
                return;
            }
            Course sel = (Course) courseCombo.getSelectedItem();
            if (sel != null) {
                DataStore.getInstance().addDoubt(sel.getId(), sel.getTitle(), studentUser.getId(), studentUser.getName(), qText);
                loadDoubts();
                refreshStats();
                dlg.dispose();
                Toast.show(this, "Your doubt has been forwarded to " + sel.getInstructorName() + "!", Toast.Type.SUCCESS);
            }
        });
        btm.add(sendBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 8. Notices & FAQs ──────────────────────────────────────────────────────
    private JPanel buildNoticesCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Official Campus Bulletins & University FAQs", "Important semester dates, placement circulars, and support guidance"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 14, 14));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 20, 22));

        // Notices
        RoundedPanel nSec = new RoundedPanel(16, Color.WHITE);
        nSec.setLayout(new BorderLayout(8, 8));
        nSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel nLbl = new JLabel("Campus Announcements & Notifications");
        nLbl.setFont(UITheme.FONT_SUBTITLE);
        nSec.add(nLbl, BorderLayout.NORTH);

        String[] nCols = {"Notice ID", "Title", "Category", "Published By", "Date", "Priority"};
        noticesTableModel = new DefaultTableModel(nCols, 0);
        ModernTable nTable = new ModernTable(noticesTableModel);
        nSec.add(ModernTable.wrapInScrollPane(nTable), BorderLayout.CENTER);
        center.add(nSec);

        // FAQs
        RoundedPanel fSec = new RoundedPanel(16, Color.WHITE);
        fSec.setLayout(new BorderLayout(8, 8));
        fSec.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel fLbl = new JLabel("Institutional Frequently Asked Questions (FAQs)");
        fLbl.setFont(UITheme.FONT_SUBTITLE);
        fSec.add(fLbl, BorderLayout.NORTH);

        String[] fCols = {"FAQ ID", "Question", "Category", "Detailed Guidance"};
        faqsTableModel = new DefaultTableModel(fCols, 0);
        ModernTable fTable = new ModernTable(faqsTableModel);
        fSec.add(ModernTable.wrapInScrollPane(fTable), BorderLayout.CENTER);
        center.add(fSec);

        loadNoticesAndFaqs();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadNoticesAndFaqs() {
        noticesTableModel.setRowCount(0);
        faqsTableModel.setRowCount(0);

        for (Notice n : DataStore.getInstance().getAllNotices()) {
            noticesTableModel.addRow(new Object[]{
                    n.getId(), n.getTitle(), n.getCategory(), n.getAuthor(), n.getCreatedAt(),
                    n.isPinned() ? "📌 Pinned Priority" : "Standard"
            });
        }

        for (FAQ f : DataStore.getInstance().getAllFAQs()) {
            faqsTableModel.addRow(new Object[]{
                    f.getId(), f.getQuestion(), f.getCategory(), f.getAnswer()
            });
        }
    }

    private void refreshAllData() {
        refreshStats();
        loadMyCourses();
        loadCatalog();
        loadAssignments();
        loadExamsAndResults();
        loadAttendance();
        loadDoubts();
        loadNoticesAndFaqs();
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}

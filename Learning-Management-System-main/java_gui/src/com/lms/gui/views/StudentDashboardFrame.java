package com.lms.gui.views;

import com.lms.gui.db.DataStore;
import com.lms.gui.model.*;
import com.lms.gui.theme.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboardFrame extends JFrame {
    private final User studentUser;
    private final CardLayout cardLayout;
    private final JPanel contentCards;

    private JPanel statsPanel;
    private DefaultTableModel catalogTableModel;
    private DefaultTableModel myCoursesTableModel;
    private DefaultTableModel lecturesTableModel;
    private DefaultTableModel assignmentsTableModel;
    private DefaultTableModel mySubmissionsTableModel;
    private DefaultTableModel examsTableModel;
    private DefaultTableModel myResultsTableModel;
    private DefaultTableModel doubtsTableModel;
    private DefaultTableModel noticesTableModel;
    private DefaultTableModel faqsTableModel;

    private JComboBox<Course> courseLecturesCombo;

    public StudentDashboardFrame(User studentUser) {
        super("EduPro LMS - Student Portal (" + studentUser.getName() + ")");
        this.studentUser = studentUser;

        setSize(1220, 800);
        setMinimumSize(new Dimension(1020, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.APP_BG);
        setLayout(new BorderLayout());

        // ── Sidebar ────────────────────────────────────────────────────────────
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(UITheme.SIDEBAR_BG);

        // Sidebar Top
        JPanel sideTop = new JPanel();
        sideTop.setLayout(new BoxLayout(sideTop, BoxLayout.Y_AXIS));
        sideTop.setOpaque(false);
        sideTop.setBorder(new EmptyBorder(24, 20, 20, 20));

        JLabel brandLbl = new JLabel("🎓 EduPro LMS");
        brandLbl.setFont(UITheme.FONT_TITLE);
        brandLbl.setForeground(Color.WHITE);

        JLabel roleBadge = new JLabel("  STUDENT PORTAL  ");
        roleBadge.setFont(UITheme.FONT_SMALL_BOLD);
        roleBadge.setOpaque(true);
        roleBadge.setBackground(UITheme.ACCENT);
        roleBadge.setForeground(Color.WHITE);
        roleBadge.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

        JLabel studentNameLbl = new JLabel(studentUser.getName());
        studentNameLbl.setFont(UITheme.FONT_BODY_BOLD);
        studentNameLbl.setForeground(Color.WHITE);

        JLabel studentDeptLbl = new JLabel(studentUser.getDepartment() != null ? studentUser.getDepartment() : "Undergraduate");
        studentDeptLbl.setFont(UITheme.FONT_SMALL);
        studentDeptLbl.setForeground(UITheme.SIDEBAR_TEXT);

        sideTop.add(brandLbl);
        sideTop.add(Box.createVerticalStrut(10));
        sideTop.add(roleBadge);
        sideTop.add(Box.createVerticalStrut(12));
        sideTop.add(studentNameLbl);
        sideTop.add(Box.createVerticalStrut(2));
        sideTop.add(studentDeptLbl);
        sidebar.add(sideTop, BorderLayout.NORTH);

        // Nav Buttons
        JPanel navPanel = new JPanel(new GridLayout(7, 1, 0, 6));
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(10, 16, 10, 16));

        navPanel.add(createNavButton("📊 Dashboard", "OVERVIEW"));
        navPanel.add(createNavButton("📖 Course Catalog", "CATALOG"));
        navPanel.add(createNavButton("🎥 My Learning & Videos", "LEARNING"));
        navPanel.add(createNavButton("📑 Assignments", "ASSIGNMENTS"));
        navPanel.add(createNavButton("🎯 Quizzes & Exams", "EXAMS"));
        navPanel.add(createNavButton("💡 Ask Doubts & Q&A", "DOUBTS"));
        navPanel.add(createNavButton("📰 Notices & FAQs", "NOTICES"));

        sidebar.add(navPanel, BorderLayout.CENTER);

        // Logout
        JPanel sideBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 16));
        sideBottom.setOpaque(false);
        ModernButton logoutBtn = new ModernButton("🚪 Log Out", ModernButton.Style.DANGER);
        logoutBtn.setPreferredSize(new Dimension(190, 40));
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
        contentCards.add(buildDoubtsCard(), "DOUBTS");
        contentCards.add(buildNoticesCard(), "NOTICES");

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
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
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

    // ── 1. Overview ────────────────────────────────────────────────────────────
    private JPanel buildOverviewCard() {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Student Academic Dashboard", "Track enrolled coursework, assignment deadlines, and exam test results"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(16, 16));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        statsPanel = new JPanel(new GridLayout(2, 3, 16, 16));
        statsPanel.setOpaque(false);
        refreshStats();
        center.add(statsPanel, BorderLayout.NORTH);

        // Enrolled Courses Quick Summary Table
        JPanel enrollSec = new JPanel(new BorderLayout(6, 6));
        enrollSec.setOpaque(false);
        JLabel eLbl = new JLabel("My Enrolled Courses & Study Progress");
        eLbl.setFont(UITheme.FONT_SUBTITLE);
        enrollSec.add(eLbl, BorderLayout.NORTH);

        String[] cols = {"Course ID", "Course Title", "Instructor", "Enrollment Date", "Completion Progress"};
        myCoursesTableModel = new DefaultTableModel(cols, 0);
        ModernTable myCoursesTable = new ModernTable(myCoursesTableModel);
        enrollSec.add(ModernTable.wrapInScrollPane(myCoursesTable), BorderLayout.CENTER);

        center.add(enrollSec, BorderLayout.CENTER);
        card.add(center, BorderLayout.CENTER);
        return card;
    }

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
        int noticeCount = ds.getAllNotices().size();

        statsPanel.add(UITheme.createStatCard("Enrolled Courses", String.valueOf(enrolledCount), "📚", UITheme.PRIMARY));
        statsPanel.add(UITheme.createStatCard("Average Progress", String.format("%.0f%%", avgProgress), "📈", UITheme.SUCCESS));
        statsPanel.add(UITheme.createStatCard("Graded Assignments", String.valueOf(gradedCount), "📑", UITheme.ACCENT));
        statsPanel.add(UITheme.createStatCard("Exams Completed", String.valueOf(examsTaken), "🎯", new Color(139, 92, 246)));
        statsPanel.add(UITheme.createStatCard("Doubts Inquired", String.valueOf(doubtsCount), "💡", UITheme.WARNING));
        statsPanel.add(UITheme.createStatCard("Campus Notices", String.valueOf(noticeCount), "📢", UITheme.DANGER));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private void loadMyCourses() {
        myCoursesTableModel.setRowCount(0);
        for (Enrollment e : DataStore.getInstance().getEnrollmentsByStudent(studentUser.getId())) {
            Course c = DataStore.getInstance().getCourseById(e.getCourseId());
            myCoursesTableModel.addRow(new Object[]{
                    e.getCourseId(),
                    e.getCourseTitle(),
                    c != null ? c.getInstructorName() : "Faculty",
                    e.getEnrollmentDate().toString(),
                    e.getProgress() + "% Completed"
            });
        }
    }

    // ── 2. Course Catalog & 1-Click Enrollment ──────────────────────────────────
    private JPanel buildCatalogCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Course Catalog & University Curriculum", "Discover subjects, check instructor details, and enroll instantly"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);
        ModernButton enrollBtn = new ModernButton("🎓 Enroll in Selected Course", ModernButton.Style.SUCCESS);
        toolBar.add(enrollBtn);
        center.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"Course ID", "Course Title", "Category", "Faculty Instructor", "Enrolled Students", "Status for You", "Overview"};
        catalogTableModel = new DefaultTableModel(cols, 0);
        ModernTable table = new ModernTable(catalogTableModel);
        center.add(ModernTable.wrapInScrollPane(table), BorderLayout.CENTER);

        enrollBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a course from the catalog to enroll.");
                return;
            }
            int courseId = (int) catalogTableModel.getValueAt(row, 0);
            Course c = DataStore.getInstance().getCourseById(courseId);
            if (c != null) {
                try {
                    DataStore.getInstance().enrollStudent(studentUser.getId(), studentUser.getName(), c.getId(), c.getTitle());
                    loadCatalog();
                    refreshStats();
                    JOptionPane.showMessageDialog(this, "🎉 Congratulations! You have successfully enrolled in:\n" + c.getTitle());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Enrollment Notice", JOptionPane.INFORMATION_MESSAGE);
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
                    c.getEnrolledStudentsCount(),
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

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Selector Bar
        JPanel selectBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        selectBar.setOpaque(false);
        JLabel cLbl = new JLabel("Select Enrolled Course:");
        cLbl.setFont(UITheme.FONT_BODY_BOLD);

        courseLecturesCombo = new JComboBox<>();
        courseLecturesCombo.setFont(UITheme.FONT_BODY);
        courseLecturesCombo.setPreferredSize(new Dimension(320, 36));
        courseLecturesCombo.addActionListener(e -> updateLecturesForSelectedCourse());

        ModernButton playBtn = new ModernButton("▶ Watch Lecture & Study Notes", ModernButton.Style.PRIMARY);

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
                JOptionPane.showMessageDialog(this, "Select a lecture module to open.");
                return;
            }
            int lecId = (int) lecturesTableModel.getValueAt(row, 0);
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
        dlg.setSize(680, 560);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        // Simulated Video Screen
        JPanel videoScreen = new JPanel(new BorderLayout());
        videoScreen.setBackground(new Color(15, 23, 42));
        videoScreen.setPreferredSize(new Dimension(680, 240));

        JLabel playIcon = new JLabel("🎬 " + v.getTitle(), SwingConstants.CENTER);
        playIcon.setFont(new Font("Segoe UI", Font.BOLD, 20));
        playIcon.setForeground(Color.WHITE);
        videoScreen.add(playIcon, BorderLayout.CENTER);

        // Video Player Scrubber & Controls
        JPanel ctrlBar = new JPanel(new BorderLayout(10, 0));
        ctrlBar.setOpaque(false);
        ctrlBar.setBorder(new EmptyBorder(8, 16, 8, 16));

        JSlider scrubber = new JSlider(0, 100, 35);
        scrubber.setOpaque(false);

        JLabel durLbl = new JLabel("15:20 / " + v.getDuration());
        durLbl.setForeground(Color.WHITE);
        durLbl.setFont(UITheme.FONT_SMALL_BOLD);

        ctrlBar.add(scrubber, BorderLayout.CENTER);
        ctrlBar.add(durLbl, BorderLayout.EAST);
        videoScreen.add(ctrlBar, BorderLayout.SOUTH);

        dlg.add(videoScreen, BorderLayout.NORTH);

        // Notes and Description
        JPanel notesSec = new JPanel(new BorderLayout(10, 10));
        notesSec.setBorder(new EmptyBorder(16, 20, 16, 20));

        JTextArea notesArea = new JTextArea("Syllabus & Lecture Notes:\n\n" + v.getNotesText() + "\n\nFile Resource: " + v.getFileName() + "\nUpload Date: " + v.getUploadDate());
        notesArea.setFont(UITheme.FONT_BODY);
        notesArea.setEditable(false);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesSec.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        dlg.add(notesSec, BorderLayout.CENTER);

        // Bottom: Complete Lesson Button
        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        ModernButton markDoneBtn = new ModernButton("✅ Mark Lesson as Completed (+20% Progress)", ModernButton.Style.SUCCESS);
        markDoneBtn.addActionListener(e -> {
            Enrollment enr = DataStore.getInstance().getEnrollmentsByStudent(studentUser.getId()).stream()
                    .filter(x -> x.getCourseId() == v.getCourseId()).findFirst().orElse(null);
            if (enr != null) {
                int newProg = Math.min(100, enr.getProgress() + 20);
                DataStore.getInstance().updateProgress(studentUser.getId(), v.getCourseId(), newProg);
                refreshStats();
                loadMyCourses();
                JOptionPane.showMessageDialog(dlg, "Progress updated to " + newProg + "% for " + v.getCourseTitle() + "!");
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
        card.add(UITheme.createHeader("Course Assignments & Submissions", "Complete homework exercises, upload answers, and view teacher grades"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 15, 15));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Available Assignments
        JPanel aSec = new JPanel(new BorderLayout(6, 6));
        aSec.setOpaque(false);
        JPanel aTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        aTop.setOpaque(false);
        JLabel aLbl = new JLabel("Open Course Assignments");
        aLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton submitBtn = new ModernButton("📤 Submit Solution", ModernButton.Style.PRIMARY);
        aTop.add(aLbl);
        aTop.add(submitBtn);
        aSec.add(aTop, BorderLayout.NORTH);

        String[] aCols = {"ID", "Course", "Title", "Due Date", "Max Marks", "Instructor", "Problem Statement"};
        assignmentsTableModel = new DefaultTableModel(aCols, 0);
        ModernTable aTable = new ModernTable(assignmentsTableModel);
        aSec.add(ModernTable.wrapInScrollPane(aTable), BorderLayout.CENTER);
        center.add(aSec);

        // My Submissions
        JPanel sSec = new JPanel(new BorderLayout(6, 6));
        sSec.setOpaque(false);
        JLabel sLbl = new JLabel("My Submission Record & Graded Feedback");
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
                JOptionPane.showMessageDialog(this, "Select an assignment to submit.");
                return;
            }
            int assignId = (int) assignmentsTableModel.getValueAt(row, 0);
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
        dlg.setSize(540, 480);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel taskLbl = new JLabel("<html><b>Task Details:</b><br/>" + a.getDescription() + "</html>");
        taskLbl.setFont(UITheme.FONT_BODY);
        center.add(taskLbl, BorderLayout.NORTH);

        JTextArea ansArea = new JTextArea(8, 20);
        ansArea.setFont(UITheme.FONT_BODY);
        ansArea.setLineWrap(true);
        ansArea.setWrapStyleWord(true);
        center.add(createFieldWrapper("Write / Paste your solution or code below:", new JScrollPane(ansArea)), BorderLayout.CENTER);

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
            JOptionPane.showMessageDialog(this, "Assignment submitted successfully for review!");
        });
        btm.add(sendBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 5. Quizzes & Exams ─────────────────────────────────────────────────────
    private JPanel buildExamsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Timed Online Quizzes & Midterms", "Test your knowledge with auto-graded assessments and inspect score reports"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 15, 15));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Available Exams
        JPanel eSec = new JPanel(new BorderLayout(6, 6));
        eSec.setOpaque(false);
        JPanel eTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        eTop.setOpaque(false);
        JLabel eLbl = new JLabel("Available Course Exams & Quizzes");
        eLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton startBtn = new ModernButton("🚀 Start Timed Exam Session", ModernButton.Style.SUCCESS);
        eTop.add(eLbl);
        eTop.add(startBtn);
        eSec.add(eTop, BorderLayout.NORTH);

        String[] eCols = {"Exam ID", "Course", "Title", "Duration", "Max Marks", "Questions", "Faculty", "Status"};
        examsTableModel = new DefaultTableModel(eCols, 0);
        ModernTable eTable = new ModernTable(examsTableModel);
        eSec.add(ModernTable.wrapInScrollPane(eTable), BorderLayout.CENTER);
        center.add(eSec);

        // My Results
        JPanel rSec = new JPanel(new BorderLayout(6, 6));
        rSec.setOpaque(false);
        JLabel rLbl = new JLabel("My Official Exam Transcripts");
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
                JOptionPane.showMessageDialog(this, "Select an exam to start.");
                return;
            }
            int examId = (int) examsTableModel.getValueAt(row, 0);
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

    // ── 6. Doubts & Q&A ────────────────────────────────────────────────────────
    private JPanel buildDoubtsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Student Doubt & Faculty Resolution Center", "Direct academic inquiries to your course instructors"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);
        ModernButton askBtn = new ModernButton("❓ Ask a New Doubt", ModernButton.Style.PRIMARY);
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
            JOptionPane.showMessageDialog(this, "Please enroll in at least one course first.");
            return;
        }

        JDialog dlg = new JDialog(this, "Ask an Academic Doubt", true);
        dlg.setSize(480, 380);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new BorderLayout(10, 10));
        form.setBorder(new EmptyBorder(16, 20, 16, 20));

        JComboBox<String> courseCombo = new JComboBox<>();
        for (Enrollment e : enrollments) courseCombo.addItem(e.getCourseId() + ": " + e.getCourseTitle());
        form.add(createFieldWrapper("Choose Course:", courseCombo), BorderLayout.NORTH);

        JTextArea qArea = new JTextArea(6, 20);
        qArea.setFont(UITheme.FONT_BODY);
        qArea.setLineWrap(true);
        qArea.setWrapStyleWord(true);
        form.add(createFieldWrapper("Describe your doubt or question in detail:", new JScrollPane(qArea)), BorderLayout.CENTER);

        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton sendBtn = new ModernButton("Send to Instructor", ModernButton.Style.PRIMARY);
        sendBtn.addActionListener(e -> {
            String qText = qArea.getText().trim();
            if (qText.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Please type your question.");
                return;
            }
            String sel = (String) courseCombo.getSelectedItem();
            if (sel != null) {
                int cId = Integer.parseInt(sel.split(":")[0]);
                Course c = DataStore.getInstance().getCourseById(cId);
                DataStore.getInstance().addDoubt(cId, c != null ? c.getTitle() : "Course", studentUser.getId(), studentUser.getName(), qText);
                loadDoubts();
                refreshStats();
                dlg.dispose();
                JOptionPane.showMessageDialog(this, "Your doubt has been submitted to your course faculty!");
            }
        });
        btm.add(sendBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 7. Notices & FAQs ──────────────────────────────────────────────────────
    private JPanel buildNoticesCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Campus Notices & Student Helpdesk", "University announcements, examination alerts, and frequently asked questions"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 15, 15));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Notices
        JPanel nSec = new JPanel(new BorderLayout(6, 6));
        nSec.setOpaque(false);
        JLabel nLbl = new JLabel("Official University Bulletins");
        nLbl.setFont(UITheme.FONT_SUBTITLE);
        nSec.add(nLbl, BorderLayout.NORTH);

        String[] nCols = {"Notice ID", "Title", "Category", "Published Date", "Priority", "Content"};
        noticesTableModel = new DefaultTableModel(nCols, 0);
        ModernTable nTable = new ModernTable(noticesTableModel);
        nSec.add(ModernTable.wrapInScrollPane(nTable), BorderLayout.CENTER);
        center.add(nSec);

        // FAQs
        JPanel fSec = new JPanel(new BorderLayout(6, 6));
        fSec.setOpaque(false);
        JLabel fLbl = new JLabel("Frequently Asked Questions");
        fLbl.setFont(UITheme.FONT_SUBTITLE);
        fSec.add(fLbl, BorderLayout.NORTH);

        String[] fCols = {"FAQ ID", "Topic", "Question", "Answer"};
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
                    n.getId(), n.getTitle(), n.getCategory(), n.getPostDate().toString(),
                    n.isPinned() ? "📌 Pinned" : "Standard", n.getContent()
            });
        }

        for (FAQ f : DataStore.getInstance().getAllFAQs()) {
            faqsTableModel.addRow(new Object[]{
                    f.getId(), f.getCategory(), f.getQuestion(), f.getAnswer()
            });
        }
    }

    private void refreshAllData() {
        refreshStats();
        loadMyCourses();
        loadCatalog();
        loadEnrolledCoursesCombo();
        loadAssignments();
        loadExamsAndResults();
        loadDoubts();
        loadNoticesAndFaqs();
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
        int conf = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out of the Student Portal?", "Log Out", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}

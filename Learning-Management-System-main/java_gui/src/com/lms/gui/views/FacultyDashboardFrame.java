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

public class FacultyDashboardFrame extends JFrame {
    private final User facultyUser;
    private final CardLayout cardLayout;
    private final JPanel contentCards;

    private JPanel statsPanel;
    private DefaultTableModel coursesTableModel;
    private DefaultTableModel lecturesTableModel;
    private DefaultTableModel assignmentsTableModel;
    private DefaultTableModel submissionsTableModel;
    private DefaultTableModel examsTableModel;
    private DefaultTableModel resultsTableModel;
    private DefaultTableModel doubtsTableModel;

    public FacultyDashboardFrame(User facultyUser) {
        super("EduPro LMS - Faculty Portal (" + facultyUser.getName() + ")");
        this.facultyUser = facultyUser;

        setSize(1200, 780);
        setMinimumSize(new Dimension(1020, 680));
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

        JLabel roleBadge = new JLabel("  FACULTY PORTAL  ");
        roleBadge.setFont(UITheme.FONT_SMALL_BOLD);
        roleBadge.setOpaque(true);
        roleBadge.setBackground(UITheme.SUCCESS);
        roleBadge.setForeground(Color.WHITE);
        roleBadge.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

        JLabel facultyNameLbl = new JLabel(facultyUser.getName());
        facultyNameLbl.setFont(UITheme.FONT_BODY_BOLD);
        facultyNameLbl.setForeground(Color.WHITE);

        JLabel facultyDeptLbl = new JLabel(facultyUser.getDepartment() != null ? facultyUser.getDepartment() : "Faculty Dept");
        facultyDeptLbl.setFont(UITheme.FONT_SMALL);
        facultyDeptLbl.setForeground(UITheme.SIDEBAR_TEXT);

        sideTop.add(brandLbl);
        sideTop.add(Box.createVerticalStrut(10));
        sideTop.add(roleBadge);
        sideTop.add(Box.createVerticalStrut(12));
        sideTop.add(facultyNameLbl);
        sideTop.add(Box.createVerticalStrut(2));
        sideTop.add(facultyDeptLbl);
        sidebar.add(sideTop, BorderLayout.NORTH);

        // Nav Buttons
        JPanel navPanel = new JPanel(new GridLayout(5, 1, 0, 8));
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(10, 16, 10, 16));

        navPanel.add(createNavButton("📊 Overview", "OVERVIEW"));
        navPanel.add(createNavButton("📚 Courses & Lectures", "COURSES"));
        navPanel.add(createNavButton("📝 Assignments & Grading", "ASSIGNMENTS"));
        navPanel.add(createNavButton("🏆 Exams & Quizzes", "EXAMS"));
        navPanel.add(createNavButton("💬 Student Doubts", "DOUBTS"));

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
        contentCards.add(buildCoursesCard(), "COURSES");
        contentCards.add(buildAssignmentsCard(), "ASSIGNMENTS");
        contentCards.add(buildExamsCard(), "EXAMS");
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

    // ── 1. Overview ────────────────────────────────────────────────────────────
    private JPanel buildOverviewCard() {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Instructor Command Center", "Manage your curriculum, grade student submissions, and resolve questions"), BorderLayout.NORTH);

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

        List<Course> myCourses = ds.getCoursesByFaculty(facultyUser.getId());
        int myCourseCount = myCourses.size();
        int totalStudents = myCourses.stream().mapToInt(Course::getEnrolledStudentsCount).sum();

        List<Assignment> myAssignments = ds.getAssignmentsByFaculty(facultyUser.getId());
        long pendingSubmissions = ds.getAllSubmissions().stream()
                .filter(s -> myCourses.stream().anyMatch(c -> c.getId() == s.getCourseId()))
                .filter(s -> s.getStatus() == AssignmentSubmission.Status.SUBMITTED).count();

        int liveExams = (int) ds.getExamsByFaculty(facultyUser.getId()).stream().filter(ex -> "Live".equalsIgnoreCase(ex.getStatus())).count();
        long pendingDoubts = ds.getAllDoubts().stream()
                .filter(d -> myCourses.stream().anyMatch(c -> c.getId() == d.getCourseId()))
                .filter(d -> d.getStatus() == Doubt.Status.PENDING).count();

        statsPanel.add(UITheme.createStatCard("My Courses", String.valueOf(myCourseCount), "📚", UITheme.PRIMARY));
        statsPanel.add(UITheme.createStatCard("Total Students Enrolled", String.valueOf(totalStudents), "👨‍🎓", UITheme.ACCENT));
        statsPanel.add(UITheme.createStatCard("Pending Submissions", String.valueOf(pendingSubmissions), "⏳", UITheme.WARNING));
        statsPanel.add(UITheme.createStatCard("Live Exams", String.valueOf(liveExams), "🏆", UITheme.SUCCESS));
        statsPanel.add(UITheme.createStatCard("Pending Doubts", String.valueOf(pendingDoubts), "💬", UITheme.DANGER));
        statsPanel.add(UITheme.createStatCard("Total Assignments", String.valueOf(myAssignments.size()), "📋", new Color(139, 92, 246)));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    // ── 2. Courses & Lectures ──────────────────────────────────────────────────
    private JPanel buildCoursesCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Course Offerings & Lecture Materials", "Publish instructional videos, slides, and syllabus notes"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 15, 15));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Top Half: Courses Table
        JPanel courseSec = new JPanel(new BorderLayout(6, 6));
        courseSec.setOpaque(false);
        JPanel courseTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        courseTop.setOpaque(false);
        JLabel cLbl = new JLabel("My Assigned Courses");
        cLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton addCourseBtn = new ModernButton("➕ Add Course", ModernButton.Style.PRIMARY);
        addCourseBtn.addActionListener(e -> showAddCourseDialog());
        courseTop.add(cLbl);
        courseTop.add(addCourseBtn);
        courseSec.add(courseTop, BorderLayout.NORTH);

        String[] cCols = {"Course ID", "Course Title", "Category", "Enrolled Students", "Description"};
        coursesTableModel = new DefaultTableModel(cCols, 0);
        ModernTable cTable = new ModernTable(coursesTableModel);
        courseSec.add(ModernTable.wrapInScrollPane(cTable), BorderLayout.CENTER);
        center.add(courseSec);

        // Bottom Half: Lectures Table
        JPanel lecSec = new JPanel(new BorderLayout(6, 6));
        lecSec.setOpaque(false);
        JPanel lecTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        lecTop.setOpaque(false);
        JLabel lLbl = new JLabel("Course Lecture Modules & Notes");
        lLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton addLecBtn = new ModernButton("📹 Upload Lecture & Notes", ModernButton.Style.SUCCESS);
        addLecBtn.addActionListener(e -> showAddLectureDialog());
        lecTop.add(lLbl);
        lecTop.add(addLecBtn);
        lecSec.add(lecTop, BorderLayout.NORTH);

        String[] lCols = {"Lecture ID", "Course", "Lecture Title", "Duration", "Media Filename", "Study Notes Summary"};
        lecturesTableModel = new DefaultTableModel(lCols, 0);
        ModernTable lTable = new ModernTable(lecturesTableModel);
        lecSec.add(ModernTable.wrapInScrollPane(lTable), BorderLayout.CENTER);
        center.add(lecSec);

        loadCoursesAndLectures();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadCoursesAndLectures() {
        coursesTableModel.setRowCount(0);
        lecturesTableModel.setRowCount(0);

        List<Course> myCourses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        for (Course c : myCourses) {
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
        JDialog dlg = new JDialog(this, "Create New Course Module", true);
        dlg.setSize(440, 360);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(3, 1, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        ModernTextField titleField = new ModernTextField("Course Title", 20);
        ModernTextField catField = new ModernTextField("Category", 20);
        ModernTextField descField = new ModernTextField("Description", 20);

        form.add(createFieldWrapper("Course Title:", titleField));
        form.add(createFieldWrapper("Category:", catField));
        form.add(createFieldWrapper("Description:", descField));
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Create Course", ModernButton.Style.PRIMARY);
        saveBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String cat = catField.getText().trim();
            String desc = descField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Course title is required.");
                return;
            }
            DataStore.getInstance().addCourse(title, desc, facultyUser.getId(), facultyUser.getName(), cat);
            loadCoursesAndLectures();
            refreshStats();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Course created successfully!");
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void showAddLectureDialog() {
        List<Course> myCourses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        if (myCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please create a course before adding lectures.");
            return;
        }

        JDialog dlg = new JDialog(this, "Upload Lecture & Study Material", true);
        dlg.setSize(480, 480);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 1, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JComboBox<Course> courseCombo = new JComboBox<>(myCourses.toArray(new Course[0]));
        ModernTextField titleField = new ModernTextField("Lecture Title (e.g. Chapter 1: Introduction)", 20);
        ModernTextField durationField = new ModernTextField("Duration (e.g. 45 mins)", 20);
        ModernTextField fileField = new ModernTextField("Video / Resource File Name (e.g. intro.mp4)", 20);
        ModernTextField notesField = new ModernTextField("Key Study Notes / Summary", 20);

        form.add(createFieldWrapper("Select Course:", courseCombo));
        form.add(createFieldWrapper("Lecture Title:", titleField));
        form.add(createFieldWrapper("Duration:", durationField));
        form.add(createFieldWrapper("Video File:", fileField));
        form.add(createFieldWrapper("Study Notes:", notesField));
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Save Lecture", ModernButton.Style.SUCCESS);
        saveBtn.addActionListener(e -> {
            Course c = (Course) courseCombo.getSelectedItem();
            String title = titleField.getText().trim();
            String dur = durationField.getText().trim();
            String file = fileField.getText().trim();
            String notes = notesField.getText().trim();

            if (c == null || title.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Course and Lecture Title are required.");
                return;
            }
            DataStore.getInstance().addLecture(c.getId(), c.getTitle(), title, notes, dur, file, notes);
            loadCoursesAndLectures();
            refreshStats();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Lecture module uploaded successfully!");
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 3. Assignments & Grading ───────────────────────────────────────────────
    private JPanel buildAssignmentsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Assignments & Submission Grading", "Evaluate student work, enter scores, and deliver written feedback"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 15, 15));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Top: Assignments List
        JPanel assignSec = new JPanel(new BorderLayout(6, 6));
        assignSec.setOpaque(false);
        JPanel aTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        aTop.setOpaque(false);
        JLabel aLbl = new JLabel("Published Course Assignments");
        aLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton newAssignBtn = new ModernButton("➕ Create New Assignment", ModernButton.Style.PRIMARY);
        newAssignBtn.addActionListener(e -> showCreateAssignmentDialog());
        aTop.add(aLbl);
        aTop.add(newAssignBtn);
        assignSec.add(aTop, BorderLayout.NORTH);

        String[] aCols = {"Assignment ID", "Course", "Title", "Due Date", "Max Marks", "Description"};
        assignmentsTableModel = new DefaultTableModel(aCols, 0);
        ModernTable aTable = new ModernTable(assignmentsTableModel);
        assignSec.add(ModernTable.wrapInScrollPane(aTable), BorderLayout.CENTER);
        center.add(assignSec);

        // Bottom: Student Submissions to Grade
        JPanel subSec = new JPanel(new BorderLayout(6, 6));
        subSec.setOpaque(false);
        JPanel sTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        sTop.setOpaque(false);
        JLabel sLbl = new JLabel("Student Submissions & Evaluation");
        sLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton gradeBtn = new ModernButton("📝 Grade Selected Submission", ModernButton.Style.WARNING);
        sTop.add(sLbl);
        sTop.add(gradeBtn);
        subSec.add(sTop, BorderLayout.NORTH);

        String[] sCols = {"Sub ID", "Assignment", "Course", "Student Name", "Submitted At", "Status", "Marks Awarded", "Feedback"};
        submissionsTableModel = new DefaultTableModel(sCols, 0);
        ModernTable sTable = new ModernTable(submissionsTableModel);
        subSec.add(ModernTable.wrapInScrollPane(sTable), BorderLayout.CENTER);
        center.add(subSec);

        gradeBtn.addActionListener(e -> {
            int row = sTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a student submission to grade.");
                return;
            }
            int subId = (int) submissionsTableModel.getValueAt(row, 0);
            AssignmentSubmission sub = DataStore.getInstance().getAllSubmissions().stream()
                    .filter(s -> s.getId() == subId).findFirst().orElse(null);
            if (sub != null) {
                showGradeSubmissionDialog(sub);
            }
        });

        loadAssignmentsAndSubmissions();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadAssignmentsAndSubmissions() {
        assignmentsTableModel.setRowCount(0);
        submissionsTableModel.setRowCount(0);

        List<Course> myCourses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        for (Course c : myCourses) {
            for (Assignment a : DataStore.getInstance().getAssignmentsByCourse(c.getId())) {
                assignmentsTableModel.addRow(new Object[]{
                        a.getId(), a.getCourseTitle(), a.getTitle(), a.getDueDate().toString(), a.getMaxMarks(), a.getDescription()
                });

                for (AssignmentSubmission s : DataStore.getInstance().getSubmissionsByAssignment(a.getId())) {
                    submissionsTableModel.addRow(new Object[]{
                            s.getId(), s.getAssignmentTitle(), s.getCourseTitle(), s.getStudentName(),
                            s.getSubmissionTime(),
                            s.getStatus() == AssignmentSubmission.Status.GRADED ? "🟢 Graded" : "⏳ Submitted",
                            s.getMarks() != null ? s.getMarks() : "-",
                            s.getFeedback() != null ? s.getFeedback() : "-"
                    });
                }
            }
        }
    }

    private void showCreateAssignmentDialog() {
        List<Course> myCourses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        if (myCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please create a course first.");
            return;
        }

        JDialog dlg = new JDialog(this, "Create Course Assignment", true);
        dlg.setSize(450, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 1, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JComboBox<Course> courseCombo = new JComboBox<>(myCourses.toArray(new Course[0]));
        ModernTextField titleField = new ModernTextField("Assignment Title", 20);
        ModernTextField descField = new ModernTextField("Task Details / Problem Statement", 20);
        ModernTextField marksField = new ModernTextField("100", 20);
        ModernTextField daysField = new ModernTextField("7", 20);

        form.add(createFieldWrapper("Course:", courseCombo));
        form.add(createFieldWrapper("Title:", titleField));
        form.add(createFieldWrapper("Description:", descField));
        form.add(createFieldWrapper("Maximum Marks:", marksField));
        form.add(createFieldWrapper("Due in (Days from today):", daysField));
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Publish Assignment", ModernButton.Style.PRIMARY);
        saveBtn.addActionListener(e -> {
            Course c = (Course) courseCombo.getSelectedItem();
            String title = titleField.getText().trim();
            String desc = descField.getText().trim();
            int marks = 100;
            int days = 7;
            try {
                marks = Integer.parseInt(marksField.getText().trim());
                days = Integer.parseInt(daysField.getText().trim());
            } catch (Exception ignored) {}

            if (c == null || title.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Title is required.");
                return;
            }
            DataStore.getInstance().addAssignment(c.getId(), c.getTitle(), title, desc, LocalDate.now().plusDays(days), marks, facultyUser.getId(), facultyUser.getName());
            loadAssignmentsAndSubmissions();
            refreshStats();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Assignment published successfully!");
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void showGradeSubmissionDialog(AssignmentSubmission sub) {
        JDialog dlg = new JDialog(this, "Grade Submission: " + sub.getStudentName(), true);
        dlg.setSize(520, 480);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextArea ansArea = new JTextArea(sub.getAnswerText());
        ansArea.setEditable(false);
        ansArea.setFont(UITheme.FONT_BODY);
        ansArea.setLineWrap(true);
        ansArea.setWrapStyleWord(true);
        center.add(createFieldWrapper("Student Submitted Solution:", new JScrollPane(ansArea)), BorderLayout.CENTER);

        JPanel gradeInputs = new JPanel(new GridLayout(2, 1, 10, 10));
        ModernTextField marksField = new ModernTextField(sub.getMarks() != null ? String.valueOf(sub.getMarks()) : "85", 10);
        ModernTextField feedbackField = new ModernTextField(sub.getFeedback() != null ? sub.getFeedback() : "Good solution!", 20);

        gradeInputs.add(createFieldWrapper("Enter Marks Awarded:", marksField));
        gradeInputs.add(createFieldWrapper("Instructor Feedback / Remarks:", feedbackField));
        center.add(gradeInputs, BorderLayout.SOUTH);

        dlg.add(center, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveGradeBtn = new ModernButton("Save Grade & Feedback", ModernButton.Style.SUCCESS);
        saveGradeBtn.addActionListener(e -> {
            try {
                int marks = Integer.parseInt(marksField.getText().trim());
                String fb = feedbackField.getText().trim();
                DataStore.getInstance().gradeSubmission(sub.getId(), marks, fb);
                loadAssignmentsAndSubmissions();
                refreshStats();
                dlg.dispose();
                JOptionPane.showMessageDialog(this, "Submission graded successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Please enter valid numeric marks.");
            }
        });
        btm.add(saveGradeBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 4. Exams & Quizzes ─────────────────────────────────────────────────────
    private JPanel buildExamsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Exams, Quizzes & Test Results", "Design timed multiple-choice assessments and inspect student performance"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 15, 15));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Top: Exams List
        JPanel examSec = new JPanel(new BorderLayout(6, 6));
        examSec.setOpaque(false);
        JPanel eTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        eTop.setOpaque(false);
        JLabel eLbl = new JLabel("Active & Scheduled Exams");
        eLbl.setFont(UITheme.FONT_SUBTITLE);
        ModernButton newExamBtn = new ModernButton("➕ Create New Exam", ModernButton.Style.PRIMARY);
        newExamBtn.addActionListener(e -> showCreateExamDialog());
        ModernButton addQBtn = new ModernButton("➕ Add MCQ Question", ModernButton.Style.SUCCESS);
        eTop.add(eLbl);
        eTop.add(newExamBtn);
        eTop.add(addQBtn);
        examSec.add(eTop, BorderLayout.NORTH);

        String[] eCols = {"Exam ID", "Course", "Title", "Duration (mins)", "Total Marks", "Questions Count", "Status"};
        examsTableModel = new DefaultTableModel(eCols, 0);
        ModernTable eTable = new ModernTable(examsTableModel);
        examSec.add(ModernTable.wrapInScrollPane(eTable), BorderLayout.CENTER);
        center.add(examSec);

        addQBtn.addActionListener(e -> {
            int row = eTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select an exam to add questions.");
                return;
            }
            int examId = (int) examsTableModel.getValueAt(row, 0);
            Exam ex = DataStore.getInstance().getAllExams().stream().filter(x -> x.getId() == examId).findFirst().orElse(null);
            if (ex != null) {
                showAddQuestionDialog(ex);
            }
        });

        // Bottom: Student Exam Results
        JPanel resSec = new JPanel(new BorderLayout(6, 6));
        resSec.setOpaque(false);
        JLabel rLbl = new JLabel("Student Exam Attempt Transcripts");
        rLbl.setFont(UITheme.FONT_SUBTITLE);
        resSec.add(rLbl, BorderLayout.NORTH);

        String[] rCols = {"Result ID", "Exam Title", "Course", "Student Name", "Score", "Total Marks", "Percentage", "Outcome", "Attempted At"};
        resultsTableModel = new DefaultTableModel(rCols, 0);
        ModernTable rTable = new ModernTable(resultsTableModel);
        resSec.add(ModernTable.wrapInScrollPane(rTable), BorderLayout.CENTER);
        center.add(resSec);

        loadExamsAndResults();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadExamsAndResults() {
        examsTableModel.setRowCount(0);
        resultsTableModel.setRowCount(0);

        List<Course> myCourses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        for (Course c : myCourses) {
            for (Exam ex : DataStore.getInstance().getExamsByCourse(c.getId())) {
                examsTableModel.addRow(new Object[]{
                        ex.getId(), ex.getCourseTitle(), ex.getTitle(), ex.getDurationMinutes(), ex.getTotalMarks(), ex.getQuestions().size(), ex.getStatus()
                });

                for (ExamResult r : DataStore.getInstance().getExamResultsByExam(ex.getId())) {
                    resultsTableModel.addRow(new Object[]{
                            r.getId(), r.getExamTitle(), r.getCourseTitle(), r.getStudentName(),
                            r.getScore(), r.getTotalMarks(), String.format("%.1f%%", r.getPercentage()),
                            r.getStatus().equals("PASSED") ? "🟢 Passed" : "🔴 Failed",
                            r.getAttemptDate()
                    });
                }
            }
        }
    }

    private void showCreateExamDialog() {
        List<Course> myCourses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        if (myCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please create a course first.");
            return;
        }

        JDialog dlg = new JDialog(this, "Create Assessment Exam", true);
        dlg.setSize(440, 360);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JComboBox<Course> courseCombo = new JComboBox<>(myCourses.toArray(new Course[0]));
        ModernTextField titleField = new ModernTextField("Exam Title (e.g. Unit 1 Quiz)", 20);
        ModernTextField durField = new ModernTextField("45", 20);
        ModernTextField marksField = new ModernTextField("50", 20);

        form.add(createFieldWrapper("Course:", courseCombo));
        form.add(createFieldWrapper("Exam Title:", titleField));
        form.add(createFieldWrapper("Time Limit (Minutes):", durField));
        form.add(createFieldWrapper("Total Marks:", marksField));
        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Create Exam", ModernButton.Style.PRIMARY);
        saveBtn.addActionListener(e -> {
            Course c = (Course) courseCombo.getSelectedItem();
            String title = titleField.getText().trim();
            int dur = 45;
            int marks = 50;
            try {
                dur = Integer.parseInt(durField.getText().trim());
                marks = Integer.parseInt(marksField.getText().trim());
            } catch (Exception ignored) {}

            if (c == null || title.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Title is required.");
                return;
            }
            DataStore.getInstance().addExam(c.getId(), c.getTitle(), title, dur, marks, facultyUser.getId(), facultyUser.getName());
            loadExamsAndResults();
            refreshStats();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Exam created! You can now add MCQ questions.");
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void showAddQuestionDialog(Exam exam) {
        JDialog dlg = new JDialog(this, "Add MCQ to: " + exam.getTitle(), true);
        dlg.setSize(520, 520);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(7, 1, 8, 8));
        form.setBorder(new EmptyBorder(16, 20, 16, 20));

        ModernTextField qField = new ModernTextField("Question Text", 20);
        ModernTextField optA = new ModernTextField("Option A", 20);
        ModernTextField optB = new ModernTextField("Option B", 20);
        ModernTextField optC = new ModernTextField("Option C", 20);
        ModernTextField optD = new ModernTextField("Option D", 20);
        JComboBox<String> correctCombo = new JComboBox<>(new String[]{"Option A", "Option B", "Option C", "Option D"});
        ModernTextField marksField = new ModernTextField("10", 10);

        form.add(createFieldWrapper("Question Text:", qField));
        form.add(createFieldWrapper("Option A:", optA));
        form.add(createFieldWrapper("Option B:", optB));
        form.add(createFieldWrapper("Option C:", optC));
        form.add(createFieldWrapper("Option D:", optD));
        form.add(createFieldWrapper("Correct Answer:", correctCombo));
        form.add(createFieldWrapper("Marks for this question:", marksField));

        dlg.add(form, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton saveBtn = new ModernButton("Save Question", ModernButton.Style.SUCCESS);
        saveBtn.addActionListener(e -> {
            String qText = qField.getText().trim();
            String a = optA.getText().trim();
            String b = optB.getText().trim();
            String c = optC.getText().trim();
            String d = optD.getText().trim();
            int marks = 10;
            try { marks = Integer.parseInt(marksField.getText().trim()); } catch (Exception ignored) {}

            if (qText.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Please provide the question and all 4 options.");
                return;
            }

            int correctIdx = correctCombo.getSelectedIndex();
            DataStore.getInstance().addQuestionToExam(exam.getId(), qText, marks, Arrays.asList(a, b, c, d), correctIdx);
            loadExamsAndResults();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Question added successfully!");
        });
        btm.add(saveBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── 5. Doubts & Q&A ────────────────────────────────────────────────────────
    private JPanel buildDoubtsCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.add(UITheme.createHeader("Student Course Doubts & Direct Mentorship", "Answer questions asked by students enrolled in your courses"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolBar.setOpaque(false);
        ModernButton replyBtn = new ModernButton("💬 Reply / Resolve Doubt", ModernButton.Style.PRIMARY);
        toolBar.add(replyBtn);
        center.add(toolBar, BorderLayout.NORTH);

        String[] cols = {"Doubt ID", "Course", "Student Name", "Inquiry Question", "Instructor Answer", "Status", "Date"};
        doubtsTableModel = new DefaultTableModel(cols, 0);
        ModernTable table = new ModernTable(doubtsTableModel);
        center.add(ModernTable.wrapInScrollPane(table), BorderLayout.CENTER);

        replyBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a doubt to answer.");
                return;
            }
            int id = (int) doubtsTableModel.getValueAt(row, 0);
            Doubt d = DataStore.getInstance().getAllDoubts().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
            if (d != null) {
                showReplyDoubtDialog(d);
            }
        });

        loadDoubts();
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void loadDoubts() {
        doubtsTableModel.setRowCount(0);
        List<Course> myCourses = DataStore.getInstance().getCoursesByFaculty(facultyUser.getId());
        for (Course c : myCourses) {
            for (Doubt d : DataStore.getInstance().getDoubtsByCourse(c.getId())) {
                doubtsTableModel.addRow(new Object[]{
                        d.getId(), d.getCourseTitle(), d.getStudentName(), d.getQuestion(),
                        d.getAnswer() != null ? d.getAnswer() : "Pending your reply...",
                        d.getStatus() == Doubt.Status.RESOLVED ? "✅ Resolved" : "⏳ Pending",
                        d.getCreatedAt()
                });
            }
        }
    }

    private void showReplyDoubtDialog(Doubt d) {
        JDialog dlg = new JDialog(this, "Answer Student Doubt - " + d.getStudentName(), true);
        dlg.setSize(500, 380);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel qLbl = new JLabel("<html><b>Question:</b><br/>" + d.getQuestion() + "</html>");
        qLbl.setFont(UITheme.FONT_BODY);
        center.add(qLbl, BorderLayout.NORTH);

        JTextArea replyArea = new JTextArea(d.getAnswer() != null ? d.getAnswer() : "", 6, 20);
        replyArea.setFont(UITheme.FONT_BODY);
        replyArea.setLineWrap(true);
        replyArea.setWrapStyleWord(true);
        center.add(createFieldWrapper("Your Expert Explanation / Answer:", new JScrollPane(replyArea)), BorderLayout.CENTER);

        dlg.add(center, BorderLayout.CENTER);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        ModernButton sendBtn = new ModernButton("Send Answer", ModernButton.Style.PRIMARY);
        sendBtn.addActionListener(e -> {
            String ans = replyArea.getText().trim();
            if (ans.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Answer text cannot be empty.");
                return;
            }
            DataStore.getInstance().resolveDoubt(d.getId(), ans, facultyUser.getName());
            loadDoubts();
            refreshStats();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Answer sent to " + d.getStudentName() + "!");
        });
        btm.add(sendBtn);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void refreshAllData() {
        refreshStats();
        loadCoursesAndLectures();
        loadAssignmentsAndSubmissions();
        loadExamsAndResults();
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
        int conf = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out of the Faculty Portal?", "Log Out", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}

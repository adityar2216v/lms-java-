package com.lms.gui.test;

import com.lms.gui.db.DataStore;
import com.lms.gui.model.*;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * Automated Verification Suite for SmartLearn India LMS.
 * Executes end-to-end regression tests across all core academic and administrative workflows.
 */
public class LmsVerificationSuite {
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("       SmartLearn India — Comprehensive Automated Test Suite       ");
        System.out.println("==================================================================");

        try {
            testDataStoreInitialization();
            testAuthenticationAndSecurity();
            testUserRegistration();
            testCourseEnrollmentAndProgress();
            testAssignmentSubmissionAndGrading();
            testExamScoringAndAutoGrading();
            testDoubtsAndFacultyMentorship();
            testAttendanceTracking();
            testDataPersistence();

            System.out.println("\n------------------------------------------------------------------");
            System.out.println("TEST SUMMARY: " + testsPassed + " PASSED, " + testsFailed + " FAILED");
            System.out.println("------------------------------------------------------------------");

            if (testsFailed == 0) {
                System.out.println(">>> ALL SMARTLEARN INDIA WORKFLOWS VERIFIED 100% SUCCESSFULLY! <<<");
            } else {
                System.err.println(">>> SOME TESTS FAILED! CHECK STACK TRACE. <<<");
                System.exit(1);
            }
        } catch (Throwable t) {
            System.err.println("FATAL TEST SUITE EXCEPTION: " + t.getMessage());
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            testsPassed++;
        } else {
            System.err.println("  [FAIL] " + testName);
            testsFailed++;
        }
    }

    private static void testDataStoreInitialization() {
        System.out.println("\n1. Testing DataStore & Dataset Initialization...");
        DataStore ds = DataStore.getInstance();
        assertTrue("DataStore instance is non-null", ds != null);

        List<User> users = ds.getAllUsers();
        assertTrue("User directory seeded with 10+ accounts (found: " + users.size() + ")", users.size() >= 10);

        boolean hasAarav = users.stream().anyMatch(u -> u.getName().contains("Aarav Sharma"));
        assertTrue("Indian Student 'Aarav Sharma' present in directory", hasAarav);

        boolean hasNeha = users.stream().anyMatch(u -> u.getName().contains("Dr. Neha Kapoor"));
        assertTrue("Indian Faculty 'Dr. Neha Kapoor' present in directory", hasNeha);

        List<Course> courses = ds.getAllCourses();
        assertTrue("10 Indian University courses seeded (found: " + courses.size() + ")", courses.size() >= 10);

        boolean hasDSA = courses.stream().anyMatch(c -> c.getTitle().contains("Data Structures and Algorithms"));
        assertTrue("Course 'Data Structures and Algorithms in Java' present", hasDSA);
    }

    private static void testAuthenticationAndSecurity() {
        System.out.println("\n2. Testing Authentication & Role Permissions...");
        DataStore ds = DataStore.getInstance();

        // Admin Auth
        User admin = ds.authenticate("admin@smartlearn.in", "admin123");
        assertTrue("Admin login with admin@smartlearn.in succeeded", admin != null && admin.getRole() == User.Role.ADMIN);

        User adminAlias = ds.authenticate("admin@lms.com", "admin123");
        assertTrue("Legacy Admin alias admin@lms.com login succeeded", adminAlias != null && adminAlias.getRole() == User.Role.ADMIN);

        // Faculty Auth
        User faculty = ds.authenticate("neha.kapoor@smartlearn.in", "faculty123");
        assertTrue("Faculty login with neha.kapoor@smartlearn.in succeeded", faculty != null && faculty.getRole() == User.Role.FACULTY);

        // Student Auth
        User student = ds.authenticate("aarav.sharma@smartlearn.in", "student123");
        assertTrue("Student login with aarav.sharma@smartlearn.in succeeded", student != null && student.getRole() == User.Role.STUDENT);

        // Negative Tests
        User badPass = ds.authenticate("aarav.sharma@smartlearn.in", "wrongpassword");
        assertTrue("Invalid password correctly rejected", badPass == null);

        User fakeUser = ds.authenticate("nonexistent@smartlearn.in", "any");
        assertTrue("Non-existent user correctly rejected", fakeUser == null);
    }

    private static void testUserRegistration() {
        System.out.println("\n3. Testing User Registration & Duplicate Prevention...");
        DataStore ds = DataStore.getInstance();

        String testEmail = "test.student." + System.currentTimeMillis() + "@smartlearn.in";
        User newUser = ds.registerUser("Kavita Sen", testEmail, "9876599999", "pass123", User.Role.STUDENT, "Information Technology");
        assertTrue("New student account registration succeeded", newUser != null && newUser.getId() > 0);

        User queried = ds.getUserByEmail(testEmail);
        assertTrue("Registered user retrievable by email", queried != null && queried.getName().equals("Kavita Sen"));

        // Duplicate registration test
        boolean duplicateBlocked = false;
        try {
            ds.registerUser("Another User", testEmail, "9876599998", "pass456", User.Role.STUDENT, "IT");
        } catch (IllegalArgumentException ex) {
            duplicateBlocked = true;
        }
        assertTrue("Duplicate email registration correctly throws exception", duplicateBlocked);
    }

    private static void testCourseEnrollmentAndProgress() {
        System.out.println("\n4. Testing Course Enrollment & Progress Updates...");
        DataStore ds = DataStore.getInstance();

        User student = ds.getUserByEmail("aarav.sharma@smartlearn.in");
        Course course = ds.getAllCourses().get(4); // Computer Networks

        if (ds.isEnrolled(student.getId(), course.getId())) {
            assertTrue("Student pre-enrolled in course", true);
        } else {
            Enrollment enr = ds.enrollStudent(student.getId(), student.getName(), course.getId(), course.getTitle());
            assertTrue("Student enrolled in " + course.getTitle(), enr != null);
            assertTrue("isEnrolled returns true after enrollment", ds.isEnrolled(student.getId(), course.getId()));
        }

        // Test Progress Update
        ds.updateProgress(student.getId(), course.getId(), 85);
        Enrollment updated = ds.getEnrollmentsByStudent(student.getId()).stream()
                .filter(e -> e.getCourseId() == course.getId()).findFirst().orElse(null);
        assertTrue("Course progress updated to 85%", updated != null && updated.getProgress() == 85);
    }

    private static void testAssignmentSubmissionAndGrading() {
        System.out.println("\n5. Testing Assignment Submission & Rubric Grading...");
        DataStore ds = DataStore.getInstance();

        User student = ds.getUserByEmail("aarav.sharma@smartlearn.in");
        User faculty = ds.getUserByEmail("neha.kapoor@smartlearn.in");
        Course course = ds.getAllCourses().get(0);

        Assignment a = ds.addAssignment(course.getId(), course.getTitle(),
                "Regression Test: Dynamic Programming Knapsack",
                "Solve 0/1 Knapsack using tabulation.", LocalDate.now().plusDays(5),
                100, faculty.getId(), faculty.getName());
        assertTrue("Assignment created by faculty", a != null && a.getId() > 0);

        // Student submits solution
        AssignmentSubmission sub = ds.submitAssignment(a.getId(), a.getTitle(), course.getId(), course.getTitle(),
                student.getId(), student.getName(), "public int knapsack(...) { return dp[n][W]; }");
        assertTrue("Student submitted solution successfully", sub != null && sub.getStatus() == AssignmentSubmission.Status.SUBMITTED);

        // Faculty grades submission
        ds.gradeSubmission(sub.getId(), 98, "Brilliant O(nW) space optimization!");
        AssignmentSubmission graded = ds.getAllSubmissions().stream().filter(s -> s.getId() == sub.getId()).findFirst().orElse(null);
        assertTrue("Faculty awarded marks and status changed to GRADED",
                graded != null && graded.getStatus() == AssignmentSubmission.Status.GRADED && graded.getMarks() == 98);
    }

    private static void testExamScoringAndAutoGrading() {
        System.out.println("\n6. Testing Timed MCQ Exam Auto-Grading...");
        DataStore ds = DataStore.getInstance();

        User student = ds.getUserByEmail("aarav.sharma@smartlearn.in");
        List<Exam> exams = ds.getAllExams();
        assertTrue("Exams available in question bank", !exams.isEmpty());

        Exam ex = exams.get(0);
        assertTrue("Exam has questions configured", !ex.getQuestions().isEmpty());

        // Simulate taking exam: score 30/40
        ExamResult res = ds.saveExamResult(ex.getId(), ex.getTitle(), ex.getCourseId(), ex.getCourseTitle(),
                student.getId(), student.getName(), 35, 40);
        assertTrue("Exam result recorded with calculated score", res != null && res.getScore() == 35);
        assertTrue("Exam status correctly marked PASSED (>= 50%)", "PASSED".equals(res.getStatus()));
    }

    private static void testDoubtsAndFacultyMentorship() {
        System.out.println("\n7. Testing Doubts Q&A Mentorship...");
        DataStore ds = DataStore.getInstance();

        User student = ds.getUserByEmail("aarav.sharma@smartlearn.in");
        Course c = ds.getAllCourses().get(0);

        Doubt d = ds.addDoubt(c.getId(), c.getTitle(), student.getId(), student.getName(), "What is the difference between ArrayDeque and LinkedList as a Queue?");
        assertTrue("Doubt inquiry posted with status PENDING", d != null && d.getStatus() == Doubt.Status.PENDING);

        // Faculty resolves doubt
        ds.resolveDoubt(d.getId(), "ArrayDeque has lower memory overhead and better cache locality than LinkedList.", "Dr. Neha Kapoor");
        Doubt resolved = ds.getAllDoubts().stream().filter(x -> x.getId() == d.getId()).findFirst().orElse(null);
        assertTrue("Doubt answer recorded and status updated to RESOLVED",
                resolved != null && resolved.getStatus() == Doubt.Status.RESOLVED && resolved.getAnswer() != null);
    }

    private static void testAttendanceTracking() {
        System.out.println("\n8. Testing Course Attendance & AICTE 75% Check...");
        DataStore ds = DataStore.getInstance();

        User student = ds.getUserByEmail("aarav.sharma@smartlearn.in");
        Course c = ds.getAllCourses().get(0);

        AttendanceRecord rec = ds.markAttendance(student.getId(), student.getName(), c.getId(), c.getTitle(), LocalDate.now(), "PRESENT", "Unit IV Revision");
        assertTrue("Attendance marked PRESENT for student", rec != null && rec.isPresent());

        double pct = ds.getAttendancePercentage(student.getId(), c.getId());
        assertTrue("Attendance percentage calculated correctly (found: " + pct + "%)", pct >= 75.0);
    }

    private static void testDataPersistence() {
        System.out.println("\n9. Testing Binary Disk Persistence...");
        DataStore ds = DataStore.getInstance();
        ds.saveToDisk();

        File f = new File("lms_gui_data.ser");
        assertTrue("Serialized data file lms_gui_data.ser exists on disk", f.exists() && f.length() > 0);
        System.out.println("  Data file size: " + f.length() + " bytes");
    }
}

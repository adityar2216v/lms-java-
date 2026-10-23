package com.lms.gui.db;

import com.lms.gui.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "lms_gui_data.ser";

    private static DataStore instance;

    private final Map<Integer, User> users = new LinkedHashMap<>();
    private final Map<Integer, Course> courses = new LinkedHashMap<>();
    private final Map<Integer, Enrollment> enrollments = new LinkedHashMap<>();
    private final Map<Integer, VideoLecture> videoLectures = new LinkedHashMap<>();
    private final Map<Integer, Assignment> assignments = new LinkedHashMap<>();
    private final Map<Integer, AssignmentSubmission> submissions = new LinkedHashMap<>();
    private final Map<Integer, Exam> exams = new LinkedHashMap<>();
    private final Map<Integer, ExamResult> examResults = new LinkedHashMap<>();
    private final Map<Integer, Doubt> doubts = new LinkedHashMap<>();
    private final Map<Integer, Notice> notices = new LinkedHashMap<>();
    private final Map<Integer, FAQ> faqs = new LinkedHashMap<>();

    private final AtomicInteger userIdGen = new AtomicInteger(100);
    private final AtomicInteger courseIdGen = new AtomicInteger(100);
    private final AtomicInteger enrollmentIdGen = new AtomicInteger(100);
    private final AtomicInteger videoIdGen = new AtomicInteger(100);
    private final AtomicInteger assignmentIdGen = new AtomicInteger(100);
    private final AtomicInteger submissionIdGen = new AtomicInteger(100);
    private final AtomicInteger examIdGen = new AtomicInteger(100);
    private final AtomicInteger questionIdGen = new AtomicInteger(100);
    private final AtomicInteger examResultIdGen = new AtomicInteger(100);
    private final AtomicInteger doubtIdGen = new AtomicInteger(100);
    private final AtomicInteger noticeIdGen = new AtomicInteger(100);
    private final AtomicInteger faqIdGen = new AtomicInteger(100);

    private DataStore() {}

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = loadFromDisk();
            if (instance.users.isEmpty()) {
                instance.seedInitialData();
                instance.saveToDisk();
            }
        }
        return instance;
    }

    private static DataStore loadFromDisk() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof DataStore) {
                    System.out.println("Loaded LMS data from " + DATA_FILE);
                    return (DataStore) obj;
                }
            } catch (Exception e) {
                System.err.println("Could not load from " + DATA_FILE + " (" + e.getMessage() + "). Creating fresh instance.");
            }
        }
        return new DataStore();
    }

    public synchronized void saveToDisk() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            System.out.println("Saved LMS data to " + DATA_FILE);
        } catch (Exception e) {
            System.err.println("Error saving LMS data: " + e.getMessage());
        }
    }

    private void seedInitialData() {
        // 1. Seed Users (Matching Web Application LMS Seed Data)
        User admin1 = new User(1, "Master Admin", "admin@lms.com", "9876543210", "admin123", User.Role.ADMIN, 1, "Administration");
        User admin2 = new User(2, "Admin User", "admin@example.com", "9876543200", "password", User.Role.ADMIN, 1, "IT Operations");
        User faculty1 = new User(3, "Dr. Smith", "faculty@lms.com", "9876543211", "faculty123", User.Role.FACULTY, 1, "Computer Science");
        User faculty2 = new User(4, "Prof. Sarah Connor", "faculty@example.com", "9876543219", "password", User.Role.FACULTY, 1, "Data Science");
        User student1 = new User(5, "Alex Johnson", "student@lms.com", "9876543212", "student123", User.Role.STUDENT, 1, "Computer Science");
        User student2 = new User(6, "Emily Watson", "student@example.com", "9876543213", "password", User.Role.STUDENT, 1, "Software Engineering");
        User student3 = new User(7, "David Miller", "david@example.com", "9876543214", "password", User.Role.STUDENT, 1, "Information Technology");

        users.put(admin1.getId(), admin1);
        users.put(admin2.getId(), admin2);
        users.put(faculty1.getId(), faculty1);
        users.put(faculty2.getId(), faculty2);
        users.put(student1.getId(), student1);
        users.put(student2.getId(), student2);
        users.put(student3.getId(), student3);

        // 2. Seed Courses
        Course c1 = new Course(1, "Advanced Java Programming", "Master Lambdas, Streams, Concurrency, JVM Internals & Collections.", faculty1.getId(), faculty1.getName(), "Computer Science");
        Course c2 = new Course(2, "Spring Boot Microservices", "Build scalable microservices with REST, Spring Security, JPA & Docker.", faculty1.getId(), faculty1.getName(), "Backend Development");
        Course c3 = new Course(3, "Modern JavaScript (ES6+)", "Deep dive into Async/Await, Promises, Closures & Modern JS features.", faculty2.getId(), faculty2.getName(), "Web Development");
        Course c4 = new Course(4, "React JS for Beginners", "Component architectures, Hooks, State management & Routing.", faculty2.getId(), faculty2.getName(), "Frontend Development");
        Course c5 = new Course(5, "Java Full Stack Development", "Comprehensive end-to-end full stack web architecture with databases.", faculty1.getId(), faculty1.getName(), "Full Stack");

        c1.setEnrolledStudentsCount(3);
        c2.setEnrolledStudentsCount(2);
        c3.setEnrolledStudentsCount(1);
        c4.setEnrolledStudentsCount(2);
        c5.setEnrolledStudentsCount(2);

        courses.put(c1.getId(), c1);
        courses.put(c2.getId(), c2);
        courses.put(c3.getId(), c3);
        courses.put(c4.getId(), c4);
        courses.put(c5.getId(), c5);

        // 3. Seed Enrollments
        Enrollment e1 = new Enrollment(1, student1.getId(), student1.getName(), c1.getId(), c1.getTitle(), 75);
        Enrollment e2 = new Enrollment(2, student1.getId(), student1.getName(), c2.getId(), c2.getTitle(), 45);
        Enrollment e3 = new Enrollment(3, student1.getId(), student1.getName(), c5.getId(), c5.getTitle(), 20);
        Enrollment e4 = new Enrollment(4, student2.getId(), student2.getName(), c1.getId(), c1.getTitle(), 60);
        Enrollment e5 = new Enrollment(5, student2.getId(), student2.getName(), c4.getId(), c4.getTitle(), 90);
        Enrollment e6 = new Enrollment(6, student3.getId(), student3.getName(), c3.getId(), c3.getTitle(), 30);

        enrollments.put(e1.getId(), e1);
        enrollments.put(e2.getId(), e2);
        enrollments.put(e3.getId(), e3);
        enrollments.put(e4.getId(), e4);
        enrollments.put(e5.getId(), e5);
        enrollments.put(e6.getId(), e6);

        // 4. Seed Video Lectures & Notes
        VideoLecture v1 = new VideoLecture(1, c1.getId(), c1.getTitle(), "1. Introduction to Advanced Java", "Overview of modern Java 17/21 features, records, pattern matching.", "45 mins", "lecture1_intro.mp4", "Java 17 features: Records, sealed classes, pattern matching switch.");
        VideoLecture v2 = new VideoLecture(2, c1.getId(), c1.getTitle(), "2. Stream API & Functional Interfaces", "Deep dive into map, filter, reduce, collectors, and parallel streams.", "52 mins", "lecture2_streams.mp4", "Streams enable functional programming over collections. Avoid mutating shared state.");
        VideoLecture v3 = new VideoLecture(3, c1.getId(), c1.getTitle(), "3. Multi-threading & Virtual Threads", "Concurrency utilities, locks, executors, and virtual threads.", "60 mins", "lecture3_concurrency.mp4", "Project Loom virtual threads provide high throughput with lightweight execution.");
        VideoLecture v4 = new VideoLecture(4, c2.getId(), c2.getTitle(), "1. Spring Boot 3 Core Concepts", "Dependency injection, autowiring, Spring Boot starters.", "48 mins", "lecture1_springboot.mp4", "Spring IoC container manages bean lifecycle and dependency injection automatically.");
        VideoLecture v5 = new VideoLecture(5, c2.getId(), c2.getTitle(), "2. Spring Data JPA & Hibernate", "Entities, repositories, custom JPQL queries, schema migrations.", "55 mins", "lecture2_jpa.mp4", "HikariCP pool management, lazy vs eager fetching, transaction boundaries.");

        videoLectures.put(v1.getId(), v1);
        videoLectures.put(v2.getId(), v2);
        videoLectures.put(v3.getId(), v3);
        videoLectures.put(v4.getId(), v4);
        videoLectures.put(v5.getId(), v5);

        // 5. Seed Assignments
        Assignment a1 = new Assignment(1, c1.getId(), c1.getTitle(), "Java OOPs & Stream Architecture", "Implement an immutable banking ledger system utilizing Java Streams, Records, and Lambdas.", LocalDate.now().plusDays(5), 100, faculty1.getId(), faculty1.getName());
        Assignment a2 = new Assignment(2, c2.getId(), c2.getTitle(), "Spring Data JPA Microservice", "Design a RESTful controller with JPA repositories, DTO validation, and error handlers.", LocalDate.now().plusDays(7), 50, faculty1.getId(), faculty1.getName());
        Assignment a3 = new Assignment(3, c3.getId(), c3.getTitle(), "Modern JavaScript Async Pipeline", "Construct an asynchronous pipeline handling external API fetch and retry logic.", LocalDate.now().plusDays(3), 50, faculty2.getId(), faculty2.getName());

        assignments.put(a1.getId(), a1);
        assignments.put(a2.getId(), a2);
        assignments.put(a3.getId(), a3);

        // 6. Seed Assignment Submissions
        AssignmentSubmission s1 = new AssignmentSubmission(1, a1.getId(), a1.getTitle(), c1.getId(), c1.getTitle(), student1.getId(), student1.getName(), "public record Transaction(String id, double amount, LocalDateTime timestamp) {}\n// Complete ledger implementation with parallel streams.");
        s1.setMarks(92);
        s1.setFeedback("Exceptional use of Java Records and functional streams. Clean code architecture!");
        s1.setStatus(AssignmentSubmission.Status.GRADED);

        AssignmentSubmission s2 = new AssignmentSubmission(2, a2.getId(), a2.getTitle(), c2.getId(), c2.getTitle(), student1.getId(), student1.getName(), "@RestController\n@RequestMapping(\"/api/v1/students\")\npublic class StudentController { ... }");
        s2.setStatus(AssignmentSubmission.Status.SUBMITTED);

        submissions.put(s1.getId(), s1);
        submissions.put(s2.getId(), s2);

        // 7. Seed Exams & Questions
        Exam exam1 = new Exam(1, c1.getId(), c1.getTitle(), "Midterm Examination 2026", 45, 40, faculty1.getId(), faculty1.getName(), "Live");
        
        Question q1 = new Question(1, 1, "Which collection class guarantees unique elements and does not maintain order?", 10, 
                Arrays.asList("ArrayList", "LinkedList", "HashSet", "Vector"), 2);
        Question q2 = new Question(2, 1, "Which Java feature introduced in Java 16 enables immutable data-carrier classes?", 10,
                Arrays.asList("Interfaces", "Records", "Enums", "Abstract Classes"), 1);
        Question q3 = new Question(3, 1, "What does JVM stand for?", 10,
                Arrays.asList("Java Virtual Machine", "Java Visual Manager", "Joint Variable Method", "Java Verified Memory"), 0);
        Question q4 = new Question(4, 1, "Which keyword prevents method overriding and class inheritance?", 10,
                Arrays.asList("static", "abstract", "final", "volatile"), 2);

        exam1.getQuestions().addAll(Arrays.asList(q1, q2, q3, q4));
        exams.put(exam1.getId(), exam1);

        Exam exam2 = new Exam(2, c2.getId(), c2.getTitle(), "Spring Boot Core Assessment", 30, 20, faculty1.getId(), faculty1.getName(), "Live");
        Question eq1 = new Question(5, 2, "Which annotation marks a class as a Spring Boot auto-configuration source?", 10,
                Arrays.asList("@SpringBootApplication", "@Service", "@Repository", "@Entity"), 0);
        Question eq2 = new Question(6, 2, "Default embedded servlet container in Spring Boot is:", 10,
                Arrays.asList("Jetty", "Undertow", "Apache Tomcat", "GlassFish"), 2);
        exam2.getQuestions().addAll(Arrays.asList(eq1, eq2));
        exams.put(exam2.getId(), exam2);

        // 8. Seed Exam Results
        ExamResult res1 = new ExamResult(1, exam1.getId(), exam1.getTitle(), c1.getId(), c1.getTitle(), student2.getId(), student2.getName(), 30, 40);
        examResults.put(res1.getId(), res1);

        // 9. Seed Doubts / Q&A
        Doubt d1 = new Doubt(1, c1.getId(), c1.getTitle(), student1.getId(), student1.getName(), "Could you please explain when to use Parallel Streams vs Normal Sequential Streams?");
        d1.setAnswer("Use parallel streams only when processing large CPU-intensive datasets where the cost of thread splitting and joining is lower than the serial execution time.");
        d1.setFacultyName(faculty1.getName());
        d1.setStatus(Doubt.Status.RESOLVED);

        Doubt d2 = new Doubt(2, c2.getId(), c2.getTitle(), student1.getId(), student1.getName(), "Why is field injection with @Autowired discouraged compared to constructor injection?");
        doubts.put(d1.getId(), d1);
        doubts.put(d2.getId(), d2);

        // 10. Seed Campus Notices
        Notice n1 = new Notice(1, "Final Semester Examination Timetable Released", "The university examination branch has released the final timetable for odd semester examinations. Check the portal for your batch schedule.", "Academic", "Examinations Dean", true);
        Notice n2 = new Notice(2, "Annual Tech Innovation Hackathon 2026", "Registrations are now open for the 36-hour National Coding Hackathon with prize pool of $5,000. Team up with classmates!", "Event", "Student Affairs", true);
        Notice n3 = new Notice(3, "Campus Placement Drive: Top Tier Tech Companies", "Major enterprise software companies will be conducting placement tests on campus starting next Monday. Keep resumes updated.", "Placement", "Training & Placement Cell", false);

        notices.put(n1.getId(), n1);
        notices.put(n2.getId(), n2);
        notices.put(n3.getId(), n3);

        // 11. Seed FAQs
        faqs.put(1, new FAQ(1, "How do I enroll in a new course?", "Go to the 'Course Catalog' tab, browse the available courses, and click the 'Enroll' button.", "Enrollment"));
        faqs.put(2, new FAQ(2, "How are assignment submissions graded?", "Your course instructor reviews submitted solutions, assigns scores, and writes personalized feedback in the portal.", "Assignments"));
        faqs.put(3, new FAQ(3, "Can I retake an exam after submission?", "Exam attempts are automatically evaluated and recorded upon submission. Retake policy depends on the instructor's discretion.", "Exams"));
        faqs.put(4, new FAQ(4, "How do I ask doubts directly to faculty?", "Navigate to the 'Ask Doubts' section, choose your course, and post your inquiry. The instructor is notified instantly.", "Communication"));
    }

    // ── Authentication ────────────────────────────────────────────────────────
    public synchronized User authenticate(String email, String password) {
        if (email == null || password == null) return null;
        for (User u : users.values()) {
            if (u.getEmail().equalsIgnoreCase(email.trim()) && u.getPassword().equals(password.trim())) {
                return u;
            }
        }
        return null;
    }

    public synchronized User registerUser(String name, String email, String mobile, String password, User.Role role, String department) {
        if (getUserByEmail(email) != null) {
            throw new IllegalArgumentException("User with email '" + email + "' already exists!");
        }
        int id = userIdGen.incrementAndGet();
        User u = new User(id, name, email, mobile, password, role, 1, department);
        users.put(id, u);
        saveToDisk();
        return u;
    }

    public synchronized User getUserByEmail(String email) {
        if (email == null) return null;
        for (User u : users.values()) {
            if (u.getEmail().equalsIgnoreCase(email.trim())) return u;
        }
        return null;
    }

    // ── Users Management ───────────────────────────────────────────────────────
    public synchronized List<User> getAllUsers() { return new ArrayList<>(users.values()); }
    public synchronized User getUserById(int id) { return users.get(id); }
    public synchronized void saveUser(User u) { users.put(u.getId(), u); saveToDisk(); }
    public synchronized void deleteUser(int id) { users.remove(id); saveToDisk(); }

    public synchronized List<User> getUsersByRole(User.Role role) {
        List<User> list = new ArrayList<>();
        for (User u : users.values()) {
            if (u.getRole() == role) list.add(u);
        }
        return list;
    }

    // ── Course Management ──────────────────────────────────────────────────────
    public synchronized List<Course> getAllCourses() { return new ArrayList<>(courses.values()); }
    public synchronized Course getCourseById(int id) { return courses.get(id); }
    
    public synchronized Course addCourse(String title, String description, int instructorId, String instructorName, String category) {
        int id = courseIdGen.incrementAndGet();
        Course c = new Course(id, title, description, instructorId, instructorName, category);
        courses.put(id, c);
        saveToDisk();
        return c;
    }

    public synchronized void deleteCourse(int id) {
        courses.remove(id);
        saveToDisk();
    }

    public synchronized List<Course> getCoursesByFaculty(int facultyId) {
        List<Course> list = new ArrayList<>();
        for (Course c : courses.values()) {
            if (c.getInstructorId() == facultyId) list.add(c);
        }
        return list;
    }

    // ── Enrollments ────────────────────────────────────────────────────────────
    public synchronized List<Enrollment> getAllEnrollments() { return new ArrayList<>(enrollments.values()); }
    
    public synchronized List<Enrollment> getEnrollmentsByStudent(int studentId) {
        List<Enrollment> list = new ArrayList<>();
        for (Enrollment e : enrollments.values()) {
            if (e.getStudentId() == studentId) list.add(e);
        }
        return list;
    }

    public synchronized boolean isEnrolled(int studentId, int courseId) {
        for (Enrollment e : enrollments.values()) {
            if (e.getStudentId() == studentId && e.getCourseId() == courseId) return true;
        }
        return false;
    }

    public synchronized Enrollment enrollStudent(int studentId, String studentName, int courseId, String courseTitle) {
        if (isEnrolled(studentId, courseId)) {
            throw new IllegalStateException("Student is already enrolled in this course!");
        }
        int id = enrollmentIdGen.incrementAndGet();
        Enrollment e = new Enrollment(id, studentId, studentName, courseId, courseTitle, 0);
        enrollments.put(id, e);
        Course c = courses.get(courseId);
        if (c != null) {
            c.setEnrolledStudentsCount(c.getEnrolledStudentsCount() + 1);
        }
        saveToDisk();
        return e;
    }

    public synchronized void updateProgress(int studentId, int courseId, int progress) {
        for (Enrollment e : enrollments.values()) {
            if (e.getStudentId() == studentId && e.getCourseId() == courseId) {
                e.setProgress(progress);
                saveToDisk();
                break;
            }
        }
    }

    // ── Video Lectures ─────────────────────────────────────────────────────────
    public synchronized List<VideoLecture> getAllLectures() { return new ArrayList<>(videoLectures.values()); }
    
    public synchronized List<VideoLecture> getLecturesByCourse(int courseId) {
        List<VideoLecture> list = new ArrayList<>();
        for (VideoLecture v : videoLectures.values()) {
            if (v.getCourseId() == courseId) list.add(v);
        }
        return list;
    }

    public synchronized VideoLecture addLecture(int courseId, String courseTitle, String title, String description, String duration, String fileName, String notes) {
        int id = videoIdGen.incrementAndGet();
        VideoLecture v = new VideoLecture(id, courseId, courseTitle, title, description, duration, fileName, notes);
        videoLectures.put(id, v);
        saveToDisk();
        return v;
    }

    // ── Assignments ────────────────────────────────────────────────────────────
    public synchronized List<Assignment> getAllAssignments() { return new ArrayList<>(assignments.values()); }

    public synchronized List<Assignment> getAssignmentsByCourse(int courseId) {
        List<Assignment> list = new ArrayList<>();
        for (Assignment a : assignments.values()) {
            if (a.getCourseId() == courseId) list.add(a);
        }
        return list;
    }

    public synchronized List<Assignment> getAssignmentsByFaculty(int facultyId) {
        List<Assignment> list = new ArrayList<>();
        for (Assignment a : assignments.values()) {
            if (a.getCreatedByFacultyId() == facultyId) list.add(a);
        }
        return list;
    }

    public synchronized Assignment addAssignment(int courseId, String courseTitle, String title, String description, LocalDate dueDate, int maxMarks, int facultyId, String facultyName) {
        int id = assignmentIdGen.incrementAndGet();
        Assignment a = new Assignment(id, courseId, courseTitle, title, description, dueDate, maxMarks, facultyId, facultyName);
        assignments.put(id, a);
        saveToDisk();
        return a;
    }

    // ── Submissions ────────────────────────────────────────────────────────────
    public synchronized List<AssignmentSubmission> getAllSubmissions() { return new ArrayList<>(submissions.values()); }

    public synchronized List<AssignmentSubmission> getSubmissionsByAssignment(int assignmentId) {
        List<AssignmentSubmission> list = new ArrayList<>();
        for (AssignmentSubmission s : submissions.values()) {
            if (s.getAssignmentId() == assignmentId) list.add(s);
        }
        return list;
    }

    public synchronized List<AssignmentSubmission> getSubmissionsByStudent(int studentId) {
        List<AssignmentSubmission> list = new ArrayList<>();
        for (AssignmentSubmission s : submissions.values()) {
            if (s.getStudentId() == studentId) list.add(s);
        }
        return list;
    }

    public synchronized AssignmentSubmission submitAssignment(int assignmentId, String assignmentTitle, int courseId, String courseTitle, int studentId, String studentName, String answerText) {
        int id = submissionIdGen.incrementAndGet();
        AssignmentSubmission s = new AssignmentSubmission(id, assignmentId, assignmentTitle, courseId, courseTitle, studentId, studentName, answerText);
        submissions.put(id, s);
        saveToDisk();
        return s;
    }

    public synchronized void gradeSubmission(int submissionId, int marks, String feedback) {
        AssignmentSubmission s = submissions.get(submissionId);
        if (s != null) {
            s.setMarks(marks);
            s.setFeedback(feedback);
            s.setStatus(AssignmentSubmission.Status.GRADED);
            saveToDisk();
        }
    }

    // ── Exams ──────────────────────────────────────────────────────────────────
    public synchronized List<Exam> getAllExams() { return new ArrayList<>(exams.values()); }

    public synchronized List<Exam> getExamsByCourse(int courseId) {
        List<Exam> list = new ArrayList<>();
        for (Exam ex : exams.values()) {
            if (ex.getCourseId() == courseId) list.add(ex);
        }
        return list;
    }

    public synchronized List<Exam> getExamsByFaculty(int facultyId) {
        List<Exam> list = new ArrayList<>();
        for (Exam ex : exams.values()) {
            if (ex.getFacultyId() == facultyId) list.add(ex);
        }
        return list;
    }

    public synchronized Exam addExam(int courseId, String courseTitle, String title, int durationMinutes, int totalMarks, int facultyId, String facultyName) {
        int id = examIdGen.incrementAndGet();
        Exam e = new Exam(id, courseId, courseTitle, title, durationMinutes, totalMarks, facultyId, facultyName, "Live");
        exams.put(id, e);
        saveToDisk();
        return e;
    }

    public synchronized void addQuestionToExam(int examId, String questionText, int marks, List<String> options, int correctIndex) {
        Exam ex = exams.get(examId);
        if (ex != null) {
            int qId = questionIdGen.incrementAndGet();
            Question q = new Question(qId, examId, questionText, marks, options, correctIndex);
            ex.getQuestions().add(q);
            saveToDisk();
        }
    }

    // ── Exam Results ───────────────────────────────────────────────────────────
    public synchronized List<ExamResult> getAllExamResults() { return new ArrayList<>(examResults.values()); }

    public synchronized List<ExamResult> getExamResultsByStudent(int studentId) {
        List<ExamResult> list = new ArrayList<>();
        for (ExamResult r : examResults.values()) {
            if (r.getStudentId() == studentId) list.add(r);
        }
        return list;
    }

    public synchronized List<ExamResult> getExamResultsByExam(int examId) {
        List<ExamResult> list = new ArrayList<>();
        for (ExamResult r : examResults.values()) {
            if (r.getExamId() == examId) list.add(r);
        }
        return list;
    }

    public synchronized ExamResult saveExamResult(int examId, String examTitle, int courseId, String courseTitle, int studentId, String studentName, int score, int totalMarks) {
        int id = examResultIdGen.incrementAndGet();
        ExamResult r = new ExamResult(id, examId, examTitle, courseId, courseTitle, studentId, studentName, score, totalMarks);
        examResults.put(id, r);
        saveToDisk();
        return r;
    }

    // ── Doubts / Q&A ───────────────────────────────────────────────────────────
    public synchronized List<Doubt> getAllDoubts() { return new ArrayList<>(doubts.values()); }

    public synchronized List<Doubt> getDoubtsByCourse(int courseId) {
        List<Doubt> list = new ArrayList<>();
        for (Doubt d : doubts.values()) {
            if (d.getCourseId() == courseId) list.add(d);
        }
        return list;
    }

    public synchronized List<Doubt> getDoubtsByStudent(int studentId) {
        List<Doubt> list = new ArrayList<>();
        for (Doubt d : doubts.values()) {
            if (d.getStudentId() == studentId) list.add(d);
        }
        return list;
    }

    public synchronized Doubt addDoubt(int courseId, String courseTitle, int studentId, String studentName, String question) {
        int id = doubtIdGen.incrementAndGet();
        Doubt d = new Doubt(id, courseId, courseTitle, studentId, studentName, question);
        doubts.put(id, d);
        saveToDisk();
        return d;
    }

    public synchronized void resolveDoubt(int doubtId, String answer, String facultyName) {
        Doubt d = doubts.get(doubtId);
        if (d != null) {
            d.setAnswer(answer);
            d.setFacultyName(facultyName);
            d.setStatus(Doubt.Status.RESOLVED);
            saveToDisk();
        }
    }

    // ── Notices ────────────────────────────────────────────────────────────────
    public synchronized List<Notice> getAllNotices() { return new ArrayList<>(notices.values()); }

    public synchronized Notice addNotice(String title, String content, String category, String author, boolean pinned) {
        int id = noticeIdGen.incrementAndGet();
        Notice n = new Notice(id, title, content, category, author, pinned);
        notices.put(id, n);
        saveToDisk();
        return n;
    }

    public synchronized void deleteNotice(int id) {
        notices.remove(id);
        saveToDisk();
    }

    // ── FAQs ───────────────────────────────────────────────────────────────────
    public synchronized List<FAQ> getAllFAQs() { return new ArrayList<>(faqs.values()); }

    public synchronized FAQ addFAQ(String question, String answer, String category) {
        int id = faqIdGen.incrementAndGet();
        FAQ f = new FAQ(id, question, answer, category);
        faqs.put(id, f);
        saveToDisk();
        return f;
    }
}

package com.lms.gui.db;

import com.lms.gui.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * High-performance, synchronized data store for SmartLearn India LMS.
 * Handles state management, CRUD transactions, and binary persistence to lms_gui_data.ser.
 */
public class DataStore implements Serializable {
    private static final long serialVersionUID = 202602L;
    private static final String DATA_FILE = "lms_gui_data.ser";
    private static final int CURRENT_DATA_VERSION = 202602;

    private static DataStore instance;

    private int dataVersion = CURRENT_DATA_VERSION;

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
    private final Map<Integer, AttendanceRecord> attendance = new LinkedHashMap<>();

    private final AtomicInteger userIdGen = new AtomicInteger(200);
    private final AtomicInteger courseIdGen = new AtomicInteger(200);
    private final AtomicInteger enrollmentIdGen = new AtomicInteger(200);
    private final AtomicInteger videoIdGen = new AtomicInteger(200);
    private final AtomicInteger assignmentIdGen = new AtomicInteger(200);
    private final AtomicInteger submissionIdGen = new AtomicInteger(200);
    private final AtomicInteger examIdGen = new AtomicInteger(200);
    private final AtomicInteger questionIdGen = new AtomicInteger(200);
    private final AtomicInteger examResultIdGen = new AtomicInteger(200);
    private final AtomicInteger doubtIdGen = new AtomicInteger(200);
    private final AtomicInteger noticeIdGen = new AtomicInteger(200);
    private final AtomicInteger faqIdGen = new AtomicInteger(200);
    private final AtomicInteger attendanceIdGen = new AtomicInteger(200);

    private DataStore() {}

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = loadFromDisk();
            boolean needsSeed = instance.users.isEmpty()
                    || instance.dataVersion < CURRENT_DATA_VERSION
                    || instance.users.values().stream().noneMatch(u -> u.getName().contains("Aarav"));

            if (needsSeed) {
                System.out.println("Seeding SmartLearn India institutional datasets...");
                instance.clearAndSeedIndianData();
                instance.dataVersion = CURRENT_DATA_VERSION;
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
                    System.out.println("Loaded SmartLearn India data from " + DATA_FILE);
                    return (DataStore) obj;
                }
            } catch (Exception e) {
                System.err.println("Notice: Resetting to fresh SmartLearn India dataset (" + e.getMessage() + ")");
            }
        }
        return new DataStore();
    }

    public synchronized void saveToDisk() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            System.out.println("Saved SmartLearn India data to " + DATA_FILE);
        } catch (Exception e) {
            System.err.println("Error saving SmartLearn India data: " + e.getMessage());
        }
    }

    public synchronized void clearAndSeedIndianData() {
        users.clear();
        courses.clear();
        enrollments.clear();
        videoLectures.clear();
        assignments.clear();
        submissions.clear();
        exams.clear();
        examResults.clear();
        doubts.clear();
        notices.clear();
        faqs.clear();
        attendance.clear();

        // ── 1. Administrators ──────────────────────────────────────────────────
        User admin1 = new User(1, "Admin User", "admin@smartlearn.in", "9876543210", "admin123", User.Role.ADMIN, 1, "Academic Administration");
        User adminAlias = new User(2, "Master Administrator", "admin@lms.com", "9876543200", "admin123", User.Role.ADMIN, 1, "Academic Governance");
        users.put(admin1.getId(), admin1);
        users.put(adminAlias.getId(), adminAlias);

        // ── 2. Faculty Members ─────────────────────────────────────────────────
        User fac1 = new User(3, "Dr. Neha Kapoor", "neha.kapoor@smartlearn.in", "9811223344", "faculty123", User.Role.FACULTY, 1, "Computer Science & Engineering");
        User fac2 = new User(4, "Prof. Vikram Deshmukh", "vikram.deshmukh@smartlearn.in", "9822334455", "faculty123", User.Role.FACULTY, 1, "Data Structures & Algorithms");
        User fac3 = new User(5, "Dr. Meera Iyer", "meera.iyer@smartlearn.in", "9833445566", "faculty123", User.Role.FACULTY, 1, "Artificial Intelligence & ML");
        User fac4 = new User(6, "Prof. Rajesh Verma", "rajesh.verma@smartlearn.in", "9844556677", "faculty123", User.Role.FACULTY, 1, "Database Management Systems");
        User facAlias = new User(7, "Dr. Neha Kapoor (Faculty Login)", "faculty@lms.com", "9811223300", "faculty123", User.Role.FACULTY, 1, "Computer Science & Engineering");

        users.put(fac1.getId(), fac1);
        users.put(fac2.getId(), fac2);
        users.put(fac3.getId(), fac3);
        users.put(fac4.getId(), fac4);
        users.put(facAlias.getId(), facAlias);

        // ── 3. Students ────────────────────────────────────────────────────────
        User s1 = new User(8, "Aarav Sharma", "aarav.sharma@smartlearn.in", "9876543211", "student123", User.Role.STUDENT, 1, "Computer Science (Roll: 23CSE101)");
        User sAlias = new User(9, "Aarav Sharma (Student Login)", "student@lms.com", "9876543212", "student123", User.Role.STUDENT, 1, "Computer Science (Roll: 23CSE101)");
        User s2 = new User(10, "Ananya Verma", "ananya.verma@smartlearn.in", "9876543213", "student123", User.Role.STUDENT, 1, "Computer Science (Roll: 23CSE102)");
        User s3 = new User(11, "Rohan Mehta", "rohan.mehta@smartlearn.in", "9876543214", "student123", User.Role.STUDENT, 1, "Information Technology (Roll: 23IT201)");
        User s4 = new User(12, "Priya Singh", "priya.singh@smartlearn.in", "9876543215", "student123", User.Role.STUDENT, 1, "AI & Data Science (Roll: 23AI301)");
        User s5 = new User(13, "Aditi Mishra", "aditi.mishra@smartlearn.in", "9876543216", "student123", User.Role.STUDENT, 1, "Computer Science (Roll: 23CSE105)");
        User s6 = new User(14, "Rahul Gupta", "rahul.gupta@smartlearn.in", "9876543217", "student123", User.Role.STUDENT, 1, "Information Technology (Roll: 23IT205)");
        User s7 = new User(15, "Sneha Iyer", "sneha.iyer@smartlearn.in", "9876543218", "student123", User.Role.STUDENT, 1, "AI & Data Science (Roll: 23AI304)");
        User s8 = new User(16, "Arjun Nair", "arjun.nair@smartlearn.in", "9876543219", "student123", User.Role.STUDENT, 1, "Electronics & Comm (Roll: 23ECE401)");
        User s9 = new User(17, "Kavya Reddy", "kavya.reddy@smartlearn.in", "9876543220", "student123", User.Role.STUDENT, 1, "Computer Science (Roll: 23CSE112)");
        User s10 = new User(18, "Ishaan Malhotra", "ishaan.malhotra@smartlearn.in", "9876543221", "student123", User.Role.STUDENT, 1, "Information Technology (Roll: 23IT210)");

        users.put(s1.getId(), s1);
        users.put(sAlias.getId(), sAlias);
        users.put(s2.getId(), s2);
        users.put(s3.getId(), s3);
        users.put(s4.getId(), s4);
        users.put(s5.getId(), s5);
        users.put(s6.getId(), s6);
        users.put(s7.getId(), s7);
        users.put(s8.getId(), s8);
        users.put(s9.getId(), s9);
        users.put(s10.getId(), s10);

        // ── 4. Indian University Course Catalogue ──────────────────────────────
        Course c1 = new Course(1, "Data Structures and Algorithms in Java", "Master Asymptotic Complexity, Balanced Trees, Graphs, Dynamic Programming & Java Collections.", fac2.getId(), fac2.getName(), "Computer Science");
        Course c2 = new Course(2, "Object-Oriented Programming with Java", "Deep dive into OOP principles, Lambdas, Streams, Virtual Threads & Concurrency in Java 21.", fac1.getId(), fac1.getName(), "Software Engineering");
        Course c3 = new Course(3, "Database Management Systems", "Relational Algebra, SQL query optimization, Indexing, B-Trees & ACID Transaction Management.", fac4.getId(), fac4.getName(), "Database Engineering");
        Course c4 = new Course(4, "Operating Systems", "Process scheduling, Virtual Memory, Deadlock handling, IPC & Linux kernel system calls.", fac1.getId(), fac1.getName(), "Systems Engineering");
        Course c5 = new Course(5, "Computer Networks", "OSI & TCP/IP architecture, Subnetting, Socket programming, Routing protocols & Network security.", fac4.getId(), fac4.getName(), "Networking");
        Course c6 = new Course(6, "Artificial Intelligence Fundamentals", "Informed search strategies, Heuristics, Adversarial search, Knowledge Representation & Logic.", fac3.getId(), fac3.getName(), "Artificial Intelligence");
        Course c7 = new Course(7, "Machine Learning Essentials", "Supervised and Unsupervised Learning, Regression, Classification, SVM, Clustering & Scikit-Learn.", fac3.getId(), fac3.getName(), "Data Science");
        Course c8 = new Course(8, "Web Development with React", "Modern SPA development, React Hooks, State Management, REST API integration & Tailwind.", fac1.getId(), fac1.getName(), "Web Technologies");
        Course c9 = new Course(9, "Python Programming", "Functional programming in Python, NumPy, Pandas, Data Wrangling & Automation scripting.", fac2.getId(), fac2.getName(), "Programming");
        Course c10 = new Course(10, "Software Engineering", "Agile Scrum methodologies, UML design diagrams, CI/CD pipelines & Software Quality Testing.", fac2.getId(), fac2.getName(), "Software Engineering");

        c1.setEnrolledStudentsCount(8);
        c2.setEnrolledStudentsCount(7);
        c3.setEnrolledStudentsCount(6);
        c4.setEnrolledStudentsCount(5);
        c5.setEnrolledStudentsCount(5);
        c6.setEnrolledStudentsCount(6);
        c7.setEnrolledStudentsCount(4);
        c8.setEnrolledStudentsCount(5);
        c9.setEnrolledStudentsCount(4);
        c10.setEnrolledStudentsCount(3);

        courses.put(c1.getId(), c1);
        courses.put(c2.getId(), c2);
        courses.put(c3.getId(), c3);
        courses.put(c4.getId(), c4);
        courses.put(c5.getId(), c5);
        courses.put(c6.getId(), c6);
        courses.put(c7.getId(), c7);
        courses.put(c8.getId(), c8);
        courses.put(c9.getId(), c9);
        courses.put(c10.getId(), c10);

        // ── 5. Student Enrollments ─────────────────────────────────────────────
        // Aarav Sharma (ID 8 & 9)
        enrollments.put(1, new Enrollment(1, s1.getId(), s1.getName(), c1.getId(), c1.getTitle(), 100)); // 100% completed - Ready for Certificate
        enrollments.put(2, new Enrollment(2, s1.getId(), s1.getName(), c2.getId(), c2.getTitle(), 65));
        enrollments.put(3, new Enrollment(3, s1.getId(), s1.getName(), c3.getId(), c3.getTitle(), 45));
        enrollments.put(4, new Enrollment(4, s1.getId(), s1.getName(), c6.getId(), c6.getTitle(), 25));

        // Alias for student@lms.com
        enrollments.put(5, new Enrollment(5, sAlias.getId(), sAlias.getName(), c1.getId(), c1.getTitle(), 100));
        enrollments.put(6, new Enrollment(6, sAlias.getId(), sAlias.getName(), c2.getId(), c2.getTitle(), 65));

        // Other Students
        enrollments.put(7, new Enrollment(7, s2.getId(), s2.getName(), c1.getId(), c1.getTitle(), 85));
        enrollments.put(8, new Enrollment(8, s2.getId(), s2.getName(), c6.getId(), c6.getTitle(), 60));
        enrollments.put(9, new Enrollment(9, s3.getId(), s3.getName(), c3.getId(), c3.getTitle(), 70));
        enrollments.put(10, new Enrollment(10, s3.getId(), s3.getName(), c5.getId(), c5.getTitle(), 40));
        enrollments.put(11, new Enrollment(11, s4.getId(), s4.getName(), c6.getId(), c6.getTitle(), 90));
        enrollments.put(12, new Enrollment(12, s4.getId(), s4.getName(), c7.getId(), c7.getTitle(), 80));

        // ── 6. Video Lectures & Study Notes ────────────────────────────────────
        videoLectures.put(1, new VideoLecture(1, c1.getId(), c1.getTitle(),
                "Module 1: Asymptotic Complexity & Java Collections Framework",
                "Deep analysis of Big-O, Big-Omega notation. Internal architecture of ArrayList vs LinkedList.",
                "45 mins", "dsa_mod1_complexity.mp4",
                "Key Notes:\n- Big-O denotes worst-case upper bound.\n- ArrayList uses dynamic array doubling (amortized O(1)).\n- LinkedList requires node pointers, incurring cache locality overhead."));

        videoLectures.put(2, new VideoLecture(2, c1.getId(), c1.getTitle(),
                "Module 2: Self-Balancing BSTs & AVL Tree Rotations",
                "Understanding balance factors, LL, RR, LR, and RL rotations for strict O(log n) performance.",
                "52 mins", "dsa_mod2_avl_trees.mp4",
                "Key Notes:\n- Balance Factor = height(left) - height(right).\n- AVL trees ensure height <= 1.44 log2(n).\n- Single rotations (LL/RR) and double rotations (LR/RL) re-balance after insertion."));

        videoLectures.put(3, new VideoLecture(3, c1.getId(), c1.getTitle(),
                "Module 3: Graph Traversal & Shortest Path (Dijkstra & BFS)",
                "Adjacency list representation, Breadth First Search, and PriorityQueue-backed Dijkstra algorithm.",
                "58 mins", "dsa_mod3_graphs.mp4",
                "Key Notes:\n- BFS finds shortest path in unweighted graphs in O(V + E).\n- Dijkstra calculates shortest paths from single source with non-negative edge weights in O((V + E) log V)."));

        videoLectures.put(4, new VideoLecture(4, c2.getId(), c2.getTitle(),
                "Module 1: Functional Programming & Stream Pipeline in Java 21",
                "Intermediate operations (filter, map, flatMap) vs terminal operations (collect, reduce, count).",
                "48 mins", "java_mod1_streams.mp4",
                "Key Notes:\n- Streams are lazy: computations only execute when terminal operation is triggered.\n- Prefer immutable data structures using Java records."));

        videoLectures.put(5, new VideoLecture(5, c3.getId(), c3.getTitle(),
                "Module 1: Relational Normalization & BCNF Decomposition",
                "Functional dependencies, 1NF, 2NF, 3NF, and Boyce-Codd Normal Form loss-less joins.",
                "50 mins", "dbms_mod1_normalization.mp4",
                "Key Notes:\n- BCNF requires that for every functional dependency X -> Y, X must be a superkey.\n- Prevents update, insertion, and deletion anomalies."));

        // ── 7. Assignments & Homework ──────────────────────────────────────────
        Assignment a1 = new Assignment(1, c1.getId(), c1.getTitle(),
                "Implementation of Self-Balancing AVL Tree with Deletion",
                "Write a complete generic Java class AVLTree<T extends Comparable<T>> implementing insert, delete, and inorder traversal with rotation methods.",
                LocalDate.now().plusDays(5), 100, fac2.getId(), fac2.getName());

        Assignment a2 = new Assignment(2, c2.getId(), c2.getTitle(),
                "Multi-Threaded Banking System with Java Virtual Threads",
                "Construct a simulated bank transaction engine handling concurrent deposits and transfers with proper mutex synchronizations.",
                LocalDate.now().plusDays(7), 100, fac1.getId(), fac1.getName());

        Assignment a3 = new Assignment(3, c3.getId(), c3.getTitle(),
                "Relational Schema Design & ACID Transaction Processing",
                "Design a normalized BCNF schema for a university examination portal and provide optimized SQL queries.",
                LocalDate.now().plusDays(3), 50, fac4.getId(), fac4.getName());

        assignments.put(a1.getId(), a1);
        assignments.put(a2.getId(), a2);
        assignments.put(a3.getId(), a3);

        // ── 8. Assignment Submissions ──────────────────────────────────────────
        AssignmentSubmission sub1 = new AssignmentSubmission(1, a1.getId(), a1.getTitle(), c1.getId(), c1.getTitle(),
                s1.getId(), s1.getName(),
                "// AVL Tree Implementation in Java\npublic class AVLTree<T extends Comparable<T>> {\n    private class Node { T key; int height; Node left, right; }\n    // Implemented rightRotate, leftRotate, and rebalance after recursive delete.");
        sub1.setMarks(96);
        sub1.setFeedback("Outstanding solution, Aarav! Proper balance factor calculations and clean generic comparison.");
        sub1.setStatus(AssignmentSubmission.Status.GRADED);

        AssignmentSubmission sub2 = new AssignmentSubmission(2, a2.getId(), a2.getTitle(), c2.getId(), c2.getTitle(),
                s1.getId(), s1.getName(),
                "// Virtual Threads Banking Engine\nExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();\n// Synchronized account ledger with atomic balance transfers.");
        sub2.setStatus(AssignmentSubmission.Status.SUBMITTED);

        submissions.put(sub1.getId(), sub1);
        submissions.put(sub2.getId(), sub2);

        // ── 9. Timed MCQ Quizzes & Assessments ─────────────────────────────────
        Exam exam1 = new Exam(1, c1.getId(), c1.getTitle(), "DSA Midterm Assessment 2026", 45, 40, fac2.getId(), fac2.getName(), "Live");

        Question q1 = new Question(1, 1, "What is the worst-case time complexity of searching in a Balanced Binary Search Tree (AVL Tree)?", 10,
                Arrays.asList("O(1)", "O(n)", "O(log n)", "O(n log n)"), 2);
        Question q2 = new Question(2, 1, "Which data structure is optimal to implement Dijkstra's Single Source Shortest Path algorithm?", 10,
                Arrays.asList("Binary Search Tree", "Min-Heap / PriorityQueue", "Stack", "Circular Queue"), 1);
        Question q3 = new Question(3, 1, "Which collection class in java.util guarantees unique elements and sorts by natural order?", 10,
                Arrays.asList("HashSet", "TreeSet", "LinkedHashSet", "PriorityQueue"), 1);
        Question q4 = new Question(4, 1, "What is the space complexity of Depth First Search (DFS) on a tree of maximum height h?", 10,
                Arrays.asList("O(1)", "O(h)", "O(2^h)", "O(n^2)"), 1);

        exam1.getQuestions().addAll(Arrays.asList(q1, q2, q3, q4));
        exams.put(exam1.getId(), exam1);

        Exam exam2 = new Exam(2, c2.getId(), c2.getTitle(), "Java OOPs & JVM Architecture Assessment", 30, 20, fac1.getId(), fac1.getName(), "Live");
        Question eq1 = new Question(5, 2, "Which Java 16+ feature introduces immutable, transparent data-carrier classes?", 10,
                Arrays.asList("Interfaces", "Records", "Enums", "Abstract Classes"), 1);
        Question eq2 = new Question(6, 2, "Which garbage collector in modern HotSpot JVM is designed for ultra-low latency sub-millisecond pauses?", 10,
                Arrays.asList("Serial GC", "Parallel GC", "ZGC (Z Garbage Collector)", "CMS GC"), 2);
        exam2.getQuestions().addAll(Arrays.asList(eq1, eq2));
        exams.put(exam2.getId(), exam2);

        // ── 10. Exam Results ───────────────────────────────────────────────────
        ExamResult res1 = new ExamResult(1, exam1.getId(), exam1.getTitle(), c1.getId(), c1.getTitle(), s1.getId(), s1.getName(), 40, 40);
        ExamResult res2 = new ExamResult(2, exam1.getId(), exam1.getTitle(), c1.getId(), c1.getTitle(), s2.getId(), s2.getName(), 30, 40);
        examResults.put(res1.getId(), res1);
        examResults.put(res2.getId(), res2);

        // ── 11. Doubts & Mentorship Q&A ────────────────────────────────────────
        Doubt d1 = new Doubt(1, c1.getId(), c1.getTitle(), s1.getId(), s1.getName(),
                "In Java PriorityQueue, how do we implement a custom comparator for a Max-Heap instead of the default Min-Heap?");
        d1.setAnswer("You can pass Collections.reverseOrder() or a custom lambda comparator (a, b) -> Integer.compare(b, a) to the PriorityQueue constructor.");
        d1.setFacultyName(fac2.getName());
        d1.setStatus(Doubt.Status.RESOLVED);

        Doubt d2 = new Doubt(2, c3.getId(), c3.getTitle(), s1.getId(), s1.getName(),
                "Could you please explain why BCNF is considered strictly stronger than 3NF, and when a 3NF relation is not in BCNF?");
        d2.setAnswer("A relation is in 3NF if for every FD X -> A, either X is a superkey or A is a prime attribute. BCNF removes the prime attribute relaxation—X must be a superkey in all cases.");
        d2.setFacultyName(fac4.getName());
        d2.setStatus(Doubt.Status.RESOLVED);

        Doubt d3 = new Doubt(3, c2.getId(), c2.getTitle(), s2.getId(), s2.getName(),
                "When should we prefer Virtual Threads over standard Platform Threads in Java 21?");
        doubts.put(d1.getId(), d1);
        doubts.put(d2.getId(), d2);
        doubts.put(d3.getId(), d3);

        // ── 12. Official Campus Notices ────────────────────────────────────────
        Notice n1 = new Notice(1, "Odd Semester End-Term Examinations 2026-27 Schedule",
                "The Office of the Controller of Examinations has published the final datesheet for all B.Tech and M.Tech departments. Practical lab vivas commence from 15th Nov 2026.",
                "Academic", "Controller of Examinations", true);

        Notice n2 = new Notice(2, "AICTE Smart India Hackathon (SIH 2026) Registrations Open",
                "Nominations are invited from student teams for the National Smart India Hackathon 2026. Top college teams will receive ₹1,00,000 innovation grant.",
                "National Initiative", "Dean of Student Affairs", true);

        Notice n3 = new Notice(3, "Campus Recruitment Drive: TCS, Infosys & Google India",
                "Pre-placement talks and technical coding aptitude assessments will be held in the Central Auditorium on Monday at 09:30 AM IST. Formal attire mandatory.",
                "Placement", "Training & Placement Cell", true);

        Notice n4 = new Notice(4, "Technical Paper Presentation & Project Expo — Technovision 2026",
                "Submit your original research papers on Cloud Computing, AI and IoT to the IEEE Student Branch by 5th Nov 2026.",
                "Event", "IEEE Student Branch", false);

        notices.put(n1.getId(), n1);
        notices.put(n2.getId(), n2);
        notices.put(n3.getId(), n3);
        notices.put(n4.getId(), n4);

        // ── 13. Institutional FAQs ─────────────────────────────────────────────
        faqs.put(1, new FAQ(1, "How do I enroll in a new university course?", "Navigate to the 'Course Catalog' tab, browse the available curriculum subjects, and click the 'Enroll in Selected Course' button.", "Enrollment"));
        faqs.put(2, new FAQ(2, "How is course attendance monitored?", "Faculty members record session attendance daily. You can inspect your real-time attendance percentage under the 'Attendance & Academics' tab. AICTE requires minimum 75% attendance.", "Attendance"));
        faqs.put(3, new FAQ(3, "How do I claim my Verified Certificate of Excellence?", "Once your course completion progress reaches 100%, an authenticated 'Generate Certificate' button is enabled with an official credential hash.", "Certification"));
        faqs.put(4, new FAQ(4, "How are assignment submissions evaluated?", "Course faculty review your code/solutions, assign numeric scores, and deliver personalized constructive feedback within 48 hours of submission.", "Grading"));

        // ── 14. Seed Academic Attendance Records ───────────────────────────────
        seedAttendanceRecords(s1.getId(), s1.getName(), c1.getId(), c1.getTitle());
        seedAttendanceRecords(s1.getId(), s1.getName(), c2.getId(), c2.getTitle());
        seedAttendanceRecords(sAlias.getId(), sAlias.getName(), c1.getId(), c1.getTitle());
        seedAttendanceRecords(sAlias.getId(), sAlias.getName(), c2.getId(), c2.getTitle());
        seedAttendanceRecords(s2.getId(), s2.getName(), c1.getId(), c1.getTitle());
    }

    private void seedAttendanceRecords(int studentId, String studentName, int courseId, String courseTitle) {
        LocalDate start = LocalDate.now().minusWeeks(4);
        for (int i = 0; i < 16; i++) {
            LocalDate d = start.plusDays(i * 2);
            String status = (i == 4 || i == 11) ? "ABSENT" : "PRESENT";
            int id = attendanceIdGen.incrementAndGet();
            AttendanceRecord rec = new AttendanceRecord(id, studentId, studentName, courseId, courseTitle, d, status, "Lecture Session " + (i + 1));
            attendance.put(id, rec);
        }
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

    // ── Attendance ─────────────────────────────────────────────────────────────
    public synchronized List<AttendanceRecord> getAllAttendance() { return new ArrayList<>(attendance.values()); }

    public synchronized List<AttendanceRecord> getAttendanceByStudent(int studentId) {
        List<AttendanceRecord> list = new ArrayList<>();
        for (AttendanceRecord r : attendance.values()) {
            if (r.getStudentId() == studentId) list.add(r);
        }
        return list;
    }

    public synchronized List<AttendanceRecord> getAttendanceByCourse(int courseId) {
        List<AttendanceRecord> list = new ArrayList<>();
        for (AttendanceRecord r : attendance.values()) {
            if (r.getCourseId() == courseId) list.add(r);
        }
        return list;
    }

    public synchronized AttendanceRecord markAttendance(int studentId, String studentName, int courseId, String courseTitle, LocalDate date, String status, String topic) {
        int id = attendanceIdGen.incrementAndGet();
        AttendanceRecord r = new AttendanceRecord(id, studentId, studentName, courseId, courseTitle, date, status, topic);
        attendance.put(id, r);
        saveToDisk();
        return r;
    }

    public synchronized double getAttendancePercentage(int studentId, int courseId) {
        List<AttendanceRecord> list = new ArrayList<>();
        for (AttendanceRecord r : attendance.values()) {
            if (r.getStudentId() == studentId && r.getCourseId() == courseId) {
                list.add(r);
            }
        }
        if (list.isEmpty()) return 100.0;
        long present = list.stream().filter(AttendanceRecord::isPresent).count();
        return (present * 100.0) / list.size();
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

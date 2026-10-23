# 🎓 SmartLearn India — Smart Learning Management System (LMS)

> **Tagline:** Learn Smarter. Grow Further.  
> **Positioning:** A modern, India-focused academic Learning Management System engineered for Students, Faculty, and Institutional Administrators.

---

## 🏛️ Executive Summary

**SmartLearn India** is an enterprise-grade desktop LMS platform built on pure Java SE. It delivers a modern, high-DPI desktop application featuring role-based portals, curriculum management, lecture video players, homework submissions with rubric grading, timed auto-graded MCQ examinations with question palettes, AICTE-compliant attendance tracking, and verified certificate generation.

---

## 🚀 Quick Start (1-Click Run)

### Option 1: Double-Click Launcher (Windows)
- Double-click **[`run_gui.bat`](run_gui.bat)** in the root folder, or
- Navigate to **`java gui/`** and double-click **[`run.bat`](java%20gui/run.bat)**.

### Option 2: Run Packaged Executable JAR
```powershell
# From java gui/ directory:
java -jar SmartLearn-India-LMS.jar
```

### Option 3: Compile & Run from Source (Terminal)
```powershell
cd "java gui"

# Compile all modules with UTF-8
javac -encoding UTF-8 -d bin src\com\lms\gui\model\*.java src\com\lms\gui\db\*.java src\com\lms\gui\theme\*.java src\com\lms\gui\views\*.java src\com\lms\gui\test\*.java src\com\lms\gui\*.java

# Launch application
java -cp bin com.lms.gui.LmsGuiApplication
```

---

## 🔑 Demo Accounts (With 1-Click Auto-Fill Buttons)

The login screen features **1-Click Instant Demo Login** buttons for instant role switching during presentations:

| Portal Role | Name & Designation | Email Address | Password | Key Capabilities |
| :--- | :--- | :--- | :--- | :--- |
| **👑 Master Admin** | Admin User — SmartLearn India | `admin@smartlearn.in` | `admin123` | Institutional analytics, user accounts governance, curriculum approvals, campus bulletins broadcast, doubt SLA supervision. |
| **👨‍🏫 Faculty** | Dr. Neha Kapoor (Dean of Academics) | `neha.kapoor@smartlearn.in` | `faculty123` | Course creation, lecture module uploads, homework publishing, code solution rubric grading, timed MCQ quiz builder, attendance recording, doubt mentoring. |
| **👨‍🎓 Student** | Aarav Sharma (Roll: `23CSE101`) | `aarav.sharma@smartlearn.in` | `student123` | Interactive study dashboard, 1-click course enrollment, lecture viewer with progress tracking, homework submission, timed MCQ exams with live timer, attendance audits, verified certificate generation. |

> *Note: Legacy login aliases (`admin@lms.com`, `faculty@lms.com`, `student@lms.com`) are fully supported for backwards compatibility.*

---

## 💻 Technology Stack & Architecture

- **Language & Runtime:** Java SE 21 LTS (pure Java, zero external third-party jar dependencies needed).
- **GUI Framework:** Java Swing & AWT with custom 2D anti-aliasing rendering hints (`RenderingHints.KEY_TEXT_ANTIALIAS_LCD_HRGB`).
- **Design System:** Custom vector graphics engine (`VectorIcon`), official brand emblem generator (`BrandLogo`), floating toast notifications (`Toast`), interactive password visibility toggles (`ModernPasswordField`), and custom Java 2D vector charts (`ModernChart`).
- **Architecture Pattern:** Model-View-Controller (MVC) + Singleton Thread-Safe DataStore with synchronized multi-thread safety.
- **Persistence:** Local binary serialization (`lms_gui_data.ser`) preserving all courses, user records, homework submissions, exam scores, doubts, and attendance across application restarts.

---

## 📊 Features Actually Implemented & Verified

### 1. Unified Application Shell
- **Custom Vector Iconography:** Pure Java 2D vector glyphs for courses, lectures, exams, doubts, notices, attendance, and certificates (no emojis or broken remote assets).
- **Top Navigation Bar:** Persistent breadcrumb navigation, live global search field, unread campus bulletin indicator with badge counter and dropdown, and user avatar chip with department pill.
- **Floating Toast System:** Non-blocking feedback banners (Success, Info, Warning, Error) confirming operations.
- **Interactive Security:** Built-in password visibility toggle (eye / eye-slash) in login and registration dialogs.

### 2. Student Portal
- **Academic Dashboard:** Personalized Indian greeting (`Namaste, Aarav Sharma! 🙏`), 6 real-time stat cards, weekly study hours 2D bar chart, and curriculum completion donut chart.
- **National Course Catalog:** 10 Indian university courses with real-time keyword search, faculty allocations, and 1-click enrollment.
- **Learning & Lecture Hub:** Lecture module viewer, simulated video player with scrub bar, key study notes, and "Mark Lesson as Completed (+20% Progress)" button updating real-time stats.
- **Lab Homework & Code Submissions:** View homework problem statements, submit code or solutions, and inspect numerical marks and constructive faculty remarks.
- **Timed MCQ Examination Engine:** Live countdown timer, question palette, answer selection, auto-scoring calculation, and official transcript summary dialog.
- **Attendance & Academics:** Course-wise session logs with AICTE 75% mandatory attendance rule verification badge.
- **Verified Certificate of Excellence:** Authenticated certificate modal featuring student roll number, course title, final score, verification hash, and dual signatures.
- **Doubts & Mentorship Q&A:** Post academic inquiries directly to course faculty with real-time status tracking.

### 3. Faculty Portal
- **Academic KPI Overview:** Active courses taught, enrolled student counts, pending homework to review, and doubts awaiting reply.
- **Course & Lecture Authoring:** Create new university courses, allocate syllabi, and upload lecture modules with study notes.
- **Homework & Rubric Grading:** Publish homework with maximum marks and due dates; review student submissions and enter numerical scores with written remarks.
- **MCQ Exam Builder:** Create timed quizzes, add 4-option multiple-choice questions with answer keys, and audit student score transcripts.
- **Attendance Marker:** Record daily class attendance (Present / Absent / Late) for enrolled students.
- **Doubt Mentoring:** Review questions posted by students and submit direct faculty answers.

### 4. Master Administrator Portal
- **Institutional Governance:** Overall student and faculty headcount KPIs, departmental distribution summary, and system status audit.
- **User Directory:** Live search, add new user accounts, toggle Active/Inactive account status, view profiles, and safely delete accounts.
- **Curriculum Governance:** Add and manage academic courses across engineering departments.
- **Campus Notice Broadcast:** Broadcast university announcements with optional priority pinning.
- **Doubt Supervision:** Audit all student inquiries and faculty response SLAs across departments.

---

## 🧪 Automated Regression Test Suite

An automated test suite (`LmsVerificationSuite.java`) executes 30 regression test cases verifying core functionality:

```powershell
# Run the automated verification suite
java -cp bin com.lms.gui.test.LmsVerificationSuite
```

### Actual Test Results (Verified 100% Passing):
```
==================================================================
       SmartLearn India — Comprehensive Automated Test Suite       
==================================================================

1. Testing DataStore & Dataset Initialization...
  [PASS] DataStore instance is non-null
  [PASS] User directory seeded with 10+ accounts (found: 18)
  [PASS] Indian Student 'Aarav Sharma' present in directory
  [PASS] Indian Faculty 'Dr. Neha Kapoor' present in directory
  [PASS] 10 Indian University courses seeded (found: 10)
  [PASS] Course 'Data Structures and Algorithms in Java' present

2. Testing Authentication & Role Permissions...
  [PASS] Admin login with admin@smartlearn.in succeeded
  [PASS] Legacy Admin alias admin@lms.com login succeeded
  [PASS] Faculty login with neha.kapoor@smartlearn.in succeeded
  [PASS] Student login with aarav.sharma@smartlearn.in succeeded
  [PASS] Invalid password correctly rejected
  [PASS] Non-existent user correctly rejected

3. Testing User Registration & Duplicate Prevention...
  [PASS] New student account registration succeeded
  [PASS] Registered user retrievable by email
  [PASS] Duplicate email registration correctly throws exception

4. Testing Course Enrollment & Progress Updates...
  [PASS] Student enrolled in Computer Networks
  [PASS] isEnrolled returns true after enrollment
  [PASS] Course progress updated to 85%

5. Testing Assignment Submission & Rubric Grading...
  [PASS] Assignment created by faculty
  [PASS] Student submitted solution successfully
  [PASS] Faculty awarded marks and status changed to GRADED

6. Testing Timed MCQ Exam Auto-Grading...
  [PASS] Exams available in question bank
  [PASS] Exam has questions configured
  [PASS] Exam result recorded with calculated score
  [PASS] Exam status correctly marked PASSED (>= 50%)

7. Testing Doubts Q&A Mentorship...
  [PASS] Doubt inquiry posted with status PENDING
  [PASS] Doubt answer recorded and status updated to RESOLVED

8. Testing Course Attendance & AICTE 75% Check...
  [PASS] Attendance marked PRESENT for student
  [PASS] Attendance percentage calculated correctly (found: 88.2%)

9. Testing Binary Disk Persistence...
  [PASS] Serialized data file lms_gui_data.ser exists on disk
  Data file size: 24,130 bytes

------------------------------------------------------------------
TEST SUMMARY: 30 PASSED, 0 FAILED
------------------------------------------------------------------
>>> ALL SMARTLEARN INDIA WORKFLOWS VERIFIED 100% SUCCESSFULLY! <<<
```

---

## 🎯 7-Step Viva & Teacher Evaluation Sequence

Follow this exact walkthrough to present a polished, complete demonstration:

### Step 1: Login Gateway & Aesthetics
1. Launch the application via `run_gui.bat`.
2. Point out the **SmartLearn India** logo, tagline (*Learn Smarter. Grow Further.*), and the high-contrast split-panel layout.
3. Demonstrate the **password visibility toggle (eye icon)** on the password field.
4. Show the **1-Click Demo Login** buttons on the left panel.

### Step 2: Student Portal & 2D Analytics
1. Click the **"Student: Aarav Sharma"** quick-login button and sign in.
2. Highlight the **"Namaste, Aarav Sharma! 🙏"** greeting and the top navigation bar with breadcrumbs and user avatar chip.
3. Review the **Weekly Study Activity 2D bar chart** and the **Curriculum Completion Donut Chart**.
4. Click **"Claim Verified Certificate"** to showcase the accredited **Certificate of Academic Excellence** with its verification hash.

### Step 3: Video Lecture & Real-Time Progress
1. Navigate to **"My Learning & Videos"**.
2. Select a lecture module (e.g., *Module 2: Self-Balancing BSTs*) and click **"Watch Lecture & Study Notes"**.
3. Point out the video scrubber and syllabus notes, then click **"Mark Lesson as Completed (+20% Progress)"**.
4. Observe the floating **Toast confirmation** and the immediate progress bar update on the dashboard.

### Step 4: Timed MCQ Exam with Live Auto-Grading
1. Navigate to **"Quizzes & Exams"**.
2. Click **"Start Timed Exam Session"** on the *DSA Midterm Assessment 2026*.
3. Highlight the **live ticking timer**, **question palette**, and question navigation.
4. Select answers and click **"Finish & Submit Exam"** to show instant auto-grading and transcript generation.

### Step 5: Homework Submission & AICTE Attendance
1. Navigate to **"Assignments"** and submit a sample code solution.
2. Navigate to **"Attendance & Academics"** to show the session records and compliance with the AICTE 75% minimum attendance rule.

### Step 6: Faculty Portal & Rubric Grading
1. Click **"Sign Out"** and log in as **"Faculty: Dr. Neha Kapoor"**.
2. Open **"Assignments & Grading"** and select the pending submission from Aarav Sharma.
3. Click **"Evaluate & Assign Marks"**, enter marks (e.g., `98/100`) and written remarks, and save.
4. Open **"Record Attendance"** and demonstrate marking attendance for a student.

### Step 7: Master Administrator Governance
1. Click **"Sign Out"** and log in as **"Master Admin"**.
2. Review system analytics, toggle user account active/inactive status in the **User Directory**, and broadcast a campus notice.
3. Close the application, relaunch, and demonstrate that **all data persists cleanly** from `lms_gui_data.ser`.

---

## 📋 Pre-Presentation Verification Checklist

- [x] Java 21 runtime verified (`java -version`).
- [x] Compilation completes with 0 errors (`javac -d bin ...`).
- [x] Automated test suite passes 100% (`LmsVerificationSuite`).
- [x] Packaged JARs (`SmartLearn-India-LMS.jar`) generated.
- [x] 1-Click quick launch script (`run_gui.bat`) operational.
- [x] Password visibility toggle verified on login screen.
- [x] Student, Faculty, and Admin roles tested and functional.
- [x] All 10 Indian university courses loaded with correct instructors.
- [x] Live timer and auto-grading tested on MCQ exam dialog.
- [x] Certificate of Excellence generator verified.
- [x] Real-time state persistence to `lms_gui_data.ser` confirmed.

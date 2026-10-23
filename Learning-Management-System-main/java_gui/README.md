# 🎓 EduPro Learning Management System (LMS) - Desktop GUI

A standalone, native Java desktop application replicating the full Learning Management System platform with a modern user interface, role-based security, and real-time state persistence.

---

## 🚀 How to Run the Application

### Option 1: One-Click Launcher (Windows)
Double-click the **`run.bat`** file located in this folder.

### Option 2: Run Packaged Executable JAR
Open terminal / command prompt in this directory and run:
```bash
java -jar EduPro-LMS-Desktop.jar
```

### Option 3: Compile & Run from Source
```bash
# Compile
javac -encoding UTF-8 -d bin src\com\lms\gui\model\*.java src\com\lms\gui\db\*.java src\com\lms\gui\theme\*.java src\com\lms\gui\views\*.java src\com\lms\gui\*.java

# Run
java -cp bin com.lms.gui.LmsGuiApplication
```

---

## 🔑 Login Credentials & Demo Accounts

The login screen features **1-Click Quick Demo Login** buttons so you can switch between roles seamlessly during presentations!

| Role | Email | Password | Features Accessible |
| :--- | :--- | :--- | :--- |
| **👑 Master Admin** | `admin@lms.com` | `admin123` | System KPI metrics, user management, course approvals, notice broadcasts, doubt supervision |
| **👨‍🏫 Faculty** | `faculty@lms.com` | `faculty123` | Course creation, lecture materials, assignments, student grading & feedback, MCQ quiz builder, answering doubts |
| **👨‍🎓 Student** | `student@lms.com` | `student123` | Course catalog & enrollment, video lessons & notes, assignment submission, timed MCQ exams, asking doubts, FAQs |

---

## 🎯 Viva & Teacher Presentation Walkthrough Guide

Follow this 4-step sequence to impress your teacher during tomorrow's evaluation:

### Step 1: Login Screen & Architecture
- Highlight the **clean split-panel design** with feature summary and modern aesthetics.
- Demonstrate **New Account Registration** (`Create One` button) for new students/faculty.
- Use the **1-Click Quick Demo** buttons on the left panel to log in without retyping passwords.

### Step 2: Master Admin Portal
1. **📊 Overview:** Live KPI summary cards showing active students, faculty, courses, enrollments, doubts, and notices.
2. **👥 User Management:** Search by name/email, filter by role (Student / Faculty / Admin), toggle active/inactive status, or delete accounts.
3. **📚 Course Catalog:** Create courses and allocate instructor responsibilities.
4. **📢 Campus Notices:** Broadcast university bulletins with priority pinning.
5. **💬 Doubt Monitor:** Supervise all student inquiries and faculty responses.

### Step 3: Faculty Portal
1. **📚 Courses & Lectures:** View assigned courses and upload lecture modules with study notes and video filenames.
2. **📝 Assignments & Grading:** Publish course homework with max marks and due dates. Select student submissions, enter numeric marks, and provide written feedback.
3. **🏆 MCQ Exam Builder:** Create timed quizzes and add multiple-choice questions with 4 options and answer keys.
4. **💬 Student Doubts:** View questions asked by enrolled students and send direct instructor answers.

### Step 4: Student Portal
1. **📊 Dashboard:** Track personal study progress percentage across enrolled courses.
2. **📖 Course Catalog:** Browse available university courses and enroll with 1 click.
3. **🎥 My Learning & Videos:** Select an enrolled course, view syllabus modules, open the lecture video player and study notes, and click **"Mark Lesson as Completed"** to increment progress!
4. **📑 Assignments:** View open homework, submit solutions / code, and view graded scores with teacher remarks.
5. **🎯 Quizzes & Exams:** Click **"Start Timed Exam Session"** to launch the live assessment with a ticking countdown timer, question palette, and instant auto-grading score card!
6. **💡 Ask Doubts & Q&A:** Post course-specific questions directly to instructors.
7. **📰 Notices & FAQs:** Browse university alerts and searchable FAQs.

---

## 🏗️ Technical Highlights for Viva Questions
- **Java SE Architecture:** Pure Java Swing & AWT without external heavy dependencies.
- **Design Patterns:**
  - **Singleton Pattern:** `DataStore` manages synchronized data persistence.
  - **MVC Architecture:** Clean separation between `model`, `db`, `theme`, and `views`.
  - **CardLayout Pattern:** Seamless, flicker-free dashboard navigation.
  - **2D Anti-aliasing:** Custom `Graphics2D` rendering for rounded panels, buttons, and typography.
- **Persistence:** Real-time serialization to `lms_gui_data.ser`, keeping all changes saved across application restarts.

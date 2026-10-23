package com.lms.gui.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int studentId;
    private String studentName;
    private int courseId;
    private String courseTitle;
    private LocalDate enrollmentDate;
    private int progress; // 0 - 100 %

    public Enrollment() {
        this.enrollmentDate = LocalDate.now();
        this.progress = 0;
    }

    public Enrollment(int id, int studentId, String studentName, int courseId, String courseTitle, int progress) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.enrollmentDate = LocalDate.now();
        this.progress = progress;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = Math.min(100, Math.max(0, progress)); }
}

package com.lms.gui.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExamResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int examId;
    private String examTitle;
    private int courseId;
    private String courseTitle;
    private int studentId;
    private String studentName;
    private int score;
    private int totalMarks;
    private double percentage;
    private String status; // "Pass", "Fail"
    private String attemptDate;

    public ExamResult() {
        this.attemptDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public ExamResult(int id, int examId, String examTitle, int courseId, String courseTitle, int studentId, String studentName, int score, int totalMarks) {
        this.id = id;
        this.examId = examId;
        this.examTitle = examTitle;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.studentId = studentId;
        this.studentName = studentName;
        this.score = score;
        this.totalMarks = totalMarks;
        this.percentage = totalMarks > 0 ? ((double) score / totalMarks) * 100.0 : 0.0;
        this.status = this.percentage >= 40.0 ? "PASSED" : "FAILED";
        this.attemptDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalMarks() { return totalMarks; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAttemptDate() { return attemptDate; }
    public void setAttemptDate(String attemptDate) { this.attemptDate = attemptDate; }
}

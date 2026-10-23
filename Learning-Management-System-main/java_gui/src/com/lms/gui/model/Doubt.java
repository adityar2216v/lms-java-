package com.lms.gui.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Doubt implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status {
        PENDING, RESOLVED
    }

    private int id;
    private int courseId;
    private String courseTitle;
    private int studentId;
    private String studentName;
    private String question;
    private String answer;
    private String facultyName;
    private Status status;
    private String createdAt;

    public Doubt() {
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public Doubt(int id, int courseId, String courseTitle, int studentId, String studentName, String question) {
        this.id = id;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.studentId = studentId;
        this.studentName = studentName;
        this.question = question;
        this.answer = null;
        this.facultyName = null;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

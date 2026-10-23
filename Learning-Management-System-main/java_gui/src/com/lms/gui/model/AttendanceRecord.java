package com.lms.gui.model;

import java.io.Serializable;
import java.time.LocalDate;

public class AttendanceRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int studentId;
    private String studentName;
    private int courseId;
    private String courseTitle;
    private LocalDate date;
    private String status; // "PRESENT", "ABSENT", "LATE"
    private String topic;

    public AttendanceRecord() {}

    public AttendanceRecord(int id, int studentId, String studentName, int courseId, String courseTitle, LocalDate date, String status, String topic) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.date = date;
        this.status = status;
        this.topic = topic;
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

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public boolean isPresent() {
        return "PRESENT".equalsIgnoreCase(status) || "LATE".equalsIgnoreCase(status);
    }
}

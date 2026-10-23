package com.lms.gui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int courseId;
    private String courseTitle;
    private String title;
    private int durationMinutes;
    private int totalMarks;
    private int facultyId;
    private String facultyName;
    private String status; // "Live", "Draft", "Completed"
    private List<Question> questions;

    public Exam() {
        this.questions = new ArrayList<>();
        this.status = "Live";
    }

    public Exam(int id, int courseId, String courseTitle, String title, int durationMinutes, int totalMarks, int facultyId, String facultyName, String status) {
        this.id = id;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.status = status != null ? status : "Live";
        this.questions = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public int getTotalMarks() { return totalMarks; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }

    public int getFacultyId() { return facultyId; }
    public void setFacultyId(int facultyId) { this.facultyId = facultyId; }

    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    @Override
    public String toString() {
        return title + " (" + courseTitle + ") - " + durationMinutes + " mins";
    }
}

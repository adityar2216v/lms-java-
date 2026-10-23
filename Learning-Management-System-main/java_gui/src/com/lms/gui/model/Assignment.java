package com.lms.gui.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Assignment implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int courseId;
    private String courseTitle;
    private String title;
    private String description;
    private LocalDate dueDate;
    private int maxMarks;
    private int createdByFacultyId;
    private String createdByFacultyName;

    public Assignment() {}

    public Assignment(int id, int courseId, String courseTitle, String title, String description, LocalDate dueDate, int maxMarks, int createdByFacultyId, String createdByFacultyName) {
        this.id = id;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.maxMarks = maxMarks;
        this.createdByFacultyId = createdByFacultyId;
        this.createdByFacultyName = createdByFacultyName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public int getMaxMarks() { return maxMarks; }
    public void setMaxMarks(int maxMarks) { this.maxMarks = maxMarks; }

    public int getCreatedByFacultyId() { return createdByFacultyId; }
    public void setCreatedByFacultyId(int createdByFacultyId) { this.createdByFacultyId = createdByFacultyId; }

    public String getCreatedByFacultyName() { return createdByFacultyName; }
    public void setCreatedByFacultyName(String createdByFacultyName) { this.createdByFacultyName = createdByFacultyName; }

    @Override
    public String toString() {
        return title + " - " + courseTitle;
    }
}

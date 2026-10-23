package com.lms.gui.model;

import java.io.Serializable;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String description;
    private int instructorId;
    private String instructorName;
    private String category;
    private int enrolledStudentsCount;

    public Course() {}

    public Course(int id, String title, String description, int instructorId, String instructorName, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.category = category;
        this.enrolledStudentsCount = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getEnrolledStudentsCount() { return enrolledStudentsCount; }
    public void setEnrolledStudentsCount(int enrolledStudentsCount) { this.enrolledStudentsCount = enrolledStudentsCount; }

    @Override
    public String toString() {
        return title + " (By: " + (instructorName != null ? instructorName : "TBA") + ")";
    }
}

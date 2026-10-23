package com.lms.gui.model;

import java.io.Serializable;
import java.time.LocalDate;

public class VideoLecture implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int courseId;
    private String courseTitle;
    private String title;
    private String description;
    private String duration; // e.g. "45 mins"
    private String fileName; // video file or URL
    private String notesText;
    private LocalDate uploadDate;

    public VideoLecture() {
        this.uploadDate = LocalDate.now();
    }

    public VideoLecture(int id, int courseId, String courseTitle, String title, String description, String duration, String fileName, String notesText) {
        this.id = id;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.fileName = fileName;
        this.notesText = notesText;
        this.uploadDate = LocalDate.now();
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

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getNotesText() { return notesText; }
    public void setNotesText(String notesText) { this.notesText = notesText; }

    public LocalDate getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDate uploadDate) { this.uploadDate = uploadDate; }
}

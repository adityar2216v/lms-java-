package com.lms.gui.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AssignmentSubmission implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status {
        SUBMITTED, GRADED
    }

    private int id;
    private int assignmentId;
    private String assignmentTitle;
    private int courseId;
    private String courseTitle;
    private int studentId;
    private String studentName;
    private String answerText;
    private String submissionTime;
    private Integer marks;
    private String feedback;
    private Status status;

    public AssignmentSubmission() {
        this.status = Status.SUBMITTED;
        this.submissionTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public AssignmentSubmission(int id, int assignmentId, String assignmentTitle, int courseId, String courseTitle, int studentId, String studentName, String answerText) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.assignmentTitle = assignmentTitle;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.studentId = studentId;
        this.studentName = studentName;
        this.answerText = answerText;
        this.submissionTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.marks = null;
        this.feedback = null;
        this.status = Status.SUBMITTED;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAssignmentId() { return assignmentId; }
    public void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }

    public String getAssignmentTitle() { return assignmentTitle; }
    public void setAssignmentTitle(String assignmentTitle) { this.assignmentTitle = assignmentTitle; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public String getSubmissionTime() { return submissionTime; }
    public void setSubmissionTime(String submissionTime) { this.submissionTime = submissionTime; }

    public Integer getMarks() { return marks; }
    public void setMarks(Integer marks) { this.marks = marks; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}

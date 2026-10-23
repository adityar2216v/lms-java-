package com.lms.gui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int examId;
    private String questionText;
    private int marks;
    private List<String> options;
    private int correctOptionIndex; // 0, 1, 2, 3

    public Question() {
        this.options = new ArrayList<>();
    }

    public Question(int id, int examId, String questionText, int marks, List<String> options, int correctOptionIndex) {
        this.id = id;
        this.examId = examId;
        this.questionText = questionText;
        this.marks = marks;
        this.options = options != null ? options : new ArrayList<>();
        this.correctOptionIndex = correctOptionIndex;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(int correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }
}

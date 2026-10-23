package com.lms.gui.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Notice implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String content;
    private String category; // "Academic", "Exam", "Placement", "General"
    private String author;
    private LocalDate postDate;
    private boolean pinned;

    public Notice() {
        this.postDate = LocalDate.now();
    }

    public Notice(int id, String title, String content, String category, String author, boolean pinned) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.postDate = LocalDate.now();
        this.pinned = pinned;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public LocalDate getPostDate() { return postDate; }
    public void setPostDate(LocalDate postDate) { this.postDate = postDate; }

    public LocalDate getCreatedAt() { return postDate; }

    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }
}

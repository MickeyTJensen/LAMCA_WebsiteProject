package com.example.lamcagym.Entity;

import java.time.LocalDateTime;
import java.util.Date;

public class SessionDTO {
    private String id;
    private String title;
    private LocalDateTime start;
    private Integer duration;
    private LocalDateTime end;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void calculateEnd() {
        if (this.start != null && this.duration != null) {
            this.end = this.start.plusMinutes(this.duration);
        }
    }

}
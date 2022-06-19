package com.example.knu_widget.Classes;

import java.io.Serializable;

public class Schedule implements Serializable {

    private String day;
    private String lecture;
    private String start;
    private String end;
    private String writeId;

    public Schedule() {
        this.day = "";
        this.lecture = "";
        this.start = "";
        this.end = "";
        this.writeId = "";
    }

    public Schedule(String day, String lecture, String start, String end, String writeId) {
        this.day = day;
        this.lecture = lecture;
        this.start = start;
        this.end = end;
        this.writeId = writeId;
    }

    public String getWriteId() {
        return writeId;
    }

    public void setWriteId(String writeId) {
        this.writeId = writeId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}

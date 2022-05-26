package com.example.knu_widget;

import java.io.Serializable;

public class Schedule implements Serializable {

    private String day;
    private String lecture;
    private String start;
    private String end;

    public Schedule(){
        this.day="";
        this.lecture="";
        this.start="";
        this.end="";
    }
    public Schedule(String day, String lecture, String start, String end){
        this.day=day;
        this.lecture=lecture;
        this.start=start;
        this.end=end;
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

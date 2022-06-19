package com.example.knu_widget.Classes;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ScheduleItemList {

    public static ScheduleItemList scheduleItemList = new ScheduleItemList();
    private List<Schedule> scheduleList = new ArrayList<>();
    public static ScheduleItemList getInstance(){
        return scheduleItemList;
    }

    public List<Schedule> getScheduleList(){return scheduleList;}

    public void setScheduleList(List<Schedule> scheduleList){
        this.scheduleList=scheduleList;
    }

    private void swap(int v, int e){
        Schedule temp;
        temp=scheduleList.get(v);
        scheduleList.set(v, scheduleList.get(e));
        scheduleList.set(e, temp);
    }

    public void sort(){
        int _size = scheduleList.size();

        for(int i=0;i<_size-1;i++){
            for(int j=i+1;j<_size;j++){
                if(dayInteger(scheduleList.get(i).getDay())>dayInteger(scheduleList.get(j).getDay())){
                    swap(i, j);
                }else if(dayInteger(scheduleList.get(i).getDay())==dayInteger(scheduleList.get(j).getDay())){
                    int comp = compareTime(scheduleList.get(i).getStart(), scheduleList.get(j).getStart());
                    if(comp==1){
                        swap(i, j);
                    }else if(comp==0&&compareTime(scheduleList.get(i).getEnd(), scheduleList.get(j).getEnd())==1){
                        swap(i, j);
                    }
                }
            }
        }
    }

    private int dayInteger(String day){
        switch (day) {
            case "월요일":
                return 1;
            case "화요일":
                return 2;
            case "수요일":
                return 3;
            case "목요일":
                return 4;
            case "금요일":
                return 5;
            case "토요일":
                return 6;
            default:
                return 7;
        }
    }
    private int compareTime(String t1, String t2){
        int time1 = Integer.parseInt(t1.replace(" : ", ""));
        int time2 = Integer.parseInt(t2.replace(" : ", ""));

        if(time1>time2)return 1;
        else if(time1==time2)return 0;
        return -1;
    }
}

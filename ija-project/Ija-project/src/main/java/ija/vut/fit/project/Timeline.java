package ija.vut.fit.project;

import java.util.List;

public class Timeline{
    private List<String> timeList;
    private String startTime;
    public Timeline(List<String> timeList, String startTime) {
        this.timeList = timeList;
        this.startTime = startTime;
    }
    public Timeline(){}

    public List<String> getTimeList() {
        return timeList;
    }

    public String getStartTime() {
        return startTime;
    }
}
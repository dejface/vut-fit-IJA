package ija.vut.fit.project;

import java.util.List;

public class Timeline{
    private List<String> timeList;

    public Timeline(List<String> timeList) {
        this.timeList = timeList;
    }
    public Timeline(){}

    public List<String> getTimeList() {
        return timeList;
    }
}
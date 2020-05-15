package ija.vut.fit.project;

import java.util.List;

/**
 * Class which represents Timeline object
 */
public class Timeline{
    private List<String> timeList;
    private String startTime;

    /**
     * Constructor for Timeline object
     * @param timeList - list of time schedules
     * @param startTime - starting time of time schedule
     */
    public Timeline(List<String> timeList, String startTime) {
        this.timeList = timeList;
        this.startTime = startTime;
    }
    public Timeline(){}

    /**
     * @return list of time schedules
     */
    public List<String> getTimeList() {
        return timeList;
    }

    /**
     * @return starting time of schedule
     */
    public String getStartTime() {
        return startTime;
    }
}
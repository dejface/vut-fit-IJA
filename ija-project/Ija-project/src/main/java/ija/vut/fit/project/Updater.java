package ija.vut.fit.project;

import java.time.LocalTime;
import java.util.List;

/**
 * Updater interface is used to update position in Map based on local time
 */
public interface Updater {
    void update(LocalTime time, boolean isPorted);
    Route getRoute();
    List<Stop> getStops();
}

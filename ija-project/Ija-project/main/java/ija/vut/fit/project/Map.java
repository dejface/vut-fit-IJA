package ija.vut.fit.project;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Class which represents Map object
 */
public class Map {
    private List<Stop> stops;
    private List<Street> streets;
    @JsonIgnore
    private List<Coordinate> coordinateList;
    private List<Vehicle> vehicles;


    /**
     * Constructor for Map object
     * @param coordinateList - list of coordinates
     * @param stops - list of stops
     * @param streets - list of streets
     * @param vehicles - list of vehicles
     */
    public Map(List<Coordinate> coordinateList, List<Stop> stops,List<Street> streets, List<Vehicle> vehicles) {
        this.coordinateList = coordinateList;
        this.streets = streets;
        this.vehicles = vehicles;
        this.stops = stops;
    }

    private Map(){}


    /**
     * @return list of coordinates
     */
    public List<Coordinate> getCoordinateList() {
        return coordinateList;
    }

    /**
     * @return list of vehicles
     */
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * @return list of stops
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * @return list of streets
     */
    public List<Street> getStreets() {
        return streets;
    }
}

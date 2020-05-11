package ija.vut.fit.project;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Map {
    private List<Stop> stops;
    private List<Street> streets;
    @JsonIgnore
    private List<Coordinate> coordinateList;
    private List<Vehicle> vehicles;



    public Map(List<Coordinate> coordinateList, List<Stop> stops,List<Street> streets, List<Vehicle> vehicles) {
        this.coordinateList = coordinateList;
        this.streets = streets;
        this.vehicles = vehicles;
        this.stops = stops;
    }

    private Map(){}



    public List<Coordinate> getCoordinateList() {
        return coordinateList;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public List<Street> getStreets() {
        return streets;
    }
}

package ija.vut.fit.project;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.*;
import java.util.List;

/**
 * Class which represents Route object
 */
public class Route {
    private List<Coordinate> route;

    public Route() {
    }

    /**
     * Constructor for Route object
     * @param route - list of coordinates which creates a route of vehicle
     */
    public Route(List<Coordinate> route) {
        this.route = route;
    }

    /**
     * @param x - first pair of coordinates
     * @param y - second pair of coordinates
     * @return difference of coordinates
     */
    private double diffCoords(Coordinate x, Coordinate y) {
        return Math.sqrt(Math.pow(x.getX() - y.getX(), 2) + Math.pow(x.getY() - y.getY(), 2));
    }

    /**
     * Calculates new position of vehicle, based on driven distance
     * @param distance - how far is vehicle on the route
     * @return new coordinates of vehicle
     */
    @JsonIgnore
    public Coordinate distanceOfCoords(double distance) {
        Coordinate x = null, y = null;
        double total = 0;

        for (int i = 0; i < route.size() - 1; i++) {
            x = route.get(i);
            y = route.get(i + 1);

            if (total + diffCoords(x, y) >= distance) {
                break;
            }
            total += diffCoords(x, y);
        }
        if (x == null) return null;
        double driven = (distance - total) / diffCoords(x, y);
        return new Coordinate(x.getX() + (y.getX() - x.getX()) * driven, x.getY() + (y.getY() - x.getY()) * driven);
    }

    /**
     * Iterates through route list and returns its length
     * @return length of route
     */
    @JsonIgnore
    public double getRouteLength(){
        double length = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            length += diffCoords(route.get(i), route.get(i + 1));
        }
        return length;
    }

    /**
     * @return list of route coordinates
     */
    public List<Coordinate> getRoute() {
        return route;
    }
}


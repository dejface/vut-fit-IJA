package ija.vut.fit.project;

import java.util.List;

public class Route {
    private List<Coordinate> route;

    public Route(List<Coordinate> route) {
        this.route = route;
    }

    private double diffCoords(Coordinate x, Coordinate y) {
        return Math.sqrt(Math.pow(x.getX() - y.getX(), 2) + Math.pow(x.getY() - y.getY(), 2));
    }

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

    public double getRouteLength(){
        double length = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            length += diffCoords(route.get(i), route.get(i + 1));
        }
        return length;
    }
}

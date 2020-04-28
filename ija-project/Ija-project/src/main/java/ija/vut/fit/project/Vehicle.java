package ija.vut.fit.project;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Draw, Updater {
    private Coordinate coords;
    private double speed = 0;
    private double distance = 0;
    private Route route;
    private List<Shape> gui;

    public Vehicle(Coordinate coords, double speed, Route route) {
        this.coords = coords;
        this.speed = speed;
        this.route = route;
        gui = new ArrayList<>();
        gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.RED));
    }

    private void inGui(Coordinate coordinate){
        for (Shape shape : gui){
            shape.setTranslateX((coordinate.getX() - coords.getX()) + shape.getTranslateX());
            shape.setTranslateY((coordinate.getY() - coords.getY()) + shape.getTranslateY());
        }
    }

    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    @Override
    public void update(LocalTime time) {
        distance += speed;
        if (distance > route.getRouteLength()){
            return;
        }
        Coordinate coordinate = route.distanceOfCoords(distance);
        inGui(coordinate);
        coords = coordinate;
    }
}

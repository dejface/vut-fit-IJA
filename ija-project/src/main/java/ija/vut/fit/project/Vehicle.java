package ija.vut.fit.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(converter = Vehicle.VehicleFixToDraw.class)
public class Vehicle implements Draw, Updater {
    private Coordinate coords;
    private double speed = 0;
    @JsonIgnore
    private double distance = 0;

    private Route route;
    @JsonIgnore
    private List<Shape> gui;

    private Vehicle(){}

    public Vehicle(Coordinate coords, double speed, Route route) {
        this.coords = coords;
        this.speed = speed;
        this.route = route;
        setGui();
    }

    private void inGui(Coordinate coordinate){
        for (Shape shape : gui){
            shape.setTranslateX((coordinate.getX() - coords.getX()) + shape.getTranslateX());
            shape.setTranslateY((coordinate.getY() - coords.getY()) + shape.getTranslateY());
        }
    }

    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return gui;
    }


    private void setGui() {
        gui = new ArrayList<>();
        gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.RED));
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

    public Coordinate getCoords() {
        return coords;
    }

    public double getSpeed() {
        return speed;
    }

    public Route getRoute() {
        return route;
    }

    static class VehicleFixToDraw extends StdConverter<Vehicle,Vehicle> {

        @Override
        public Vehicle convert(Vehicle value) {
            value.setGui();
            return value;
        }
    }


}

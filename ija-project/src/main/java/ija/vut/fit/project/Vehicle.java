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
    private List<Stop> stops;

    private Route route;
    @JsonIgnore
    private List<Shape> gui;

    private Vehicle(){}

    public Vehicle(Coordinate coords, double speed, Route route, List<Stop> stops) {
        this.coords = coords;
        this.speed = speed;
        this.route = route;
        this.stops = stops;
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
       // for(Stop s : stops) {
         //   if (coords.getX() == s.getCoordinates().getX() & coords.getY() == s.getCoordinates().getY()) {
         //       distance += 1.0;
          //  }
          // else {
                distance += speed;
           // }
            if (distance > route.getRouteLength()) {

                return;
            }
            Coordinate coordinate = route.distanceOfCoords(distance);
            inGui(coordinate);
            coords = coordinate;
      //  }
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

    public List<Stop> getStops() {
        return stops;
    }

    static class VehicleFixToDraw extends StdConverter<Vehicle,Vehicle> {

        @Override
        public Vehicle convert(Vehicle value) {
            value.setGui();
            return value;
        }
    }


}

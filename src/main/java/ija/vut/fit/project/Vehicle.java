package ija.vut.fit.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(converter = Vehicle.VehicleFixToDraw.class)
public class Vehicle implements Draw, Updater, EventHandler<MouseEvent> {
    private Coordinate coords;
    private double speed = 0;
    @JsonIgnore
    private double distance = 0;
    private List<Timeline> timelines;
    private List<Stop> stops;
    @JsonIgnore
    private int onStopCount = 10;

    private Route route;
    @JsonIgnore
    private List<Shape> gui;

    private Vehicle(){}

    public Vehicle(Coordinate coords, double speed, Route route, List<Stop> stops, List<Timeline> timelines) {
        this.coords = coords;
        this.speed = speed;
        this.route = route;
        this.stops = stops;
        this.timelines = timelines;
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
        if(onStopCount == 10) {
            for (Stop s : stops) {
                if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 2 &
                        Math.abs(coords.getY() - s.getCoordinates().getY()) <= 2) {
                    setOnStopCount(onStopCount - 1);
                    System.out.println(s.getId() + " " + time);
                    break;
                }
            }
        }
        if(getOnStopCount() == 10 | getOnStopCount() == 0){
            setOnStopCount(10);
            distance += speed/10;
            if (distance > route.getRouteLength()) {
                return;
            }

            Coordinate coordinate = route.distanceOfCoords(distance);
            inGui(coordinate);
            coords = coordinate;
        }
        else{
            setOnStopCount(onStopCount - 1);
        }
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

    public List<Timeline> getTimelines() {
        return timelines;
    }

    @JsonIgnore
    public  int getOnStopCount() {
        return onStopCount;
    }

    public void setOnStopCount(int onStopCount) {
        this.onStopCount = onStopCount;
    }

    @Override
    public void handle(MouseEvent event) {
        System.out.println("Stlacil si vozidlo");
    }


    static class VehicleFixToDraw extends StdConverter<Vehicle,Vehicle> {

        @Override
        public Vehicle convert(Vehicle value) {
            value.setGui();
            return value;
        }
    }


}

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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


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
    private int line;
    private boolean hasEnded;
    @JsonIgnore
    private int timelineiterator = 0;
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
        this.line = 0;
        this.hasEnded = false;
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


    static int stopCountLine1 = 0;
    static int stopCountLine2 = 0;
    static int stopCountLine3 = 0;
    static int stopCountLine4 = 0;
    static int count = 0;
    private void setGui() {
        gui = new ArrayList<>();
        if (count == 0) {
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.DEEPSKYBLUE));
            this.line = 1;
            count++;
        } else if (count == 1){
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.FUCHSIA));
            this.line = 2;
            count++;
        } else if (count == 2){
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.YELLOWGREEN));
            this.line = 3;
            count++;
        } else if (count == 3){
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.RED));
            this.line = 4;
            count = 0;
        }
    }

    public boolean getEndBool(){
        return this.hasEnded;
    }

    @Override
    public void update(LocalTime time) {

       /* if(onStopCount == 10) {
            for (Stop s : stops) {
                if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 2 &
                        Math.abs(coords.getY() - s.getCoordinates().getY()) <= 2) {
                    setOnStopCount(onStopCount - 1);
                    System.out.println(s.getId() + " " + time);
                    break;
                }
            }
        }*/




        if (timelineiterator < timelines.size()) {
            if(this.timelines.get(timelineiterator).getStartTime().compareTo(time.toString()) < 0) {


                   // System.out.println(time);
                   if (this.line == 1) {
                       if (timelines.get(timelineiterator).getTimeList().get(stopCountLine1).equals(time.toString())) {
                           for (Stop s : stops) {
                               if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 3 &
                                       Math.abs(coords.getY() - s.getCoordinates().getY()) <= 3) {
                                   setOnStopCount(onStopCount - 1);
                                   System.out.println(s.getId() + " " + time);
                                   break;
                               }
                           }
                           if ((stopCountLine1 != timelines.get(timelineiterator).getTimeList().size() - 1)) {
                               stopCountLine1++;
                           }
                           System.out.println(stopCountLine1);
                       }
                   } else if (this.line == 2) {
                       if (timelines.get(timelineiterator).getTimeList().get(stopCountLine2).equals(time.toString())) {
                           for (Stop s : stops) {
                               if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 3 &
                                       Math.abs(coords.getY() - s.getCoordinates().getY()) <= 3) {
                                   setOnStopCount(onStopCount - 1);
                                   System.out.println(s.getId() + " " + time);
                                   break;
                               }
                           }
                           if ((stopCountLine2 != timelines.get(timelineiterator).getTimeList().size() - 1)) {
                               stopCountLine2++;
                           }
                       }
                   } else if (this.line == 3) {
                       if (timelines.get(timelineiterator).getTimeList().get(stopCountLine3).equals(time.toString())) {
                           for (Stop s : stops) {
                               if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 3 &
                                       Math.abs(coords.getY() - s.getCoordinates().getY()) <= 3) {
                                   setOnStopCount(onStopCount - 1);
                                   System.out.println(s.getId() + " " + time);
                                   break;
                               }
                           }
                           if ((stopCountLine3 != timelines.get(timelineiterator).getTimeList().size() - 1)) {
                               stopCountLine3++;
                           }
                       }
                   } else if (this.line == 4) {
                       if (timelines.get(timelineiterator).getTimeList().get(stopCountLine4).equals(time.toString())) {
                           for (Stop s : stops) {
                               if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 3 &
                                       Math.abs(coords.getY() - s.getCoordinates().getY()) <= 3) {
                                   setOnStopCount(onStopCount - 1);
                                   System.out.println(s.getId() + " " + time);
                                   break;
                               }
                           }
                           if ((stopCountLine4 != timelines.get(timelineiterator).getTimeList().size() - 1)) {
                               stopCountLine4++;
                           }
                       }
                   }

               if (getOnStopCount() == 10 | getOnStopCount() == 0) {
                   setOnStopCount(10);
                   distance += speed / 10;
                   if (distance > route.getRouteLength()) {
                       //if (gui.size() != 0) {
                       //this.gui.get(0).setVisible(false);
                       //gui.remove(0);
                       //}
                       distance = 0;
                       timelineiterator += 1;
                       if (timelineiterator == timelines.size()) {
                           this.gui.get(0).setVisible(false);
                           //return;
                       }
                   }

            /*if (timelines.get(0).getTimeList().get(0).equals(time.toString()))
                System.out.println("som kokot");*/
                   Coordinate coordinate = route.distanceOfCoords(distance);
                   inGui(coordinate);
                   coords = coordinate;
               } else {
                   setOnStopCount(onStopCount - 1);
               }
           }
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
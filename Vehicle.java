package ija.vut.fit.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.sun.javafx.fxml.builder.TriangleMeshBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


@JsonDeserialize(converter = Vehicle.VehicleFixToDraw.class)
public class Vehicle implements Draw, Updater {
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
            //vehicle on map
            if(shape instanceof Circle) {
                shape.setTranslateX((coordinate.getX() - coords.getX()) + shape.getTranslateX());
                shape.setTranslateY((coordinate.getY() - coords.getY()) + shape.getTranslateY());
            }
            //realtime vehicle under map
            else if(shape instanceof Polygon) {
                shape.setTranslateX(800 * (distance / route.getRouteLength()));
            }
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

            //vehicle
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.DEEPSKYBLUE));
            //highlight route on map
            for(int i = 1; i < route.getRoute().size(); i++) {
                gui.add(new Line(route.getRoute().get(i-1).getX(), route.getRoute().get(i-1).getY(), route.getRoute().get(i).getX(), route.getRoute().get(i).getY()));
                gui.get(gui.size() - 1).setStroke(Color.DEEPSKYBLUE);
                gui.get(gui.size() - 1).setStrokeWidth(3);
                gui.get(gui.size() - 1).setVisible(false);
            }
            //show route + realtime vehicle line under map
            gui.add(new Line(10,800,810,800));
            gui.get(gui.size() - 1).setStroke(Color.DEEPSKYBLUE);
            gui.get(gui.size() - 1).setStrokeWidth(3);
            gui.get(gui.size() - 1).setVisible(false);
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(10.0, 785.0,
                    10.0, 815.0,
                    25.0, 800.0);
            polygon.setStroke(Color.BLACK);
            polygon.setFill(Color.DEEPSKYBLUE);
            gui.add(polygon);
            gui.get(gui.size() - 1).setVisible(false);

            for(int i = 0; i < this.getStops().size()-1; i++){
                double tempDist = 0;
                for(int l = 0; l < this.route.getRoute().size()-1; l++){
                    if(this.route.getRoute().get(l).getX() == this.getStops().get(i).getCoordinates().getX() &
                            this.route.getRoute().get(l).getY() == this.getStops().get(i).getCoordinates().getY()){
                        break;
                    }
                    tempDist += Math.abs( Math.sqrt(Math.pow(this.route.getRoute().get(l).getX() -
                            this.route.getRoute().get(l+1).getX(), 2) +
                            Math.pow(this.route.getRoute().get(l).getY() - this.route.getRoute().get(l+1).getY(), 2)));


                }

                Ellipse ellipse = new Ellipse((800 * (tempDist / this.route.getRouteLength()) + 10),800.0,8.0,8.0);
                ellipse.setFill(Color.DEEPSKYBLUE);
                gui.add(ellipse);
                gui.get(gui.size() - 1).setVisible(false);
                if(i%2 == 0) {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 780, this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 765, this.getStops().get(i).getId()));
                    gui.get(gui.size() - 1).setVisible(false);
                }
                else {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 820, this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 835, this.getStops().get(i).getId()));
                    gui.get(gui.size() - 1).setVisible(false);
                }
            }

            this.line = 1;
            count++;
        } else if (count == 1){
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.FUCHSIA));
            for(int i = 1; i < route.getRoute().size(); i++) {
                gui.add(new Line(route.getRoute().get(i-1).getX(), route.getRoute().get(i-1).getY(), route.getRoute().get(i).getX(), route.getRoute().get(i).getY()));
                gui.get(gui.size() - 1).setStroke(Color.FUCHSIA);
                gui.get(gui.size() - 1).setStrokeWidth(3);
                gui.get(gui.size() - 1).setVisible(false);
            }

            //show route + realtime vehicle line under map
            gui.add(new Line(10,850,810,850));
            gui.get(gui.size() - 1).setStroke(Color.FUCHSIA);
            gui.get(gui.size() - 1).setStrokeWidth(3);
            gui.get(gui.size() - 1).setVisible(false);
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(10.0, 835.0,
                    10.0, 865.0,
                    25.0, 850.0);
            polygon.setStroke(Color.BLACK);
            polygon.setFill(Color.FUCHSIA);
            gui.add(polygon);
            gui.get(gui.size() - 1).setVisible(false);

            for(int i = 0; i < this.getStops().size()-1; i++){
                double tempDist = 0;
                for(int l = 0; l < this.route.getRoute().size()-1; l++){
                    if(this.route.getRoute().get(l).getX() == this.getStops().get(i).getCoordinates().getX() &
                            this.route.getRoute().get(l).getY() == this.getStops().get(i).getCoordinates().getY()){
                        break;
                    }
                    tempDist += Math.abs( Math.sqrt(Math.pow(this.route.getRoute().get(l).getX() -
                            this.route.getRoute().get(l+1).getX(), 2) +
                            Math.pow(this.route.getRoute().get(l).getY() - this.route.getRoute().get(l+1).getY(), 2)));


                }

                Ellipse ellipse = new Ellipse((800 * (tempDist / this.route.getRouteLength()) + 10),850.0,8.0,8.0);
                ellipse.setFill(Color.FUCHSIA);
                gui.add(ellipse);
                gui.get(gui.size() - 1).setVisible(false);
                if(i%2 == 0) {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 830, this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 815, this.getStops().get(i).getId()));
                    gui.get(gui.size() - 1).setVisible(false);
                }
                else {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 870, this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 885, this.getStops().get(i).getId()));
                    gui.get(gui.size() - 1).setVisible(false);
                }
            }


            this.line = 2;
            count++;
        } else if (count == 2){
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.YELLOWGREEN));
            for(int i = 1; i < route.getRoute().size(); i++) {
                gui.add(new Line(route.getRoute().get(i-1).getX(), route.getRoute().get(i-1).getY(), route.getRoute().get(i).getX(), route.getRoute().get(i).getY()));
                gui.get(gui.size() - 1).setStroke(Color.YELLOWGREEN);
                gui.get(gui.size() - 1).setStrokeWidth(3);
                gui.get(gui.size() - 1).setVisible(false);
            }

            //show route + realtime vehicle line under map
            gui.add(new Line(10,900,810,900));
            gui.get(gui.size() - 1).setStroke(Color.YELLOWGREEN);
            gui.get(gui.size() - 1).setStrokeWidth(3);
            gui.get(gui.size() - 1).setVisible(false);
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(10.0, 885.0,
                    10.0, 915.0,
                    25.0, 900.0);
            polygon.setStroke(Color.BLACK);
            polygon.setFill(Color.YELLOWGREEN);
            gui.add(polygon);
            gui.get(gui.size() - 1).setVisible(false);

            for(int i = 0; i < this.getStops().size()-1; i++){
                double tempDist = 0;
                for(int l = 0; l < this.route.getRoute().size()-1; l++){
                    if(this.route.getRoute().get(l).getX() == this.getStops().get(i).getCoordinates().getX() &
                            this.route.getRoute().get(l).getY() == this.getStops().get(i).getCoordinates().getY()){
                        break;
                    }
                    tempDist += Math.abs( Math.sqrt(Math.pow(this.route.getRoute().get(l).getX() -
                            this.route.getRoute().get(l+1).getX(), 2) +
                            Math.pow(this.route.getRoute().get(l).getY() - this.route.getRoute().get(l+1).getY(), 2)));


                }

                Ellipse ellipse = new Ellipse((800 * (tempDist / this.route.getRouteLength()) + 10),900.0,8.0,8.0);
                ellipse.setFill(Color.YELLOWGREEN);
                gui.add(ellipse);
                gui.get(gui.size() - 1).setVisible(false);
                if(i%2 == 0) {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 880, this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 865, this.getStops().get(i).getId()));
                    gui.get(gui.size() - 1).setVisible(false);
                }
                else {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 920, this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 935, this.getStops().get(i).getId()));
                    gui.get(gui.size() - 1).setVisible(false);
                }
            }

            this.line = 3;
            count++;
        } else if (count == 3){
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.RED));
            for(int i = 1; i < route.getRoute().size(); i++) {
                gui.add(new Line(route.getRoute().get(i-1).getX(), route.getRoute().get(i-1).getY(), route.getRoute().get(i).getX(), route.getRoute().get(i).getY()));
                gui.get(gui.size() - 1).setStroke(Color.RED);
                gui.get(gui.size() - 1).setStrokeWidth(3);
                gui.get(gui.size() - 1).setVisible(false);
            }

            //show route + realtime vehicle line under map
            gui.add(new Line(10,950,810,950));
            gui.get(gui.size() - 1).setStroke(Color.RED);
            gui.get(gui.size() - 1).setStrokeWidth(3);
            gui.get(gui.size() - 1).setVisible(false);
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(10.0, 935.0,
                    10.0, 965.0,
                    25.0, 950.0);
            polygon.setStroke(Color.BLACK);
            polygon.setFill(Color.RED);
            gui.add(polygon);
            gui.get(gui.size() - 1).setVisible(false);

            for(int i = 0; i < this.getStops().size()-1; i++){
                double tempDist = 0;
                for(int l = 0; l < this.route.getRoute().size()-1; l++){
                    if(this.route.getRoute().get(l).getX() == this.getStops().get(i).getCoordinates().getX() &
                            this.route.getRoute().get(l).getY() == this.getStops().get(i).getCoordinates().getY()){
                        break;
                    }
                    tempDist += Math.abs( Math.sqrt(Math.pow(this.route.getRoute().get(l).getX() -
                            this.route.getRoute().get(l+1).getX(), 2) +
                            Math.pow(this.route.getRoute().get(l).getY() - this.route.getRoute().get(l+1).getY(), 2)));


                }

                Ellipse ellipse = new Ellipse((800 * (tempDist / this.route.getRouteLength()) + 10),950.0,8.0,8.0);
                ellipse.setFill(Color.RED);
                gui.add(ellipse);
                gui.get(gui.size() - 1).setVisible(false);
                if(i%2 == 0) {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 930, this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 915, this.getStops().get(i).getId()));
                    gui.get(gui.size() - 1).setVisible(false);
                }
                else {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 970, this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 985, this.getStops().get(i).getId()));
                    gui.get(gui.size() - 1).setVisible(false);
                }
            }

            this.line = 4;
            count = 0;
        }
        gui.get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (Shape shape : gui){
                    if(shape instanceof Line | shape instanceof Polygon | shape instanceof Text | shape instanceof Ellipse) {
                        if(!shape.isVisible()) {
                            shape.setVisible(true);
                        }
                        else shape.setVisible(false);
                    }
                }

            }
        });
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
                       if (this.line == 1) stopCountLine1 = 0;
                       else if (this.line == 2) stopCountLine2 = 0;
                       else if (this.line == 3) stopCountLine3 = 0;
                       else if (this.line == 4) stopCountLine4 = 0;
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


    static class VehicleFixToDraw extends StdConverter<Vehicle,Vehicle> {

        @Override
        public Vehicle convert(Vehicle value) {
            value.setGui();
            return value;
        }
    }


}
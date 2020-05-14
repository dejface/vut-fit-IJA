package ija.vut.fit.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;



@JsonDeserialize(converter = Vehicle.VehicleFixToDraw.class)
public class Vehicle implements Draw, Updater {
    private Coordinate coords;
    private double speed = 0;
    @JsonIgnore
    private double distance = 0;
    private double firstLine;
    private double secondLine;
    private double thirdLine;
    private double fourthLine;
    private List<Timeline> timelines;
    private List<Stop> stops;
    @JsonIgnore
    private int onStopCount = 10;
    private int line;
    @JsonIgnore
    private int timelineiterator = 0;
    private short timelineiterator1 = 0;
    private short timelineiterator2 = 0;
    private short timelineiterator3 = 0;
    private short timelineiterator4 = 0;
    private Route route;
    @JsonIgnore
    private List<Shape> gui;
    static int stopCountLine1 = 0;
    static int stopCountLine2 = 0;
    static int stopCountLine3 = 0;
    static int stopCountLine4 = 0;
    static int count = 0;


    private Vehicle(){}

    public Vehicle(Coordinate coords, double speed, Route route, List<Stop> stops, List<Timeline> timelines) {
        this.coords = coords;
        this.speed = speed;
        this.route = route;
        this.stops = stops;
        this.timelines = timelines;
        this.line = 0;
        setGui();
    }

    private void inGui(Coordinate coordinate){
        for (Shape shape : gui){
            if (shape instanceof Circle) {
                shape.setTranslateX((coordinate.getX() - coords.getX()) + shape.getTranslateX());
                shape.setTranslateY((coordinate.getY() - coords.getY()) + shape.getTranslateY());
            } else if (shape instanceof Polygon) {
                shape.setTranslateX(800 * (distance / route.getRouteLength()));
            }
        }
    }

    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return gui;
    }
    
    private void guiChange(int line){
       for (int i = 1; i < route.getRoute().size(); i++){
            gui.add(new Line(route.getRoute().get(i-1).getX(), route.getRoute().get(i-1).getY(),
                    route.getRoute().get(i).getX(), route.getRoute().get(i).getY()));
            if (line == 1) gui.get(gui.size() - 1).setStroke(Color.DEEPSKYBLUE);
            else if (line == 2) gui.get(gui.size() - 1).setStroke(Color.FUCHSIA);
            else if (line == 3) gui.get(gui.size() - 1).setStroke(Color.YELLOWGREEN);
            else if (line == 4) gui.get(gui.size() - 1).setStroke(Color.RED);
            gui.get(gui.size() - 1).setStrokeWidth(3.0);
            gui.get(gui.size() - 1).setVisible(false);
        }
    }

    private void setGui() {
        gui = new ArrayList<>();
        if (count == 0) {
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.DEEPSKYBLUE));
            this.line = 1;
            guiChange(this.line);

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
            count++;
        } else if (count == 1){
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.FUCHSIA));
            this.line = 2;
            guiChange(this.line);

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
            count++;
        } else if (count == 2){
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.YELLOWGREEN));
            this.line = 3;
            guiChange(this.line);

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

            count++;
        } else if (count == 3){
            gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.RED));
            this.line = 4;
            guiChange(this.line);

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

            count = 0;
        }
        gui.get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (Shape shape : gui){
                    if (shape instanceof Line | shape instanceof Polygon | shape instanceof Text | shape instanceof Ellipse){
                        if (!shape.isVisible()){
                            shape.setVisible(true);
                        } else shape.setVisible(false);
                    }
                }
            }
        });
    }

    private int parseTimeString(String s){
        String[] t = s.split(":");
        if (t.length == 3)
            return Integer.parseInt(t[0]) * 3600 + Integer.parseInt(t[1]) * 60 + Integer.parseInt(t[2]);
        else
            return Integer.parseInt(t[0]) * 3600 + Integer.parseInt(t[1]) * 60;
    }
    static int counter =0;
    public void handlePort(int delay, LocalTime time) {
      //  if (this.line == 2) counter++;
        for (int i = timelineiterator; i <= timelines.size() - 1; i++){
           /* if (counter == 1){
                System.out.println("ahoj");
            }*/
            int currentTime = parseTimeString(time.toString()) - 1;
            int lastScheduledTime = parseTimeString(timelines.get(i).getTimeList().get(timelines.get(i).getTimeList().size()-1));

            if (currentTime == parseTimeString(timelines.get(i).getStartTime())) break;
            if (currentTime > lastScheduledTime) {
                if (this.line == 1) {
                    timelineiterator1++;
                    timelineiterator = timelineiterator1;
                   // this.gui.get(0).setVisible(false);
                } else if (this.line == 2) {
                    timelineiterator2++;
                    timelineiterator = timelineiterator2;
                    //this.gui.get(0).setVisible(false);
                } else if (this.line == 3) {
                    timelineiterator3++;
                    timelineiterator = timelineiterator3;
                    //this.gui.get(0).setVisible(false);
                } else if (this.line == 4){
                    timelineiterator4++;
                    timelineiterator = timelineiterator4;
                    //this.gui.get(0).setVisible(false);
                }
            }
        }
        //System.out.println(timelineiterator);

        //if (!this.gui.get(0).isVisible()) this.gui.get(0).setVisible(true);

        for (int i = 0; i < timelines.get(timelineiterator).getTimeList().size() - 1; i++) {
            int start = parseTimeString(timelines.get(timelineiterator).getStartTime());
            int currentTimeChange = parseTimeString(time.toString());
            int intervalRight = parseTimeString(timelines.get(timelineiterator).getTimeList().get(i + 1));
            int intervalLeft = parseTimeString(timelines.get(timelineiterator).getTimeList().get(i));
            if (i == 0) delay += 8;
            else delay += 9;
            if ((intervalLeft <= currentTimeChange) && (currentTimeChange <= intervalRight)) {
                int change = currentTimeChange - delay - start;
                distance = speed / 10;
                Coordinate coordinate = null;

                if (this.line == 1) coordinate = route.distanceOfCoords(firstLine = change * distance);
                else if (this.line == 2) coordinate = route.distanceOfCoords(secondLine = change * distance);
                else if (this.line == 3) coordinate = route.distanceOfCoords(thirdLine = change * distance);
                else if (this.line == 4) coordinate = route.distanceOfCoords(fourthLine = change * distance);

                inGui(coordinate);
                coords = coordinate;
                if (this.line == 1) {
                    if (currentTimeChange > intervalLeft)
                        stopCountLine1 = i + 1;
                    else stopCountLine1 = i;
                } else if (this.line == 2){
                    if (currentTimeChange > intervalLeft)
                        stopCountLine2 = i + 1;
                    else stopCountLine2 = i;
                } else if (this.line == 3){
                    if (currentTimeChange > intervalLeft)
                        stopCountLine3 = i + 1;
                    else stopCountLine3 = i;
                } else if (this.line == 4){
                    if (currentTimeChange > intervalLeft)
                        stopCountLine4 = i + 1;
                    else stopCountLine4 = i;
                }
                break;
            } else if (currentTimeChange < intervalLeft) {
                int change = currentTimeChange - start;
                if (change < 0) {
                    gui.get(0).setVisible(false);
                    return;
                }
                distance = speed / 10;
                Coordinate coordinate = null;

                if (this.line == 1) coordinate = route.distanceOfCoords(firstLine = change * distance);
                else if (this.line == 2) coordinate = route.distanceOfCoords(secondLine = change * distance);
                else if (this.line == 3) coordinate = route.distanceOfCoords(thirdLine = change * distance);
                else if (this.line == 4) coordinate = route.distanceOfCoords(fourthLine = change * distance);

                inGui(coordinate);
                coords = coordinate;
                if (this.line == 1) stopCountLine1 = i;
                else if (this.line == 2) stopCountLine2 = i;
                else if (this.line == 3) stopCountLine3 = i;
                else if (this.line == 4) stopCountLine4 = i;
                break;
            }
        }
    }


    @Override
    public void update(LocalTime time, boolean isPorted) {
        if (this.line == 1) timelineiterator = timelineiterator1;
        else if (this.line == 2) timelineiterator = timelineiterator2;
        else if (this.line == 3) timelineiterator = timelineiterator3;
        else if (this.line == 4) timelineiterator = timelineiterator4;
        if (timelineiterator < timelines.size()) {
            if(this.timelines.get(timelineiterator).getStartTime().compareTo(time.toString()) < 0) {
                if (!gui.get(0).isVisible()) gui.get(0).setVisible(true);
                int delay;
                if (this.line == 1) {
                       if (isPorted) {
                           firstLine = 0;
                           delay = 0;
                           handlePort(delay, time);
                       }
                       if (timelines.get(timelineiterator).getTimeList().get(stopCountLine1).equals(time.toString())) {
                           for (Stop s : stops) {
                               if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 3 &
                                       Math.abs(coords.getY() - s.getCoordinates().getY()) <= 3) {
                                   setOnStopCount(onStopCount - 1);
                                   break;
                               }
                           }
                           if ((stopCountLine1 != timelines.get(timelineiterator).getTimeList().size() - 1)) {
                               stopCountLine1++;
                           }
                       }
                   } else if (this.line == 2) {
                       if (isPorted) {
                           secondLine = 0;
                           delay = 0;
                           handlePort(delay, time);
                       }
                       if (timelines.get(timelineiterator).getTimeList().get(stopCountLine2).equals(time.toString())) {
                           for (Stop s : stops) {
                               if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 3 &
                                       Math.abs(coords.getY() - s.getCoordinates().getY()) <= 3) {
                                   setOnStopCount(onStopCount - 1);
                                   break;
                               }
                           }
                           if ((stopCountLine2 != timelines.get(timelineiterator).getTimeList().size() - 1)) {
                               stopCountLine2++;
                           }
                       }
                   } else if (this.line == 3) {
                       if (isPorted) {
                           thirdLine = 0;
                           delay = 0;
                           handlePort(delay, time);
                       }
                       if (timelines.get(timelineiterator).getTimeList().get(stopCountLine3).equals(time.toString())) {
                           for (Stop s : stops) {
                               if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 3 &
                                       Math.abs(coords.getY() - s.getCoordinates().getY()) <= 3) {
                                   setOnStopCount(onStopCount - 1);
                                   break;
                               }
                           }
                           if ((stopCountLine3 != timelines.get(timelineiterator).getTimeList().size() - 1)) {
                               stopCountLine3++;
                           }
                       }
                   } else if (this.line == 4) {
                       if (isPorted) {
                           fourthLine = 0;
                           delay = 0;
                           handlePort(delay, time);
                       }
                       if (timelines.get(timelineiterator).getTimeList().get(stopCountLine4).equals(time.toString())) {
                           for (Stop s : stops) {
                               if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 3 &
                                       Math.abs(coords.getY() - s.getCoordinates().getY()) <= 3) {
                                   setOnStopCount(onStopCount - 1);
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
                   if (!isPorted) {
                       distance += speed / 10;
                       firstLine = 0;
                       secondLine = 0;
                       thirdLine = 0;
                       fourthLine = 0;
                   }
                   else {
                       if (this.line == 1) distance = firstLine;
                       else if (this.line == 2) distance = secondLine;
                       else if (this.line == 3) distance = thirdLine;
                       else if (this.line == 4) distance = fourthLine;
                   }
                   if (distance > route.getRouteLength()) {
                       if (this.line == 1) {
                           timelineiterator1++;
                           stopCountLine1 = 0;
                       } else if (this.line == 2) {
                           timelineiterator2++;
                           stopCountLine2 = 0;
                       } else if (this.line == 3) {
                           timelineiterator3++;
                           stopCountLine3 = 0;
                       } else if (this.line == 4) {
                           timelineiterator4++;
                           stopCountLine4 = 0;
                       }
                       distance = 0;
                       //timelineiterator += 1;
                       /*if (timelineiterator == timelines.size()) {
                           this.gui.get(0).setVisible(false);
                       }*/
                       gui.get(0).setVisible(false);
                   }
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
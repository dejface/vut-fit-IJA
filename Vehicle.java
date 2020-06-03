package ija.vut.fit.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Class representing vehicle object
 */
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
    public List<Stop> stops;
    private List<Street> streets;
    @JsonIgnore
    private Street currStreet;
    @JsonIgnore
    private int onStopCount = 10;
    private int line;
    @JsonIgnore
    private int timelineiterator = 0;
    private short timelineiterator1 = 0;
    private short timelineiterator2 = 0;
    private short timelineiterator3 = 0;
    private short timelineiterator4 = 0;
    public Route route;
    @JsonIgnore
    private List<Shape> gui;
    static int stopCountLine1 = 0;
    static int stopCountLine2 = 0;
    static int stopCountLine3 = 0;
    static int stopCountLine4 = 0;
    static int count = 0;
    static int L1 = 0, L2 = 0, L3 = 0, L4 = 0;


    private Vehicle(){}

    /**
     * Constructor for Vehicle object
     *
     * @param coords - coordinations of object
     * @param speed - traversal speed
     * @param route - route of the vehicle (list of streets)
     * @param stops - stops of the vehicle (list of stops)
     * @param timelines - time schedule of departures
     */
    public Vehicle(Coordinate coords, double speed, Route route, List<Stop> stops, List<Timeline> timelines) {
        this.coords = coords;
        this.speed = speed;
        this.route = route;
        this.stops = stops;
        this.timelines = timelines;
        this.line = 0;
        setGui();
    }

    /**
     * Makes vehicle move around the map
     * @param coordinate - current coordinates
     */
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

    /**
     * @return - list of shapes that are shown in GUI
     */
    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    /**
     * Changes streets color and shows a route of vehicle after mouse click
     * @param line - number of bus line
     */
    private void guiChange(int line){
        for (int i = 0; i < route.getRoute().size(); i++){
            gui.add(new Line(route.getRoute().get(i).getFrom().getX(), route.getRoute().get(i).getFrom().getY(),
                    route.getRoute().get(i).getTo().getX(), route.getRoute().get(i).getTo().getY()));
            if (line == 1) gui.get(gui.size() - 1).setStroke(Color.DEEPSKYBLUE);
            else if (line == 2) gui.get(gui.size() - 1).setStroke(Color.FUCHSIA);
            else if (line == 3) gui.get(gui.size() - 1).setStroke(Color.YELLOWGREEN);
            else if (line == 4) gui.get(gui.size() - 1).setStroke(Color.RED);
            gui.get(gui.size() - 1).setStrokeWidth(3.0);
            gui.get(gui.size() - 1).setVisible(false);
        }
    }

    /**
     * Generates shapes which are shown later in GUI
     */
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

            for(int i = 0; i < this.getStops().size(); i++){
                double tempDist = 0;

                for(int l = 0; l < this.route.getRoute().size(); l++){
                    boolean stopFound = false;
                    if(this.route.getRoute().get(l).getStops() != null) {
                        for (int s = 0; s < this.route.getRoute().get(l).getStops().size(); s++) {
                            if (this.getStops().get(i).equals(this.route.getRoute().get(l).getStops().get(s))) {
                                if(l > 0) {
                                    if(this.route.getRoute().get(l).getFrom().equals(this.route.getRoute().get(l-1).getFrom())
                                            || this.route.getRoute().get(l).getFrom().equals(this.route.getRoute().get(l-1).getTo())){
                                        tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getFrom());
                                    }
                                    else tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getTo());
                                }
                                else tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getFrom());

                                stopFound = true;
                                break;
                            }
                        }
                    }

                    if(stopFound){
                        stopFound = false;
                        break;
                    }
                    tempDist += getDistance(this.route.getRoute().get(l).getFrom(),this.route.getRoute().get(l).getTo());
                }

                Ellipse ellipse = new Ellipse((800 * (tempDist / this.route.getRouteLength()) + 10),800.0,8.0,8.0);
                ellipse.setFill(Color.DEEPSKYBLUE);
                gui.add(ellipse);
                gui.get(gui.size() - 1).setVisible(false);
                if(i%2 == 0) {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 765, this.getStops().get(i).getId()+"\n"+this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                }
                else {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 835, this.getStops().get(i).getId()+"\n"+this.getTimelines().get(0).getTimeList().get(i)));
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

                for(int l = 0; l < this.route.getRoute().size(); l++){
                    boolean stopFound = false;
                    if(this.route.getRoute().get(l).getStops() != null) {
                        for (int s = 0; s < this.route.getRoute().get(l).getStops().size(); s++) {
                            if (this.getStops().get(i).equals(this.route.getRoute().get(l).getStops().get(s))) {
                                if(l > 0) {
                                    if(this.route.getRoute().get(l).getFrom().equals(this.route.getRoute().get(l-1).getFrom())
                                            || this.route.getRoute().get(l).getFrom().equals(this.route.getRoute().get(l-1).getTo())){
                                        tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getFrom());
                                    }
                                    else tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getTo());
                                }
                                else tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getFrom());

                                stopFound = true;
                                break;
                            }
                        }
                    }

                    if(stopFound){
                        stopFound = false;
                        break;
                    }
                    tempDist += getDistance(this.route.getRoute().get(l).getFrom(),this.route.getRoute().get(l).getTo());
                }

                Ellipse ellipse = new Ellipse((800 * (tempDist / this.route.getRouteLength()) + 10),850.0,8.0,8.0);
                ellipse.setFill(Color.FUCHSIA);
                gui.add(ellipse);
                gui.get(gui.size() - 1).setVisible(false);
                if(i%2 == 0) {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 815, this.getStops().get(i).getId()+"\n"+this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                }
                else {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 885, this.getStops().get(i).getId()+"\n"+this.getTimelines().get(0).getTimeList().get(i)));
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

                for(int l = 0; l < this.route.getRoute().size(); l++){
                    boolean stopFound = false;
                    if(this.route.getRoute().get(l).getStops() != null) {
                        for (int s = 0; s < this.route.getRoute().get(l).getStops().size(); s++) {
                            if (this.getStops().get(i).equals(this.route.getRoute().get(l).getStops().get(s))) {
                                if(l > 0) {
                                    if(this.route.getRoute().get(l).getFrom().equals(this.route.getRoute().get(l-1).getFrom())
                                            || this.route.getRoute().get(l).getFrom().equals(this.route.getRoute().get(l-1).getTo())){
                                        tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getFrom());
                                    }
                                    else tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getTo());
                                }
                                else tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getFrom());

                                stopFound = true;
                                break;
                            }
                        }
                    }

                    if(stopFound){
                        stopFound = false;
                        break;
                    }
                    tempDist += getDistance(this.route.getRoute().get(l).getFrom(),this.route.getRoute().get(l).getTo());
                }

                Ellipse ellipse = new Ellipse((800 * (tempDist / this.route.getRouteLength()) + 10),900.0,8.0,8.0);
                ellipse.setFill(Color.YELLOWGREEN);
                gui.add(ellipse);
                gui.get(gui.size() - 1).setVisible(false);
                if(i%2 == 0) {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 865, this.getStops().get(i).getId()+"\n"+this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                }
                else {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 935,this.getStops().get(i).getId()+"\n"+this.getTimelines().get(0).getTimeList().get(i)));
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

                for(int l = 0; l < this.route.getRoute().size(); l++){
                    boolean stopFound = false;
                    if(this.route.getRoute().get(l).getStops() != null) {
                        for (int s = 0; s < this.route.getRoute().get(l).getStops().size(); s++) {
                            if (this.getStops().get(i).equals(this.route.getRoute().get(l).getStops().get(s))) {
                                if(l > 0) {
                                    if(this.route.getRoute().get(l).getFrom().equals(this.route.getRoute().get(l-1).getFrom())
                                            || this.route.getRoute().get(l).getFrom().equals(this.route.getRoute().get(l-1).getTo())){
                                        tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getFrom());
                                    }
                                    else tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getTo());
                                }
                                else tempDist += getDistance(this.route.getRoute().get(l).getStops().get(s).getCoordinates(), this.route.getRoute().get(l).getFrom());

                                stopFound = true;
                                break;
                            }
                        }
                    }

                    if(stopFound){
                        stopFound = false;
                        break;
                    }
                    tempDist += getDistance(this.route.getRoute().get(l).getFrom(),this.route.getRoute().get(l).getTo());
                }

                Ellipse ellipse = new Ellipse((800 * (tempDist / this.route.getRouteLength()) + 10),950.0,8.0,8.0);
                ellipse.setFill(Color.RED);
                gui.add(ellipse);
                gui.get(gui.size() - 1).setVisible(false);
                if(i%2 == 0) {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 915, this.getStops().get(i).getId()+"\n"+this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                }
                else {
                    gui.add(new Text((800 * (tempDist / this.route.getRouteLength())) - 20, 985, this.getStops().get(i).getId()+"\n"+this.getTimelines().get(0).getTimeList().get(i)));
                    gui.get(gui.size() - 1).setVisible(false);
                }
            }

            count = 0;
        }

    }

    /**
     * Parses localtime string to integer and converts it to seconds
     * @param s - localtime string to be converted
     * @return time in seconds
     */
    private int parseTimeString(String s){
        String[] t = s.split(":");
        if (t.length == 3)
            return Integer.parseInt(t[0]) * 3600 + Integer.parseInt(t[1]) * 60 + Integer.parseInt(t[2]);
        else
            return Integer.parseInt(t[0]) * 3600 + Integer.parseInt(t[1]) * 60;
    }


    /**
     * Handles time change in simulation
     * @param delay - delay in seconds, which is created on bus stop
     * @param time - new localtime which was changed by user
     */
    public void handlePort(int delay, LocalTime time) {
        for (int i = timelineiterator; i <= timelines.size() - 1; i++){

            int currentTime = parseTimeString(time.toString()) - 1;
            int lastScheduledTime = parseTimeString(timelines.get(i).getTimeList().get(timelines.get(i).getTimeList().size()-1));

            if (currentTime == parseTimeString(timelines.get(i).getStartTime())) break;
            if (currentTime > lastScheduledTime) {
                if (this.line == 1) {
                    timelineiterator1++;
                    timelineiterator = timelineiterator1;
                } else if (this.line == 2) {
                    timelineiterator2++;
                    timelineiterator = timelineiterator2;
                } else if (this.line == 3) {
                    timelineiterator3++;
                    timelineiterator = timelineiterator3;
                } else if (this.line == 4){
                    timelineiterator4++;
                    timelineiterator = timelineiterator4;
                }
            }
        }


        for (int i = 0; i < timelines.get(timelineiterator).getTimeList().size() - 1; i++) {
            int start = parseTimeString(timelines.get(timelineiterator).getStartTime());
            int currentTimeChange = parseTimeString(time.toString());
            int intervalRight = parseTimeString(timelines.get(timelineiterator).getTimeList().get(i + 1));
            int intervalLeft = parseTimeString(timelines.get(timelineiterator).getTimeList().get(i));
            if (i == 0) delay += 8;
            else delay += 9;
            if ((intervalLeft <= currentTimeChange) && (currentTimeChange <= intervalRight)) {
                int change = currentTimeChange - delay - start;
                findAndSetCurrStreet();
                distance = speed / currStreet.getTraffic();
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
                findAndSetCurrStreet();
                distance = speed / currStreet.getTraffic();
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


    /**
     * Updates and calculates new position of vehicle
     * @param time - localtime in simulation
     * @param isPorted - boolean which indicates wheter time was changed by user, or not
     */
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

                    if (onStopCount == 10){
                        for (Stop s : stops) {
                            if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 2.5 &
                                    Math.abs(coords.getY() - s.getCoordinates().getY()) <= 2.5) {
                                L1++;
                                if (L1 == 2) break;
                                else if (L1 >= 3) {L1 = 0; break;}
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

                    for (Stop s : stops) {
                        if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 2 &
                                Math.abs(coords.getY() - s.getCoordinates().getY()) <= 2) {
                            L2++;
                            if (L2 == 2) break;
                            else if (L2 >= 3) {L2 = 0; break;}
                            setOnStopCount(onStopCount - 1);
                            break;
                        }
                    }
                    if ((stopCountLine2 != timelines.get(timelineiterator).getTimeList().size() - 1)) {
                        stopCountLine2++;
                    }
                } else if (this.line == 3) {
                    if (isPorted) {
                        thirdLine = 0;
                        delay = 0;
                        handlePort(delay, time);
                    }

                    for (Stop s : stops) {
                        if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 2 &
                                Math.abs(coords.getY() - s.getCoordinates().getY()) <= 2) {
                            L3++;
                            if (L3 == 2) break;
                            else if (L3 >= 3) {L3 = 0; break;}
                            setOnStopCount(onStopCount - 1);
                            break;
                        }
                    }
                    if ((stopCountLine3 != timelines.get(timelineiterator).getTimeList().size() - 1)) {
                        stopCountLine3++;
                    }
                } else if (this.line == 4) {
                    if (isPorted) {
                        fourthLine = 0;
                        delay = 0;
                        handlePort(delay, time);
                    }

                    for (Stop s : stops) {
                        if (Math.abs(coords.getX() - s.getCoordinates().getX()) <= 2 &
                                Math.abs(coords.getY() - s.getCoordinates().getY()) <= 2) {
                            L4++;
                            if (L4 == 2) break;
                            else if (L4 >= 3) {L4 = 0; break;}
                            setOnStopCount(onStopCount - 1);
                            break;
                        }
                    }
                    if ((stopCountLine4 != timelines.get(timelineiterator).getTimeList().size() - 1)) {
                        stopCountLine4++;
                    }
                }

                if (getOnStopCount() == 10 | getOnStopCount() == 0) {
                    setOnStopCount(10);
                    if (!isPorted) {
                        findAndSetCurrStreet();
                        distance += speed / currStreet.traffic;
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
                            int stopiterator = 0;
                            for(Shape shape : this.getGUI()){
                                if(shape instanceof Text){
                                    String a = ((Text) shape).getText();
                                    a = a.substring(0,a.length()-8);
                                    a = a + this.getTimelines().get(timelineiterator1).getTimeList().get(stopiterator);
                                    ((Text) shape).setText(a);
                                    stopiterator++;
                                }
                            }
                            stopCountLine1 = 0;
                        } else if (this.line == 2) {
                            timelineiterator2++;
                            int stopiterator = 0;
                            for(Shape shape : this.getGUI()){
                                if(shape instanceof Text){
                                    String a = ((Text) shape).getText();
                                    a = a.substring(0,a.length()-8);
                                    a = a + this.getTimelines().get(timelineiterator2).getTimeList().get(stopiterator);
                                    ((Text) shape).setText(a);
                                    stopiterator++;
                                }
                            }
                            stopCountLine2 = 0;
                        } else if (this.line == 3) {
                            timelineiterator3++;
                            int stopiterator = 0;
                            for(Shape shape : this.getGUI()){
                                if(shape instanceof Text){
                                    String a = ((Text) shape).getText();
                                    a = a.substring(0,a.length()-8);
                                    a = a + this.getTimelines().get(timelineiterator3).getTimeList().get(stopiterator);
                                    ((Text) shape).setText(a);
                                    stopiterator++;
                                }
                            }
                            stopCountLine3 = 0;
                        } else if (this.line == 4) {
                            timelineiterator4++;
                            int stopiterator = 0;
                            for(Shape shape : this.getGUI()){
                                if(shape instanceof Text){
                                    String a = ((Text) shape).getText();
                                    a = a.substring(0,a.length()-8);
                                    a = a + this.getTimelines().get(timelineiterator4).getTimeList().get(stopiterator);
                                    ((Text) shape).setText(a);
                                    stopiterator++;
                                }
                            }
                            stopCountLine4 = 0;
                        }
                        distance = 0;

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

    /**
     * @return distance betweeen 2 points
     */
    @JsonIgnore
    private double getDistance(Coordinate x, Coordinate y){
        return Math.sqrt(Math.pow(x.getX() - y.getX(), 2) + Math.pow(x.getY() - y.getY(), 2));
    }

    /**
     * Checks if vehicle is located on current street
     * @return - true if vehicle is on currStreet
     */
    @JsonIgnore
    private boolean isOnCurrStreet(){
        return Math.abs(getDistance(currStreet.getFrom(),coords) + getDistance(coords,currStreet.getTo())
                - getDistance(currStreet.getFrom(),currStreet.getTo())) <= 3.0;

    }

    /**
     * Sets current street that the vehicle is located on
     */
    @JsonIgnore
    public void findAndSetCurrStreet() {
        if(currStreet == null) currStreet = streets.get(0);
        if(!isOnCurrStreet()){
            for(Street s : streets){
                currStreet = s;
                if(isOnCurrStreet()){
                    break;
                }
            }
        }
    }

    /**
     * @return - coordinates of object
     */
    public Coordinate getCoords() {
        return coords;
    }

    /**
     * @return - speed of object
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @return - root of object
     */
    @Override
    public Route getRoute() {
        return route;
    }

    /**
     * @return - stops of object
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * @return - time schedule of object
     */
    public List<Timeline> getTimelines() {
        return timelines;
    }

    /**
     * @return - streets of object
     */
    public List<Street> getStreets() {
        return streets;
    }

    /**
     * @return - count in seconds, while vehicle is on stop
     */
    @JsonIgnore
    public  int getOnStopCount() {
        return onStopCount;
    }

    /**
     * Sets new time in seconds for vehicle to wait on stop
     * @param onStopCount - new count
     */
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

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }
}
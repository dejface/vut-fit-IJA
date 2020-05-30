package ija.vut.fit.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;

/**
 * Class which represents Stop object
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Stop.class)
public class Stop implements Draw{
    private String id;
    private Coordinate coordinates;
    @JsonIgnore
    private Street onStreet;
    private Route route;

    private Stop(){}

    /**
     * Constructor for Stop object
     * @param stopId - name of the stop
     * @param coordinate - stop coordinates
     */
    public Stop(String stopId, Coordinate coordinate) {
        this.id = stopId;
        this.coordinates = coordinate;
    }

    /**
     * @return coordinates
     */
    public Coordinate getCoordinates() {
        return this.coordinates;
    }

    /**
     * @return name of stop
     */
    public String getId() {
        return id;
    }

    /**
     * @return overriden method toString()
     */
    @Override
    public String toString() {
        return "stop(" + getId() + ")";
    }

    /**
     * @return list of shapes which contains stops and their name
     */
    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return Arrays.asList(
                new Circle(coordinates.getX(), coordinates.getY(), 8, Color.LIGHTGRAY),
                new Text(coordinates.getX()-25, coordinates.getY()-8, id));
    }

    @Override
    public Route getRoute() {
        return route;
    }
}

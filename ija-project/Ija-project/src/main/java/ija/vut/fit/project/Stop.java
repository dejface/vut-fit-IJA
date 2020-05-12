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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Stop.class)
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Stop implements Draw{
    private String id;
    private Coordinate coordinates;
    @JsonIgnore
    private Street onStreet;

    private Stop(){}

    public Stop(String stopId, Coordinate coordinate, Street street) {
        this.id = stopId;
        this.coordinates = coordinate;
        this.onStreet = street;
    }

    public Coordinate getCoordinates() {
        return this.coordinates;
    }


    public String getId() {
        return id;
    }

    @JsonIgnore
    public Street getStreet() {
        if (this.onStreet == null) {
            return null;
        } else {
            return this.onStreet;
        }
    }

    @JsonIgnore
    public void setStreet(Street s) {
        this.onStreet = s;
    }

    @Override
    public String toString() {
        return "stop(" + getId() + ")";
    }

    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return Arrays.asList(
                new Circle(coordinates.getX(), coordinates.getY(), 8, Color.LIGHTGRAY),
                new Text(coordinates.getX()-25, coordinates.getY()-8, id));
    }
}

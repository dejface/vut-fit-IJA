package ija.vut.fit.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;

public class Street implements Draw {
    private Coordinate from, to;
    private String name;
    private List<Stop> stops;

    public Street() {
    }

    public Street(String name, Coordinate from, Coordinate to) {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return Arrays.asList(
                new Text(Math.min(from.getX(),to.getX()) + (Math.abs(from.getX() - to.getX())/2), Math.min(from.getY(),to.getY()) + (Math.abs(from.getY() - to.getY())/2),name),
                new Line(from.getX(), from.getY(), to.getX(), to.getY())
        );
    }

    public Coordinate getFrom() {
        return from;
    }

    public Coordinate getTo() {
        return to;
    }

    public String getName() {
        return name;
    }
}

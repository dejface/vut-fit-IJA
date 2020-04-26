package ija.vut.fit.project;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;

public class Street implements Draw {
    private Coordinate start, stop;
    private String name;

    public Street(String name,Coordinate start, Coordinate stop) {
        this.name = name;
        this.start = start;
        this.stop = stop;
    }

    @Override
    public List<Shape> getGUI() {
        return Arrays.asList(
                new Text(start.getX() + (Math.abs(start.getX() - stop.getX())/2), start.getY() + Math.abs(start.getY() - stop.getY()/2),name),
                new Line(start.getX(), start.getY(), stop.getX(), stop.getY())
        );
    }
}

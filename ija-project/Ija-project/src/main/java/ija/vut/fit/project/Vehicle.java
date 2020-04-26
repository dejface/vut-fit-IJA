package ija.vut.fit.project;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Draw {
    private Coordinate coords;
    private double speed;
    private List<Shape> gui;

    public Vehicle(Coordinate coords, double speed) {
        this.coords = coords;
        this.speed = speed;
        gui = new ArrayList<>();
        gui.add(new Circle(coords.getX(), coords.getY(), 8, Color.RED));
    }

    @Override
    public List<Shape> getGUI() {
        return gui;
    }
}

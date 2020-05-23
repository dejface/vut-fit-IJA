package ija.vut.fit.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;
import java.util.List;

/**
 * Class which represents Street object
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name", scope = Street.class)
@JsonDeserialize(converter = Street.StreetFixToDraw.class)
public class Street implements Draw {
    private Coordinate from, to;
    public String name;
    private List<Stop> stops;
    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    public int traffic = 10;

    public Street() {
    }

    /**
     * Constructor for Street object
     * @param name - name of the street
     * @param from - starting coordinates
     * @param to - ending coordinates
     */
    public Street(String name, Coordinate from, Coordinate to) {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    /**
     * Returns list of shapes which represents street and its name
     */
    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    @JsonIgnore
    private void setGui() {

        gui = Arrays.asList(
                new Line(from.getX(), from.getY(), to.getX(), to.getY()),
                new Text(Math.min(from.getX(),to.getX()) + (Math.abs(from.getX() - to.getX())/2),
                        Math.min(from.getY(),to.getY()) + (Math.abs(from.getY() - to.getY())/2),name));



    }


    /**
     * @return starting coordinates
     */
    public Coordinate getFrom() {
        return from;
    }

    /**
     * @return traffic throughput
     */
    public int getTraffic() {
        return traffic;
    }

    /**
     * @return ending coordinates
     */
    public Coordinate getTo() {
        return to;
    }

    /**
     * @return street name
     */
    public String getName() {
        return name;
    }

    /**
     * @return overriden method toString()
     */
    @Override
    public String toString() {
        return "stop(" + getName() + ")";
    }

    static class StreetFixToDraw extends StdConverter<Street,Street> {

        @Override
        public Street convert(Street value) {
            value.setGui();
            return value;
        }
    }
}

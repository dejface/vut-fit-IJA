package ija.vut.fit.project;


import java.util.Objects;

/**
 * Class which represents Coordinate object
 */
public class Coordinate {
    private double x;
    private double y;

    private Coordinate(){}

    /**
     * Constructor for Coordinate object
     * @param x - coordinate x
     * @param y - coordinate y
     */
    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return coordinate x
     */
    public double getX() {
        return x;
    }

    /**
     * @return coordinate y
     */
    public double getY() {
        return y;
    }

    /**
     * @return overriden method toString
     */
    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

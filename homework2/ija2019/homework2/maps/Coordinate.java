package ija.ija2019.homework2.maps;

import java.util.Objects;

public class Coordinate {
    private int x = 0;
    private int y = 0;

    public static Coordinate create(int x, int y){
        Coordinate myCoord = new Coordinate();
        if (x >= 0 && y >= 0) {
            myCoord.x = x;
            myCoord.y = y;
        } else {
            return null;
        }
        return myCoord;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int diffX(Coordinate c) {
        return this.x - c.x;
    }

    public int diffY(Coordinate c) {
        return this.y - c.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

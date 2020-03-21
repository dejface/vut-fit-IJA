package ija.ija2019.homework2.maps;


import java.util.*;

public class myStreet implements Street{
    public String streetID;
    public List<Coordinate> coords;
    public List<Stop> stops;

    @Override
    public boolean addStop(Stop stop) {
        if (((this.begin().getX() <= stop.getCoordinate().getX() && this.end().getX() >= stop.getCoordinate().getX())
            && (this.begin().getY() <= stop.getCoordinate().getY() && this.end().getY() >= stop.getCoordinate().getY()))
            || ((this.end().getX() <= stop.getCoordinate().getX() && this.begin().getX() >= stop.getCoordinate().getX())
            && (this.end().getY() <= stop.getCoordinate().getY() && this.begin().getY() >= stop.getCoordinate().getY()))){
            stops.add(stop);
            stop.setStreet(this);
            return true;
        } else return false;
    }

    @Override
    public Coordinate begin() {
        return this.coords.get(0);
    }

    @Override
    public Coordinate end() {
        if ((long) this.coords.size() == 3) {
            return this.coords.get(2);
        } else{
            return this.coords.get(1);
        }
    }

    @Override
    public boolean follows(Street s) {
        if (s == null){
            return false;
        }else if (this.end().getX() == s.begin().getX() && this.end().getY() == s.begin().getY()){
            return true;
        } else if ((this.end().diffX(s.begin()) < 0) || (this.end().diffY(s.begin()) < 0 )){
            if (this.begin().getX() == s.end().getX() && this.begin().getY() == s.end().getY()){
                return true;
            } else return false;
        }
        return false;
    }

    @Override
    public List<Coordinate> getCoordinates() {
        return this.coords;
    }

    @Override
    public String getId() {
        return this.streetID;
    }

    @Override
    public List<Stop> getStops() {
        return this.stops;
    }
}

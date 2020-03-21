package ija.ija2019.homework2.maps;

public class myStop implements Stop {
    public String stopId;
    public Coordinate coordinate;
    public Street onStreet;

    @Override
    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    @Override
    public String getId() {
        return this.stopId;
    }

    @Override
    public Street getStreet() {
        if (this.onStreet == null) {
            return null;
        } else {
            return this.onStreet;
        }
    }

    @Override
    public void setStreet(Street s) {
        this.onStreet = s;
    }

    @Override
    public String toString() {
        return "stop(" + getId() + ")";
    }
}
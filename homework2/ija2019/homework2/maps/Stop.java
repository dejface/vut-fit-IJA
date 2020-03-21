package ija.ija2019.homework2.maps;

public interface Stop {

    static Stop defaultStop(String id, Coordinate c){
        myStop stop = new myStop();
        stop.stopId = id;
        stop.coordinate = c;
        stop.onStreet = null;
        return stop;
    }
    Coordinate getCoordinate();

    String getId();

    Street getStreet();

    void setStreet(Street s);
}
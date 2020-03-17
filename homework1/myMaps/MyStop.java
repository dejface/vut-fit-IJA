/*
 * @author xorave05
 */
package vut.fit.ija.homework1.myMaps;

import vut.fit.ija.homework1.maps.Stop;
import vut.fit.ija.homework1.maps.Street;
import vut.fit.ija.homework1.maps.Coordinate;

public class MyStop implements Stop{
    public String stopId;
    public Coordinate coordinate;
    public Street onStreet;

    public MyStop(String id, Coordinate coords){
        this.stopId = id;
        this.coordinate = coords;
    }

    public MyStop(String id){
        this.stopId = id;
    }

    @Override
    public String getId(){
        return this.stopId;
    }

    @Override
    public Coordinate getCoordinate(){
        return this.coordinate; 
    }

    @Override
    public void setStreet(Street s){
        this.onStreet = s; 
    }

    @Override
    public Street getStreet(){

        if (this.onStreet == null) {
        	return null;
        } else {
        	return this.onStreet;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) { 
            return true; 
        }
        if (o == null || o.getClass() != this.getClass()) { 
        	return false; 
        }
        MyStop c = (MyStop) o;

        return (stopId == c.stopId || (stopId != null && stopId.equals(c.getId()))
                && (onStreet == c.onStreet || (onStreet != null && onStreet.equals(c.getStreet())))
                && (coordinate == c.coordinate || (coordinate != null && coordinate.equals(c.getCoordinate()))));
    }
}
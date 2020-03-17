/*
 * @author xorave05
 */
package vut.fit.ija.homework1.myMaps;

import java.util.*;

import vut.fit.ija.homework1.maps.Coordinate;
import vut.fit.ija.homework1.maps.Stop;
import vut.fit.ija.homework1.maps.Street;

public class MyStreet implements Street{
	public String streetID;
	public Coordinate streetBegin;
	public Coordinate streetEnd;
	public List<Stop> stops;
	    
	public MyStreet(String name, Coordinate begin, Coordinate end){
		this.streetID = name;
	    this.streetBegin = begin;
	    this.streetEnd = end;
	    this.stops = new ArrayList<>();
	}

	@Override
	public String getId(){
	        return this.streetID;
	    }

	@Override
	public List<Coordinate> getCoordinates(){
		List<Coordinate> list = new ArrayList<>();
	    list.add(this.streetBegin);
	    list.add(this.streetEnd);
	    return list;
	}

	@Override
	public List<Stop> getStops(){
	        return this.stops; 
	    }
	        
	@Override
	public void addStop(Stop stop){
		stops.add(stop);
	    stop.setStreet(this);
	}
}
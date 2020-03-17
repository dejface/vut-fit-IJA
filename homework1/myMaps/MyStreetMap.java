/*
 * @author xorave05
 */

package vut.fit.ija.homework1.myMaps;

import java.util.*;

import vut.fit.ija.homework1.maps.StreetMap;
import vut.fit.ija.homework1.maps.Street;

public class MyStreetMap implements StreetMap{
    public List<Street> MapStreets;
    
    public MyStreetMap(){
        this.MapStreets = new ArrayList<>();
    }
        
    @Override
    public void addStreet(Street s){
        this.MapStreets.add(s); 
    }
        
    @Override
    public Street getStreet(String id){
        for (Street street : this.MapStreets){
            if (street.getId().equals(id)){
                return street;
            } 
        }
        return null;
    }
}
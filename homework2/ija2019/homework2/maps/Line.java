package ija.ija2019.homework2.maps;

import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;

public interface Line {

    boolean addStop(Stop stop);

    boolean addStreet(Street street);

    static Line defaultLine(String id){
        myLine line = new myLine();
        line.lineNumber = id;
        return line;
    }

    List<SimpleImmutableEntry<Street, Stop>> getRoute();
}
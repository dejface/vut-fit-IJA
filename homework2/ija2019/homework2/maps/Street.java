package ija.ija2019.homework2.maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Street {

        boolean addStop(Stop stop);

        Coordinate begin();

        static Street defaultStreet(String id, Coordinate... coordinates){
                int count = 0;
                myStreet myStreet = new myStreet();
                myStreet.streetID = id;
                myStreet.coords = Arrays.asList(coordinates);
                myStreet.stops = new ArrayList<>();
                for (int i = 0; i < myStreet.coords.stream().count(); i++){
                        count += 1;
                }
                if (count == 3){
                        Coordinate begin = myStreet.begin();
                        Coordinate end = myStreet.end();
                        Coordinate mid = myStreet.coords.get(1);
                        if (begin.diffX(mid) != 0){
                                return null;
                        }
                        if (end.diffY(mid) != 0){
                                return null;
                        }
                }

                return myStreet;
        }

        Coordinate end();

        boolean follows(Street s);

        List<Coordinate> getCoordinates();

        String getId();

        List<Stop> getStops();
}
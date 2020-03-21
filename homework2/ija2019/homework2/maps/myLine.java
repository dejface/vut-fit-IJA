package ija.ija2019.homework2.maps;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class myLine implements Line {
    public String lineNumber;
    private List<Stop> stopsOfLine = new ArrayList<>();
    private List<Street> streetsOfLine = new ArrayList<>();
    private final List<AbstractMap.SimpleImmutableEntry<Street,Stop>> lineRoute = new ArrayList<>();

    @Override
    public final boolean addStop(Stop stop) {
        if (this.stopsOfLine.size() == 0){
            stopsOfLine.add(stop);
            streetsOfLine.add(stop.getStreet());
            lineRoute.add(new AbstractMap.SimpleImmutableEntry<Street, Stop>(stop.getStreet(),stop));
            return true;
        }
        if (stop.getStreet() == null) return false;
        if (!stop.getStreet().follows(this.streetsOfLine.get(this.streetsOfLine.size()-1))){
            return false;
        } else {
            stopsOfLine.add(stop);
            streetsOfLine.add(stop.getStreet());
            lineRoute.add(new AbstractMap.SimpleImmutableEntry<Street, Stop>(stop.getStreet(),stop));
            return true;
        }
    }

    @Override
    public final boolean addStreet(Street street) {
        streetsOfLine.add(street);
        if (street.getStops().size() == 0) {
            lineRoute.add(new AbstractMap.SimpleImmutableEntry<Street, Stop>(street, null));
        }
        return true;
    }
    @Override
    public final List<AbstractMap.SimpleImmutableEntry<Street,Stop>> getRoute() {
        return Collections.unmodifiableList(this.lineRoute);
    }
}

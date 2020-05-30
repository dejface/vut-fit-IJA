package ija.vut.fit.project;

public abstract class UpdateClass implements Updater, Draw  {
    private Route route;

    @Override
    public Route getRoute() {
        return route;
    }
}

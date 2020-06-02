package ija.vut.fit.project;

/**
 * Abstract class which helps to get access to Route in controller
 */
public abstract class UpdateClass implements Updater, Draw  {
    private Route route;

    @Override
    public Route getRoute() {
        return route;
    }
}

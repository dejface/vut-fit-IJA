package ija.vut.fit.project;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

/**
 * MainController class is connected to "layout.fxml" and communicates with GUI
 */
public class MainController {

    @FXML Pane paneContent;
    @FXML private TextField speedScale;
    @FXML private TextField timerField;
    @FXML private TextField timeChange;

    private List<Updater> updates = new ArrayList<>();
    private Timer timer;
    private LocalTime time = LocalTime.parse("06:00:00");
    //private List<Draw> vehicles = new ArrayList<>();
    //private List<Draw> streets = new ArrayList<>();
    static int count = 0;
    private boolean isRestart = false;

    public MainController() {

    }

    @FXML
    /**
     * Changes speed of simulation, speed is based on user input
     */
    private void onSpeedChange(){
        try{
            float scale = Float.parseFloat(speedScale.getText());
            if (!(scale > 0)){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invlid float number! Please enter valid one");
                alert.showAndWait();
            }
            timer.cancel();
            startRoute(scale);
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invlid float number! Please enter valid one");
            alert.showAndWait();
        }
    }

    @FXML
    /**
     * Sets new local time, based on user input
     */
    private void onTimeChange(){
        try {
            time = LocalTime.parse(timeChange.getText());
            isRestart = true;
            count = 0;
        } catch (DateTimeParseException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invlid time input! Please enter valid one in format HH:mm:ss");
            alert.showAndWait();
        }
    }

    @FXML
    /**
     * Changes scale of gui when scrolling
     */
    private void onZoom(ScrollEvent event){
        event.consume();
        double zoom = event.getDeltaY();
        if (zoom > 0.0) zoom = 1.1;
        else zoom = 0.9;

        paneContent.setScaleX(zoom * paneContent.getScaleX());
        paneContent.setScaleY(zoom * paneContent.getScaleY());
    }

    /**
     * Adds of drawable contents to GUI
     * @param contents - list of streets, stops, vehicles
     */
    public void setContents(List<Draw> contents) {
        for (Draw draw : contents) {
            paneContent.getChildren().addAll(draw.getGUI());
            if (draw instanceof Updater) {
                updates.add((Updater) draw);
            }
        }
     //   this.getVehicles();
    }
/*
    public void getVehicles(){
        for (Draw draw : this.contents){
            if (draw instanceof Vehicle) this.vehicles.add(draw);
            else if (draw instanceof Street) this.streets.add(draw);
        }
    }
*/

    /**
     * Starts route and timer of simulation. Route speed is based on scale parameter.
     * @param scale - speed of the simulation
     */
    public void startRoute(double scale){
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = time.plusSeconds(1);
                for (Updater updater : updates){
                    updater.update(time, isRestart);
                }
                if (isRestart && (count == 0)){
                    count++;
                    isRestart = false;
                }
                Platform.runLater(()-> {
                    try {
                        timerField.setText(time.toString());
                    } catch (Exception e){

                    }
                });

            }
        }, 0, (long) (1000 / scale));
    }
}

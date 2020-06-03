package ija.vut.fit.project;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * MainController class is connected to "layout.fxml" and communicates with GUI
 */
public class MainController {

    @FXML Pane paneContent;
    @FXML private TextField speedScale;
    @FXML private TextField timerField;
    @FXML private TextField timeChange;
    @FXML private ChoiceBox choiceBox;

    private List<Updater> updates = new ArrayList<>();
    private Timer timer;
    private LocalTime time = LocalTime.parse("06:00:00");
    static int count = 0;
    private boolean isRestart = false;
    private List<Draw> vehicles = new ArrayList<>();
    private List<Draw> contents = new ArrayList<>();
    private List<Street> newList1 = new ArrayList<>();
    private List<Street> newList2 = new ArrayList<>();
    private List<Street> newList3 = new ArrayList<>();
    private List<Street> newList4 = new ArrayList<>();
    private List<Stop> origStops1 = new ArrayList<>();
    private List<Stop> origStops2 = new ArrayList<>();
    private List<Stop> origStops3 = new ArrayList<>();
    private List<Stop> origStops4 = new ArrayList<>();
    private List<Street> origRoute1 = new ArrayList<>();
    private List<Street> origRoute2 = new ArrayList<>();
    private List<Street> origRoute3 = new ArrayList<>();
    private List<Street> origRoute4 = new ArrayList<>();
    private boolean routeRedefined = false;
    private static boolean newRoute = false;
    private static boolean newRoute1 = false;
    private static boolean newRoute2 = false;
    private static boolean newRoute3 = false;
    private static boolean newRoute4 = false;
    private static int countOfDetours = 0;

    public MainController() {

    }

    ObservableList<String> box = FXCollections.observableArrayList("Line 1 (BLUE)", "Line 2 (PURPLE)", "Line 3 (YELLOW)", "Line 4 (RED)");
    @FXML
    private void initialize(){
        choiceBox.setItems(box);
        choiceBox.setValue("Line 1 (BLUE)");
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
        if (zoom > 0.0) {
            zoom = 1.1;
            if (paneContent.getScaleX() <= 2.5 && paneContent.getScaleY() <= 2.5) {
                paneContent.setScaleX(zoom * paneContent.getScaleX());
                paneContent.setScaleY(zoom * paneContent.getScaleY());
            }
        }
        else {
            zoom = 0.9;

            if (paneContent.getScaleX() >= 0.9 && paneContent.getScaleY() >= 0.9) {
                paneContent.setScaleX(zoom * paneContent.getScaleX());
                paneContent.setScaleY(zoom * paneContent.getScaleY());
            }
        }
    }

    /**
     * Adds of drawable contents to GUI
     * @param contents - list of streets, stops, vehicles
     */
    public void setContents(List<Draw> contents) {
        this.contents = contents;
        for (Draw draw : contents) {
            paneContent.getChildren().addAll(draw.getGUI());
            if (draw instanceof Street) {
                for (Shape shape: draw.getGUI()) {
                    draw.getGUI().get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if(event.getButton().equals(MouseButton.PRIMARY)) {
                                for (Shape shape : draw.getGUI()) {
                                    if (shape instanceof Line) {
                                        if (countOfDetours == 0){
                                            newRoute = false;
                                        }
                                        if (newRoute){
                                            if (newRoute1) {
                                                if (!newList1.contains(draw)) {
                                                    newList1.add((Street) draw);
                                                }
                                            } else if (newRoute2) {
                                                if (!newList2.contains(draw)) {
                                                    newList2.add((Street) draw);
                                                }
                                            } else if (newRoute3) {
                                                if (!newList3.contains(draw)) {
                                                    newList3.add((Street) draw);
                                                }
                                            } else if (newRoute4) {
                                                if (!newList4.contains(draw)) {
                                                    newList4.add((Street) draw);
                                                }
                                            }
                                        } else if (((Street) draw).traffic == 100) {
                                            shape.setStroke(Color.BLACK);
                                            ((Street) draw).traffic = 10;
                                        } else {
                                            ((Street) draw).traffic += 10;
                                            shape.setStroke(Color.rgb(255, 255 - (((Street) draw).traffic / 10 * 25), 0));
                                        }
                                        System.out.println("Traffic level on street " + ((Street) draw).name + " is " + ((Street) draw).traffic);
                                    }
                                    if (shape instanceof Text) {
                                        if (((Street) draw).traffic == 10) ((Text) shape).setText(((Street) draw).name);
                                        else {
                                            ((Text) shape).setTextAlignment(TextAlignment.CENTER);
                                            ((Text) shape).setText(((Street) draw).name + "\nTraffic lvl: " + ((Street) draw).traffic / 10);
                                        }
                                    }
                                }
                            }
                            else if(event.getButton().equals(MouseButton.SECONDARY)){
                                for (Shape shape : draw.getGUI()) {
                                    if (shape instanceof Line) {
                                        if (!((Street) draw).closed) {
                                            shape.setStroke(Color.RED);
                                            ((Street) draw).closed = true;
                                            newRoute = true;
                                            getVehicles();
                                            removeStreetFromRoute((Street) draw);

                                        } else if (((Street) draw).closed) {
                                            shape.setStroke(Color.BLACK);
                                            ((Street) draw).closed = false;
                                            newRoute = false;
                                            updates.get(0).getRoute().setRoute(origRoute1);
                                            updates.get(1).getRoute().setRoute(origRoute2);
                                            updates.get(2).getRoute().setRoute(origRoute3);
                                            updates.get(3).getRoute().setRoute(origRoute4);
                                            ((Vehicle) updates.get(0)).setStops(origStops1);
                                            ((Vehicle) updates.get(1)).setStops(origStops2);
                                            ((Vehicle) updates.get(2)).setStops(origStops3);
                                            ((Vehicle) updates.get(3)).setStops(origStops4);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }

            }
            if (draw instanceof Vehicle){
                draw.getGUI().get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        for (Shape shape : draw.getGUI()){
                            if (shape instanceof Line | shape instanceof Polygon | shape instanceof Text | shape instanceof Ellipse){
                                if (!shape.isVisible()){
                                    shape.setVisible(true);
                                } else shape.setVisible(false);
                            }
                        }
                    }
                });
            }


            if (draw instanceof Updater) {
                updates.add((Updater) draw);
            }
        }
    }

    public void getVehicles(){
        if (vehicles.size() > 0) {
            vehicles.subList(0, vehicles.size()).clear();
        }
        for (Draw draw : this.contents){
            if (draw instanceof Vehicle) {
                this.vehicles.add(draw);
            }
        }
    }

    public void removeStreetFromRoute(Street s){
        if (updates.size() > 0) {
            updates.subList(0, updates.size()).clear();
        }
        for (Draw draw : this.vehicles) {
            if (draw instanceof Updater) updates.add((Updater) draw);
        }
        for (int i = 0; i < updates.size(); i++) {
            for (int j = 0; j < updates.get(i).getRoute().getRoute().size(); j++) {
                if (s.equals(updates.get(i).getRoute().getRoute().get(j))) {
                    updates.get(i).getRoute().getRoute().remove(j);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("WARNING: You have just blocked a street!");
                    if (i == 0) {
                        newRoute1 = true;
                        alert.setContentText("Please define a new route for Line number " + (i+1) + " (BLUE)");
                        countOfDetours++;
                    } else if (i == 1) {
                        newRoute2 = true;
                        alert.setContentText("Please define a new route for Line number " + (i+1) + " (PURPLE)");
                        countOfDetours++;
                    } else if (i == 2) {
                        alert.setContentText("Please define a new route for Line number " + (i+1) + " (YELLOW)");
                        newRoute3 = true;
                        countOfDetours++;
                    } else if (i == 3) {
                        alert.setContentText("Please define a new route for Line number " + (i+1) + " (RED)");
                        newRoute4 = true;
                        countOfDetours++;
                    }
                    alert.showAndWait();
                    break;
                }
            }
        }
    }

    public void confirm(){
        String line = (String) choiceBox.getValue();
        if(!routeRedefined) {
            origRoute1.addAll(updates.get(0).getRoute().getRoute());
            origRoute2.addAll(updates.get(1).getRoute().getRoute());
            origRoute3.addAll(updates.get(2).getRoute().getRoute());
            origRoute4.addAll(updates.get(3).getRoute().getRoute());
            origStops1.addAll(updates.get(0).getStops());
            origStops2.addAll(updates.get(1).getStops());
            origStops3.addAll(updates.get(2).getStops());
            origStops4.addAll(updates.get(3).getStops());
            routeRedefined = true;
        }
        switch (line) {
            case "Line 1 (BLUE)":
                updates.get(0).getRoute().getRoute().clear();
                for (Street street : newList1) updates.get(0).getRoute().getRoute().add(street);
                updates.get(0).getStops().clear();
                for (int i = 0; i < updates.get(0).getRoute().getRoute().size(); i++) {
                    List<Stop> stops = updates.get(0).getRoute().getRoute().get(i).getStops();
                    if (stops == null) continue;
                    updates.get(0).getStops().addAll(stops);
                }
                newRoute1 = false;
                countOfDetours--;
                break;
            case "Line 2 (PURPLE)":
                updates.get(1).getRoute().getRoute().clear();
                for (Street street : newList2) updates.get(1).getRoute().getRoute().add(street);
                updates.get(1).getStops().clear();
                for (int i = 0; i < updates.get(1).getRoute().getRoute().size(); i++) {
                    List<Stop> stops = updates.get(1).getRoute().getRoute().get(i).getStops();
                    if (stops == null) continue;
                    updates.get(1).getStops().addAll(stops);
                }
                newRoute2 = false;
                countOfDetours--;
                break;
            case "Line 3 (YELLOW)":
                updates.get(2).getRoute().getRoute().clear();
                for (Street street : newList3) updates.get(2).getRoute().getRoute().add(street);
                updates.get(2).getStops().clear();
                for (int i = 0; i < updates.get(2).getRoute().getRoute().size(); i++) {
                    List<Stop> stops = updates.get(2).getRoute().getRoute().get(i).getStops();
                    if (stops == null) continue;
                    updates.get(2).getStops().addAll(stops);
                }
                newRoute3 = false;
                countOfDetours--;
                break;
            case "Line 4 (RED)":
                updates.get(3).getRoute().getRoute().clear();
                for (Street street : newList4) updates.get(3).getRoute().getRoute().add(street);
                updates.get(3).getStops().clear();
                for (int i = 0; i < updates.get(3).getRoute().getRoute().size(); i++) {
                    List<Stop> stops = updates.get(3).getRoute().getRoute().get(i).getStops();
                    if (stops == null) continue;
                    updates.get(3).getStops().addAll(stops);
                }
                newRoute4 = false;
                countOfDetours--;
                break;
        }
    }

    /**
     * Starts route and timer of simulation. Route speed is based on scale parameter.
     * @param scale - speed of the simulation
     */
    public void startRoute(double scale){
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    time = time.plusSeconds(1);
                    for (Updater updater : updates){
                        updater.update(time, isRestart);
                    }
                    if (isRestart && (count == 0)){
                        count++;
                        isRestart = false;
                    }
                    try {
                        timerField.setText(time.toString());
                    } catch (Exception e){

                    }
                });
            }
        }, 0, (long) (1000 / scale));
    }
}
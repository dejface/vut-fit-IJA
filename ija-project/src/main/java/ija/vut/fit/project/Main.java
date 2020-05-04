package ija.vut.fit.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        BorderPane root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        MainController controller = loader.getController();
        List<Draw> contents = new ArrayList<>();
        Vehicle vehicle = new Vehicle(new Coordinate(100,100),20, new Route(Arrays.asList(
                new Coordinate(100,100),
                new Coordinate(200,200)
        )));
        //contents.add(vehicle);


        YAMLFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper mapper = new ObjectMapper(factory);


        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(100,200));
        Vehicle auto = new Vehicle(coordinates.get(0),10,new Route(Arrays.asList(
                new Coordinate(100,100),
                new Coordinate(200,200)
        )));

        Map mapa = mapper.readValue(new File("test.yml"), Map.class );
        Map auticka = mapper.readValue(new File("testVehicle.yml"), Map.class );
        auticka.getVehicles().forEach(a-> contents.add(a));
        mapa.getStops().forEach(z-> contents.add(z));
        mapa.getStreets().forEach(s -> contents.add(s));

        controller.setContents(contents);
        controller.startRoute(1);
    }
}

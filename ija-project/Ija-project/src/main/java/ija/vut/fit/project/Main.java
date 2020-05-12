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
import java.util.Collection;
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




        YAMLFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper mapper = new ObjectMapper(factory);


        Map mapa = mapper.readValue(new File("test.yml"), Map.class );

        contents.addAll(mapa.getStreets());
        contents.addAll(mapa.getStops());
        contents.addAll(mapa.getVehicles());

        //mapa.getVehicles().forEach(vehicle -> vehicle.getTimelines().forEach(timeline -> System.out.println(timeline.getTimeList())));

        controller.setContents(contents);
        controller.startRoute(1);

    }
}

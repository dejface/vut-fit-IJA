package ija.vut.fit.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
        contents.add(new Vehicle(new Coordinate(100,100),20, new Route(Arrays.asList(
                new Coordinate(100,100),
                new Coordinate(500,500)
        ))));
        contents.add(new Street("Antoninska", new Coordinate(100,100), new Coordinate(500,500)));
        controller.setContents(contents);
        controller.startRoute(1);
    }
}

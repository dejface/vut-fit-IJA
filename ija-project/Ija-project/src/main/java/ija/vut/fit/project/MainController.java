package ija.vut.fit.project;

import javafx.fxml.FXML;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private Pane paneContent;

    private List<Draw> contents = new ArrayList<>();

    @FXML
    private void onZoom(ScrollEvent event){
        event.consume();
        double zoom = event.getDeltaY();
        if (zoom > 0.0) zoom = 1.1;
        else zoom = 0.9;

        paneContent.setScaleX(zoom * paneContent.getScaleX());
        paneContent.setScaleY(zoom * paneContent.getScaleY());
    }

    public void setContents(List<Draw> contents) {
        this.contents = contents;
        for (Draw draw : contents){
            paneContent.getChildren().addAll(draw.getGUI());
        }
    }
}

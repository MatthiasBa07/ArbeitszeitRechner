package org.example.zeitrechner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class makeUI extends Application {
    public void start(Stage primaryStage) {
        try {
           BorderPane root = new BorderPane();
           Scene scene1 = new Scene(root, 400, 400);
           Label text = new Label("du");
           root.setCenter(text);
           primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}


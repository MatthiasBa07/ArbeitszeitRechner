package org.example.zeitrechner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class makeUI extends Application {
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        Label label1 = new Label("Ihr Arbeitsrechner");
        Button timestamp = new Button("Stempeln");
        timestamp.setOnAction(e -> label1.setText("+ 200'000 V-Bucks"));

        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(getClass().getResource("styleUI.css").toExternalForm());

        root.setTop(label1);
        root.setCenter(timestamp);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Arbeitszeitrechner");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}


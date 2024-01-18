package org.example.zeitrechner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class makeUI extends Application {
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        Label lblTitle = new Label("Ihr Arbeitsrechner");
        lblTitle.getStyleClass().add("lblTitle");

        Label lblStamp = new Label("Bitte Stempeln");
        lblStamp.getStyleClass().add("lblStamp");

        Label lblEmpty = new Label(" ");

        Button btnTimestamp = new Button("Stempeln");
        btnTimestamp.getStyleClass().add("timestamp");
        btnTimestamp.setOnAction(e -> lblStamp.setText("Gestempelt"));

        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(getClass().getResource("styleUI.css").toExternalForm());

        root.getChildren();

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Arbeitszeitrechner");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}


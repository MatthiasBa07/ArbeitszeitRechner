package org.example.zeitrechner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class makeUI extends Application {
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        root.getStyleClass().add("root");

        Label lblTitle = new Label("Ihr Arbeitsrechner");
        lblTitle.getStyleClass().add("lblTitle");
        root.add(lblTitle, 2, 0);

        Label lblStamp = new Label("Bitte Stempeln");
        lblStamp.getStyleClass().add("lblStamp");
        root.add(lblStamp, 2, 1);

        Button btnTimestamp = new Button("Stempeln");
        btnTimestamp.getStyleClass().add("timestamp");
        btnTimestamp.setOnAction(e -> lblStamp.setText("Gestempelt"));
        root.add(btnTimestamp, 2, 2);

        Button btnNewPerson = new Button("Neue Person");
        btnNewPerson.getStyleClass().add("btnNewPerson");
        root.add(btnNewPerson,0,0);

        for (int i = 0; i < 5; i++) {
            Button btnListPerson = new Button("Person");
            btnListPerson.getStyleClass().add("btnListPerson");
            root.add(btnListPerson,0,i + 1);
        }

        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(getClass().getResource("styleUI.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Arbeitszeitrechner");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}


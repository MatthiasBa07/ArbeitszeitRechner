package org.example.zeitrechner;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

public class makeUI extends Application {
    Label label = new Label("Label");

    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            ScrollPane leftScroll = new ScrollPane();
            VBox leftVbox = new VBox();
            BorderPane leftTopPane = new BorderPane();

            leftTopPane.getStyleClass().add("leftTopPane");
            leftScroll.getStyleClass().add("leftScroll");
            leftScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

            Button neuPersonButton = new Button("Neue Person");
            neuPersonButton.getStyleClass().add("personButton");
            leftTopPane.setCenter(neuPersonButton);

            ArrayList<Person> personen = PersonJDBCDao.getInstance().getAllPerson();
            ArrayList<BorderPane> paneList = new ArrayList<>();
            for (Person person : personen) {
                Button personButton = new Button(person.getFullName());
                personButton.getStyleClass().add("personButton");
                leftVbox.getChildren().add(personButton);
                personButton.setOnAction(e -> {
                    root.setTop(paneList.get(personen.lastIndexOf(person)));
                });

                BorderPane borderPane = new BorderPane();
                Label nameLabel = new Label(person.getFullName());
                nameLabel.getStyleClass().add("nameLabel");
                borderPane.setTop(nameLabel);
                paneList.add(borderPane);
            }






            leftScroll.setContent(leftVbox);

            VBox leftPane = new VBox();
            leftPane.getStyleClass().add("leftPane");
            leftPane.getChildren().addAll(leftTopPane, leftScroll);
            root.setLeft(leftPane);


            Scene scene = new Scene(root, 600, 600);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styleUI.css")).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Zeitrechner");
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(makeUI.class.getResourceAsStream("uhr.jpg"))));
            primaryStage.show();
            primaryStage.setResizable(false);
            primaryStage.setOnCloseRequest(e -> System.exit(0));



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}


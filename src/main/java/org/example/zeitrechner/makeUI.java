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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.SimpleTimeZone;

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

            BorderPane homePane = new BorderPane();
            Label homePaneWelcome = new Label("Willkommen im Zeitrechner");
            homePaneWelcome.getStyleClass().add("homePaneWelcome");
            HBox homePaneWelcomeBox = new HBox(homePaneWelcome);
            homePaneWelcomeBox.setAlignment(Pos.CENTER);
            homePane.setTop(homePaneWelcomeBox);
            Label homePlanePerson = new Label("←\tWähle eine Person aus\n\toder erstelle eine neue.");
            homePlanePerson.getStyleClass().add("homePaneWelcome");
            VBox homeLeftBox = new VBox(homePlanePerson);
            homeLeftBox.setAlignment(Pos.CENTER);
            homePane.setLeft(homeLeftBox);
            root.setCenter(homePane);

            Button neuPersonButton = new Button("Neue Person");
            BorderPane neuPersonPane = new BorderPane();
            neuPersonPane.setCenter(new Label("Neue Person"));
            neuPersonButton.setOnAction(e -> {
                root.setCenter(neuPersonPane);
            });
            neuPersonButton.getStyleClass().add("personButton");
            leftTopPane.setCenter(neuPersonButton);

            ArrayList<Person> personen = PersonJDBCDao.getInstance().getAllPerson("Vorname");






            BorderPane borderPane = new BorderPane();

            BorderPane contentBorderPane = new BorderPane();
            HBox stampBox = new HBox();
            BorderPane stampPane = new BorderPane();
            HBox lastStampBox = new HBox();
            HBox stampButtonBox = new HBox();
            Label nameLabel = new Label("Vormane Nachname");
            HBox nameLabelBox = new HBox(nameLabel);
            Button stampButton = new Button("Stempeln");
            Label stampLabel = new Label("Keine Stempel.");

            nameLabel.getStyleClass().add("nameLabel");
            stampButton.getStyleClass().add("stampButton");
            stampLabel.getStyleClass().add("stampLabel");

            borderPane.setCenter(contentBorderPane);
            contentBorderPane.setTop(stampBox);
            stampBox.getChildren().add(stampPane);
            stampPane.setLeft(stampButtonBox);
            stampPane.setRight(lastStampBox);

            nameLabelBox.setAlignment(Pos.CENTER);
            borderPane.setTop(nameLabelBox);

            stampButtonBox.setAlignment(Pos.CENTER_LEFT);
            stampButtonBox.getChildren().add(stampButton);

            lastStampBox.getChildren().add(stampLabel);


            for (Person person : personen) {
                Button personButton;
                if (person.getFullName().length()>18) {
                    String name = person.getFullName();
                    name=name.replace(" ","\n");
                    personButton = new Button(name);
                } else {
                    personButton = new Button(person.getFullName());
                }
                personButton.getStyleClass().add("personButton");
                leftVbox.getChildren().add(personButton);

                personButton.setOnAction(e -> {
                    if (person.getFullName().length()>28) {
                        String name = person.getFullName();
                        name=name.replace(" ","\n");
                        nameLabel.setText(name);
                    } else {
                        nameLabel.setText(person.getFullName());
                    }

                    try {
                        if (TimestampJDBCDao.getInstance().getTimestampByPerson(person) == null){
                            stampLabel.setText("Keine Stempel.");
                        } else {
                            Timestamp lastTimestamp = TimestampJDBCDao.getInstance().getTimestampByPerson(person).getLast();
                            String oldDateString = lastTimestamp.getDate().toString();
                            SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = oldDateFormat.parse(oldDateString);

                            SimpleDateFormat newDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                            String newDateString = newDateFormat.format(date);

                            int[] lastTimeArray =  Calculator.getInstance().sekToTime(lastTimestamp.getSek());
                            String lastTime = Calculator.getInstance().addZero(lastTimeArray[0]) + ":" + Calculator.getInstance().addZero(lastTimeArray[1]) + ":" + Calculator.getInstance().addZero(lastTimeArray[2]);

                            stampLabel.setText("Letzter Stempel:\n" + newDateString + "\n" + lastTime);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                stampButton.setOnAction(e -> {
                    try {
                        TimestampJDBCDao.getInstance().insertTimestamp(person, LocalDate.now(), LocalTime.now().toSecondOfDay());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                root.setCenter(borderPane);
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


package org.example.zeitrechner;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class makeUI extends Application {

    BorderPane root = new BorderPane();
    ScrollPane leftScroll = new ScrollPane();
    VBox leftVbox = new VBox();
    BorderPane leftTopPane = new BorderPane();

    Label nameLabel = new Label("Vormane Nachname");
    Button stampButton = new Button("Stempeln");
    Label stampLabel = new Label("Keine Stempel.");
    BorderPane loadingPane = new BorderPane();
    BorderPane loadFailPane = new BorderPane();
    BorderPane borderPane = new BorderPane();

    Button manTimeButton = new Button("Manuell hinzufügen");

    @Override
    public void start(Stage primaryStage) {
        try {
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
            neuPersonButton.setOnAction(e -> {
                Stage neuPersonWindow = new Stage();

                neuPersonWindow.initModality(Modality.APPLICATION_MODAL);
                neuPersonWindow.setTitle("Person erstellen");

                BorderPane layout = new BorderPane();
                layout.setPadding(new Insets(25, 25, 0, 25));

                TextFormatter<String> vornameFormatter = new TextFormatter<>(new DefaultStringConverter(), "", change ->
                        change.getControlNewText().length() <= 15 ? change : null);
                TextFormatter<String> nachnameFormatter = new TextFormatter<>(new DefaultStringConverter(), "", change ->
                        change.getControlNewText().length() <= 15 ? change : null);


                TextField vornameInput = new TextField();
                vornameInput.setPromptText("Vorname eingeben");
                vornameInput.setTextFormatter(vornameFormatter);

                Label vornameLabel = new Label("Vorname");

                VBox vornameBox = new VBox(vornameLabel, vornameInput);

                TextField nachnameInput = new TextField();
                nachnameInput.setPromptText("Nachname eingeben");
                nachnameInput.setTextFormatter(nachnameFormatter);

                Label nachnameLabel = new Label("Nachname");

                VBox nachnameBox = new VBox(nachnameLabel, nachnameInput);

                Button confirmButton = new Button("Erstellen");
                confirmButton.setOnAction(c -> {
                    try {
                        String vorname;
                        if (vornameInput.getLength() > 15) {
                            vorname = vornameInput.getText(0, 15);
                        } else {
                            vorname = vornameInput.getText();
                        }

                        String nachname;
                        if (nachnameInput.getLength() > 15) {
                            nachname = nachnameInput.getText(0, 15);
                        } else {
                            nachname = nachnameInput.getText();
                        }

                        PersonJDBCDao.getInstance().insertPerson(vorname, nachname);
                        neuPersonWindow.close();
                        loadPersons();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                Button cancelButton = new Button("Abbrechen");
                cancelButton.setOnAction(c -> {
                    neuPersonWindow.close();
                });
                HBox buttonBox = new HBox(cancelButton, confirmButton);
                buttonBox.setPrefHeight(50);
                buttonBox.setMaxHeight(50);
                buttonBox.setPrefWidth(250);
                buttonBox.setMaxWidth(250);
                buttonBox.setSpacing(100);

                VBox mainBox = new VBox();
                mainBox.setSpacing(30);
                mainBox.getStyleClass().add("mainBox");
                mainBox.getChildren().addAll(vornameBox, nachnameBox);

                layout.setCenter(mainBox);
                layout.setBottom(buttonBox);

                Scene scene = new Scene(layout, 300, 300);
                neuPersonWindow.getIcons().add(new Image(Objects.requireNonNull(makeUI.class.getResourceAsStream("uhr.jpg"))));
                neuPersonWindow.setScene(scene);

                neuPersonWindow.showAndWait();
            });
            neuPersonButton.getStyleClass().add("personButton");
            leftTopPane.setCenter(neuPersonButton);

            BorderPane contentBorderPane = new BorderPane();
            HBox stampBox = new HBox();
            BorderPane stampPane = new BorderPane();
            HBox lastStampBox = new HBox();
            HBox stampButtonBox = new HBox();
            HBox nameLabelBox = new HBox(nameLabel);

            loadFailPane.setCenter(new Label("Ein Fehler ist Aufgetreten."));

            Image loading = new Image(Objects.requireNonNull(getClass().getResourceAsStream("load.gif")));

            ImageView imageView = new ImageView(loading);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            loadingPane.setCenter(imageView);

            nameLabel.getStyleClass().add("nameLabel");
            stampButton.getStyleClass().add("stampButton");
            stampLabel.getStyleClass().add("stampLabel");
            manTimeButton.getStyleClass().add("manButton");
            stampBox.getStyleClass().add("stampBox");
            stampBox.setSpacing(40);

            borderPane.setCenter(contentBorderPane);
            contentBorderPane.setTop(stampBox);
            stampBox.getChildren().add(stampPane);
            stampPane.setLeft(stampButtonBox);
            stampPane.setCenter(lastStampBox);
            stampBox.getChildren().add(manTimeButton);

            nameLabelBox.setAlignment(Pos.CENTER);
            borderPane.setTop(nameLabelBox);

            stampButtonBox.setAlignment(Pos.CENTER_LEFT);
            stampButtonBox.getChildren().add(stampButton);

            lastStampBox.getChildren().add(stampLabel);

            loadPersons();

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

    private void loadPersons() {
        leftVbox.getChildren().clear();
        ArrayList<Person> personen = null;
        try {
            personen = PersonJDBCDao.getInstance().getAllPerson("Vorname");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Person person : personen) {

            Button personButton;
            if (person.getFullName().length() > 18) {
                String name = person.getFullName();
                name = name.replace(" ", "\n");
                personButton = new Button(name);
            } else {
                personButton = new Button(person.getFullName());
            }
            personButton.getStyleClass().add("personButton");
            leftVbox.getChildren().add(personButton);

            personButton.setOnAction(e -> {

                root.setCenter(loadingPane);

                Task<Void> datenLaden = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {

                        if (person.getFullName().length() > 28) {
                            String name = person.getFullName();
                            name = name.replace(" ", "\n");
                            nameLabel.setText(name);
                        } else {
                            nameLabel.setText(person.getFullName());
                        }

                        try {
                            if (TimestampJDBCDao.getInstance().getTimestampByPerson(person) == null) {
                                stampLabel.setText("Keine Stempel.");
                            } else {
                                Timestamp lastTimestamp = TimestampJDBCDao.getInstance().getTimestampByPerson(person).getLast();
                                String oldDateString = lastTimestamp.getDate().toString();
                                SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = oldDateFormat.parse(oldDateString);

                                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                                String newDateString = newDateFormat.format(date);

                                int[] lastTimeArray = Calculator.getInstance().sekToTime(lastTimestamp.getSek());
                                String lastTime = Calculator.getInstance().addZero(lastTimeArray[0]) + ":" + Calculator.getInstance().addZero(lastTimeArray[1]) + ":" + Calculator.getInstance().addZero(lastTimeArray[2]);

                                stampLabel.setText("Letzter Stempel:\n" + newDateString + "\n" + lastTime);

                            }
                        } catch (SQLException | ParseException ex) {
                            throw new RuntimeException(ex);
                        }

                        stampButton.setOnAction(r -> {

                            Timestamp timestamp = new Timestamp(0, person, LocalDate.now(), LocalTime.now().toSecondOfDay());

                            try {
                                TimestampJDBCDao.getInstance().insertTimestamp(timestamp);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }


                            String getName = nameLabel.getText();
                            getName = getName.replace("\n", " ");
                            String[] split = getName.split(" ");
                            Person person1;
                            try {
                                person1 = PersonJDBCDao.getInstance().getPersonByName(split[0], split[1]);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }

                            try {
                                if (TimestampJDBCDao.getInstance().getTimestampByPerson(person1) == null) {
                                    stampLabel.setText("Keine Stempel.");
                                } else {
                                    Timestamp lastTimestamp = TimestampJDBCDao.getInstance().getTimestampByPerson(person1).getLast();
                                    String oldDateString = lastTimestamp.getDate().toString();
                                    SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = oldDateFormat.parse(oldDateString);

                                    SimpleDateFormat newDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                                    String newDateString = newDateFormat.format(date);

                                    int[] lastTimeArray = Calculator.getInstance().sekToTime(lastTimestamp.getSek());
                                    String lastTime = Calculator.getInstance().addZero(lastTimeArray[0]) + ":" + Calculator.getInstance().addZero(lastTimeArray[1]) + ":" + Calculator.getInstance().addZero(lastTimeArray[2]);

                                    String lableShowOvertime = new String(Calculator.getInstance().calculateOverTime(0, lastTimestamp, TimestampJDBCDao.getInstance().getTimestampByPerson(person).get(TimestampJDBCDao.getInstance().getTimestampByPerson(person).size() - 2)));


                                    stampLabel.setText("Letzter Stempel:\n" + newDateString + "\n" + lastTime + "\n" + (lableShowOvertime));

                                }
                            } catch (SQLException | ParseException ex) {
                                throw new RuntimeException(ex);
                            }

                        });
                        return null;
                    }
                };


                manTimeButton.setOnAction(act -> {
                    Stage manTimeWindow = new Stage();

                    manTimeWindow.initModality(Modality.APPLICATION_MODAL);
                    manTimeWindow.setTitle("Zeit Eintragen");

                    BorderPane layout = new BorderPane();
                    layout.setPadding(new Insets(25, 25, 0, 25));

                    TextFormatter<String> vornameFormatter = new TextFormatter<>(new DefaultStringConverter(), "", change ->
                            change.getControlNewText().length() <= 15 ? change : null);
                    TextFormatter<String> nachnameFormatter = new TextFormatter<>(new DefaultStringConverter(), "", change ->
                            change.getControlNewText().length() <= 15 ? change : null);


                    TextField vornameInput = new TextField();
                    vornameInput.setPromptText("Vorname eingeben");
                    vornameInput.setTextFormatter(vornameFormatter);

                    Label vornameLabel = new Label("Vorname");

                    VBox vornameBox = new VBox(vornameLabel, vornameInput);

                    TextField nachnameInput = new TextField();
                    nachnameInput.setPromptText("Nachname eingeben");
                    nachnameInput.setTextFormatter(nachnameFormatter);

                    Label nachnameLabel = new Label("Nachname");

                    VBox nachnameBox = new VBox(nachnameLabel, nachnameInput);

                    Button confirmButton = new Button("Erstellen");
                    confirmButton.setOnAction(c -> {
                        try {
                            String vorname;
                            if (vornameInput.getLength() > 15) {
                                vorname = vornameInput.getText(0, 15);
                            } else {
                                vorname = vornameInput.getText();
                            }

                            String nachname;
                            if (nachnameInput.getLength() > 15) {
                                nachname = nachnameInput.getText(0, 15);
                            } else {
                                nachname = nachnameInput.getText();
                            }

                            PersonJDBCDao.getInstance().insertPerson(vorname, nachname);
                            manTimeWindow.close();
                            loadPersons();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    Button cancelButton = new Button("Abbrechen");
                    cancelButton.setOnAction(c -> {
                        manTimeWindow.close();
                    });
                    HBox buttonBox = new HBox(cancelButton, confirmButton);
                    buttonBox.setPrefHeight(50);
                    buttonBox.setMaxHeight(50);
                    buttonBox.setPrefWidth(250);
                    buttonBox.setMaxWidth(250);
                    buttonBox.setSpacing(100);

                    VBox mainBox = new VBox();
                    mainBox.setSpacing(30);
                    mainBox.getStyleClass().add("mainBox");
                    mainBox.getChildren().addAll(vornameBox, nachnameBox);

                    layout.setCenter(mainBox);
                    layout.setBottom(buttonBox);

                    Scene scene = new Scene(layout, 300, 300);
                    manTimeWindow.getIcons().add(new Image(Objects.requireNonNull(makeUI.class.getResourceAsStream("uhr.jpg"))));
                    manTimeWindow.setScene(scene);

                    manTimeWindow.showAndWait();
                });


                datenLaden.setOnSucceeded(event -> {
                    root.setCenter(borderPane);
                });

                datenLaden.setOnFailed(event -> {
                    root.setCenter(loadFailPane);
                });

                new Thread(datenLaden).start();
            });
        }
    }

    public static void main(String[] args) {
        launch();
    }
}


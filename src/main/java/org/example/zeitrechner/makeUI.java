package org.example.zeitrechner;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
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
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    ChoiceBox<String> dayChoose = new ChoiceBox<>();
    ScrollPane dayScroll = new ScrollPane();


    Button delButton = new Button("Stempel löschen...");
    VBox stampsBox = new VBox(dayChoose, dayScroll, delButton);
    VBox daysBox = new VBox();
    Label overtimeLabel = new Label("aktueller Stand:\n0");

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

                confirmButton.disableProperty().bind(
                        Bindings.createBooleanBinding(() ->
                                        vornameInput.getText().trim().isEmpty() || nachnameInput.getText().trim().isEmpty(),
                                vornameInput.textProperty(),
                                nachnameInput.textProperty()
                        )
                );

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
                buttonBox.setSpacing(115);

                VBox mainBox = new VBox();
                mainBox.setSpacing(30);
                mainBox.getStyleClass().add("mainBox");
                mainBox.getChildren().addAll(vornameBox, nachnameBox);

                layout.setCenter(mainBox);
                layout.setBottom(buttonBox);

                Scene scene = new Scene(layout, 300, 300);
                neuPersonWindow.getIcons().add(new Image(Objects.requireNonNull(makeUI.class.getResourceAsStream("uhr.jpg"))));
                neuPersonWindow.setScene(scene);
                neuPersonWindow.setResizable(false);

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
            stampBox.setPrefHeight(75);

            contentBorderPane.setTop(stampBox);
            contentBorderPane.setLeft(stampsBox);
            contentBorderPane.setCenter(overtimeLabel);
            borderPane.setCenter(contentBorderPane);
            stampBox.getChildren().add(stampPane);
            stampPane.setLeft(stampButtonBox);
            stampPane.setCenter(lastStampBox);
            stampBox.getChildren().add(manTimeButton);

            contentBorderPane.setAlignment(overtimeLabel, Pos.TOP_LEFT);

            overtimeLabel.setPadding(new Insets(0,0,0,20));

            nameLabelBox.setAlignment(Pos.CENTER);
            borderPane.setTop(nameLabelBox);

            stampButtonBox.setAlignment(Pos.TOP_LEFT);
            stampButtonBox.getChildren().add(stampButton);

            lastStampBox.getChildren().add(stampLabel);

            loadPersons();


            leftScroll.setContent(leftVbox);

            VBox leftPane = new VBox();
            leftPane.getStyleClass().add("leftPane");
            leftPane.getChildren().addAll(leftTopPane, leftScroll);
            root.setLeft(leftPane);


            stampsBox.setSpacing(10);
            dayChoose.setPrefWidth(150);
            delButton.setPrefWidth(150);
            dayScroll.setFitToHeight(true);
            dayScroll.setContent(daysBox);
            dayScroll.setPrefHeight(350);
            dayScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            dayScroll.setPadding(new Insets(10));
            daysBox.setSpacing(20);

            daysBox.getChildren().add(new Label(""));


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
        if (personen != null) {
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
                            daysBox.getChildren().clear();
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
                                Timestamp lastTimestamp = null;
                                try {
                                    if (TimestampJDBCDao.getInstance().getTimestampByPerson(person1) == null) {
                                        stampLabel.setText("Keine Stempel.");
                                    } else {
                                        lastTimestamp = TimestampJDBCDao.getInstance().getTimestampByPerson(person1).getLast();
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

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                                daysBox.getChildren().clear();
                                if (!dayChoose.getValue().equals("Keine Stempel")) {
                                    daysBox.getChildren().clear();
                                    dayChoose.setValue(lastTimestamp.getDate().format(formatter));

                                } else if (dayChoose.getValue().equals("Keine Stempel")) {
                                    dayChoose.setDisable(false);
                                    dayScroll.setDisable(false);
                                    delButton.setDisable(false);
                                    daysBox.getChildren().clear();
                                    dayChoose.getItems().clear();

                                    String date = lastTimestamp.getDate().format(formatter);
                                    dayChoose.setValue(date);
                                    dayChoose.getItems().add(date);
                                    dayChoose.setValue(date);
                                }

                            });

                            ArrayList<Timestamp> timestamps = new ArrayList<>();
                            if (TimestampJDBCDao.getInstance().getTimestampByPerson(person) != null) {
                                timestamps = TimestampJDBCDao.getInstance().getTimestampByPerson(person);
                            }

                            if (!timestamps.isEmpty()) {
                                dayChoose.setDisable(false);
                                dayScroll.setDisable(false);
                                delButton.setDisable(false);
                                daysBox.getChildren().clear();
                                dayChoose.getItems().clear();


                                ArrayList<LocalDate> dateList = new ArrayList<>();
                                for (Timestamp timestamp : timestamps) {
                                    dateList.add(timestamp.getDate());
                                }
                                dateList.sort(Collections.reverseOrder());
                                Set<LocalDate> dateSet = new LinkedHashSet<>(dateList);
                                dateList.clear();
                                dateList.addAll(dateSet);
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                                for (LocalDate date : dateList) {
                                    dayChoose.getItems().add(date.format(formatter));
                                }
                                dayChoose.setValue(dayChoose.getItems().getFirst());

                                LocalDate dayChooseDate = LocalDate.parse(dayChoose.getValue(), formatter);
                                daysBox.getChildren().clear();
                                timestamps.sort(new Comparator<Timestamp>() {
                                    @Override
                                    public int compare(Timestamp t1, Timestamp t2) {
                                        return Integer.compare(t2.getSek(), t1.getSek());
                                    }
                                });
                                for (Timestamp timestamp : timestamps) {
                                    if (timestamp.getDate().equals(dayChooseDate)) {
                                        int[] time = Calculator.getInstance().sekToTime(timestamp.getSek());
                                        ArrayList<String> stringTime = new ArrayList<>();
                                        for (int i = 0; i < time.length; i++) {
                                            stringTime.add(Calculator.getInstance().addZero(time[i]));
                                        }
                                        Label label = new Label(stringTime.get(0) + ":" + stringTime.get(1) + ":" + stringTime.get(2));
                                        label.setPrefWidth(100);
                                        label.setStyle("-fx-alignment: center;");
                                        daysBox.getChildren().add(label);
                                    }
                                }

                                dayChoose.setOnAction(e -> {
                                    if (dayChoose.getValue() != null && !dayChoose.getValue().equals("Keine Stempel")) {
                                        LocalDate dayChooseActualDate = LocalDate.parse(dayChoose.getValue(), formatter);
                                        ArrayList<Timestamp> timestampsInLambda = new ArrayList<>();
                                        try {
                                            if (TimestampJDBCDao.getInstance().getTimestampByPerson(person) != null) {
                                                timestampsInLambda = TimestampJDBCDao.getInstance().getTimestampByPerson(person);
                                            }
                                        } catch (SQLException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                        daysBox.getChildren().clear();
                                        for (Timestamp timestamp : timestampsInLambda) {
                                            if (timestamp.getDate().equals(dayChooseActualDate)) {
                                                int[] time = Calculator.getInstance().sekToTime(timestamp.getSek());
                                                ArrayList<String> stringTime = new ArrayList<>();
                                                for (int i = 0; i < time.length; i++) {
                                                    stringTime.add(Calculator.getInstance().addZero(time[i]));
                                                }
                                                Label label = new Label(stringTime.get(0) + ":" + stringTime.get(1) + ":" + stringTime.get(2));
                                                label.setPrefWidth(100);
                                                label.setStyle("-fx-alignment: center;");
                                                daysBox.getChildren().add(label);
                                            }
                                        }
                                    }
                                });

                            } else {
                                daysBox.getChildren().clear();
                                dayChoose.getItems().clear();
                                dayChoose.setValue("Keine Stempel");
                                dayChoose.setDisable(true);
                                dayScroll.setDisable(true);
                                delButton.setDisable(true);
                            }

                            int overtime = Calculator.getInstance().calculateOverTime(person);
                            int[] overtimeList = Calculator.getInstance().sekToTime(overtime);
                            overtimeLabel.setText(overtimeList[0] + ":" + overtimeList[1] + ":" + overtimeList[2]);

                            return null;
                        }
                    };


                    manTimeButton.setOnAction(act -> {
                        Stage manTimeWindow = new Stage();

                        manTimeWindow.initModality(Modality.APPLICATION_MODAL);
                        manTimeWindow.setTitle("Zeit Eintragen");

                        BorderPane layout = new BorderPane();
                        layout.setPadding(new Insets(25, 25, 0, 25));

                        DatePicker datePicker = new DatePicker();
                        datePicker.setPromptText("Datum");
                        datePicker.setPrefWidth(250);
                        datePicker.getEditor().setDisable(true);
                        datePicker.getEditor().setOpacity(1);

                        Label dateLabel = new Label("Datum");

                        VBox dateBox = new VBox(dateLabel, datePicker);

                        TextField timePickerH = new TextField();
                        TextField timePickerM = new TextField();
                        TextField timePickerS = new TextField();

                        timePickerH.setPromptText("hh");
                        timePickerM.setPromptText("mm");
                        timePickerS.setPromptText("ss");

                        timePickerH.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*")) {
                                timePickerH.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                            if (timePickerH.getText().length() > 2) {
                                timePickerH.setText(timePickerH.getText().substring(0, 2));
                            }
                            if (!timePickerH.getText().isEmpty()) {
                                int eingabe = Integer.parseInt(timePickerH.getText());
                                if (eingabe > 23) {
                                    timePickerH.setText(oldValue);
                                }
                            }
                        });

                        timePickerM.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*")) {
                                timePickerM.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                            if (timePickerM.getText().length() > 2) {
                                timePickerM.setText(timePickerM.getText().substring(0, 2));
                            }
                            if (!timePickerM.getText().isEmpty()) {
                                int eingabe = Integer.parseInt(timePickerM.getText());
                                if (eingabe > 59) {
                                    timePickerM.setText(oldValue);
                                }
                            }
                        });

                        timePickerS.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*")) {
                                timePickerS.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                            if (timePickerS.getText().length() > 2) {
                                timePickerS.setText(timePickerS.getText().substring(0, 2));
                            }
                            if (!timePickerS.getText().isEmpty()) {
                                int eingabe = Integer.parseInt(timePickerS.getText());
                                if (eingabe > 59) {
                                    timePickerS.setText(oldValue);
                                }
                            }
                        });

                        timePickerM.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*")) {
                                timePickerM.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                            if (timePickerM.getText().length() > 2) {
                                timePickerM.setText(timePickerM.getText().substring(0, 2));
                            }
                        });

                        timePickerS.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*")) {
                                timePickerS.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                            if (timePickerS.getText().length() > 2) {
                                timePickerS.setText(timePickerS.getText().substring(0, 2));
                            }
                        });

                        Label timeLabel = new Label("Zeit");
                        Label spaceLabel = new Label(":");
                        Label spaceLabel1 = new Label(":");

                        HBox hTimeBox = new HBox(timePickerH, spaceLabel, timePickerM, spaceLabel1, timePickerS);
                        hTimeBox.setSpacing(5);

                        VBox timeBox = new VBox(timeLabel, hTimeBox);

                        Button confirmButton = new Button("Anwenden");

                        confirmButton.disableProperty().bind(
                                Bindings.createBooleanBinding(() ->
                                                timePickerH.getText().trim().isEmpty() || timePickerM.getText().trim().isEmpty() || datePicker.getValue() == null,
                                        timePickerH.textProperty(),
                                        timePickerM.textProperty(),
                                        datePicker.valueProperty()
                                )
                        );

                        confirmButton.setOnAction(c -> {
                            int sek;

                            if (timePickerS.getText().trim().isEmpty()) {
                                sek = Calculator.getInstance().timeToSek(new int[]{Integer.parseInt(timePickerH.getText()), Integer.parseInt(timePickerM.getText()), 0});
                            } else {
                                sek = Calculator.getInstance().timeToSek(new int[]{Integer.parseInt(timePickerH.getText()), Integer.parseInt(timePickerM.getText()), Integer.parseInt(timePickerS.getText())});
                            }

                            try {
                                TimestampJDBCDao.getInstance().insertTimestamp(person, datePicker.getValue(), sek);

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

                            manTimeWindow.close();


                            String getName = nameLabel.getText();
                            getName = getName.replace("\n", " ");
                            String[] split = getName.split(" ");
                            Person person1;
                            try {
                                person1 = PersonJDBCDao.getInstance().getPersonByName(split[0], split[1]);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            Timestamp lastTimestamp = null;
                            try {
                                if (TimestampJDBCDao.getInstance().getTimestampByPerson(person1) == null) {
                                    stampLabel.setText("Keine Stempel.");
                                } else {
                                    lastTimestamp = TimestampJDBCDao.getInstance().getTimestampByPerson(person1).getLast();
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


                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                            daysBox.getChildren().clear();
                            if (!dayChoose.getValue().equals("Keine Stempel")) {
                                daysBox.getChildren().clear();
                                dayChoose.setValue(lastTimestamp.getDate().format(formatter));

                            } else if (dayChoose.getValue().equals("Keine Stempel")) {
                                dayChoose.setDisable(false);
                                dayScroll.setDisable(false);
                                delButton.setDisable(false);
                                daysBox.getChildren().clear();
                                dayChoose.getItems().clear();

                                String date = lastTimestamp.getDate().format(formatter);
                                dayChoose.setValue(date);
                                dayChoose.getItems().add(date);
                                dayChoose.setValue(date);
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
                        buttonBox.setSpacing(103);

                        VBox mainBox = new VBox();
                        mainBox.setSpacing(30);
                        mainBox.getStyleClass().add("mainBox");
                        mainBox.getChildren().addAll(dateBox, timeBox);


                        layout.setCenter(mainBox);
                        layout.setBottom(buttonBox);

                        Scene scene = new Scene(layout, 300, 300);
                        manTimeWindow.getIcons().add(new Image(Objects.requireNonNull(makeUI.class.getResourceAsStream("uhr.jpg"))));
                        manTimeWindow.setScene(scene);
                        manTimeWindow.setResizable(false);

                        manTimeWindow.showAndWait();
                    });


                    datenLaden.setOnSucceeded(event -> {
                        root.setCenter(borderPane);
                    });

                    datenLaden.setOnFailed(event -> {
                        Throwable exception = event.getSource().getException();
                        System.out.println("Error occurred: " + exception.getMessage());
                        root.setCenter(loadFailPane);
                    });

                    new Thread(datenLaden).start();
                });
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}


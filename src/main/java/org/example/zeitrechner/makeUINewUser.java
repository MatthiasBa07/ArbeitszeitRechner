package org.example.zeitrechner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class makeUINewUser extends Application {
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        root.getStyleClass().add("rootNewUser");

        Scene scene = new Scene(root, 400, 380);
        scene.getStylesheets().add(getClass().getResource("styleUI.css").toExternalForm());

        Label lblTitle = new Label("Neuer Benutzer erstellen");
        lblTitle.getStyleClass().add("lblTitle");
        root.add(lblTitle, 0, 0);

        Label lblPrename = new Label("Vorname:");
        lblPrename.getStyleClass().add("lblStamp");
        root.add(lblPrename, 0, 1);

        TextField txtPrename = new TextField();
        txtPrename.getStyleClass().add("textField");
        root.add(txtPrename, 0, 2);

        Label lblSurname = new Label("Nachname:");
        lblSurname.getStyleClass().add("lblStamp");
        root.add(lblSurname, 0, 3);

        TextField txtSurname = new TextField();
        txtSurname.getStyleClass().add("textField");
        root.add(txtSurname, 0, 4);

        Label lblUserCreateConfirm = new Label("");
        lblUserCreateConfirm.getStyleClass().add("lblUserCreateConfirm");
        root.add(lblUserCreateConfirm, 0, 5);

        Button btnCreateNewUser = new Button("Benutzer erstellen");
        btnCreateNewUser.getStyleClass().add("btnCreateNewUser");
        btnCreateNewUser.setOnAction(e -> lblUserCreateConfirm.setText("Benutzer erstellt"));
        root.add(btnCreateNewUser,0,6);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Neuer Benutzer");
        primaryStage.show();
    }
}

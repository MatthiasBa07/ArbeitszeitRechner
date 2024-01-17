module org.example.zeitrechner {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.zeitrechner to javafx.fxml;
    exports org.example.zeitrechner;
}
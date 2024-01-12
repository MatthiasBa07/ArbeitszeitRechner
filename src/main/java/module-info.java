module org.example.zeitrechner {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.zeitrechner to javafx.fxml;
    exports org.example.zeitrechner;
}
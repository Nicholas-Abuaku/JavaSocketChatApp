module com.example.server_start {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.server_start to javafx.fxml;
    exports com.example.server_start;
}
module com.example.stopper {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.stopper to javafx.fxml;
    exports com.example.stopper;
}
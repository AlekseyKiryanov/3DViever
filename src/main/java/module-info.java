module com.cgvsu {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.desktop;
    requires com.google.common;


    opens com.cgvsu to javafx.fxml;
    exports com.cgvsu;
    exports com.cgvsu.logger;
    opens com.cgvsu.logger to javafx.fxml;
}
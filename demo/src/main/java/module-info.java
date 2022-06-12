module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    //requires java.desktop; added in order to work with java.awt.event.KeyEvent, don't need anymore

    opens com.example to javafx.fxml;
    exports com.example;
}

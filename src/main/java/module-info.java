module com.example.poetools {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.poetools to javafx.fxml;
    exports com.example.poetools;
}
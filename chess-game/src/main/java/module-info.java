module com.example.chess {
    requires javafx.controls;
    requires transitive javafx.graphics;

    opens com.example.chess to javafx.fxml;

    exports com.example.chess;
}
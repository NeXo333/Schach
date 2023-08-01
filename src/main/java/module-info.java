module thowl.model {
  requires javafx.controls;
  requires javafx.fxml;
  requires transitive javafx.graphics;

  opens thowl.model to
      javafx.fxml;

  exports thowl.model;
}

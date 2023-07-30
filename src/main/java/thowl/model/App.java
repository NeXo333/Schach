package thowl.model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Class contains the main method and creates the chessboard
 *
 * @autor Marlon Schrader
 */
public class App extends Application {

  /**
   * Main Method: Calls from class BoardUtils CreateChessboard() to visualize the board and adds the
   * Header Point from which everything will be executed
   *
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) {
    GridPane chessboard = BoardUtils.createChessboard();
    Scene scene = new Scene(chessboard, BoardUtils.BOARD_SIZE + 150, BoardUtils.BOARD_SIZE + 150);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Chess by TH-OWL");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  public static void setRoot(String string) {
    // If you have any specific functionality for this method, you can add it here.
  }
}

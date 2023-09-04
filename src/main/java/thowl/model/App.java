package thowl.model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class contains the main method and creates the chessboard
 *
 * @autor Marlon Schrader
 */
public class App extends Application {

  private Stage primaryStage;

  /**
   * Main Method: Calls from class BoardUtils CreateChessboard() to visualize the board and adds the
   * Header Point from which everything will be executed
   *
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Creates a Button to launch the second GUI = Starting a chess game.
        Button openSecondGUIButton = new Button("Start new Game");
        openSecondGUIButton.setOnAction(event -> openSecondGUI());
        openSecondGUIButton.setTranslateX(140);
        openSecondGUIButton.setTranslateY(30);

        //Creates a Button to Quit the Starting Screen and Close the game. 
        Button exit = new Button("Quit");
        exit.setOnAction(event -> primaryStage.close());
        exit.setTranslateX(140);
        exit.setTranslateY(30);

        //Creates a Button to Change the Grid Collor.
        Button Color = new Button("Set Grid Color");
        Color.setTranslateX(140);
        Color.setTranslateY(30);

        //Creates a Button to enabel the function to play on time
        Button Time = new Button("Enable play on time");
        Time.setTranslateX(140);
        Time.setTranslateY(30);

        //Creates a Button to disabel the function to play on time
        Button XTime = new Button ("Disable play on time");
        XTime.setTranslateX(140);
        XTime.setTranslateY(30);

        //Creates a Button to enable show moves
        Button Moves = new Button ("Enable Show Moves");
        Moves.setOnAction(event -> BoardUtils.MovesCheck.setWert(1));
        Moves.setTranslateX(140);
        Moves.setTranslateY(30);

        //Creates a Button to disable show moves
        Button XMoves = new Button ("Disable Show Moves");
        XMoves.setOnAction(event -> BoardUtils.MovesCheck.setWert(0));
        XMoves.setTranslateX(140);
        XMoves.setTranslateY(30);

        //Creates a VBox and adds the buttons
        VBox vbox = new VBox(10); 
        vbox.getChildren().addAll(openSecondGUIButton, Color, Time, XTime, Moves, XMoves, exit);

      

        StackPane root = new StackPane();
        root.getChildren().add(vbox);

        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Chess Game");

        primaryStage.setScene(scene);

        primaryStage.show();
    }
  

  
  
  
  
  
  
    private void openSecondGUI() {
    BoardUtils boardUtils = new BoardUtils();
    GridPane chessboard = boardUtils.createChessboard();

    Scene scene = new Scene(chessboard, 850, 850);
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



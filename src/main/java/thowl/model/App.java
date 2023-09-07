package thowl.model;

import java.io.OutputStream;
import java.io.PrintStream;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class contains methods for 3 different GUI`s Starting Gui
 *
 * @autor Dylan Senger
 */
public class App extends Application {

  private Stage primaryStage;
  TextArea textArea;

  public static String javaVersion() {
    return System.getProperty("java.version");
  }

  public static String javafxVersion() {
    return System.getProperty("javafx.version");
  }

  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Main Method: Starts the first Gui and the Functionality of the Buttons.
   *
   * <p>Main Method: Calls from class BoardUtils CreateChessboard() to visualize the board and adds
   * the Header Point from which everything will be executed
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

    // Creates a Button to Quit the Starting Screen and Close the game.
    Button exit = new Button("Quit");
    exit.setOnAction(event -> primaryStage.close());
    exit.setTranslateX(140);
    exit.setTranslateY(30);

    // Creates a Button to Change the Grid Collor.
    // Changing the Grid Color doesnt work, but i didnt want to delete the Code yet
    // im sure there is a simple fix for making the function work.
    Button Color = new Button("Set Grid Color");
    Color.setTranslateX(140);
    Color.setTranslateY(30);
    Color.setOnAction(event -> openColorGui());

    // Creates a Button to enabel the function to play on time
    // Disabled because there was no Time to Implement the Functiona
    Button Time = new Button("Enable play on time");
    Time.setTranslateX(140);
    Time.setTranslateY(30);
    Time.setDisable(true);

    // Creates a Button to disabel the function to play on time
    // Disabled because there was no Time to Implement the Functiona
    Button XTime = new Button("Disable play on time");
    XTime.setTranslateX(140);
    XTime.setTranslateY(30);
    XTime.setDisable(true);

    // Creates a Button to enable show moves
    Button Moves = new Button("Enable Show Moves");
    Moves.setOnAction(event -> BoardUtils.MovesCheck.setWert(1));
    Moves.setTranslateX(140);
    Moves.setTranslateY(30);

    // Creates a Button to disable show moves
    Button XMoves = new Button("Disable Show Moves");
    XMoves.setOnAction(event -> BoardUtils.MovesCheck.setWert(0));
    XMoves.setTranslateX(140);
    XMoves.setTranslateY(30);

    // Creates a VBox and adds the buttons
    VBox vbox = new VBox(10);
    vbox.getChildren().addAll(openSecondGUIButton, Color, Time, XTime, Moves, XMoves, exit);

    StackPane root = new StackPane();
    root.getChildren().add(vbox);

    Scene scene = new Scene(root, 400, 300);

    primaryStage.setTitle("Chess Game");

    primaryStage.setScene(scene);

    RedirectedOutputStream redirectedOutputStream = new RedirectedOutputStream();
    PrintStream printStream = new PrintStream(redirectedOutputStream);

    primaryStage.show();
  }

  private class RedirectedOutputStream extends OutputStream {
    @Override
    public void write(int b) {
      // Adds the read text to the textArea.
      if (textArea != null) {
        textArea.appendText(String.valueOf((char) b));
      }
    }
  }

  // Function to open the color gui
  private void openColorGui() {
    ChangeColor colorGui = new ChangeColor();
    colorGui.start(new Stage());
  }

  // Function to open the actual chesboardGUI.
  private void openSecondGUI() {
    BoardUtils boardUtils = new BoardUtils();
    GridPane chessboard = boardUtils.createChessboard();

    VBox vbox = new VBox(10);
    // Button to let one of the players surrender during game.
    // The player whose turn it is also surrenders.
    Button Surrender = new Button("Surrender");
    Surrender.setOnAction(
        event -> {
          ChessPiece chesspiece = new ChessPiece();
          Color currentColor = chesspiece.getCurrentTurnColor();

          if (currentColor == Color.WHITE) {
            System.out.println("White player surrendered");
          } else {
            System.out.println("Black player surrendered");
          }
        });
    // Button to let you quit the game during playing.
    Button exit2 = new Button("Quit");
    exit2.setOnAction(event -> primaryStage.close());

    HBox buttonBox = new HBox(10, Surrender, exit2);
    buttonBox.setAlignment(Pos.TOP_RIGHT);

    Region space = new Region();
    HBox.setHgrow(space, Priority.ALWAYS);

    // Adding Textarea to show the Terminaloutput on Screnn.
    textArea = new TextArea();
    textArea.setEditable(false);
    textArea.setWrapText(true);

    RedirectedOutputStream redirectedOutputStream = new RedirectedOutputStream();
    PrintStream printStream = new PrintStream(redirectedOutputStream);

    vbox.getChildren().addAll(space, buttonBox, chessboard, textArea);

    System.setOut(printStream);

    Scene scene = new Scene(vbox, 850, 850);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Chess by TH-OWL");
    primaryStage.show();
  }
}

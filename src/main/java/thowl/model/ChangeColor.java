package thowl.model;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class contains methods for changing the Color of the chesboard. Starts a GUI with Buttons that
 * are supposed to set the gridcolor. The Problem with changing the color lays within the allocation
 * of collors in my Code.
 *
 * @autor Dylan Senger
 */
public class ChangeColor extends Application {
  private ColorVariable colorVariable = new ColorVariable();

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Set Grid Color");

    // Button changing Grid Color to Default and closing the ColorGui.
    Button color1 = new Button("Default");
    color1.setTranslateX(120);
    color1.setTranslateY(20);
    color1.setOnAction(
        event -> {
          Thread task1 =
              new Thread(
                  () -> {
                    colorVariable.setColor1(Color.WHITE);
                  });

          Thread task2 =
              new Thread(
                  () -> {
                    colorVariable.setColor2(Color.LIGHTGRAY);
                  });

          Thread task3 =
              new Thread(
                  () -> {
                    BoardUtils colorv = new BoardUtils();
                    // BoardUtils.updateColors();
                  });

          Thread task4 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          primaryStage.close();
                        });
                  });
          task1.start();
          task2.start();
          task3.start();
          task4.start();
        });

    // Button changing Grid Color to Green & White and closing the ColorGui.
    Button color2 = new Button("Green & White");
    color2.setTranslateX(120);
    color2.setTranslateY(20);
    color2.setOnAction(
        event -> {
          Thread task1 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          colorVariable.setColor1(Color.GREEN);
                        });
                  });

          Thread task2 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          colorVariable.setColor2(Color.WHITE);
                        });
                  });

          Thread task3 =
              new Thread(
                  () -> {
                    BoardUtils colorv = new BoardUtils();
                    // BoardUtils.updateColors();
                  });

          Thread task4 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          primaryStage.close();
                        });
                  });
          task1.start();
          task2.start();
          task3.start();
          task4.start();
        });

    // Button changing Grid Color to Orange & Black and closing the ColorGui.
    Button color3 = new Button("Orange & Black");
    color3.setTranslateX(120);
    color3.setTranslateY(20);
    color3.setOnAction(
        event -> {
          Thread task1 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          colorVariable.setColor1(Color.ORANGE);
                        });
                  });

          Thread task2 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          colorVariable.setColor2(Color.LIGHTGRAY);
                        });
                  });

          Thread task3 =
              new Thread(
                  () -> {
                    BoardUtils colorv = new BoardUtils();
                    // BoardUtils.updateColors();
                  });

          Thread task4 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          primaryStage.close();
                        });
                  });
          task1.start();
          task2.start();
          task3.start();
          task4.start();
        });

    // Button changing Grid Blue & Black and closing the ColorGui.
    Button color4 = new Button("Blue & Black");
    color4.setTranslateX(120);
    color4.setTranslateY(20);
    color4.setOnAction(
        event -> {
          Thread task1 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          colorVariable.setColor1(Color.BLUE);
                        });
                  });

          Thread task2 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          colorVariable.setColor2(Color.BLACK);
                        });
                  });

          Thread task3 =
              new Thread(
                  () -> {
                    BoardUtils colorv = new BoardUtils();
                    // BoardUtils.updateColors();
                  });

          Thread task4 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          primaryStage.close();
                        });
                  });
          task1.start();
          task2.start();
          task3.start();
          task4.start();
        });

    // Button changing Grid Color to Red & Black and closing the ColorGui.
    Button color5 = new Button("Red & Black");
    color5.setTranslateX(120);
    color5.setTranslateY(20);
    color5.setOnAction(
        event -> {
          Thread task1 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          colorVariable.setColor1(Color.RED);
                        });
                  });

          Thread task2 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          colorVariable.setColor2(Color.LIGHTGRAY);
                        });
                  });

          Thread task3 =
              new Thread(
                  () -> {
                    BoardUtils colorv = new BoardUtils();
                    // BoardUtils.updateColors();
                  });

          Thread task4 =
              new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          primaryStage.close();
                        });
                  });
          task1.start();
          task2.start();
          task3.start();
          task4.start();
        });

    VBox vbox = new VBox(10);
    vbox.getChildren().addAll(color1, color2, color3, color4, color5);

    Scene scene = new Scene(vbox, 300, 200);

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Class is unused because the allocation of Colors doesnt work as i like it to. If used it gets
   * and sets Colors to a variable used to change the GridColor.
   *
   * @autor Dylan Senger
   */
  public class ColorVariable {

    private Color color1;
    private Color color2;

    public synchronized Color getColor1() {
      return color1;
    }

    public synchronized void setColor1(Color color1) {
      this.color1 = color1;
    }

    public synchronized Color getColor2() {
      return color2;
    }

    public synchronized void setColor2(Color color2) {
      this.color2 = color2;
    }
  }
}

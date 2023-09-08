package thowl.model;

import javafx.scene.paint.Color;

/**
 * The TurnManager class is responsible for managing the turns in a chess game. It keeps track of
 * the current turn's color and allows switching between players' turns.
 *
 * @author Marlon Schrader
 */
public class TurnManager {
  private static TurnManager instance;
  private Color currentTurnColor;

  private TurnManager() {
    // Initialize the turn to white's turn
    currentTurnColor = Color.WHITE;
  }

  /**
   * Gets the single instance of the TurnManager class.
   *
   * @return The TurnManager instance.
   */
  public static TurnManager getInstance() {
    if (instance == null) {
      instance = new TurnManager();
    }
    return instance;
  }

  /**
   * Gets the color of the current turn.
   *
   * @return The color of the current turn (either Color.WHITE or Color.BLACK).
   */
  public Color getCurrentTurnColor() {
    return currentTurnColor;
  }

  /**
   * Switches the current turn from one player to the other. It toggles between Color.WHITE and
   * Color.BLACK.
   */
  public void switchTurn() {
    currentTurnColor = (currentTurnColor == Color.WHITE) ? Color.BLACK : Color.WHITE;
  }

  /**
   * The GameManager class is a nested class within TurnManager that provides a convenient way to
   * access the TurnManager instance from other parts of the code.
   *
   * @author Marlon Schrader
   */
  public class GameManager {
    private static TurnManager turnManagerInstance;

    /**
     * Gets the TurnManager instance.
     *
     * @return The TurnManager instance.
     */
    public static TurnManager getTurnManager() {
      if (turnManagerInstance == null) {
        turnManagerInstance = TurnManager.getInstance();
      }
      return turnManagerInstance;
    }
  }
}

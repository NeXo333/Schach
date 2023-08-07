package thowl.model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Has methods to create a colored chessboard + indices and will be called by the main method
 * (App.java)
 */
public class BoardUtils {
  public double cellSize = 70.0;
  public Cell[][] cell = new Cell[8][8];

  /**
   * Creates the colored boared and calls method addIndices for the details
   *
   * @return Chessborard
   */
  public GridPane createChessboard() {
    GridPane chessboard = new GridPane();
    chessboard.setAlignment(Pos.CENTER);
    chessboard.setHgap(2); // Horizontal gap between cells
    chessboard.setVgap(2); // Vertical gap between cells
    // doesnt func
    chessboard.setPadding(new javafx.geometry.Insets(10));

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Color cellColor = (row + col) % 2 == 0 ? Color.WHITE : Color.LIGHTGRAY;

        // Test for positioning of a pawn.
        if (row == 1 || row == 6) {
          String pieceName = "pawn";
          String pieceImagePath = "/images/blackPawn.png"; // https://shorturl.at/cfnNV
          Image pieceImage = new Image(getClass().getResourceAsStream(pieceImagePath));
          cell[row][col] = new Cell(cellSize, cellColor, null, pieceName, pieceImage);

          // Set the piece image for the cell
          cell[row][col].setPieceImage(pieceImage);
          chessboard.add(cell[row][col], col + 1, row + 1);

        } else {
          cell[row][col] = new Cell(cellSize, cellColor, null, null, null);
          chessboard.add(cell[row][col], col + 1, row + 1);
        }
      }
    }

    // Add row indices (1-8)
    for (int row = 0; row < 8; row++) {
      Label rowIndex = new Label(String.valueOf(row + 1));
      rowIndex.setPrefSize(cellSize, cellSize);
      rowIndex.setAlignment(Pos.CENTER);
      chessboard.add(rowIndex, 0, row + 1);
    }

    // Add column indices (A-H)
    String[] columns = {"A", "B", "C", "D", "E", "F", "G", "H"};
    for (int col = 0; col < 8; col++) {
      Label colIndex = new Label(columns[col]);
      colIndex.setPrefSize(cellSize, cellSize);
      colIndex.setAlignment(Pos.CENTER);
      chessboard.add(colIndex, col + 1, 0);
    }

    return chessboard; // Return the created chessboard GridPane
  }
}

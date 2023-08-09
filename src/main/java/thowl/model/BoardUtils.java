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

    // create white pieces on the top
    createPiece(chessboard, 0, 0, Color.WHITE, "rook", "/images/whiteRook.png");
    createPiece(chessboard, 0, 1, Color.WHITE, "knight", "/images/whiteKnight.png");
    createPiece(chessboard, 0, 2, Color.WHITE, "bishop", "/images/whiteBishop.png");
    createPiece(chessboard, 0, 3, Color.WHITE, "queen", "/images/whiteQueen.png");
    createPiece(chessboard, 0, 4, Color.WHITE, "king", "/images/whiteKing.png");
    createPiece(chessboard, 0, 5, Color.WHITE, "bishop", "/images/whiteBishop.png");
    createPiece(chessboard, 0, 6, Color.WHITE, "knight", "/images/whiteKnight.png");
    createPiece(chessboard, 0, 7, Color.WHITE, "rook", "/images/whiteRook.png");

    // Place white pawns
    for (int col = 0; col < 8; col++) {
      createPiece(chessboard, 1, col, Color.WHITE, "pawn", "/images/whitePawn.png");
    }

    // Create black pieces on the buttom
    createPiece(chessboard, 7, 0, Color.BLACK, "rook", "/images/blackRook.png");
    createPiece(chessboard, 7, 1, Color.BLACK, "knight", "/images/blackKnight.png");
    createPiece(chessboard, 7, 2, Color.BLACK, "bishop", "/images/blackBishop.png");
    createPiece(chessboard, 7, 3, Color.BLACK, "queen", "/images/blackQueen.png");
    createPiece(chessboard, 7, 4, Color.BLACK, "king", "/images/blackKing.png");
    createPiece(chessboard, 7, 5, Color.BLACK, "bishop", "/images/blackBishop.png");
    createPiece(chessboard, 7, 6, Color.BLACK, "knight", "/images/blackKnight.png");
    createPiece(chessboard, 7, 7, Color.BLACK, "rook", "/images/blackRook.png");

    // Place black pawns
    for (int col = 0; col < 8; col++) {
      createPiece(chessboard, 6, col, Color.BLACK, "pawn", "/images/blackPawn.png");
    }

    // Create empty cells for the rest of the chessboard
    for (int row = 2; row <= 5; row++) {
      for (int col = 0; col < 8; col++) {
        Color cellColor = (row + col) % 2 == 0 ? Color.LIGHTGRAY : Color.WHITE;
        cell[row][col] = new Cell(cellSize, cellColor, null, null, null);
        chessboard.add(cell[row][col], col + 1, row + 1);
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

    // Test if you can move a piece
    movePiece(cell, 1, 1, 3, 1);

    return chessboard; // Return the created chessboard GridPane
  }

  private void createPiece(
      GridPane chessboard,
      int row,
      int col,
      Color pieceColor,
      String pieceName,
      String pieceImagePath) {
    Color cellColor = (row + col) % 2 == 0 ? Color.LIGHTGRAY : Color.WHITE;
    Image pieceImage = new Image(getClass().getResourceAsStream(pieceImagePath));

    cell[row][col] = new Cell(cellSize, cellColor, pieceColor, pieceName, pieceImage);
    cell[row][col].setPieceImage(pieceImage);

    chessboard.add(cell[row][col], col + 1, row + 1);
  }

  // Testing with pieces moving:

  public void movePiece(Cell[][] cellArray, int fromRow, int fromCol, int toRow, int toCol) {
    Cell fromCell = cell[fromRow][fromCol];

    // Get the piece values from the source cell
    Color pieceColor = fromCell.getPieceColor();
    String pieceName = fromCell.getPieceName();
    Image pieceImage = fromCell.getPieceImage();

    // Set the piece values in the destination cell
    cellArray[toRow][toCol].setPieceValues(pieceColor, pieceName, pieceImage);
    cell[toRow][toCol].setPieceImage(pieceImage);
    // Clear the piece values in the source cell
    cellArray[fromRow][fromCol].clearPiece();

    // Update the appearance of the cells
    cellArray[fromRow][toRow].updateCellAppearance();
    cellArray[toRow][toCol].updateCellAppearance();
  }

  // ...

}

package thowl.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Has methods to create a colored chessboard + indices and will be called by the main method
 * (App.java)
 */
public class BoardUtils {
  public int cellSize = 70;
  public Cell[][] cell = new Cell[8][8];
  private Cell selectedPiece = null;

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

    // Create empty cells for the rest of the chessboard
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Color cellColor = (row + col) % 2 == 0 ? Color.LIGHTGRAY : Color.WHITE;
        cell[row][col] = new Cell(row, col, cellSize, cellColor, null, null, null);

        // Set up the event handler for the cell
        Cell currentCell = cell[row][col];
        currentCell.setOnMouseClicked(event -> handleCellClick(currentCell));

        chessboard.add(
            cell[row][col], col + 1, row + 1); // one extra row & col for the indices in gridPane
      }
    }
    // Adds the pieces on there starting positions
    startPosition(chessboard);

    // adds the indices: 1-8 (row) and A-H (col)
    addIndices(chessboard);

    // Test if you can move a piece
    movePiece(cell, 1, 1, 3, 1);

    return chessboard; // Return the created chessboard GridPane
  }

  /* For logic in moving a piece / handle cell click
  private void logic(Cell cell) {
    String pieceName = cell.getPieceName();
    if (pieceName != null) {
      switch (pieceName) {
        case "pawn":
          handlePawnMove(cell);
          break;
        case "knight":
          handleKnightMove(cell);
          break;
          // ... (handle other pieces)
      }
    }
  }
  */

  public void handleCellClick(Cell clickedCell) {
    int row = clickedCell.getRow();
    int col = clickedCell.getCol();

    if (selectedPiece == null) {
      // First click: Select the piece to move
      if (clickedCell.getPieceName() != null) {
        selectedPiece = clickedCell;
        cell[row][col].setBackground(
            new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
      }
    } else {
      // Second click: Move the piece to the clicked cell
      int fromRow = selectedPiece.getRow();
      int fromCol = selectedPiece.getCol();

      // Perform the piece movement using the fromRow, fromCol, toRow, and toCol values
      movePiece(cell, fromRow, fromCol, row, col);

      // Clear the selection and reset the background
      cell[row][col].setBackground(
          new Background(
              new BackgroundFill(cell[row][col].getFieldColor(), CornerRadii.EMPTY, Insets.EMPTY)));
      selectedPiece = null;
    }

    // Toggle visibility of the highlight overlay
  }

  private void startPosition(GridPane chessboard) {
    // white rook
    Image pieceImage = new Image(getClass().getResourceAsStream("/images/whiteRook.png"));
    cell[0][0].setPieceValues(Color.WHITE, "rook", pieceImage);
    cell[0][7].setPieceValues(Color.WHITE, "rook", pieceImage);
    // black rook
    pieceImage = new Image(getClass().getResourceAsStream("/images/blackRook.png"));
    cell[7][0].setPieceValues(Color.BLACK, "rook", pieceImage);
    cell[7][7].setPieceValues(Color.BLACK, "rook", pieceImage);

    // white knight
    pieceImage = new Image(getClass().getResourceAsStream("/images/whiteKnight.png"));
    cell[0][1].setPieceValues(Color.WHITE, "knight", pieceImage);
    cell[0][6].setPieceValues(Color.WHITE, "knight", pieceImage);
    // black knight
    pieceImage = new Image(getClass().getResourceAsStream("/images/blackKnight.png"));
    cell[7][1].setPieceValues(Color.BLACK, "knight", pieceImage);
    cell[7][6].setPieceValues(Color.BLACK, "knight", pieceImage);

    // white bishop
    pieceImage = new Image(getClass().getResourceAsStream("/images/whiteBishop.png"));
    cell[0][2].setPieceValues(Color.WHITE, "bishop", pieceImage);
    cell[0][5].setPieceValues(Color.WHITE, "bishop", pieceImage);
    // black bishop
    pieceImage = new Image(getClass().getResourceAsStream("/images/blackBishop.png"));
    cell[7][2].setPieceValues(Color.BLACK, "bishop", pieceImage);
    cell[7][5].setPieceValues(Color.BLACK, "bishop", pieceImage);

    // white king
    pieceImage = new Image(getClass().getResourceAsStream("/images/whiteKing.png"));
    cell[0][3].setPieceValues(Color.WHITE, "king", pieceImage);
    // black King
    pieceImage = new Image(getClass().getResourceAsStream("/images/blackKing.png"));
    cell[7][3].setPieceValues(Color.BLACK, "king", pieceImage);

    // white queen
    pieceImage = new Image(getClass().getResourceAsStream("/images/whiteQueen.png"));
    cell[0][4].setPieceValues(Color.WHITE, "queen", pieceImage);
    // black queen
    pieceImage = new Image(getClass().getResourceAsStream("/images/blackQueen.png"));
    cell[7][4].setPieceValues(Color.BLACK, "queen", pieceImage);

    // white pawn loop
    pieceImage = new Image(getClass().getResourceAsStream("/images/whitePawn.png"));
    for (int col = 0; col < 8; col++) {
      cell[1][col].setPieceValues(Color.WHITE, "pawn", pieceImage);
    }

    // black pawn loop
    pieceImage = new Image(getClass().getResourceAsStream("/images/blackPawn.png"));
    for (int column = 0; column < 8; column++) {
      cell[6][column].setPieceValues(Color.WHITE, "pawn", pieceImage);
    }
  }

  public void addIndices(GridPane chessboard) {
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
  }

  // Testing with pieces moving:
  public void movePiece(Cell[][] cellArray, int fromRow, int fromCol, int toRow, int toCol) {
    // Get the piece values from the source cell
    Color pieceColor = cellArray[fromRow][fromCol].getPieceColor();
    String pieceName = cellArray[fromRow][fromCol].getPieceName();
    Image pieceImage = cellArray[fromRow][fromCol].getPieceImage();

    // Set the piece values in the destination cell
    cellArray[toRow][toCol].setPieceValues(pieceColor, pieceName, pieceImage);
    // Clear the piece values in the source cell
    cellArray[fromRow][fromCol].clearPiece();
  }
}

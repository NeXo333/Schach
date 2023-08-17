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
  public int cellSize = 70;
  public Cell[][] cell = new Cell[8][8];
  private Cell selectedCell = null;
  private Color currentTurnColor = Color.WHITE;

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
    // doesnt function
    chessboard.setPadding(new javafx.geometry.Insets(10));

    // Create empty cells for the rest of the chessboard
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Color cellColor = (row + col) % 2 == 0 ? Color.LIGHTGRAY : Color.WHITE;
        cell[row][col] = new Cell(row, col, cellSize, cellColor, null, null, null);

        // Set up the event handler for the cell
        final int finalRow = row;
        final int finalCol = col;
        cell[row][col].setOnMouseClicked(
            event -> {
              handleCellClick(cell[finalRow][finalCol]);
            });

        chessboard.add(
            cell[row][col], col + 1, row + 1); // one extra row & col for the indices in gridPane
      }
    }
    // Adds the pieces on there starting positions
    startPosition();

    // adds the indices: 1-8 (row) and A-H (col)
    addIndices(chessboard);

    return chessboard; // Return the created chessboard GridPane
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

  public void handleCellClick(Cell currentCell) {
    int fromRow;
    int fromCol;
    int toRow;
    int toCol;

    if (selectedCell == null) {
      // First click: Select the piece to move
      if (currentCell.getPieceName() != null) {
        selectedCell = currentCell;

        // Highlight the selected cell by changing its background color
        currentCell.setRectangleFill(Color.YELLOW);
      }
    } else {
      // Second click: Move the piece to the clicked cell
      fromRow = selectedCell.getRow();
      fromCol = selectedCell.getCol();
      toRow = currentCell.getRow();
      toCol = currentCell.getCol();
      if (selectedCell.getPieceColor() != currentTurnColor) {
        System.out.print("It's not your turn. "); // Inform the player
        selectedCell.setRectangleFill(selectedCell.getFieldColor());
        selectedCell = null;
        return; // Exit the function without making any move
      }

      if (selectedCell.getPieceName() == "rook") {
        if (isRookMoveAllowed(fromRow, fromCol, toRow, toCol)) {
          movePiece(fromRow, fromCol, toRow, toCol);
          selectedCell.setRectangleFill(selectedCell.getFieldColor());
          selectedCell = null;
        } else {
          System.out.print(" not a possible move ");
        }
      } else if (selectedCell.getPieceName() == "pawn") {
        if (isPawnMoveAllowed(fromRow, fromCol, toRow, toCol)) {
          movePiece(fromRow, fromCol, toRow, toCol);
          selectedCell.setRectangleFill(selectedCell.getFieldColor());
          selectedCell = null;
        } else {
          System.out.print(" not a possible move ");
        }
      } else if (selectedCell.getPieceName() == "bishop") {
        if (isBishopMoveAllowed(fromRow, fromCol, toRow, toCol)) {
          movePiece(fromRow, fromCol, toRow, toCol);
          selectedCell.setRectangleFill(selectedCell.getFieldColor());
          selectedCell = null;
        } else {
          System.out.print(" not a possible move ");
        }
      } else if (selectedCell.getPieceName() == "knight") {
        if (isKnightMoveAllowed(fromRow, fromCol, toRow, toCol)) {
          movePiece(fromRow, fromCol, toRow, toCol);
          selectedCell.setRectangleFill(selectedCell.getFieldColor());
          selectedCell = null;
        } else {
          System.out.print(" not a possible move ");
        }
      } else if (selectedCell.getPieceName() == "king") {
        if (isKingMoveAllowed(fromRow, fromCol, toRow, toCol)) {
          movePiece(fromRow, fromCol, toRow, toCol);
          selectedCell.setRectangleFill(selectedCell.getFieldColor());
          selectedCell = null;
        } else {
          System.out.print(" not a possible move ");
        }
      } else if (selectedCell.getPieceName() == "queen") {
        if (isQueenMoveAllowed(fromRow, fromCol, toRow, toCol)) {
          movePiece(fromRow, fromCol, toRow, toCol);
          selectedCell.setRectangleFill(selectedCell.getFieldColor());
          selectedCell = null;
        } else {
          selectedCell.setRectangleFill(currentCell.getFieldColor()); // deselect the piece
          selectedCell = null;
          System.out.print(" not a possible move ");
        }
      }
    }
  }

  // TODO: implement game logic for every piece. (Can be split into different Methods)
  public Boolean isMoveAllowed(Cell selectedCell, Cell clickedCell) {
    // case pawn
    // case rook ..
    return true;
  }

  public Boolean isQueenMoveAllowed(int fromRow, int fromCol, int toRow, int toCol) {
    int rowDistance = Math.abs(toRow - fromRow);
    int colDistance = Math.abs(toCol - fromCol);

    // Queen can move horizontally, vertically, or diagonally
    if ((fromRow == toRow || fromCol == toCol) || (rowDistance == colDistance)) {
      // Check if the path is clear for the move
      int rowStep = (toRow > fromRow) ? 1 : (toRow < fromRow) ? -1 : 0;
      int colStep = (toCol > fromCol) ? 1 : (toCol < fromCol) ? -1 : 0;

      for (int row = fromRow + rowStep, col = fromCol + colStep;
          row != toRow || col != toCol;
          row += rowStep, col += colStep) {
        if (!cell[row][col].isEmpty()) {
          return false; // Path is blocked
        }
      }

      // Check if the destination cell is empty or occupied by an opponent's piece
      if (cell[toRow][toCol].isEmpty()
          || cell[toRow][toCol].getPieceColor() != cell[fromRow][fromCol].getPieceColor()) {
        return true;
      }
    }

    return false;
  }

  public Boolean isKingMoveAllowed(int fromRow, int fromCol, int toRow, int toCol) {
    int rowDistance = Math.abs(toRow - fromRow);
    int colDistance = Math.abs(toCol - fromCol);

    // King can move one square in any direction
    if (rowDistance <= 1 && colDistance <= 1) {
      // Check if the destination cell is empty or occupied by an opponent's piece
      if (cell[toRow][toCol].isEmpty()
          || cell[toRow][toCol].getPieceColor() != cell[fromRow][fromCol].getPieceColor()) {
        return true;
      }
    }

    return false;
  }

  public Boolean isKnightMoveAllowed(int fromRow, int fromCol, int toRow, int toCol) {
    int rowDistance = Math.abs(toRow - fromRow);
    int colDistance = Math.abs(toCol - fromCol);

    // Knight can move in an L-shape (2 squares in one direction and 1 square in the other)
    if ((rowDistance == 2 && colDistance == 1) || (rowDistance == 1 && colDistance == 2)) {

      // Destination cell should be empty or occupied by opponent's piece
      if (cell[toRow][toCol].isEmpty()
          || (cell[toRow][toCol].getPieceColor() != cell[fromRow][fromCol].getPieceColor())) {
        return true; // Move is allowed
      }
    }

    return false; // Invalid move
  }

  // hard to unterstand
  public Boolean isBishopMoveAllowed(int fromRow, int fromCol, int toRow, int toCol) {
    // Bishop can move diagonally
    if (Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol)) {
      int rowDirection = (toRow > fromRow) ? 1 : -1;
      int colDirection = (toCol > fromCol) ? 1 : -1;

      int currentRow = fromRow + rowDirection;
      int currentCol = fromCol + colDirection;

      while (currentRow != toRow && currentCol != toCol) {
        if (!cell[currentRow][currentCol].isEmpty()) {
          return false; // Path is blocked
        }

        currentRow += rowDirection;
        currentCol += colDirection;
      }

      // Destination cell should be either empty or occupied by opponent's piece
      if (cell[toRow][toCol].isEmpty()
          || (cell[toRow][toCol].getPieceColor() != cell[fromRow][fromCol].getPieceColor())) {
        return true; // Move is allowed
      }
    }

    return false; // Invalid move
  }

  /**
   * Checks if rook moves are possible.
   *
   * @param fromRow
   * @param fromCol
   * @param toRow
   * @param toCol
   * @return true if possible, false if not
   */
  public Boolean isRookMoveAllowed(int fromRow, int fromCol, int toRow, int toCol) {
    // Rook can move horizontally or vertically
    if (fromRow == toRow || fromCol == toCol) {
      // Check if there are any pieces in the path between from and to positions

      // If moving horizontally
      if (fromRow == toRow) {
        int startCol = Math.min(fromCol, toCol) + 1;
        int endCol = Math.max(fromCol, toCol);

        for (int col = startCol; col < endCol; col++) {
          if (!cell[fromRow][col].isEmpty()) {
            return false; // Path is blocked
          }
        }
      }

      // If moving vertically
      if (fromCol == toCol) {
        int startRow = Math.min(fromRow, toRow) + 1;
        int endRow = Math.max(fromRow, toRow);

        for (int row = startRow; row < endRow; row++) {
          if (!cell[row][fromCol].isEmpty()) {
            return false; // Path is blocked
          }
        }
      }

      // Destination cell should be either empty or occupied by opponent's piece
      if (cell[toRow][toCol].isEmpty()
          || (cell[toRow][toCol].getPieceColor() != cell[fromRow][fromCol].getPieceColor())) {
        return true; // Move is allowed
      }
    }

    return false; // Invalid move
  }

  /**
   * Checks if a pawn move is allowed.
   *
   * @param fromRow
   * @param fromCol
   * @param toRow
   * @param toCol
   * @return true if possible, false if not
   */
  public Boolean isPawnMoveAllowed(int fromRow, int fromCol, int toRow, int toCol) {
    Cell oldCell = cell[fromRow][fromCol];
    Cell newCell = cell[toRow][toCol];

    if (!"pawn".equals(oldCell.getPieceName())) {
      return false;
    }

    if (oldCell.getPieceColor() == Color.WHITE) {

      // true: trying to move forward. false: trying to hit.
      if (fromCol == toCol) {

        // one cell forward
        if ((fromRow - toRow == -1) && newCell.isEmpty()) {
          return true;
        }
        // two cells forward
        if ((fromRow == 1 && toRow == 3) && cell[2][toCol].isEmpty() && newCell.isEmpty()) {
          return true;
        }

        // hit (left or right)
      } else if (((fromCol - toCol == -1) || (fromCol - toCol == 1))
          && (fromRow - toRow == -1)
          && newCell.getPieceColor() == Color.BLACK) {
        return true;
      }

      // black moves
    } else {
      // true: trying to move forward. false: trying to hit.
      if (fromCol == toCol) {

        // one cell forward
        if ((fromRow - toRow == 1) && newCell.isEmpty()) {
          return true;
        }
        // two cells forward
        if ((fromRow == 6 && toRow == 4) && cell[5][toCol].isEmpty() && newCell.isEmpty()) {
          return true;
        }

        // hit (left or right)
      } else if (((fromCol - toCol == 1) || (fromCol - toCol == -1))
          && (fromRow - toRow == 1)
          && newCell.getPieceColor() == Color.WHITE) {
        return true;
      }
    }
    return false;
  }

  private void startPosition() {
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
      cell[6][column].setPieceValues(Color.BLACK, "pawn", pieceImage);
    }
  }

  /**
   * Move a piece and print its changes.
   *
   * @param fromRow
   * @param fromCol
   * @param toRow
   * @param toCol
   */
  public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
    String pieceKilled = cell[toRow][toCol].getPieceName();

    // Get the piece values from the source cell
    Color pieceColor = cell[fromRow][fromCol].getPieceColor();
    String pieceName = cell[fromRow][fromCol].getPieceName();
    Image pieceImage = cell[fromRow][fromCol].getPieceImage();

    // Clear the piece values in the source cell
    cell[fromRow][fromCol].clearPiece();

    // Set the piece values in the destination cell
    cell[toRow][toCol].setPieceValues(pieceColor, pieceName, pieceImage);

    // Update the current turn's color
    currentTurnColor = (currentTurnColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

    // printing action to the terminal (Could be implemented on the screen).
    char firstChar = pieceName.charAt(0);
    char fromChar = (char) (fromCol + 'A');
    char toChar = (char) (toCol + 'A');
    if (pieceKilled == null) {
      System.out.print(
          "\n"
              + firstChar
              + ""
              + (fromRow + 1)
              + fromChar
              + " to "
              + (toRow + 1)
              + toChar
              + "     "); // nothing in here. Auto format is better like this
    } else {
      System.out.print(
          "\n"
              + firstChar
              + ""
              + (fromRow + 1)
              + fromChar
              + " to "
              + (toRow + 1)
              + toChar
              + " takes "
              + pieceKilled);
    }
  }
}

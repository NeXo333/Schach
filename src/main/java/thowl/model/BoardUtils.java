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
  private ChessPiece chessPiece = new ChessPiece(); // Declare an instance of ChessPiece
  private Cell selectedCell = null;

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
      // exit method if wrong color tries to move
      if (selectedCell.getPieceColor() != chessPiece.getCurrentTurnColor()) {
        System.out.print("It's not your turn. "); // Inform the player
        selectedCell.setRectangleFill(selectedCell.getFieldColor());
        selectedCell = null;
        return; // Exit the function without making any move
      }

      // checks if it is a valid move
      String pieceName = selectedCell.getPieceName(); // first clicked cell (piece to move)

      if (chessPiece.isMoveAllowed(cell, pieceName, fromRow, fromCol, toRow, toCol)) {
        chessPiece.movePiece(cell, fromRow, fromCol, toRow, toCol);
        selectedCell.setRectangleFill(selectedCell.getFieldColor());
        selectedCell = null;
      } else { // deselects the piece if the move was not possible
        selectedCell.setRectangleFill(selectedCell.getFieldColor());
        selectedCell = null;
        System.out.println("Not a possible move");
      }
    }
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
}

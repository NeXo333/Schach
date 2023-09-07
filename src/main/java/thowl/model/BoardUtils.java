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

  public final int cellSize = 70;
  private final int cellCount = 8;

  public Cell[][] cell = new Cell[cellCount][cellCount];
  private Cell selectedCell = null;

  private ChessPiece chessPiece = new ChessPiece(); // Declare an instance of ChessPiece

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
    for (int row = 0; row < cellCount; row++) {
      for (int col = 0; col < cellCount; col++) {
        Color cellColor = (row + col) % 2 == 0 ? Color.WHITE : Color.LIGHTGRAY;
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

  // Is used to enabel or disable the handleCellClick Funktion via the Gui and the buttons.
  public class MovesCheck {
    private static int wert;

    public MovesCheck() {
      //
    }

    public static void setWert(int neuerWert) {
      wert = neuerWert;
    }

    public static int getWert() {
      return wert;
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

        // Highlight the selected cell by changing its background color if its enabled by the user
        // in the Starting Gui.
        if (MovesCheck.wert == 1) {
          currentCell.setRectangleFill(Color.YELLOW);
        } else {
          return;
        }

        // Highlight the possible moves in lightyellow
        chessPiece.showAllpossibleMoves(cell, selectedCell.getRow(), selectedCell.getCol());
      }

    } else {
      // Second click: Move the piece to the clicked cell
      fromRow = selectedCell.getRow();
      fromCol = selectedCell.getCol();
      toRow = currentCell.getRow();
      toCol = currentCell.getCol();
      // exit method if wrong color tries to move
      if (selectedCell.getPieceColor() != chessPiece.currentTurnColor) {
        System.out.print("It's not your turn. "); // Inform the player
        // resets all Field colouring
        clearFieldColor();
        selectedCell = null;

        return; // Exit the function without making any move
      }

      if (chessPiece.isMoveAllowed(cell, fromRow, fromCol, toRow, toCol)) {
        // when move would put own king into check than error
        if (!chessPiece.iskingInCheck(cell, fromRow, fromCol, toRow, toCol)) {

          if (cell[fromRow][fromCol].getPieceName() == "king") {
            chessPiece.kingPositionStorage(chessPiece.currentTurnColor, toRow, toCol);
          }

          // TODO: look for checkmate before choosing a piece (queen and knight if pawn at end row)
          // Check for pawn promotion (pawn at end row)
          chessPiece.nextMove += 1;
          if (cell[toRow][toCol].getPieceName() == "pawn" && (toRow == 0 || toRow == 7)) {
            chessPiece.openPromotionDialog(cell, cell[toRow][toCol].getPieceColor(), toRow, toCol);
            System.out.println("pawn changed to " + cell[toRow][toCol].getPieceName());
          }

          chessPiece.movePiece(cell, fromRow, fromCol, toRow, toCol);

          // set conditions back
          chessPiece.RookOrKingMoved(cell, fromRow, fromCol, toRow, toCol);
          chessPiece.whiteShortCastle = 0;
          chessPiece.blackShortCastle = 0;

          // testing for checkmate (Player change happen in isCheck)
          if (chessPiece.isCheck(cell)) {
            System.out.println("check");
            if (chessPiece.isCheckmate(cell)) {
              // TODO: make pop up window instead of printing to the terminal
              if (chessPiece.currentTurnColor == Color.WHITE) {
                System.out.println("CHECKMATE: BLACK WON");
              } else if (chessPiece.currentTurnColor == Color.BLACK) {
                System.out.println("CHECKMATE: WHITE WON");
              }
            }
          }
          clearFieldColor();
          selectedCell = null;
          // nextMove += 1;
        } else {
          // deselects the piece if the move was not possible
          selectedCell.setRectangleFill(selectedCell.getFieldColor());
          clearFieldColor();
          selectedCell = null;
          System.out.print(" King in check ");
        }
      } else {
        // deselects the piece if the move was not possible
        selectedCell.setRectangleFill(selectedCell.getFieldColor());
        clearFieldColor();
        selectedCell = null;
        System.out.print(" Not a possible move ");
      }
    }
  }

  // clears the field color of a clicked field
  private void clearFieldColor() {
    for (int i = 0; i < cellCount; i++) {
      for (int j = 0; j < cellCount; j++) {
        cell[j][i].setRectangleFill(cell[i][j].getFieldColor());
      }
    }
  }

  // used for updating complete gamefield color
  public static void updateColors(ColorVariable colorVariable) {
    // Verwenden Sie die übergebene ColorVariable-Instanz, um die Farben zu aktualisieren
    Color finalColor1 = colorVariable.getColor1();
    Color finalColor2 = colorVariable.getColor2();

    // Rest Ihrer Code, der die Farben verwendet...
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

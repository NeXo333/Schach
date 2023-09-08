package thowl.model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import thowl.model.ChangeColor.ColorVariable;
import thowl.model.TurnManager.GameManager;

/**
 * Utility class for managing the chessboard and game logic. This class is responsible for creating
 * and managing the chessboard, handling player moves, and enforcing game rules such as check and
 * checkmate.
 *
 * @author Marlon Schrader & Justin Schmidt
 */
public class BoardUtils {

  public final int cellSize = 70; // size of each chessboard cell
  private final int cellCount = 8; // cells per row

  // stores all the data about the board and piece positions
  public Cell[][] cell = new Cell[cellCount][cellCount];
  private Cell fromCell = null; // clicked cell in eventhandler

  private ChessPiece chessPiece = new ChessPiece(); // used for movement logic
  private ControlCheckmate control = new ControlCheckmate(); // looks after check
  private SpecialMoves special = new SpecialMoves(); // enPassant, Pawn promotion, castling

  // responsible for color access and color change
  TurnManager turnManager = GameManager.getTurnManager();

  /**
   * Creates the colored chessboard as a GridPane and calls addIndices() for row and column indices
   * and startPosition() to put the pieces on it.
   *
   * @return Chessboard as a GridPane
   */
  public GridPane createChessboard() {
    GridPane chessboard = new GridPane();
    chessboard.setAlignment(Pos.CENTER);
    chessboard.setHgap(2); // Horizontal gap between cells
    chessboard.setVgap(2); // Vertical gap between cells

    // Create the 64 fieds/ cells without any pieces on them first
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
    // Adds the pieces on there starting positions. Board with pieces on it.
    startPosition();

    // adds the indices: 1-8 (row) and A-H (col)
    addIndices(chessboard);

    return chessboard; // Return the created chessboard GridPane
  }

  /**
   * Adds row and column indices to the chessboard GridPane (row = 1-8 & col = A-H).
   *
   * @param chessboard The GridPane representing the chessboard.
   */
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

  /** Is used to enabel or disable the handleCellClick function via the Gui and the buttons. */
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

  /**
   * Handles a cell click event, enabling the player to select and move chess pieces. First click:
   * NewCell becomes the fromCell and the second click is the newCell or destination cell.
   *
   * @param newCell The clicked cell.
   */
  public void handleCellClick(Cell newCell) {
    int fromRow;
    int fromCol;
    int toRow;
    int toCol;

    if (fromCell == null) {
      // First click: Select the piece to move
      if (newCell.getPieceName() != null) {
        fromCell = newCell;

        // Highlight the selected cell by changing its background color if its enabled by the user
        // in the Starting Gui.
        if (MovesCheck.wert == 1) {
          newCell.setRectangleFill(Color.YELLOW);
        } else {
          return;
        }
        // Highlight the possible moves in lightyellow
        chessPiece.showAllpossibleMoves(cell, fromCell.getRow(), fromCell.getCol());
      }

    } else {
      // Second click: Move the piece to the clicked cell
      fromRow = fromCell.getRow();
      fromCol = fromCell.getCol();
      toRow = newCell.getRow();
      toCol = newCell.getCol();
      // exit method if wrong color tries to move
      if (fromCell.getPieceColor() != turnManager.getCurrentTurnColor()) {
        System.out.print("It's not your turn. "); // Inform the player
        // resets Field coloring
        clearFieldColor();
        fromCell = null;

        return; // Exit the function without making any move
      }
      // is move a valid chess move?
      if (chessPiece.isMoveAllowed(cell, fromRow, fromCol, toRow, toCol)) {
        // when move would put own king into check than else
        if (!control.iskingInCheck(cell, fromRow, fromCol, toRow, toCol)) {
          // if the king moved, then update kings position
          if (cell[fromRow][fromCol].getPieceName() == "king") {
            control.kingPositionStorage(turnManager.getCurrentTurnColor(), toRow, toCol);
          }

          // params for printing move
          String color =
              (cell[fromRow][fromCol].getPieceColor() == Color.WHITE) ? "white " : "black ";
          String pieceName = cell[fromRow][fromCol].getPieceName();
          char fromChar = (char) (fromCol + 'A'); // makes the indice int to letter
          char toChar = (char) (toCol + 'A');

          // executes the move
          chessPiece.movePiece(cell, fromRow, fromCol, toRow, toCol);

          // Check for pawn promotion (pawn at end row)
          chessPiece.special.nextMove += 1;
          if (cell[toRow][toCol].getPieceName() == "pawn" && (toRow == 0 || toRow == 7)) {
            special.openPromotionDialog(cell, cell[toRow][toCol].getPieceColor(), toRow, toCol);
            System.out.println("pawn changed to " + cell[toRow][toCol].getPieceName());
          }

          // print move
          System.out.println(
              color
                  + pieceName
                  + " from "
                  + (fromRow + 1)
                  + fromChar
                  + " to "
                  + (toRow + 1)
                  + toChar);

          // reset conditions for castling. If rook or king moved no longer allow castling
          special.RookOrKingMoved(cell, fromRow, fromCol, toRow, toCol);
          special.whiteShortCastle = 0;
          special.blackShortCastle = 0;

          // change current turn color and look if recent move made enemy into check/ checkmate
          turnManager.switchTurn();
          int kingRow =
              (turnManager.getCurrentTurnColor() == Color.WHITE)
                  ? control.whiteKingRow
                  : control.blackKingRow;
          int kingCol =
              (turnManager.getCurrentTurnColor() == Color.WHITE)
                  ? control.whiteKingCol
                  : control.blackKingCol;

          if (chessPiece.isCellUnderAttack(cell, kingRow, kingCol)) { // is king under attack ?
            System.out.println("check");
            if (control.isCheckmate(cell)) {
              // TODO: Make pop up window instead of printing to the terminal
              if (turnManager.getCurrentTurnColor() == Color.WHITE) {
                System.out.println("CHECKMATE: BLACK WON");
              } else {
                System.out.println("CHECKMATE: WHITE WON");
              }
            }
          }
          // deselect cell after move is made
          clearFieldColor();
          fromCell = null;
        } else {
          // deselect cell if own king is in check
          fromCell.setRectangleFill(fromCell.getFieldColor());
          clearFieldColor();
          fromCell = null;
          System.out.println(" Puts the king into check or is already in check ");
        }
      } else {
        // deselect the cell if its an invalid move
        fromCell.setRectangleFill(fromCell.getFieldColor());
        clearFieldColor();
        fromCell = null;
        System.out.println(" Not a possible move ");
      }
    }
  }

  /** Sets the chess pieces on there initial positions. */
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

  /** Clears the field color of all cells on the chessboard. */
  private void clearFieldColor() {
    for (int i = 0; i < cellCount; i++) {
      for (int j = 0; j < cellCount; j++) {
        cell[j][i].setRectangleFill(cell[i][j].getFieldColor());
      }
    }
  }

  // doesnt function
  /**
   * Updates the colors of the chessboard based on the given ColorVariable instance.
   *
   * @param colorVariable The ColorVariable instance specifying the colors to update.
   */
  public static void updateColors(ColorVariable colorVariable) {
    // Verwenden Sie die übergebene ColorVariable-Instanz, um die Farben zu aktualisieren
    Color finalColor1 = colorVariable.getColor1();
    Color finalColor2 = colorVariable.getColor2();

    // Rest Ihrer Code, der die Farben verwendet...
  }
}

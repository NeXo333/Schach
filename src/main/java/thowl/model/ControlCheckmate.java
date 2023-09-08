package thowl.model;

import javafx.scene.paint.Color;
import thowl.model.TurnManager.GameManager;

/**
 * The ControlCheckmate class is responsible for managing check & checkmate detection and related
 * operations in a chess game. It tracks the positions of both white and black kings, checks if a
 * move resultsin a king being in check, and determines if a player is in checkmate.
 *
 * @author Marlon Schrader
 */
public class ControlCheckmate {

  // king position
  public int whiteKingRow = 0;
  public int whiteKingCol = 3;
  public int blackKingRow = 7;
  public int blackKingCol = 3;

  ChessPiece piece = new ChessPiece(); // for gamelogic

  // responsible for color access and color change
  TurnManager turnManager = GameManager.getTurnManager();

  /**
   * Creates a copy of the game field, which is necessary for checking move validity, including
   * check, checkmate, and castling.
   *
   * @param source The source original cell[][].
   * @param destination The destination chessboard cells for copying.
   */
  public void copyGameField(Cell[][] source, Cell[][] destination) {
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Color fieldColor = source[row][col].getFieldColor();
        Color pieceColor = source[row][col].getPieceColor();
        String pieceName = source[row][col].getPieceName();
        destination[row][col] = new Cell(row, col, 70, fieldColor, pieceColor, pieceName, null);
      }
    }
  }

  // WHEN USING THIS METHOD USE FIRST: CELL[FROMROW][TOCOL].GETPIECENAME() == KING
  /**
   * Stores the position of a king based on color (white or black).
   *
   * @param color The color of the king (Color.WHITE or Color.BLACK).
   * @param toRow The row to which the king is moving.
   * @param toCol The column to which the king is moving.
   */
  public void kingPositionStorage(Color color, int toRow, int toCol) {
    if (color == Color.WHITE) {
      whiteKingRow = toRow;
      whiteKingCol = toCol;
    } else {
      blackKingRow = toRow;
      blackKingCol = toCol;
    }
    // Change color settings in Gui. try catch for color not possible
  }

  /**
   * Checks if a move puts the current player's own king into check.
   *
   * @param cell The chessboard cells.
   * @param fromRow The source row of the piece.
   * @param fromCol The source column of the piece.
   * @param toRow The destination row.
   * @param toCol The destination column.
   * @return true if the move puts the king in check, false otherwise.
   */
  public boolean iskingInCheck(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
    Cell[][] copy = new Cell[8][8];
    copyGameField(cell, copy);

    // store the kings position to reset them later
    int oldWhiteKingRow = whiteKingRow;
    int oldWhiteKingCol = whiteKingCol;
    int oldBlackKingRow = blackKingRow;
    int oldBlackKingCol = blackKingCol;
    Boolean isKing = false;

    // moves gets executed on a copied Cell[][]
    String pieceName = cell[fromRow][fromCol].getPieceName();
    piece.movePiece(copy, fromRow, fromCol, toRow, toCol);

    if (pieceName == "king") {
      isKing = true;
      // (Could also be using the method KingStorage)
      if (turnManager.getCurrentTurnColor() == Color.WHITE) {
        kingPositionStorage(Color.WHITE, toRow, toCol);
      } else {
        kingPositionStorage(Color.BLACK, toRow, toCol);
      }
    }

    int kingRow = (turnManager.getCurrentTurnColor() == Color.WHITE) ? whiteKingRow : blackKingRow;
    int kingCol = (turnManager.getCurrentTurnColor() == Color.WHITE) ? whiteKingCol : blackKingCol;

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        if (turnManager.getCurrentTurnColor() == Color.WHITE) {
          if (copy[row][col].getPieceColor() == Color.BLACK) {
            if (isKing && piece.isMoveAllowed(copy, row, col, whiteKingRow, whiteKingCol)) {
              // reset the vars
              isKing = false;
              kingPositionStorage(Color.WHITE, oldWhiteKingRow, oldWhiteKingCol);
              return true;
            }
            if (piece.isMoveAllowed(copy, row, col, kingRow, kingCol)) {
              return true;
            }
          }
        } else {
          if (copy[row][col].getPieceColor() == Color.WHITE) {
            if (isKing && piece.isMoveAllowed(copy, row, col, blackKingRow, blackKingCol)) {
              // reset the vars
              isKing = false;
              kingPositionStorage(Color.WHITE, oldBlackKingRow, oldBlackKingCol);
              return true;
            }
            if (piece.isMoveAllowed(copy, row, col, kingRow, kingCol)) {
              return true;
            }
          }
        }
      }
    }
    // reset the vars
    if (pieceName == "king") {
      isKing = false;
      // (Could also be using the method KingStorage)
      if (turnManager.getCurrentTurnColor() == Color.WHITE) {
        kingPositionStorage(Color.WHITE, toRow, toCol);
      } else {
        kingPositionStorage(Color.BLACK, toRow, toCol);
      }
    }
    return false;
  }

  /**
   * Determines if the current player is in checkmate.
   *
   * @param cell The chessboard cells.
   * @return true if the player is in checkmate, false otherwise.
   */
  public Boolean isCheckmate(Cell[][] cell) {
    Cell[][] copy = new Cell[8][8];
    copyGameField(cell, copy);

    for (int fromRow = 0; fromRow < 8; fromRow++) {
      for (int fromCol = 0; fromCol < 8; fromCol++) {
        if (copy[fromRow][fromCol].getPieceColor() == turnManager.getCurrentTurnColor()) {
          for (int toRow = 0; toRow < 8; toRow++) {
            for (int toCol = 0; toCol < 8; toCol++) {
              if (piece.isMoveAllowed(copy, fromRow, fromCol, toRow, toCol)) {
                // is there a move which puts the king out of check? if yes then return false
                if (!iskingInCheck(copy, fromRow, fromCol, toRow, toCol)) {
                  return false;
                }
              }
            }
          }
        }
      }
    }
    return true;
  }
}

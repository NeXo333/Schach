package thowl.model;

import javafx.scene.paint.Color;

public class ControlCheckmate {
  // for king in check
  public int whiteKingRow = 0;
  public int whiteKingCol = 3;
  public int blackKingRow = 7;
  public int blackKingCol = 3;

  private ChessPiece piece = new ChessPiece();

  // getter for currentTurnColor
  public Color getColor() {
    return piece.currentTurnColor;
  }

  // WHEN USING THIS METHOD USE FIRST: CELL[FROMROW][TOCOL].GETPIECENAME() == KING
  // stores Kings position and is important for check
  public void kingPositionStorage(Color color, int toRow, int toCol) {
    if (color == Color.WHITE) {
      whiteKingRow = toRow;
      whiteKingCol = toCol;
    } else if (color == Color.BLACK) {
      blackKingRow = toRow;
      blackKingCol = toCol;
    }
    // Change color settings in Gui. try catch for color not possible
  }

  // checks if your move puts your own King into check
  public boolean iskingInCheck(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
    Cell[][] copy = new Cell[8][8];
    copyGameField(cell, copy);

    int oldWhiteKingRow = whiteKingRow;
    int oldWhiteKingCol = whiteKingCol;
    int oldBlackKingRow = blackKingRow;
    int oldBlackKingCol = blackKingCol;
    Boolean isKing = false;

    String pieceName = cell[fromRow][fromCol].getPieceName();
    piece.movePiece(copy, fromRow, fromCol, toRow, toCol);

    // if the moved piece is the king, then update the position storer of the kings position
    if (pieceName == "king") {
      isKing = true;
      kingPositionStorage(getColor(), toRow, toCol);
    }

    int kingRow = (getColor() == Color.WHITE) ? whiteKingRow : blackKingRow;
    int kingCol = (getColor() == Color.WHITE) ? whiteKingCol : blackKingCol;

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        if (getColor() == Color.WHITE) {
          if (copy[row][col].getPieceColor() == Color.BLACK) {
            // look if the enemy could attack the king at its new position
            if (isKing && piece.isMoveAllowed(copy, row, col, whiteKingRow, whiteKingCol)) {
              // reset the vars
              isKing = false;
              kingPositionStorage(getColor(), oldWhiteKingRow, oldWhiteKingCol);

              return true;
            }
            if (piece.isMoveAllowed(copy, row, col, kingRow, kingCol)) {
              return true;
            }
          }
        } else if (getColor() == Color.BLACK) {
          if (copy[row][col].getPieceColor() == Color.WHITE) {
            if (isKing && piece.isMoveAllowed(copy, row, col, blackKingRow, blackKingCol)) {
              // reset the vars
              isKing = false;
              kingPositionStorage(getColor(), oldBlackKingRow, oldBlackKingCol);

              return true;
            }
            if (piece.isMoveAllowed(copy, row, col, kingRow, kingCol)) {
              return true;
            }
          }
        }
      }
    }
    isKing = false;
    if (getColor() == Color.WHITE) {
      kingPositionStorage(getColor(), oldWhiteKingRow, oldWhiteKingCol);
    } else {
      kingPositionStorage(getColor(), oldBlackKingRow, oldBlackKingCol);
    }
    return false;
  }

  public Boolean isCheckmate(Cell[][] cell) {
    Cell[][] copy = new Cell[8][8];
    copyGameField(cell, copy);

    for (int fromRow = 0; fromRow < 8; fromRow++) {
      for (int fromCol = 0; fromCol < 8; fromCol++) {
        if (copy[fromRow][fromCol].getPieceColor() == getColor()) {
          for (int toRow = 0; toRow < 8; toRow++) {
            for (int toCol = 0; toCol < 8; toCol++) {
              if (piece.isMoveAllowed(copy, fromRow, fromCol, toRow, toCol)) {
                // is there a move which puts the king out of check? Then its not checkmate
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

  /*  Creates a copy of the game field (necessary for check/ checkmate and castling to see,
  if the moves are possible */
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
}

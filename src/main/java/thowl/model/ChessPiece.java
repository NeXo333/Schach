package thowl.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import thowl.model.TurnManager.GameManager;

/**
 * The ChessPiece class manages chess piece movements and enforces the rules of chess for each piece
 * type. It provides methods to check the validity of moves, highlight possible moves, and has the
 * modePiece method. Additionaly it looks while validating possible moves for special moves like
 * enPassant and then calls the SpecialMoves class further on.
 *
 * @author Marlon Schrader
 */
public class ChessPiece {

  // responsible for color access and color change
  TurnManager turnManager = GameManager.getTurnManager();

  // enPassant, Pawn promotion, castling
  SpecialMoves special = new SpecialMoves();

  /**
   * Checks if the given cell is under attack by an opponent's piece.
   *
   * @param cell The chessboard cells.
   * @param attackedRow The row of the attacked cell.
   * @param attackedCol The column of the attacked cell.
   * @return true if the cell is under attack, false otherwise.
   */
  public Boolean isCellUnderAttack(Cell[][] cell, int attackedRow, int attackedCol) {
    for (int fromRow = 0; fromRow < 8; fromRow++) {
      for (int fromCol = 0; fromCol < 8; fromCol++) {
        if (turnManager.getCurrentTurnColor() == Color.WHITE
            && cell[fromRow][fromCol].getPieceColor() == Color.BLACK) {
          if (isMoveAllowed(cell, fromRow, fromCol, attackedRow, attackedCol)) {
            return true;
          }
        } else if (turnManager.getCurrentTurnColor() == Color.BLACK
            && cell[fromRow][fromCol].getPieceColor() == Color.WHITE) {
          if (isMoveAllowed(cell, fromRow, fromCol, attackedRow, attackedCol)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Highlights all possible moves for a piece on a given cell (doesnt check if moves into check).
   *
   * @param cell The chessboard cells.
   * @param fromRow The source row of the piece.
   * @param fromCol The source column of the piece.
   */
  public void showAllpossibleMoves(Cell[][] cell, int fromRow, int fromCol) {
    for (int j = 0; j < 8; j++) {
      for (int i = 0; i < 8; i++) {
        if (isMoveAllowed(cell, fromRow, fromCol, i, j)) {
          cell[i][j].setRectangleFill(Color.LIGHTYELLOW);
        }
      }
    }
  }

  /**
   * Checks if a move from one cell to another is allowed for a piece.
   *
   * @param cell The chessboard cells.
   * @param fromRow The source row of the piece.
   * @param fromCol The source column of the piece.
   * @param toRow The destination row.
   * @param toCol The destination column.
   * @return true if the move is allowed, false otherwise.
   */
  public boolean isMoveAllowed(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
    String pieceName = cell[fromRow][fromCol].getPieceName();
    if (pieceName == null) {
      return false;
    }
    switch (pieceName) { // if is***MoveAllowed returns then either true or false
      case "pawn":
        return isPawnMoveAllowed(cell, fromRow, fromCol, toRow, toCol);
      case "rook":
        return isRookMoveAllowed(cell, fromRow, fromCol, toRow, toCol);
      case "bishop":
        return isBishopMoveAllowed(cell, fromRow, fromCol, toRow, toCol);
      case "knight":
        return isKnightMoveAllowed(cell, fromRow, fromCol, toRow, toCol);
      case "king":
        return isKingMoveAllowed(cell, fromRow, fromCol, toRow, toCol);
      case "queen":
        return isQueenMoveAllowed(cell, fromRow, fromCol, toRow, toCol);
      default:
        return false; // Invalid piece name
    }
  }

  /**
   * Moves a piece from one cell to another and performs special actions if needed.
   *
   * @param cell The chessboard cells.
   * @param fromRow The source row of the piece.
   * @param fromCol The source column of the piece.
   * @param toRow The destination row.
   * @param toCol The destination column.
   */
  public void movePiece(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {

    // Get the piece values from the source cell
    Color pieceColor = cell[fromRow][fromCol].getPieceColor();
    String pieceName = cell[fromRow][fromCol].getPieceName();
    Image pieceImage = cell[fromRow][fromCol].getPieceImage();

    // check for enPassant
    if (pieceName == "pawn" && special.isEnPassant(cell, fromRow, fromCol, toRow, toCol)) {
      special.executeEnPassant(cell, toRow, toCol);
    }

    // check for castling
    if (pieceName == "king" && (special.whiteKingNotMoved || special.blackKingNotMoved)) {
      /* will only execute if certain conditions inside of isKingMoveAllowed are true.
      Moves rooks for its own*/
      special.executeCastling(cell);
    }

    // Set the piece values in the destination cell
    cell[toRow][toCol].setPieceValues(pieceColor, pieceName, pieceImage);

    // Clear the piece values in the source cell
    cell[fromRow][fromCol].clearPiece();
  }

  /**
   * Checks if a pawn's move to a destination cell is allowed.
   *
   * @param cell The chessboard cells.
   * @param fromRow The source row of the piece.
   * @param fromCol The source column of the piece.
   * @param toRow The destination row.
   * @param toCol The destination column.
   * @return true if the move is allowed, false otherwise.
   */
  public Boolean isPawnMoveAllowed(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
    Cell oldCell = cell[fromRow][fromCol];
    Cell newCell = cell[toRow][toCol];

    if (!"pawn".equals(oldCell.getPieceName())) {
      return false;
    }

    if (oldCell.getPieceColor() == Color.WHITE) {
      if (fromCol == toCol) { // steps forward (1-2)
        if (fromRow - toRow == -1 && newCell.isEmpty()) {
          return true; // One cell forward
        }
        if (fromRow == 1 && toRow == 3 && cell[2][toCol].isEmpty() && newCell.isEmpty()) {
          // vars to remember the last pawn that moved 2 steps forward
          special.nextMove = 0;
          special.lastMovedPawnRow = toRow;
          special.lastMovedPawnCol = toCol;
          return true; // Two cells forward
        } // attack
      } else if ((fromCol - toCol == -1 || fromCol - toCol == 1) && fromRow - toRow == -1) {
        if (newCell.getPieceColor() == Color.BLACK
            || special.isEnPassant(cell, fromRow, fromCol, toRow, toCol)) {
          return true; // Hit (left or right)
        }
      }
    } else {
      if (fromCol == toCol) { // steps forward (1-2)
        if (fromRow - toRow == 1 && newCell.isEmpty()) {
          return true; // One cell forward
        }
        if (fromRow == 6 && toRow == 4 && cell[5][toCol].isEmpty() && newCell.isEmpty()) {
          // vars to remember the last pawn that moved 2 steps forward
          special.nextMove = 0;
          special.lastMovedPawnRow = toRow;
          special.lastMovedPawnCol = toCol;
          return true; // Two cells forward
        } // attack
      } else if ((fromCol - toCol == 1 || fromCol - toCol == -1) && fromRow - toRow == 1) {
        if (newCell.getPieceColor() == Color.WHITE
            || special.isEnPassant(cell, fromRow, fromCol, toRow, toCol)) {
          return true; // Hit (left or right)
        }
      }
    }
    return false; // Invalid move
  }

  // Would always the same documentation from now on

  /**
   * Checks if a king's move to a destination cell is allowed.
   *
   * @return true if the move is allowed, false otherwise.
   */
  public Boolean isKingMoveAllowed(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
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

    // look for castling. King is not allowed to be in check
    if ((fromRow == 0 || fromRow == 7) && (toCol == 1 || toCol == 5)) { // from starting position

      if ((turnManager.getCurrentTurnColor() == Color.WHITE)
          && (fromRow == 0 && fromCol == 3)
          && (toRow == 0 && toCol == 1) // rook from king point of view on the right!
          && (special.whiteKingNotMoved && special.whiteRightRookNotMoved) // check if unmoved
          && (cell[0][1].isEmpty() && cell[0][2].isEmpty())) { // no pieces in between

        if (!isCellUnderAttack(cell, 0, 1) // no pieces attacking the cells
            && !isCellUnderAttack(cell, 0, 2)
            && !isCellUnderAttack(cell, 0, 3)) {
          special.whiteShortCastle = 1; // 0 = no castling, 1 = short castling, 2 = long castling
          return true;
        }
      } else if ((turnManager.getCurrentTurnColor() == Color.WHITE)
          && (fromRow == 0 && fromCol == 3)
          && (toRow == 0 && toCol == 5)
          && (special.whiteKingNotMoved && special.whiteLeftRookNotMoved)
          && (cell[0][4].isEmpty() && cell[0][5].isEmpty() && cell[0][6].isEmpty())) {

        if (!isCellUnderAttack(cell, 0, 3)
            && !isCellUnderAttack(cell, 0, 4)
            && !isCellUnderAttack(cell, 0, 5)) {
          special.whiteShortCastle = 2; // long castling / queenside castling
          return true;
        }
        // black Castling
      } else if ((turnManager.getCurrentTurnColor() == Color.BLACK)
          && (fromRow == 7 && fromCol == 3)
          && (toRow == 7 && toCol == 1)
          && (special.blackKingNotMoved && special.blackLeftRookNotMoved)
          && (cell[7][1].isEmpty() && cell[7][2].isEmpty())) {

        if (!isCellUnderAttack(cell, 7, 1)
            && !isCellUnderAttack(cell, 7, 2)
            && !isCellUnderAttack(cell, 7, 3)) {
          special.blackShortCastle = 1; // same as prior expalined
          return true;
        }
      } else if ((turnManager.getCurrentTurnColor() == Color.BLACK)
          && (fromRow == 7 && fromCol == 3)
          && (toRow == 7 && toCol == 5)
          && (special.blackKingNotMoved && special.blackRightRookNotMoved)
          && (cell[7][4].isEmpty() && cell[7][5].isEmpty() && cell[7][6].isEmpty())) {

        if (!isCellUnderAttack(cell, 7, 3)
            && !isCellUnderAttack(cell, 7, 4)
            && !isCellUnderAttack(cell, 7, 5)) {
          special.blackShortCastle = 2;
          return true;
        }
      }
    }
    return false; // Invalid move
  }

  /**
   * Checks if a rook's move to a destination cell is allowed.
   *
   * @return true if the move is allowed, false otherwise.
   */
  public Boolean isRookMoveAllowed(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
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
   * Checks if a knight's move to a destination cell is allowed.
   *
   * @return true if the move is allowed, false otherwise.
   */
  public Boolean isKnightMoveAllowed(
      Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
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
  /**
   * Checks if a bishop's move to a destination cell is allowed.
   *
   * @return true if the move is allowed, false otherwise.
   */
  public Boolean isBishopMoveAllowed(
      Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
    // Bishop can move diagonally
    if (Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol)) {
      int rowDirection = (toRow > fromRow) ? 1 : -1;
      int colDirection = (toCol > fromCol) ? 1 : -1;

      int currentRow = fromRow + rowDirection;
      int currentCol = fromCol + colDirection;

      while (currentRow != toRow && currentCol != toCol) {
        if (currentRow < 0 || currentRow > 7 || currentCol < 0 || currentCol > 7) {
          return false; // Invalid coordinates
        }

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
   * Checks if a queen's move to a destination cell is allowed.
   *
   * @return true if the move is allowed, false otherwise.
   */
  public Boolean isQueenMoveAllowed(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
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

    return false; // Invalid move
  }
}

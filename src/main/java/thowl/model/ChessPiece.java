package thowl.model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChessPiece {

  public Color currentTurnColor = Color.WHITE; // Game starts for white

  // for en passant
  public int nextMove = 0; // only next move allowed for enPassant
  private int lastMovedPawnRow; // remember last pawn that made to 2 cell steps
  private int lastMovedPawnCol;

  // for castling (white left rook == The left rook from white king POV)
  // when a piece moved castling no longer possible
  public Boolean whiteLeftRookNotMoved = true;
  public Boolean whiteRightRookNotMoved = true;
  public Boolean whiteKingNotMoved = true;
  public int whiteShortCastle = 0; // 0 = no castle, 1 == short castle, 2 == long castle

  public Boolean blackLeftRookNotMoved = true;
  public Boolean blackRightRookNotMoved = true;
  public Boolean blackKingNotMoved = true;
  public int blackShortCastle = 0; // 0 = no castle, 1 == short castle, 2 == long castle

  public Color getCurrentTurnColor() {
    if (currentTurnColor == Color.WHITE) {
      return Color.WHITE;
    } else {
      return Color.BLACK;
    }
  }

  /* update if a piece moved, bacause once its moved the castling no longer functions. Called after every move when cell[fromRow][fromCol].getPieceName  == rook or king */
  public void RookOrKingMoved(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
    if (fromRow == 0 && fromCol == 3) {
      whiteKingNotMoved = false;
      whiteLeftRookNotMoved = false;
      whiteRightRookNotMoved = false;
    } else if (fromRow == 0 && fromCol == 0) {
      whiteRightRookNotMoved = false;
    } else if (fromRow == 0 && fromCol == 7) {
      whiteLeftRookNotMoved = false;
    }

    if (fromRow == 7 && fromCol == 3) {
      blackKingNotMoved = false;
      blackLeftRookNotMoved = false;
      blackRightRookNotMoved = false;
    } else if (fromRow == 7 && fromCol == 0) {
      blackLeftRookNotMoved = false;
    } else if (fromRow == 7 && fromCol == 7) {
      blackRightRookNotMoved = false;
    }
  }

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
    // look for every possible castling move
    if ((fromRow == 0 || fromRow == 7) && (toCol == 1 || toCol == 5)) {

      if ((currentTurnColor == Color.WHITE)
          && (fromRow == 0 && fromCol == 3)
          && (toRow == 0 && toCol == 1)
          && (whiteKingNotMoved && whiteRightRookNotMoved)
          && (cell[0][1].isEmpty() && cell[0][2].isEmpty())) {

        if (!isCellUnderAttack(cell, 0, 1)
            && !isCellUnderAttack(cell, 0, 2)
            && !isCellUnderAttack(cell, 0, 3)) {
          whiteShortCastle = 1;
          return true;
        }
      } else if ((currentTurnColor == Color.WHITE)
          && (fromRow == 0 && fromCol == 3)
          && (toRow == 0 && toCol == 5)
          && (whiteKingNotMoved && whiteLeftRookNotMoved)
          && (cell[0][4].isEmpty() && cell[0][5].isEmpty() && cell[0][6].isEmpty())) {

        if (!isCellUnderAttack(cell, 0, 3)
            && !isCellUnderAttack(cell, 0, 4)
            && !isCellUnderAttack(cell, 0, 5)) {
          whiteShortCastle = 2;
          return true;
        }
        // black Castling
      } else if ((currentTurnColor == Color.BLACK)
          && (fromRow == 7 && fromCol == 3)
          && (toRow == 7 && toCol == 1)
          && (blackKingNotMoved && blackLeftRookNotMoved)
          && (cell[7][1].isEmpty() && cell[7][2].isEmpty())) {

        if (!isCellUnderAttack(cell, 7, 1)
            && !isCellUnderAttack(cell, 7, 2)
            && !isCellUnderAttack(cell, 7, 3)) {
          blackShortCastle = 1;
          return true;
        }
      } else if ((currentTurnColor == Color.BLACK)
          && (fromRow == 7 && fromCol == 3)
          && (toRow == 7 && toCol == 5)
          && (blackKingNotMoved && blackRightRookNotMoved)
          && (cell[7][4].isEmpty() && cell[7][5].isEmpty() && cell[7][6].isEmpty())) {

        if (!isCellUnderAttack(cell, 7, 3)
            && !isCellUnderAttack(cell, 7, 4)
            && !isCellUnderAttack(cell, 7, 5)) {
          blackShortCastle = 2;
          return true;
        }
      }
    }
    return false;
  }

  // Only needs to move the rook accordingly, because the king is moved with movePiece method
  public void executeCastling(Cell[][] cell) {
    if (currentTurnColor == Color.WHITE) {
      if (whiteShortCastle == 1) {
        cell[0][2].setPieceValues(Color.WHITE, "rook", cell[0][0].getPieceImage());
        cell[0][0].clearPiece();
      } else if (whiteShortCastle == 2) {
        cell[0][4].setPieceValues(Color.WHITE, "rook", cell[0][7].getPieceImage());
        cell[0][7].clearPiece();
      }
    } else {
      if (blackShortCastle == 1) {
        cell[7][2].setPieceValues(Color.BLACK, "rook", cell[7][0].getPieceImage());
        cell[7][0].clearPiece();
      } else if (blackShortCastle == 2) {
        cell[7][4].setPieceValues(Color.BLACK, "rook", cell[7][7].getPieceImage());
        cell[7][7].clearPiece();
      }
    }
  }

  /**
   * Method can look if a cell could be attacked by an opponents piece. If a piece can attack the
   * cell then return false
   *
   * @param cell
   * @param attackedRow
   * @param attackedCol
   * @return
   */
  public Boolean isCellUnderAttack(Cell[][] cell, int attackedRow, int attackedCol) {
    for (int fromRow = 0; fromRow < 8; fromRow++) {
      for (int fromCol = 0; fromCol < 8; fromCol++) {
        if (currentTurnColor == Color.WHITE
            && cell[fromRow][fromCol].getPieceColor() == Color.BLACK) {
          if (isMoveAllowed(cell, fromRow, fromCol, attackedRow, attackedCol)) {
            return true;
          }
        } else if (currentTurnColor == Color.BLACK
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
   * is called by the eventhandler when clicking a piece to move to another cell and gets the
   * pieceName given from which it then calls which Movement logic method needs to be called. If the
   * move is possible it returns true (and the method in eventhandler will call movePiece method and
   * reposition the piece).
   *
   * @param cell
   * @param fromRow
   * @param fromCol
   * @param toRow
   * @param toCol
   * @return
   */
  public boolean isMoveAllowed(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
    String pieceName = cell[fromRow][fromCol].getPieceName();
    if (pieceName == null) {
      return false;
    }
    switch (pieceName) {
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
   * Move a piece and print its changes.
   *
   * @param fromRow
   * @param fromCol
   * @param toRow
   * @param toCol
   */
  public void movePiece(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {

    // Get the piece values from the source cell
    Color pieceColor = cell[fromRow][fromCol].getPieceColor();
    String pieceName = cell[fromRow][fromCol].getPieceName();
    Image pieceImage = cell[fromRow][fromCol].getPieceImage();

    // check for enPassant
    if (pieceName == "pawn" && isEnPassant(cell, fromRow, fromCol, toRow, toCol)) {
      executeEnPassant(cell, toRow, toCol);
    }

    // check for castling
    if (pieceName == "king" && (whiteKingNotMoved || blackKingNotMoved)) {
      // will only execute if certain conditions inside of isKingMoveAllowed are true
      // moves rook for its own (king is moved through setPieceValues)
      executeCastling(cell);
    }

    // Clear the piece values in the source cell
    cell[fromRow][fromCol].clearPiece();

    // Set the piece values in the destination cell
    cell[toRow][toCol].setPieceValues(pieceColor, pieceName, pieceImage);
  }

  public class PromoteButton extends Button {

    ChessPiece newPiece = new ChessPiece();

    public PromoteButton(
        ImageView icon, Cell[][] cell, int toRow, int toCol, Color pieceColor, String pieceName) {
      setGraphic(icon);
      setOnAction(
          event -> {
            newPiece.promotePawn(cell, toRow, toCol, pieceColor, pieceName);
          });
    }
  }

  // Creates the window for choosing a new Piece and calls the PromotoButton class as eventhandler
  public void openPromotionDialog(Cell[][] cell, Color pieceColor, int toRow, int toCol) {
    ImageView queen = new ImageView();
    ImageView rook = new ImageView();
    ImageView bishop = new ImageView();
    ImageView knight = new ImageView();

    // white pieces
    if (pieceColor == Color.WHITE) {
      Image queenImage = new Image(getClass().getResourceAsStream("/images/whiteQueen.png"));
      queen.setImage(queenImage);

      Image rookImage = new Image(getClass().getResourceAsStream("/images/whiteRook.png"));
      rook.setImage(rookImage);

      Image bishopImage = new Image(getClass().getResourceAsStream("/images/whiteBishop.png"));
      bishop.setImage(bishopImage);

      Image knightImage = new Image(getClass().getResourceAsStream("/images/whiteknight.png"));
      knight.setImage(knightImage);
    } else {
      // black pieces
      Image queenImage = new Image(getClass().getResourceAsStream("/images/blackQueen.png"));
      queen.setImage(queenImage);

      Image rookImage = new Image(getClass().getResourceAsStream("/images/blackRook.png"));
      rook.setImage(rookImage);

      Image bishopImage = new Image(getClass().getResourceAsStream("/images/blackBishop.png"));
      bishop.setImage(bishopImage);

      Image knightImage = new Image(getClass().getResourceAsStream("/images/blackknight.png"));
      knight.setImage(knightImage);
    }

    // box for choosing to which piece the pawn is promoted
    HBox buttonsBox =
        new HBox(
            new PromoteButton(queen, cell, toRow, toCol, pieceColor, "queen"),
            new PromoteButton(rook, cell, toRow, toCol, pieceColor, "rook"),
            new PromoteButton(bishop, cell, toRow, toCol, pieceColor, "bishop"),
            new PromoteButton(knight, cell, toRow, toCol, pieceColor, "knight"));

    // Window for choosing the new piece
    Alert promotionAlert = new Alert(AlertType.NONE);
    promotionAlert.setTitle("Pawn Promotion");
    promotionAlert.setHeaderText("Choose a piece to promote your pawn:");

    // Set content and buttons
    promotionAlert.getDialogPane().setContent(buttonsBox);
    ButtonType applyButtonType = new ButtonType("Apply");
    promotionAlert.getButtonTypes().add(applyButtonType);

    // Disables close button (X button)
    Stage stage = (Stage) promotionAlert.getDialogPane().getScene().getWindow();
    stage.setOnCloseRequest(e -> e.consume());
    promotionAlert.showAndWait();
  }

  // Called by the eventhandler in PromoteButton and sets the chosen piece on the board
  public void promotePawn(Cell[][] cell, int toRow, int toCol, Color pieceColor, String pieceName) {
    String capitalizedPieceName = pieceName.substring(0, 1).toUpperCase() + pieceName.substring(1);

    String imagePath;
    if (pieceColor == Color.WHITE) {
      imagePath = "/images/white" + capitalizedPieceName + ".png";

    } else {
      imagePath = "/images/black" + capitalizedPieceName + ".png";
    }
    Image image = new Image(getClass().getResourceAsStream(imagePath));
    cell[toRow][toCol].setPieceValues(pieceColor, pieceName, image);
  }

  public Boolean isPawnMoveAllowed(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
    Cell oldCell = cell[fromRow][fromCol];
    Cell newCell = cell[toRow][toCol];

    if (!"pawn".equals(oldCell.getPieceName())) {
      return false;
    }

    if (oldCell.getPieceColor() == Color.WHITE) {
      if (fromCol == toCol) {
        if (fromRow - toRow == -1 && newCell.isEmpty()) {
          return true; // One cell forward
        }
        if (fromRow == 1 && toRow == 3 && cell[2][toCol].isEmpty() && newCell.isEmpty()) {
          nextMove = 0;
          lastMovedPawnRow = toRow;
          lastMovedPawnCol = toCol;
          return true; // Two cells forward
        }
      } else if ((fromCol - toCol == -1 || fromCol - toCol == 1) && fromRow - toRow == -1) {
        if (newCell.getPieceColor() == Color.BLACK
            || isEnPassant(cell, fromRow, fromCol, toRow, toCol)) {
          return true; // Hit (left or right)
        }
      }
    } else {
      if (fromCol == toCol) {
        if (fromRow - toRow == 1 && newCell.isEmpty()) {
          return true; // One cell forward
        }
        if (fromRow == 6 && toRow == 4 && cell[5][toCol].isEmpty() && newCell.isEmpty()) {
          nextMove = 0;
          lastMovedPawnRow = toRow;
          lastMovedPawnCol = toCol;
          return true; // Two cells forward
        }
      } else if ((fromCol - toCol == 1 || fromCol - toCol == -1) && fromRow - toRow == 1) {
        if (newCell.getPieceColor() == Color.WHITE
            || isEnPassant(cell, fromRow, fromCol, toRow, toCol)) {
          return true; // Hit (left or right)
        }
      }
    }
    return false;
  }

  // checks if enPassant is possible
  public Boolean isEnPassant(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
    if (currentTurnColor == Color.WHITE) {
      if (fromRow == 4
          && nextMove == 1
          && (lastMovedPawnRow == toRow - 1 && lastMovedPawnCol == toCol)
          && cell[toRow][toCol].isEmpty()) {
        return true;
      }
    } else if (currentTurnColor == Color.BLACK) {
      if (fromRow == 3
          && nextMove == 1
          && (lastMovedPawnRow == toRow + 1 && lastMovedPawnCol == toCol) // only one pawn possible
          && cell[toRow][toCol].isEmpty()) {
        return true;
      }
    }
    return false;
  }

  public void executeEnPassant(Cell[][] cell, int toRow, int toCol) {
    if (currentTurnColor == Color.WHITE) {
      cell[toRow - 1][toCol].clearPiece();
    } else {
      cell[toRow + 1][toCol].clearPiece();
    }
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

    return false;
  }

  // hardcoded numbers bad should be altered
  public void showAllpossibleMoves(Cell[][] cell, int fromRow, int fromCol) {
    for (int j = 0; j < 8; j++) {
      for (int i = 0; i < 8; i++) {
        if (isMoveAllowed(cell, fromRow, fromCol, i, j)) {
          cell[i][j].setRectangleFill(Color.LIGHTYELLOW);
        }
      }
    }
  }
}

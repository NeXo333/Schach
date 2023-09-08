package thowl.model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import thowl.model.TurnManager.GameManager;

/**
 * The SpecialMoves class is responsible for handling special chess moves such as castling, en
 * passant, and pawn promotion. It also tracks the state of the board regarding these moves. This
 * class contains methods for executing these special moves and checking their validity.
 *
 * @author Marlon Schrader
 */
public class SpecialMoves {

  // Fields for tracking castling possibilities
  public Boolean whiteLeftRookNotMoved = true;
  public Boolean whiteRightRookNotMoved = true;
  public Boolean whiteKingNotMoved = true;
  public int whiteShortCastle = 0; // 0 = no castle, 1 == short castle, 2 == long castle

  public Boolean blackLeftRookNotMoved = true;
  public Boolean blackRightRookNotMoved = true;
  public Boolean blackKingNotMoved = true;
  public int blackShortCastle = 0; // 0 = no castle, 1 == short castle, 2 == long castle

  // Fields for en passant move tracking
  public int nextMove = 0; // Only the next move is allowed for en passant
  public int lastMovedPawnRow; // Remember the last pawn that made a 2-cell move
  public int lastMovedPawnCol;

  // responsible for color access and color change
  TurnManager turnManager = GameManager.getTurnManager();

  /**
   * Checks if either a rook or king has moved, updating the corresponding castling flags
   * accordingly.
   *
   * @param cell The chessboard represented as a 2D array of cells.
   * @param fromRow The row from which a piece is moved.
   * @param fromCol The column from which a piece is moved.
   * @param toRow The row to which a piece is moved.
   * @param toCol The column to which a piece is moved.
   */
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

  /**
   * Executes the castling move by moving the rook accordingly, as the king's move is validated by
   * the moveAllowed() method.
   *
   * @param cell The chessboard represented as a 2D array of cells.
   */
  public void executeCastling(Cell[][] cell) {
    if (turnManager.getCurrentTurnColor() == Color.WHITE) {
      if (whiteShortCastle == 1) { // 1 means short castle, move rook
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
   * Checks if the en passant move is possible.
   *
   * @param cell The chessboard represented as a 2D array of cells.
   * @param fromRow The row from which a piece is moved.
   * @param fromCol The column from which a piece is moved.
   * @param toRow The row to which a piece is moved.
   * @param toCol The column to which a piece is moved.
   * @return True if en passant is possible, false otherwise.
   */
  public Boolean isEnPassant(Cell[][] cell, int fromRow, int fromCol, int toRow, int toCol) {
    if (turnManager.getCurrentTurnColor() == Color.WHITE) {
      if (fromRow == 4
          && nextMove == 1 // only in the move possible after the pawn went forward
          && (lastMovedPawnRow == toRow - 1 && lastMovedPawnCol == toCol)
          && cell[toRow][toCol].isEmpty()) {
        return true;
      }
    } else {
      if (fromRow == 3
          && nextMove == 1
          && (lastMovedPawnRow == toRow + 1 && lastMovedPawnCol == toCol) // only one pawn possible
          && cell[toRow][toCol].isEmpty()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Removes the pawn that is captured through an en passant move.
   *
   * @param cell The chessboard represented as a 2D array of cells.
   * @param toRow The row where the capturing pawn moves.
   * @param toCol The column where the capturing pawn moves.
   */
  public void executeEnPassant(Cell[][] cell, int toRow, int toCol) {
    if (turnManager.getCurrentTurnColor() == Color.WHITE) {
      cell[toRow - 1][toCol].clearPiece();
    } else {
      cell[toRow + 1][toCol].clearPiece();
    }
  }

  /**
   * The PromoteButton class represents a button for pawn promotion in the chess game. It extends
   * the JavaFX Button class and is used to choose the promoted piece when a pawn reaches the
   * opposite end of the board (queen, rook, bishop, knight).
   */
  public class PromoteButton extends Button {
    public PromoteButton(
        ImageView icon, Cell[][] cell, int toRow, int toCol, Color pieceColor, String pieceName) {
      setGraphic(icon);
      setOnAction(
          event -> {
            promotePawn(cell, toRow, toCol, pieceColor, pieceName);
          });
    }
  }

  /**
   * Opens a dialog window for choosing a new piece to promote a pawn and sets the chosen piece on
   * the board.
   *
   * @param cell The chessboard represented as a 2D array of cells.
   * @param pieceColor The color of the promoting pawn.
   * @param toRow The row where the promotion occurs.
   * @param toCol The column where the promotion occurs.
   */
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

    promotionAlert.showAndWait();
  }

  /**
   * Promotes a pawn to a selected piece and updates the cell on the board.
   *
   * @param cell The chessboard represented as a 2D array of cells.
   * @param toRow The row where the promotion occurs.
   * @param toCol The column where the promotion occurs.
   * @param pieceColor The color of the promoted piece.
   * @param pieceName The name of the promoted piece.
   */
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
}

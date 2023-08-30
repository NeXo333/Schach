package thowl.model;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

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

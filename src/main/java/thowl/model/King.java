package thowl.model;

import javafx.scene.paint.Color;

public class King extends ChessPiece {

  public King(int row, int col, Color color) {
    super(row, col, color);
  }

  @Override
  public void move(int newRow, int newCol) {
    // Implement the movement logic for the king
    // Here, you can define how a king can move on the chessboard
    // For example, a king can move one square in any direction (horizontally, vertically, or
    // diagonally)
    // You can update the row and col properties based on the new position if the move is valid.
  }
}

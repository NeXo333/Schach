package thowl.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class Cell extends StackPane {

  public int row; // shows the position on the board (in cell[row][col])
  public int col;
  public int cellSize = 70; // from BoardUtils. Wouldnt accept it direct from the other class

  private Color fieldColor;
  private Image pieceImage;
  private Color pieceColor;
  private String pieceName;
  private ImageView pieceImageView;

  /**
   * Creation of a cell for every cell on the board and controlling it with the big 2D cell[][] in
   * boardUtils for movements and changes
   */
  public Cell(
      int row,
      int col,
      int size,
      Color fieldColor,
      Color pieceColor,
      String pieceName,
      Image pieceImage) {
    this.row = row;
    this.col = col;
    this.fieldColor = fieldColor;
    this.pieceColor = pieceColor;
    this.pieceName = pieceName;
    this.pieceImage = pieceImage;

    // Create the background rectangle
    Rectangle background = new Rectangle(size, size, fieldColor);

    // Set border properties
    background.setStroke(Color.BLACK);
    background.setStrokeType(StrokeType.INSIDE); // Or StrokeType.OUTSIDE as needed
    background.setStrokeWidth(1.0);

    // Create the ImageView for the piece image
    pieceImageView = new ImageView();
    pieceImageView.setFitWidth(size);
    pieceImageView.setFitHeight(size);

    // Add the background and piece image to the StackPane
    getChildren().addAll(background, pieceImageView);
  }

  // Method to change the background color of the cell
  public void setBackground(Color color) {
    Rectangle background = (Rectangle) getChildren().get(0);
    background.setFill(color);
  }

  // ... other methods ...

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public Color getFieldColor() {
    return fieldColor;
  }

  // Add getter and setter methods for piece image
  public Image getPieceImage() {
    return pieceImageView.getImage();
  }

  public Color getPieceColor() {
    return pieceColor;
  }

  public void setPieceColor(Color pieceColor) {
    this.pieceColor = pieceColor;
  }

  public String getPieceName() {
    return pieceName;
  }

  public void setPieceName(String pieceName) {
    this.pieceName = pieceName;
  }

  // big setter:
  public void setPieceValues(Color pieceColor, String pieceName, Image pieceImage) {
    this.pieceColor = pieceColor;
    this.pieceName = pieceName;
    this.pieceImage = pieceImage;
    pieceImageView.setImage(pieceImage);
  }

  // clear cell
  public void clearPiece() {
    this.pieceColor = null;
    this.pieceName = null;
    this.pieceImage = null;
    pieceImageView.setImage(null);
  }
}

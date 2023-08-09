package thowl.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class Cell extends StackPane {
  public double cellSize = 70.0; // from BoardUtils. Wouldnt accept it direct from the other class
  private Color fieldColor;
  private Image pieceImage;
  private Color pieceColor;
  private String pieceName;
  private ImageView pieceImageView;

  public Cell(double size, Color fieldColor, Color pieceColor, String pieceName, Image pieceImage) {
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

  // Add getter and setter methods for piece image
  public Image getPieceImage() {
    return pieceImageView.getImage();
  }

  public void setPieceImage(Image pieceImage) {
    pieceImageView.setImage(pieceImage);
  }

  public Color getPieceColor() {
    return pieceColor;
  }

  public void setPieceColor(Color pieceColor) {
    this.pieceColor = pieceColor;
  }

  public String getPieceName() {
    System.out.println(pieceName);
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
  }

  // clear cell
  public void clearPiece() {
    this.pieceColor = null;
    this.pieceName = null;
    this.pieceImage = null;
    pieceImageView.setImage(null);
  }

  public void updateCellAppearance() {
    Rectangle background = new Rectangle(cellSize, cellSize, fieldColor);
    background.setStroke(Color.BLACK);
    background.setStrokeType(StrokeType.INSIDE);
    background.setStrokeWidth(1.0);

    // Update the piece image if there is one
    if (pieceImage != null) {
      pieceImageView.setImage(pieceImage);
    } else {
      // If there is no piece image, clear the existing one
      pieceImageView.setImage(null);
    }

    // Clear the children and add the updated background and piece image
    getChildren().clear();
    getChildren().addAll(background, pieceImageView);
  }

  // Could be used for eventhandler
  // Helper methods to get row and column indices
  private int getRowIndex() {
    return GridPane.getRowIndex(this);
  }

  private int getColIndex() {
    return GridPane.getColumnIndex(this);
  }
}

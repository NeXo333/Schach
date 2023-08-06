package thowl.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class Cell extends StackPane {
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

    // Set border properties
    background.setStroke(Color.BLACK);
    background.setStrokeWidth(1.0);

    // Create the ImageView for the piece image
    pieceImageView = new ImageView();
    pieceImageView.setFitWidth(size);
    pieceImageView.setFitHeight(size);

    // Add the background and piece image to the StackPane
    getChildren().addAll(background, pieceImageView);
  }

  public void setPieceImage(Image image) {
    pieceImageView.setImage(image);
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

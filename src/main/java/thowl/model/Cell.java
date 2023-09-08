package thowl.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Represents a cell on the chessboard with properties such as position, field color, piece color,
 * piece name, and piece image and consists of an rectangle as visual. For every square its own cell
 * and one cell[][] to store all cells.
 *
 * @author Marlon Schrader
 */
public class Cell extends StackPane {

  public int row; // position on the board
  public int col;

  private Color fieldColor;
  private Image pieceImage;
  private Color pieceColor;
  private String pieceName;
  private ImageView pieceImageView;
  private Rectangle background;

  private final int cellCount = 8;

  /**
   * Constructs a cell with the specified properties.
   *
   * @param row The row position of the cell.
   * @param col The column position of the cell.
   * @param size The size of the cell.
   * @param fieldColor The color of the cell's background.
   * @param pieceColor The color of the piece on the cell.
   * @param pieceName The name of the piece on the cell.
   * @param pieceImage The image representing the piece on the cell.
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

    // Creates the sqaures/ cells for the board
    background = new Rectangle(size, size, fieldColor);

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

  /**
   * Getter for cell/field/square background color.
   *
   * @return background
   */
  public Paint getRectangleFill() {
    return this.background.getFill();
  }

  /** Setter for cell backrgound color */
  public void setRectangleFill(Color color) {
    this.background.setFill(color);
    return;
  }

  /**
   * Getter for row position.
   *
   * @return row.
   */
  public int getRow() {
    return row;
  }

  /**
   * Sets the row position of the cell.
   *
   * @param row The row position to set.
   */
  public void setRow(int row) {
    this.row = row;
  }

  /**
   * Gets the column position of the cell.
   *
   * @return The column position.
   */
  public int getCol() {
    return col;
  }

  /**
   * Sets the column position of the cell.
   *
   * @param col The column position to set.
   */
  public void setCol(int col) {
    this.col = col;
  }

  /**
   * Gets the color of the cell's background.
   *
   * @return The field color.
   */
  public Color getFieldColor() {
    return fieldColor;
  }

  /**
   * Gets the image representing the piece on the cell.
   *
   * @return The piece image.
   */
  public Image getPieceImage() {
    return pieceImageView.getImage();
  }

  /**
   * Gets the color of the piece on the cell.
   *
   * @return The piece color.
   */
  public Color getPieceColor() {
    return pieceColor;
  }

  /**
   * Sets the color of the piece on the cell.
   *
   * @param pieceColor The piece color to set.
   */
  public void setPieceColor(Color pieceColor) {
    this.pieceColor = pieceColor;
  }

  /**
   * Gets the name of the piece on the cell.
   *
   * @return The piece name.
   */
  public String getPieceName() {
    return pieceName;
  }

  /**
   * Sets the name of the piece on the cell.
   *
   * @param pieceName The piece name to set.
   */
  public void setPieceName(String pieceName) {
    this.pieceName = pieceName;
  }

  /**
   * Checks if the cell is empty (no piece on it).
   *
   * @return true if the cell is empty, false otherwise.
   */
  public Boolean isEmpty() {
    if (pieceName == null) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Big setter. Sets the properties of the piece on the cell.
   *
   * @param pieceColor The color of the piece.
   * @param pieceName The name of the piece.
   * @param pieceImage The image representing the piece.
   */
  public void setPieceValues(Color pieceColor, String pieceName, Image pieceImage) {
    this.pieceColor = pieceColor;
    this.pieceName = pieceName;
    this.pieceImage = pieceImage;
    pieceImageView.setImage(pieceImage);
  }

  /** Clears the cell, removing any piece information from it. */
  public void clearPiece() {
    this.pieceColor = null;
    this.pieceName = null;
    this.pieceImage = null;
    pieceImageView.setImage(null);
  }
}

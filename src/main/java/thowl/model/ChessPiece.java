package thowl.model;

import javafx.scene.paint.Color;

public abstract class ChessPiece {
    private int row;
    private int col;
    private Color color;

    public ChessPiece(int row, int col, Color color) {
        this.row = row;
        this.col = col;
        this.color = color;
    }

    // Abstract method to be implemented by each chess piece subclass
    public abstract void move(int newRow, int newCol);

    // Getters and setters for row, col, and color

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

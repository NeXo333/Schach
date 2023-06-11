package com.example.chess;

public abstract class ChessPiece {
    private int row;
    private int col;
    private PieceColor color;

    public ChessPiece(int row, int col, PieceColor color) {
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public enum PieceColor {
        White,
        Black
    }
}

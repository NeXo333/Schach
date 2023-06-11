package com.example.chess;
import com.example.chess.ChessPiece.PieceColor;


import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChessApp extends Application {

    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 80;

    @Override
    public void start(Stage primaryStage) {
        GridPane chessboard = createChessboard();

        Scene scene = new Scene(chessboard, BOARD_SIZE * SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess Game");
        primaryStage.show();
    }

    /** 
     * Creates the chessboards and colors it 
     * 
     * @return chessboard
     */
    private GridPane createChessboard() {
        GridPane chessboard = new GridPane();
        chessboard.setAlignment(Pos.CENTER);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                square.setFill((row + col) % 2 == 0 ? Color.WHITE : Color.LIGHTGRAY);
                chessboard.add(square, col, row);
            }
        }

        addIndices(chessboard);

        return chessboard;
    }

    /**
     * Adds for the rows the numbers 1 to 8 and for the columns the letters a to h
     * 
     * @param chessboard
     */
    private void addIndices(GridPane chessboard) {
    for (int i = 0; i < BOARD_SIZE; i++) {
        char colIndex = (char) ('a' + i);

        //Adds the letters to the columns
        Text colText = new Text(String.valueOf(colIndex));
        colText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        chessboard.add(colText, i, 0);

         //Puts the letters for the columns centered and top 
        GridPane.setHalignment(colText, HPos.CENTER);
        GridPane.setValignment(colText, VPos.TOP);

        //Adds the numbers to the rows
        Text rowText = new Text(String.valueOf(BOARD_SIZE - i));
        rowText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        chessboard.add(rowText, 0, i);
        }
    }









    
  
    public static void main(String[] args) {
        launch(args);
    
    }
}


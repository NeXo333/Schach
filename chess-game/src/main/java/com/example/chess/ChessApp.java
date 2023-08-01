
package com.example.chess;

import javafx.application.Application;
import javafx.geometry.HPos;
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
    //the board is now an array for easier data manipulation
    private Rectangle[][] squares;
// second array that checks clicked squares
    private boolean[][] clickedFlags;

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
        squares = new Rectangle[BOARD_SIZE][BOARD_SIZE];
        clickedFlags = new boolean[BOARD_SIZE][BOARD_SIZE];

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                squares[row][col] = square;
                squares[row][col].setFill((row + col) % 2 == 0 ? Color.WHITE : Color.LIGHTGRAY);

                //adds a Eventhandler that will be further improved
                int finalRow = row;
                int finalCol = col;
                square.setOnMouseClicked(e -> handleSquareClicked(finalRow,finalCol));

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
//handleSquare is a proof of concept and is in wip
    private void handleSquareClicked(int row, int col) {
        System.out.println("Clicked on square at row: " + row + ", column: " + col);
        // Toggle the clicked flag for the square
        clickedFlags[row][col] = !clickedFlags[row][col];

        // Update the square appearance based on the clicked flag
        squares[row][col].setFill(clickedFlags[row][col] ? Color.YELLOW : ((row + col) % 2 == 0 ? Color.WHITE : Color.LIGHTGRAY));
    }


    public static void main(String[] args) {
        launch(args);

    }
}

package thowl.model;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Has methods to create a colored chessboard + indices and
 * will be called by the main method (App.java)
 */
public class BoardUtils {
    public static final int BOARD_FIELDS = 8;
    public static final int BOARD_SIZE = BOARD_FIELDS * 70;

    /**
     * Creates the colored boared and calls method addIndices for the details
     * 
     * @return Chessborard
     */
    public static GridPane createChessboard() {
        // creates colored 2D Array
        Color[][] boardArray = new Color[BOARD_FIELDS][BOARD_FIELDS];
        for (int row = 0; row < BOARD_FIELDS; row++) {
            for (int col = 0; col < BOARD_FIELDS; col++) {
                boardArray[row][col] = (row + col) % 2 == 0 ? Color.WHITE : Color.LIGHTGRAY;
            }
        }
        // creates the Squares
        GridPane chessboard = new GridPane();
        chessboard.setAlignment(Pos.CENTER);
        for (int row = 0; row < BOARD_FIELDS; row++) {
            for (int col = 0; col < BOARD_FIELDS; col++) {
                // Square size important to static BOARD_SIZE
                Rectangle square = new Rectangle(70, 70, boardArray[row][col]);
                chessboard.add(square, col, row + 1); // +1 because positioning would be wrong
            }
        }
        addIndices(chessboard);
        return chessboard;
    }

    /**
     * Cretaes for the columns the numbers 1-8 and for the
     * rows the letters a-h
     * 
     * @param Gridpane Chessboard
     */
    private static void addIndices(GridPane chessboard) {
        // Add column labels 'a' to 'h'
        for (int col = 0; col < BOARD_FIELDS; col++) {
            Text colLabel = new Text(String.valueOf((char) ('a' + col)));
            colLabel.setFont(Font.font("Arial", 16));
            chessboard.add(colLabel, col, 0);
            GridPane.setHalignment(colLabel, HPos.CENTER); // Center horizontally in cell
        }
        // Add row labels '1' to '8'
        for (int row = 0; row < BOARD_FIELDS; row++) {
            Text rowLabel = new Text(String.valueOf(BOARD_FIELDS - row));
            rowLabel.setFont(Font.font("Arial", 16));
            chessboard.add(rowLabel, BOARD_FIELDS, row + 1);
            GridPane.setValignment(rowLabel, VPos.CENTER); // Center vertically in cell
        }
    }
}

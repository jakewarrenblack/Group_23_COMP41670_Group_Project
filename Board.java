import java.util.ArrayList;

public class Board {
    Point[] points = new Point[24];
    // We also need to have spaces for the bars and the off sections
    String[][] boardPrint = new String[15][14];

    public Board() {
        for (int i = 0; i < 24; i++) {
            this.points[i] = new Point(i);
        }

        // The top of the board
        boardPrint[0] = new String[]{"-13", "-+-", "-+-", "-+-", "-+-", "18-", "BAR", "-19", "-+-", "-+-", "-+-", "-+-", "-24", " OFF"};

        // The bottom of the board
        boardPrint[14] = new String[]{"-12", "-+-", "-+-", "-+-", "-+-", "-7-", "BAR", "-6-", "-+-", "-+-", "-+-", "-+-", "-1-", " OFF"};

        // The middle row, separating the two halves of the board
        boardPrint[7] = new String[]{"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "    "};

    }

    /**
     * <b>Draws the board</b>
     * <ul>
     *     <li>Iterates over the quadrants (top-left, top-right, bottom-left, bottom-right)</li>
     *     <li>For each quadrant, it calculates the starting row, column, and increment values</li>
     *     <li>The boardPrint array represents the positions on the board, initialised to empty spaces</li>
     *     <li>For each point in a quadrant, checks if there's a piece (using getColour)</li>
     *     <li>If no piece, leave it blank. Otherwise, use the B/W value.</li>
     * </ul>
     */
    private void updateBoard() {

        // Two outer loops iterate through 1. vertical and 2. horizontal quadrants.
        for (int vert = 0; vert < 2; vert++) {
            for (int hor = 0; hor < 2; hor++) {

                int startRow = 14 * (1 - vert);
                int increment = vert * 2 - 1;
                int startCol = 13 - 7 * hor;
                int col = startCol;

                // Need to figure out how we're tracking the bar and off positions
                for (int j = 0; j < 6; j++) {
                    boardPrint[startRow + (j + 1) * increment][col] = "   ";
                }

                for (int i = 0; i < 6; i++) {
                    col = startCol - (i+1);
                    System.out.println("Row "+startRow+" Col "+col);
                    for (int j = 0; this.points[i].numPieces() > j; j++) {
                        // Value will be blank, or a piece colour (B, W), if there's a piece here
                        boardPrint[startRow + (j + 1) * increment][col] = this.points[i].getColour();
                    }
                    for (int j = this.points[i].numPieces(); j < 6; j++) {
                        boardPrint[startRow + (j + 1) * increment][col] = " | ";
                    }
                }
            }
        }
    }

    public void print () {
        updateBoard();
        for (String[] strings : boardPrint) {
            for (String string : strings) {
                System.out.print(string);
            }
            System.out.println();
        }
    }
}


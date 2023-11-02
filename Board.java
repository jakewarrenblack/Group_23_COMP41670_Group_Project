import java.util.ArrayList;

public class Board {
    //    Quadrant[] quadrants = new Quadrant[4];
    Point[] points = new Point[24];
    // We also need to have spaces for the bars and the off sections
    String[][] boardPrint = new String[15][14];

    public Board() {
        for (int i = 0; i < 24; i++) {
            this.points[i] = new Point(i);
        }
        boardPrint[0] = new String[]{"-13", "-+-", "-+-", "-+-", "-+-", "18-", "BAR", "-19", "-+-", "-+-", "-+-", "-+-", "-24", " OFF"};
        boardPrint[14] = new String[]{"-12", "-+-", "-+-", "-+-", "-+-", "-7-", "BAR", "-6-", "-+-", "-+-", "-+-", "-+-", "-1-", " OFF"};
        boardPrint[7] = new String[]{"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "    "};

    }

    private void updateBoard() {
        int point;
        for (int vert = 0; vert < 2; vert++) {
            for (int hor = 0; hor < 2; hor++) {
                int quadrant = hor + vert * 2;
                int startRow = 14 * (1 - vert);
                int increment = vert * 2 - 1;
                int startCol = 13 - 7 * hor;
                int col = startCol;
                // Need to figure out how we're tracking the bar and off positions
                for (int j = 0; j < 6; j++) {
                    boardPrint[startRow + (j + 1) * increment][col] = "   ";
                }
                for (int i = 0; i < 6; i++) {
                    point = quadrant * 6 + i;
                    col = startCol - (i+1);
                    System.out.println("Row "+startRow+" Col "+col);
                    for (int j = 0; this.points[i].numPieces() > j; j++) {
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
        for (int i = 0; i < boardPrint.length; i++) {
            for (int j = 0; j < boardPrint[i].length; j++) {
                System.out.print(boardPrint[i][j]);
            }
            System.out.println();
        }
    }
}


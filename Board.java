import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

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
    public Piece removePiece(int index){
        return points[index].removePiece();
    }

    public void addPiece(int index,Piece piece){
        points[index].addPiece(piece);
    }

    public void placePieces(Player player){
        for (int j=0;j<15;j++){
            points[player.getPiece(j).getPosition()].addPiece(player.getPiece(j));
        }
    }

    public int numPieces(int index){
        return points[index].numPieces();
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


    protected void updateBoard(){
        ArrayList<Point> pointArrayList = new ArrayList<>(Arrays.asList(points));
        int[] cols = new int[]{12,11,10,9,8,7,5,4,3,2,1,0,0,1,2,3,4,5,7,8,9,10,11,12,6,6,13,13};
        int[] rows = new int[]{13,13,13,13,13,13,13,13,13,13,13,13,1,1,1,1,1,1,1,1,1,1,1,1,13,1,13,1};

        for (int pointIndex=0;pointIndex<24;pointIndex++){
            int col = cols[pointIndex];
            int row = rows[pointIndex];
            int increment=-1;

            if (row==1){
                increment=1;
            }

            for (int i = 0; i < 6; i++) {
                for (int j = 0; pointArrayList.get(pointIndex).numPieces() > j; j++) {
                    // Assign the string representation of the Point to the boardPrint array
                    boardPrint[row + (j) * increment][col] = pointArrayList.get(pointIndex).toString();
                }
                for (int j = pointArrayList.get(pointIndex).numPieces(); j < 6; j++) {
                    boardPrint[row + (j) * increment][col] = " | ";
                }
            }
        }

        for (int pointIndex=24; pointIndex < 28; pointIndex++){
            int col = cols[pointIndex];
            int row = rows[pointIndex];
            int increment=-1;

            if (row==1){
                increment=1;
            }

            for (int j = 0; j < 6; j++) {
                boardPrint[row + (j) * increment][col] = "   ";
            }
        }
    }


    public void print (Player.Color color, String[] recentLog) {
        updateBoard();

        int[] printOrder = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14};
        if (color.equals(Player.Color.BLACK)){printOrder=new int[]{14,1,2,3,4,5,6,7,8,9,10,11,12,13,0};}
        for (int i:printOrder){
            String[] strings = boardPrint[i];
            for (String string : strings) {
                System.out.print(string);
            }
            if (i>3&i<=13){
                System.out.print("     "+recentLog[i-4]);
            }
            System.out.print("\n");
        }
    }



    public Point getPoint(int index){return points[index];}
    public String getColour(int index){return points[index].getColour();}

    public String[][] getBoardPrint() {
        return boardPrint;
    }

    public Point[] getPoints(){
        return this.points;
    }
}
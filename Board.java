import javax.swing.*;

public class Board {
    private Point[] points = new Point[26];
    private Point[] bars = new Point[2];
    private Point[] offs = new Point[2];

    // We also need to have spaces for the bars and the off sections
    private final String[][] boardPrint = new String[15][14];
    /* layout stores the positional information for the points on the board
     *  positionWhite stores the positions of each point from the perspective of the WHITE player
     *  positionBlack the positions from the perspective of the BLACK player
     *  col the column in which the point appears
     *  row the row in which is starts
     *  points 24 and 25 are the BAR points for WHITE and BLACK respectively
     *  points 26 and 27 are the OFF areas for WHITE and BLACK respectively
     */

    // using this to replace all the enums we had before
    // this way, we can just refer to the points by their index, rather than having to use so many enums
    public static class BoardLayout {
        public static final Point[] POINTS = new Point[26];

        // this is a static block, which is executed when the class is loaded, no need for BoardLayout to be instantiated,
        // means the POINTS array available immediately

        static {
            // loop through 24 normal points
            for (int i = 1; i <= 24; i++) {
                int positionWhite = i;
                int positionBlack = 25 - i;

                // the column is the remainder of the index divided by 13, and the row is the index divided by 13
                // this is because there are 13 points in each row
                int col = (i - 1) % 13;
                int row = (i - 1) / 13;
                POINTS[i] = new Point(positionWhite, positionBlack, col, row);
            }

            // these are special points
            // the bar points, for when the player has pieces on the bar
            POINTS[0] = new Point(0, 25, 13, 13); // OFFWBARB
            POINTS[25] = new Point(25, 0, 13, 1); // OFFBBARW
        }
    }

    public Board() {
        for (int i = 1; i < 25; i++) {
            Point point = BoardLayout.POINTS[i];
            this.points[i] = new Point(point.getPosition(true), point.getPosition(false), point.getCoords()[1], point.getCoords()[0]);
        }
        Point offWBarB = BoardLayout.POINTS[0];
        this.points[0] = new OffBoard(offWBarB.getPosition(true), offWBarB.getPosition(false), offWBarB.getCoords()[1], offWBarB.getCoords()[0]);
        Point offBBarW = BoardLayout.POINTS[25];
        this.points[25] = new OffBoard(offBBarW.getPosition(true), offBBarW.getPosition(false), offBBarW.getCoords()[1], offBBarW.getCoords()[0]);
        // The top of the board
        boardPrint[0] = new String[]{"-13", "-+-", "-+-", "-+-", "-+-", "18-", "BAR", "-19", "-+-", "-+-", "-+-", "-+-", "-24", " OFF"};
        // The bottom of the board
        boardPrint[14] = new String[]{"-12", "-+-", "-+-", "-+-", "-+-", "-7-", "BAR", "-6-", "-+-", "-+-", "-+-", "-+-", "-1-", " OFF"};
        // The middle row, separating the two halves of the board
        boardPrint[7] = new String[]{"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "    "};
    }

    /**
     * Removes a piece from a particular point
     * @param       index       The point from which the piece is to be taken
     * @return                  The piece which is taken from the point
     */
    public Piece removePiece(int index){return points[index].removePiece();}

    /**
     * Adds a piece to a particular point
     * @param       index       The point on which the piece is to be placed
     * @param       piece       The point on which to place the piece
     */
    public void addPiece(int index,Piece piece){
        points[index].addPiece(piece);
    }

    /**
     * Places all a player's pieces on the correct points
     * @param       player      The player whose pieces we want to place
     */
    public void placePieces(Player player){
        for (int j=0;j<player.numPieces();j++){
            points[player.piecePosition(j)].addPiece(player.getPiece(j));
        }
    }
    /**
     * Gives the number of pieces on a given point
     * @param       index       The point we're interested in
     * @return                  The number of pieces on the point
     */
    public int numPieces(int index){
        return points[index].numPieces();
    }
    /**
     * Creates a text version of the board which can be printed to the console
     * Each point is a 6x1 array of 3 character strings
     * If there are pieces on the point, letters indicating the pieces' colour  are displayed
     * One letter for each piece
     * Any open spaces on the point (they can hold up to six pieces) show " | "
     * The co-ordinates of the points are given in two integer arrays
     */

    /*
    some rules:
        - can't move to point with 2+ enemy pieces

        - or a point with 6 of your pieces

        - if you land on one single with enemy piece, that's out


        - you need to use both dice if you CAN

        - if you can only use ONE, you MUST use the larger one

        - and if you roll doubles, you effectively have four dice of the same value
     */


    protected void updateBoard() {
        int pointIndex = 0;
        for (Point point : points) {
            int stacks = 1;

            if (pointIndex == 0 || pointIndex == points.length - 1) {
                stacks = 2;
            }

            for (int stack = 0; stack < stacks; stack++) {
                if (stacks > 1) {
                    ((OffBoard) point).setColor(stack == 0);
                }

                int col = point.getCoords()[1];
                int row = point.getCoords()[0];
                int increment = row == 1 ? 1 : -1;

                for (int i = 0; i < 6; i++) {

                    for (int j = 0; point.numPieces() > j; j++) {
                        boardPrint[row + (j) * increment][col] = point.getColour();
                    }
                    for (int j = point.numPieces(); j < 6; j++) {
                        boardPrint[row + (j) * increment][col] = emptySpace(pointIndex);
                    }
                }
            }
            pointIndex++;
        }
    }

    public String emptySpace(int i){
        if (i>0&i<25){
            return " | ";
        } else {
            return "   ";
        }
    }
    /**
     * Prints a text representation of the board to the screen
     * The board can be drawn from the perspective of the black player or the white player
     * A log of the most recent messsages sent to the players is displayed on the right
     * @param color         The colour of the current player
     * @param recentLog     The log of messages to display
     */
    public void print (Player.Color color, String[] recentLog) {
        updateBoard();

        int[] printOrder = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

        if (color.equals(Player.Color.BLACK)) {
            printOrder = new int[]{14, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 0};
        }

        for (int i : printOrder) {
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

    /**
     * Returns a single point specified by its position on the board
     * @param index         The position of the point to return
     * @return              The point to return
     */
    public Point getPoint(int index){
        return points[index];
    }

    public Point[] getPoints(){
        return this.points;
    }

    public OffBoard getBar(Player player){
        return (OffBoard) (player.getColor() == Player.Color.WHITE ? points[BoardLayout.POINTS[0].getPosition(true)] : points[BoardLayout.POINTS[0].getPosition(false)]);
    }

    public OffBoard getOff(Player player){
        return (OffBoard) (player.getColor() == Player.Color.WHITE ? points[BoardLayout.POINTS[25].getPosition(true)] : points[BoardLayout.POINTS[25].getPosition(false)]);
    }

    public boolean hasBarPieces(Player player){
        return !getBar(player).isEmpty();
    }

    public boolean isOff(int index, Player player){
        int off = player.getColor() == Player.Color.WHITE ? BoardLayout.POINTS[25].getPosition(true) : BoardLayout.POINTS[25].getPosition(false);
        return points[index].equals(points[off]);
    }
}
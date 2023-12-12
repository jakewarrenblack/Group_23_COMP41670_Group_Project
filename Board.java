import javax.swing.*;

public class Board {
    private Point[] points = new Point[26];
    private Point[] bars = new Point[2];
    private Point[] offs = new Point[2];

    // We also need to have spaces for the bars and the off sections
    private String[][] boardPrint = new String[15][14];
    /* layout stores the positional information for the points on the board
     *  positionWhite stores the positions of each point from the perspective of the WHITE player
     *  positionBlack the positions from the perspective of the BLACK player
     *  col the column in which the point appears
     *  row the row in which is starts
     *  points 24 and 25 are the BAR points for WHITE and BLACK respectively
     *  points 26 and 27 are the OFF areas for WHITE and BLACK respectively
     */
    public enum layout{
            OFFWBARB(0,25,13,13),
            ONE(1,24,12,13),
            TWO(2,23,11,13),
            THREE(3,22,10,13),
            FOUR(4,21,9,13),
            FIVE(5,20,8,13),
            SIX(6,19,7,13),
            SEVEN(7,18,5,13),
            EIGHT(8,17,4,13),
            NINE(9,16,3,13),
            TEN(10,15,2,13),
            ELEVEN(11,14,1,13),
            TWELVE(12,13,0,13),
            THIRTEEN(13,12,0,1),
            FOURTEEN(14,11,1,1),
            FIFTEEN(15,10,2,1),
            SIXTEEN(16,9,3,1),
            SEVENTEEN(17,8,4,1),
            EIGHTEEN(18,7,5,1),
            NINETEEN(19,6,7,1),
            TWENTY(20,5,8,1),
            TWENTYONE(21,4,9,1),
            TWENTYTWO(22,3,10,1),
            TWENTYTHREE(23,2,11,1),
            TWENTYFOUR(24,1,12,1),
            OFFBBARW(25,0,13,1);
        private final int pipWhite, pipBlack, col, row;
        layout(int pipWhite, int pipBlack, int col, int row){
            this.pipWhite = pipWhite;
            this.pipBlack = pipBlack;
            this.col=col;
            this.row=row;
        }
        public int getWhite(){return pipWhite;}
        public int getBlack(){return pipBlack;}
        public int getCol(){return col;}
        public int getRow(){return row;}

    }



    public Board() {
        for (int i = 1; i < 25; i++) {
            this.points[i] = new Point(layout.values()[i].getWhite(),layout.values()[i].getBlack(),layout.values()[i].getCol(),layout.values()[i].getRow());
        }
        this.points[0]=new OffBoard(layout.OFFWBARB.getWhite(), layout.OFFWBARB.getBlack(),layout.OFFWBARB.getCol(), layout.OFFWBARB.getRow());
        this.points[25]=new OffBoard(layout.OFFBBARW.getWhite(), layout.OFFBBARW.getBlack(),layout.OFFBBARW.getCol(), layout.OFFBBARW.getRow());
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
        return player.getColor()== Player.Color.WHITE ? (OffBoard) points[layout.OFFBBARW.getWhite()] : (OffBoard) points[layout.OFFWBARB.getWhite()];
    }

    public OffBoard getOff(Player player){
        return player.getColor()==Player.Color.WHITE ? (OffBoard) points[layout.OFFWBARB.getWhite()] : (OffBoard) points[layout.OFFBBARW.getWhite()];
    }

    public boolean hasBarPieces(Player player){
        return !getBar(player).isEmpty();
    }

    public boolean isOff(int index, Player player){
        int off = player.getColor()==Player.Color.WHITE ? layout.OFFWBARB.getWhite():layout.OFFBBARW.getWhite();
        return points[index].equals(points[off]);
    }
}
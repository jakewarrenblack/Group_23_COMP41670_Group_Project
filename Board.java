public class Board {
    private Point[] points = new Point[28];

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
    enum layout{ONE(0,23,12,13),TWO(1,22,11,13),THREE(2,21,10,13),
            FOUR(3,20,9,13),FIVE(4,19,8,13),SIX(5,18,7,13),
            SEVEN(6,17,5,13),EIGHT(7,16,4,13),NINE(8,15,3,13),
            TEN(9,14,2,13),ELEVEN(10,13,1,13),TWELVE(11,12,0,13),
            THIRTEEN(12,11,0,1),FOURTEEN(13,10,1,1),FIFTEEN(14,9,2,1),
            SIXTEEN(15,8,3,1),SEVENTEEN(16,7,4,1),EIGHTEEN(17,6,5,1),
            NINETEEN(18,5,7,1),TWENTY(19,4,8,1),TWENTYONE(20,3,9,1),
            TWENTYTWO(21,2,10,1),TWENTYTHREE(22,1,11,1),TWENTYFOUR(23,0,12,1),
            BARW(-1,25,6,13),BARB(25,-1,6,1),
            OFFW(24,-2,13,13),OFFB(-2,24,13,1);
        private int positionWhite;
        private int positionBlack;
        private int col;
        private int row;
        layout(int positionWhite,int positionBlack,int col, int row){
            this.positionWhite=positionWhite;
            this.positionBlack=positionBlack;
            this.col=col;
            this.row=row;
        }
        public int getWhite(){return positionWhite;}
        public int getBlack(){return positionBlack;}
        public int getCol(){return col;}
        public int getRow(){return row;}
    }

//    private final int[][] layout = new int[][]{{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27},
//            {23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4},
//            {12,11,10,9,8,7,5,4,3,2,1,0,0,1,2,3,4,5,7,8,9,10,11,12,6,6,13,13},
//            {13,13,13,13,13,13,13,13,13,13,13,13,1,1,1,1,1,1,1,1,1,1,1,1,13,1,13,1}};
    public Board() {
        for (int i = 0; i < 28; i++) {
            this.points[i] = new Point(layout.values()[i].getWhite(),layout.values()[i].getBlack(),layout.values()[i].getCol(),layout.values()[i].getRow());
        }

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
    protected void updateBoard() {
        for (int pointIndex=0;pointIndex<28;pointIndex++){
            int col = getCoords(pointIndex)[1];
            int row = getCoords(pointIndex)[0];
            int increment=-1;
            if (row==1){increment=1;}
            for (int i = 0; i < 6; i++) {
                for (int j = 0; numPieces(pointIndex) > j; j++) {
                    // Value will be blank, or a piece colour (B, W), if there's a piece here
                    boardPrint[row + (j) * increment][col] = getColour(pointIndex);
                }
                for (int j = numPieces(pointIndex); j < 6; j++) {
                    boardPrint[row + (j) * increment][col] = emptySpace(pointIndex);
                }
            }
        }
    }
    public String emptySpace(int i){
        if (i<24){
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

    /**
     * Returns a single point specified by its position on the board
     * @param index         The position of the point to return
     * @return              The point to return
     */
    public Point getPoint(int index){return points[index];}

    /**
     * Returns the colour of the pieces on a single point
     * @param index         The position of the point to return
     * @return              A string indicating the colour of the pieces on the point
     */
    public String getColour(int index){return points[index].getColour();}
    public int[] getCoords(int index){return points[index].getCoords();}

    public String[][] getBoardPrint() {
        return boardPrint;
    }
    public boolean isPlayers(int index, Player chkPlayer){
        return points[index].isPlayers(chkPlayer);
    }
    public boolean isFull(int index){return points[index].isFull();}
    public boolean isBlot(int index){return points[index].isBlot();}
    public boolean isEmpty(int index){return points[index].isEmpty();}
}


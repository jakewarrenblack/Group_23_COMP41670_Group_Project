import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private Point[] points;

    private final String gameTracker;

    private String[][] boardPrint;

    private int numPoints = 24;
    private int pointSize = 6;
    private int gap = 1;
    private int border = 1;
    private int barWhite = numPoints+1;
    private int barBlack = 0;
    private int offWhite = 0;
    private int offBlack = numPoints+1;

    public Board(int gameNumber, int matchGames, String playerBscore,String playerWscore) {
        this.points=new Point[this.numPoints+2];
        int midPoint = (int) numPoints/2;
        int quarterPoint = (int) numPoints/4;
        // The printout of the board must have enough rows for two sets of points, 2 boarders, and the gap in between
        // It must have enough columns for half of the points plus the bars and the off boards and a column for the log
        this.boardPrint = new String[gap+2*(border+pointSize)][midPoint+3];
        Arrays.stream(this.boardPrint).forEach(a -> Arrays.fill(a, ""));
        this.points = createPoints();
        // Now set up the string array which will get printed to the console as the game state
        // Confusingly, although in respect of Points class column always comes first, then row
        // For the boardPrint string row comes first, then column
        this.boardPrint=createPrintOut(this.boardPrint,playerBscore,playerWscore);
        this.gameTracker = "Game "+gameNumber+" of "+matchGames;
    }

    public Point[] createPoints(){
        Point[] points=new Point[numPoints+2];
        int midPoint = (int) numPoints/2;
        int quarterPoint = (int) numPoints/4;
        // First add the off board positions for white off/black bar and black off/white bar
        points[0] = new OffBoard(0,numPoints+1,midPoint+1,gap+pointSize*2);
        points[numPoints+1] = new OffBoard(numPoints+1,0,midPoint+1,border);
        // Now add all the on board points
        int col = midPoint+1;
        for (int i=1;i<=numPoints;i++){
            col+=colIncrement(i,midPoint,quarterPoint);
            int row = i<=midPoint?gap+2*pointSize:border;
            points[i] = new Point(i,numPoints-i+1,col,row);
            int printOutRow = row + (border * i<=midPoint?1:-1);
            boardPrint[printOutRow][col]=colNames(i,quarterPoint);
        }
        return points;
    }
    public String[][] createPrintOut(String[][] printOut,String playerBscore,String playerWscore){
        int midPoint = (int) numPoints/2;
        int quarterPoint = (int) numPoints/4;
        // Now set up the string array which will get printed to the console as the game state
        // Confusingly, although in respect of Points class column always comes first, then row
        // For the boardPrint string row comes first, then column
        printOut[0][midPoint+1] = "OFF";
        printOut[gap+2*(border+pointSize)-1][midPoint+1]="OFF";
        printOut[0][quarterPoint]="BAR";
        printOut[gap+2*(border+pointSize)-1][quarterPoint]="BAR";
        printOut[0][boardPrint[0].length-1]="    "+playerBscore;
        printOut[gap+2*(border+pointSize)-1][printOut[gap+2*(border+pointSize)-1].length-1]="    "+playerWscore;
        for (int i=0;i<gap;i++) {
            String[] printGap = new String[midPoint + 2];
            Arrays.fill(printGap, "   ");
            printOut[pointSize + border + i] = printGap;
        }
        return printOut;
    }
    protected String colNames(int position,int quarterPoint){
        String name = "-+-";
        String colName = String.valueOf(position);
        if (position%quarterPoint==0) {
            if ((position - 1) < (quarterPoint * 2)) {
                if (colName.length() == 1) {
                    name = "-" + colName + "-";
                } else {
                    name = "-" + colName;
                }
            } else {
                if (colName.length() == 1) {
                    name = "-" + colName + "-";
                } else {
                    name = colName + "-";
                }
            }
        }
        if ((position-1)%quarterPoint==0){
            if ((position-1)<(quarterPoint*2)){
                if (colName.length()==1){
                    name = "-" + colName + "-";
                } else {
                    name = colName + "-";
                }
            } else {
                name = "-"+colName;
            }
        }
        return name;
    }

    private int colIncrement(int position, int midPoint, int quarterPoint){
        // If we're on the first half of the points on the board we move from right to left
        int colIncrement = position<=midPoint?-1:1;
        // If we're at exactly the mid point the column stays the same as we switch from bottom to top
        colIncrement*=position==midPoint+1?0:1;
            // If we're at the quarter points we skip a column to accommodate the bar
        colIncrement*=(position-1)%quarterPoint==0&(position-1)%midPoint!=0?2:1;
        return colIncrement;
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
            // Normal on-board points only have one stack as they can hold only one colour of checkers
            int stacks = 1;
            // Off/Bar points have 2 stacks as they can hold both
            if (pointIndex == 0 || pointIndex == points.length - 1) {
                stacks = 2;
            }

            for (int stack = 0; stack < stacks; stack++) {
                // If the point is an Off/Bar hybrid, set the focus colour first
                if (stacks > 1) {
                    ((OffBoard) point).setColor(stack == 0);
                }
                // Column first, then row
                int col = point.getCoords()[0];
                int row = point.getCoords()[1];
                // For the top half of the board we move down (increment=1)
                // For the bottom half of the board we move up (increment=-1)
                int increment = row == 1 ? 1 : -1;
                for (int i = 0; i < 6; i++) {
                    // This will not display any checkers on a point in excess of 6
                    // Likely to be a problem when taking checkers off at the end of the game
                    for (int j = 0; point.numPieces() > j&j<6; j++) {
                        boardPrint[row + (j) * increment][col] = point.getColour();
                    }
                    for (int j = point.numPieces(); j < 6; j++) {
                        boardPrint[row + (j * increment)][col] = emptySpace(pointIndex);
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
            if (i==2){System.out.print("     "+gameTracker);}
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
        return player.getColor()== Player.Color.WHITE ? (OffBoard) points[barWhite] : (OffBoard) points[barBlack];
    }

    public OffBoard getOff(Player player){
        return player.getColor()==Player.Color.WHITE ? (OffBoard) points[offWhite] : (OffBoard) points[offBlack];
    }

    public boolean hasBarPieces(Player player){
        getBar(player).setColor(player.getColor());
        return !getBar(player).isEmpty();
    }

    public boolean isOff(int index, Player player){
        OffBoard off = getOff(player);
        return points[index].equals(off);
    }
    public String getColour(int index){return points[index].getColour();}

    public void setColour(int index,Player.Color color){
        points[index].setColor(color);
    }

    public void moveToBar(int blotPosition, Log log){
        if (points[blotPosition].isBlot()) {
            Piece blot = points[blotPosition].removePiece();
            int barIndex = blot.getColor() == Player.Color.WHITE ? 25 : 0;
            points[barIndex].setColor(blot.getColor());
            points[barIndex].addPiece(blot);
            log.updateLog(blot.getPlayer().getName()+"'s piece was move from "+blotPosition+" to the bar");
        }
    }
}
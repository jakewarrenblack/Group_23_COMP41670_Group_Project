import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The board on which the Backgammon game is played
 * Consists of 24 points, in two rows, each split in two by a bar
 *
 */
public class Board {
    /**
     * An array of Points which can hold Pieces
     */
    private Point[] points;
    /**
     * Where the game currently being played sits within the match series
     */
    private final String gameTracker;
    /**
     * A string representation of the board showing the current game state
     * which can be printed out to the console
     */
    private String[][] boardPrint;
    /**
     * The number of standard points on the board
     * Could conceivably be changed for "Super Backgammon"
     * but in normal play should always be 24
     */
    private int numPoints = 24;
    /**
     * How many pieces can be placed on a point
     */
    private int pointSize = 6;
    /**
     * How many blank rows should be placed between the two series of points
     * when displaying the board on the console
     */
    private int gap = 2;
    /**
     * How big should the border around the points be when displaying the board on the console
     */
    private int border = 1;
    /**
     * The index of the White bar in the Points array
     */
    private int barWhite = numPoints+1;
    /**
     * The index of the Black bar in the points array
     */
    private int barBlack = 0;
    /**
     * The index of the White Off Board in the points array
     */
    private int offWhite = 0;
    /**
     * The index of the Black Off Board in the points array
     */
    private int offBlack = numPoints+1;

    /**
     * The players in the current game
     */
    private Player[] players;

    /**
     * Construct a new instance of Board
     *
     * @param gameNumber the position of the current game within the series comprising the match
     * @param matchGames the number of games in the match series
     */


    public Board(int gameNumber, int matchGames, Player[] players) {
        this.points=new Point[this.numPoints+2];
        this.players = players;

        int midPoint = numPoints /2;
        // The printout of the board must have enough rows for two sets of points, 2 boarders, and the gap in between
        // It must have enough columns for half of the points plus the bars and the off boards and a column for the log
        this.boardPrint = new String[gap+2*(border+pointSize)][midPoint+3];
        Arrays.stream(this.boardPrint).forEach(a -> Arrays.fill(a, ""));
        this.points = createPoints();

        // This needs to happen *after* the points have been initialised
        this.placePieces();

        // Now set up the string array which will get printed to the console as the game state
        // Confusingly, although in respect of Points class column always comes first, then row
        // For the boardPrint string row comes first, then column
        this.boardPrint=createPrintOut(this.boardPrint);
        this.gameTracker = "Game "+gameNumber+" of "+matchGames;
    }

    /**
     * Create the Point instances which will comprise the board
     * The points will be constructed with defined pip scores for black and white
     * and a defined row and column start point for when the board is printed to the console
     * These will be calculated based on the board's parameters
     * and the position of the Point within the Points[] array
     *
     * @return and array of Point instances
     */
    public Point[] createPoints(){
        Point[] points=new Point[numPoints+2];
        int midPoint = numPoints /2;
        int quarterPoint = numPoints /4;
        // First add the off board positions for white off/black bar and black off/white bar
        points[0] = new OffBoard(0,numPoints+1,midPoint+1,gap+pointSize*2);
        points[numPoints+1] = new OffBoard(numPoints+1,0,midPoint+1,border);
        // Now add all the onboard points
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


    /**
     * Pre-populate a String representation of the board which can be printed out to the console
     *
     * @param printOut
     *
     * @return a String array representing the game state which can be printed to the console
     */

    public String[][] createPrintOut(String[][] printOut){
        int midPoint = numPoints /2;
        int quarterPoint = numPoints /4;

        // Now set up the string array which will get printed to the console as the game state
        // Confusingly, although in respect of Points class column always comes first, then row
        // For the boardPrint string row comes first, then column
        printOut[0][midPoint+1] = "OFF";
        printOut[gap+2*(border+pointSize)-1][midPoint+1]="OFF";
        printOut[0][quarterPoint]="BAR";
        printOut[gap+2*(border+pointSize)-1][quarterPoint]="BAR";
        printOut[0][boardPrint[0].length-1]="    "+ this.players[0].getScore();
        printOut[gap+2*(border+pointSize)-1][printOut[gap+2*(border+pointSize)-1].length-1]="    "+ this.players[1].getScore();
        for (int i=0;i<gap;i++) {
            String[] printGap = new String[midPoint + 2];
            Arrays.fill(printGap, "   ");
            printOut[pointSize + border + i] = printGap;
        }
        return printOut;
    }

    /**
     * Return the text which should appear above or below a point
     * on the game state printed to the console so the points can be identified
     * Only the first and last point in each board quarter should be shown
     * The rest are represented by a plus sign
     *
     * @param position the position of the point on the board
     * @param quarterPoint how many points are in a quarter
     * @return A 3 character string which can be added to the boardPrint string array
     */
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

    /**
     * How much to change the column value by when creating each new Point instance
     * On the bottom of the board need to reduce by 1, on the top increase by one
     * Exception is when crossing the bar when the magnitude of the increment should be two
     * And when moving from the bottom of the board to the top, when the column should stay constant
     * while the row changes
     *
     * @param position the position of the point on the board
     * @param midPoint how many points are in a half board
     * @param quarterPoint how many points are in a quarter board
     * @return integer representing the column change
     */
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
     */
    public void placePieces(){
        for(Player player: this.players){
            for (int j=0;j<player.numPieces();j++){
                points[player.piecePosition(j)].addPiece(player.getPiece(j));
            }
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

    /**
     * What to print on a point when there are no pieces there
     * If it's a normal on-board point it shoudl be "|"
     * If it's the bar or off it should be " "
     * @param i
     * @return a three character string which can be added to the boardPrint String array to print to console
     */
    public String emptySpace(int i){
        if (i>0&i<points.length-1){
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
        int[] printOrder = new int[boardPrint.length];
        for (int i=0;i<boardPrint.length;i++){
            printOrder[i]=i;
        }
        if (color.equals(Player.Color.BLACK)){
            printOrder[0]=boardPrint.length-1;
            printOrder[boardPrint.length-1]=0;
        }
        for (int i=0;i<printOrder.length;i++) {
            String[] strings = boardPrint[printOrder[i]];
            for (String string : strings) {
                System.out.print(string);
            }
            if (i==2){System.out.print("     "+gameTracker);}
            if (i>(boardPrint.length-recentLog.length-2)&i<=boardPrint.length-2){
                System.out.print("     "+recentLog[i-5]);
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

    /**
     * Return the Point array comprising the board
     * @return a Point[] array
     */
    public Point[] getPoints(){
        return this.points;
    }

    /**
     * Return the bar of the specified player
     * @param player whose bar board point we want to return
     * @return the OffBoard instance containing the bar of the specified player
     */
    public OffBoard getBar(Player player){
        int barWhite = numPoints + 1;
        int barBlack = 0;
        return player.getColor()== Player.Color.WHITE ? (OffBoard) points[barWhite] : (OffBoard) points[barBlack];
    }
    /**
     * Return the off board area of the specified player
     * @param player whose off-board point we want to return
     * @return the OffBoard instance containing the off board area of the specified player
     */
    public OffBoard getOff(Player player){
        int offWhite = 0;
        int offBlack = numPoints + 1;
        return player.getColor()==Player.Color.WHITE ? (OffBoard) points[offWhite] : (OffBoard) points[offBlack];
    }

    /**
     * Does the specified player have any pieces on the bar
     * @param player
     * @return true if the player has pieces on the bar, false if not
     */
    public boolean hasBarPieces(Player player){
        getBar(player).setColor(player.getColor());
        return !getBar(player).isEmpty();
    }

    /**
     * The colour of the pieces on the specified point
     * @param index the position of the Point on the Board
     * @return a string indicating the colour of the Pieces on the Point
     */
    public String getColour(int index){return points[index].getColour();}

    /**
     * Set the focus colour of the specified point
     * Only really works for OffBoard Points - other points have colours
     * defined by the pieces they hold
     *
     * @param index The position of the point on the board
     * @param color the colour on which to focus the point
     */
    public void setColour(int index,Player.Color color){
        points[index].setColor(color);
    }

    /**
     * If a player lands on a point holding a single piece of the opposite colour
     * that piece is moved to the bard
     *
     * @param blotPosition The position of the point holding the blot to be moved to the bar
     * @param log The current match's log so the notification of the move to bar can be logged
     */
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
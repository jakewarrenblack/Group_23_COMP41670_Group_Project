/*
    IF the player has opted to use each die separately:

    FOR EACH DIE:

        1. Make sure they aren't making some obviously invalid move, like moving a piece to the same place it started, or in the wrong direction,

        2. or off the board completely (unless that's allowed, but I suppose that's a special condition, check that later)

        3. Check the triangle they're trying to move to isn't occupied by two or more enemy pieces

        4. Check the triangle they're trying to move to isn't occupied by 6 of their own pieces (it's full)

        5. If their piece is already on the bar, don't let them do anything but move off the bar (And I think they go back to the start from here?)

        6. Check if the user's dice roll will permit the move they're trying to make

    ELSE the player is making a single move using the total dice value

        1. Make sure they aren't making some obviously invalid move, like moving a piece to the same place it started, or in the wrong direction,

        2. or off the board completely (unless that's allowed, but I suppose that's a special condition, check that later)

        3. Check the triangle they're trying to move to isn't occupied by two or more enemy pieces

        4. Check the triangle they're trying to move to isn't occupied by 6 of their own pieces (it's full)

        5. If their piece is already on the bar, don't let them do anything but move off the bar (And I think they go back to the start from here?)

        6. Check if the user's dice roll will permit the move they're trying to make

    If player rolled double, they essentially get to move the same checker N spaces x 4
*/

public class Move {
    private final int startPoint;
    private final int endPoint;
    private final Player player;
    private final Die die;

    private final boolean bearOffAllowed;

    public Move(int startPoint, int endPoint, Player player, Die die, boolean bearOffAllowed){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.player = player;
        this.die = die;
        this.bearOffAllowed = bearOffAllowed;
    }

    /**
     *
     * We can view an *illegal* move as distinct from an *invalid* move.
     * Illegal meaning it just doesn't even make sense, like moving backwards, moving to your current position, moving off the board prematurely.
     * @return boolean
     */

    private boolean isMoveLegal(){
        //  Check not moving from current place back to current place (like picking the piece up and putting it back down again)
        if(this.startPoint == this.endPoint){
            return false;
        }

        //  Check not moving backwards
        if(this.startPoint < this.endPoint){
            return false;
        }

        //  Check not moving off the board (presuming bearing off is not yet permitted)
        if(!this.bearOffAllowed){
            // TODO: How to check this? Player is trying to move off the board?
            return false;
        }

        return true;
    }

    /**
     * True if position is occupied by 2 or more enemy pieces, or is occupied by 6 friendly pieces.
     */
    private boolean isBlocked(){
        // TODO: Only bother with next part IF the user is trying to move to an index higher than 2
        //  Otherwise, that's not even possible, and the user can knock the other player's piece off
        //  Get the position the user is trying to move to, and check if there are 2+ pieces preceding it

        return false;
    }

    /**
     * Check if the user's dice roll will permit the move they're trying to make.
     * @return boolean
     */
    private boolean isValidDiceRoll(){
        return false;
    }

    // Validating a move consists of several smaller validation checks
    public boolean validateMove(){
        return isMoveLegal() && !isBlocked() && isValidDiceRoll();
    }


}

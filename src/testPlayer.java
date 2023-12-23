public class testPlayer extends Player {
    // A move using the smaller die roll is not valid if you're left unable to use the larger die roll
    // A contrived example of this is where black rolls a 5 and a 3 with the following configuration
    // 1 checker on point 9
    // 1 checker on point 12
    // 5 checkers on point 15
    // 6 checkers on point 20
    // This subclass exists to test that scenario
    protected final int[] startPoints = new int[]{24,24,20,20,20,20,20,20,15,15,15,15,15,12,10};

    public testPlayer(String name, Color color){
        super(name,color);
        for (int i=0;i<startPoints.length;i++){
            int position = startPoints[i];
            pieces[i]=new Piece(this,position);
        }
    }
    public int piecePosition(int index){return super.piecePosition(index);}

    public int startPosition(int index){return startPoints[index];}
}

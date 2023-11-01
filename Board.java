public class Board {
    Quadrant[] quadrants = new Quadrant[4];

    public Board(){
        for(int i=0; i<4; i++){
            this.quadrants[i] = new Quadrant(i);
        }
    }

    public void print(){
        for(Quadrant q: this.quadrants){
            System.out.println();
            q.print();
        }
    }
}

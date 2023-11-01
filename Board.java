public class Board {
    Quadrant[] quadrants = new Quadrant[4];

    public Board(){
        for(int i=0; i<4; i++){
            this.quadrants[i] = new Quadrant(i);
        }

        // maybe a sort of printing cascade?
        // e.g call this.quadrants.print
        // they print their borders
        // and Quadrants in turn print their Triangles
        // Triangles print their Points
        // Points print their Pieces, if applicable

    }

    public void print(){
        for(Quadrant q: this.quadrants){
            q.print();
        }
    }
}

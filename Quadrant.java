import java.util.ArrayList;

public class Quadrant {
    int position;
    Triangle[] triangles = new Triangle[6];

    public Quadrant(int position){
        this.position = position;

        for(int i=0; i<6; i++){
            // Quadrants at position 0 and 1 are at the top of the Board
            if(this.position == 0 || this.position == 1){
                this.triangles[i] = new Triangle(i);
            }
            // Quadrants at position 2 and 3 are at the bottom of the Board
            else{
                this.triangles[i] = new Triangle(i);
            }
        }
    }

    // Quadrants and Triangles are closely related
    // So, using an inner class to represent Triangles.
    private static class Triangle{
        private final int position;

        private final Point[] points = new Point[6];

        public Triangle(int position){
            this.position = position;

            for(int i=0; i<6; i++){
                this.points[i] = new Point(i, new ArrayList<Piece>());
            }
        }

        public int getPosition(){
            return this.position;
        }

        public Point[] getPoints(){
            return this.points;
        }


    }
    public void print(){

    }

}

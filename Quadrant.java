import java.util.ArrayList;

public class Quadrant {
    int position;
    Triangle[] triangles = new Triangle[6];

    public Quadrant(int position){
        this.position = position;
        // 2 quadrants will have UP oriented triangles, and 2 others DOWN

        for(int i=0; i<6; i++){
            // Quadrants at position 0 and 1 are at the top of the Board
            if(this.position == 0 || this.position == 1){
                this.triangles[i] = new Triangle(i, Triangle.Orientation.DOWN);
            }
            // Quadrants at position 2 and 3 are at the bottom of the Board
            else{
                this.triangles[i] = new Triangle(i, Triangle.Orientation.UP);
            }
        }
    }

    // Quadrants and Triangles are closely related
    // So, using an inner class to represent Triangles.
    private static class Triangle{
        private final int position;
        private enum Orientation {UP, DOWN} // Is the Triangle pointing upwards or downwards
        private final Orientation orientation;

        private final Point[] points = new Point[6];

        public Triangle(int position, Orientation orientation){
            this.position = position;
            this.orientation = orientation;

            for(int i=0; i<6; i++){
                this.points[i] = new Point(i, new ArrayList<Piece>());
            }
        }

        public void print() {
            if(this.orientation == Orientation.UP){
                System.out.println("  *  ");
                System.out.println(" *** ");
                System.out.println("*****");
            }
            else{
                System.out.println("*****");
                System.out.println(" *** ");
                System.out.println("  *  ");
            }
        }

        public int getPosition(){
            return this.position;
        }

        public Orientation getOrientation(){
            return this.orientation;
        }

        public Point[] getPoints(){
            return this.points;
        }
    }


}

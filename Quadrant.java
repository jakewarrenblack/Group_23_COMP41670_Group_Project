import java.util.ArrayList;

public class Quadrant {
    int position;
    Triangle[] triangles = new Triangle[6];

    public Quadrant(int position){
        this.position = position;

        for(int i=0; i<6; i++){
                this.triangles[i] = new Triangle(i);
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
                this.points[i] = new Point(i);
            }
        }

        public int getPosition(){
            return this.position;
        }

        public Point[] getPoints(){
            return this.points;
        }

        private String getTriangleString() {
            StringBuilder triangleOutput = new StringBuilder();
            for (Point p : this.points) {
                if (p.isOccupied()) {
                    triangleOutput.append("X");
                } else {
                    triangleOutput.append("|");
                }
            }
            return triangleOutput.toString();
        }

    }

    public void print() {
        // Print top border
        System.out.println("13--+---+---+---+---18 BAR 19--+---+---+---+---24 OFF");

        // Print points and bar
        for (Triangle t : this.triangles) {
            System.out.print("| ");
            System.out.print(t.getTriangleString());
        }
        System.out.println("|");

        // Print bottom border
        System.out.println("12--+---+---+---+---07 BAR 06--+---+---+---+---01 OFF");
    }
}

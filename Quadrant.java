public class Quadrant {
    int position;

    public Quadrant(int position){
        this.position = position;
    }

    // Quadrants and Triangles are closely related
    // So, using an inner class to represent Triangles.
    private static class Triangle{
        private final int position;
        private enum Orientation {UP, DOWN} // Is the Triangle pointing upwards or downwards
        private final Orientation orientation;

        public Triangle(int position, Orientation orientation){
            this.position = position;
            this.orientation = orientation;
        }
    }


}

package assignmnet4;

import javafx.scene.shape.Polygon;

public class Polygons {

    public static final class Triangle extends Polygon {
        public Triangle(double size) {
            size--;
            this.getPoints().addAll(new Double[]{
                    1.0, size,
                    size/2, 1.0,
                    size, size});
        }
    }

    public static final class Diamond extends Polygon {
        public Diamond(double size) {
            size--;
            this.getPoints().addAll(new Double[]{
                    1.0, size/2,
                    size/2, 1.0,
                    size, size/2,
                    size/2, size
            });
        }
    }

}

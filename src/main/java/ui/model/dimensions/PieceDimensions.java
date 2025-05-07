package ui.model.dimensions;

import java.awt.*;

public class PieceDimensions {
    private final int margin;

    private int radius;

    private Dimension dimension;

    public PieceDimensions() {
        this.margin = 10;
        this.radius = 20;

        int size = 2 * radius + 2 * margin;
        this.dimension = new Dimension(size, size);
    }

    public int getMargin() {
        return margin;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }
}

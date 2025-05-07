package ui.model.dimensions;
import java.awt.*;

public class BoardDimensions {

    private final Dimension innerDimension;
    private final Dimension outerDimension;
    private final int rows, cols;

    private final int margin;

    private final PieceDimensions pieceDimensions;


    public BoardDimensions() {
        this.rows = 8;
        this.cols = 8;
        this.margin = 10;
        this.pieceDimensions = new PieceDimensions();

        int width = this.cols * pieceDimensions.getDimension().width;
        int height = this.rows * pieceDimensions.getDimension().height;
        this.innerDimension = new Dimension(width, height);
        this.outerDimension = new Dimension(width + 2 * margin, height + 2 * margin);
    }

    public Dimension getInnerDimension() {
        return innerDimension;
    }

    public Dimension getOuterDimension() {
        return outerDimension;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMargin() {
        return margin;
    }

    public PieceDimensions getPieceDimensions() {
        return pieceDimensions;
    }
}

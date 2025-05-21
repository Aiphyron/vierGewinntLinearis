package ui.view;

import ui.model.GameModel;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private final GameModel model;

    public BoardPanel(ConnectFourFrame view, GameModel model) {
        this.model = model;
        this.setPreferredSize(model.getBoardDimensions().getOuterDimension());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        //this.model.recalculateBoardDimensions(getWidth(), getHeight());
        drawBoard(g2d);
    }

    private void drawBoard(Graphics2D g2d) {
        int margin = this.model.getBoardDimensions().getMargin();
        int cursorX = margin;
        int cursorY = margin;

        g2d.setColor(Color.BLUE);
        g2d.fillRect(
                cursorX,
                cursorY,
                this.model.getBoardDimensions().getInnerDimension().width,
                this.model.getBoardDimensions().getInnerDimension().height
        );

        g2d.setColor(Color.WHITE);
        int pieceMargin = this.model.getBoardDimensions().getPieceDimensions().getMargin();
        int pieceDiameter = this.model.getBoardDimensions().getPieceDimensions().getRadius() * 2;
        int pieceWidth = this.model.getBoardDimensions().getPieceDimensions().getDimension().width;
        int pieceHeight = this.model.getBoardDimensions().getPieceDimensions().getDimension().height;
        for (int col = 0; col < this.model.getBoardDimensions().getCols(); col++) {
            for (int row = 0; row < this.model.getBoardDimensions().getRows(); row++) {
                g2d.fillOval(
                        cursorX + pieceMargin,
                        cursorY + pieceMargin,
                        pieceDiameter,
                        pieceDiameter);
                cursorY += pieceHeight;
            }
            cursorY = margin;
            cursorX += pieceWidth;
        }
    }

}

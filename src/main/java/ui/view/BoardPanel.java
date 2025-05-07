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

        this.model.recalculateBoardDimensions(getWidth(), getHeight());
        drawBoard(g2d);
    }

    private void drawBoard(Graphics2D g2d) {
        int cursorX = this.model.getBoardDimensions().getMargin();
        int cursorY = this.model.getBoardDimensions().getMargin();

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(
                cursorX,
                cursorY,
                this.model.getBoardDimensions().getInnerDimension().width,
                this.model.getBoardDimensions().getInnerDimension().height
        );
    }

}

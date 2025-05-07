package ui.model;

import ui.model.dimensions.BoardDimensions;

public class GameModel {

    private PlayerEnum[][] board;

    private final BoardDimensions boardDimensions;

    public GameModel() {
        this.boardDimensions = new BoardDimensions();
        this.board = new PlayerEnum[boardDimensions.getRows()][boardDimensions.getCols()];
    }

    public PlayerEnum[][] getBoard() {
        return board;
    }

    public BoardDimensions getBoardDimensions() {
        return boardDimensions;
    }

    public void recalculateBoardDimensions(int panelWidth, int panelHeight) {

        if (
                panelWidth != boardDimensions.getOuterDimension().width
                        && panelHeight != boardDimensions.getOuterDimension().height
        ) {
            int margin = this.boardDimensions.getMargin();
            int pieceMargin = this.boardDimensions.getPieceDimensions().getMargin();

            int innerWidth = panelWidth - 2 * margin;
            int innerHeight = panelHeight - 2 * margin;
            int pieceSize = innerWidth / this.boardDimensions.getCols();
            int radius = (pieceSize- 2 * pieceMargin) / 2;

            this.boardDimensions.getInnerDimension().setSize(innerWidth, innerHeight);
            this.boardDimensions.getOuterDimension().setSize(panelWidth, panelHeight);
            this.boardDimensions.getPieceDimensions().getDimension().setSize(pieceSize, pieceSize);
            this.boardDimensions.getPieceDimensions().setRadius(radius);
        }

    }

}

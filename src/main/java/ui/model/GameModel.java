package ui.model;

import ui.model.dimensions.BoardDimensions;

public class GameModel {

    // PlayerEnum [Row][Column]
    private PlayerEnum[][] board;

    // Player names and client names
    private String player1Name;
    private String client1Name;
    private String player2Name;
    private String client2Name;

    // Save which player this is
    private PlayerEnum playerIdentity = PlayerEnum.ONE;

    // Dimensions of the board
    private final BoardDimensions boardDimensions;

    public GameModel() {
        this.boardDimensions = new BoardDimensions();
        this.board = new PlayerEnum[boardDimensions.getRows()][boardDimensions.getCols()];
    }

    public PlayerEnum[][] getBoard() {
        return board;
    }

    public void setBoardPiece(int col) {
        if (col >= 0 && col < boardDimensions.getCols()) {
            int row = getAvailableRow(col);
            if (row == -1) {
                throw new IllegalArgumentException("Column is full");
            }
            if (this.board[row][col] != null) {
                throw new IllegalArgumentException("Cell already occupied");
            }
            this.board[row][col] = playerIdentity;
        } else {
            throw new IndexOutOfBoundsException("Row or column out of bounds");
        }
    }

    /**
     * Returns the row index of the first available cell in the specified column.
     * If no cells are available, returns -1.
     *
     * @param col the column index to check
     * @return the row index of the first available cell, or -1 if none are available
     */
    private int getAvailableRow(int col) {
        for (int row = boardDimensions.getRows() - 1; row >= 0; row--) {
            if (this.board[row][col] == null) {
                return row;
            }
        }
        return -1;
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

    public void setBoard(PlayerEnum[][] board) {
        if (board.length != boardDimensions.getRows() || board[0].length != boardDimensions.getCols()) {
            throw new IllegalArgumentException("Board dimensions do not match the model's dimensions");
        }
        this.board = board;
    }

    public void setPlayerIdentity(PlayerEnum playerEnum) {
        if (playerEnum != PlayerEnum.ONE && playerEnum != PlayerEnum.TWO) {
            throw new IllegalArgumentException("Invalid player identity");
        }
        this.playerIdentity = playerEnum;
    }
}

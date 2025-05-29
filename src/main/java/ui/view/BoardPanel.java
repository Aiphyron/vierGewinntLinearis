package ui.view;

import ui.model.GameModel;
import ui.model.PlayerEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {

    private final GameModel model;

    private static Color PLAYER1_COLOR = new Color(255, 0, 0); // Red
    private static Color PLAYER2_COLOR = new Color(255, 255, 0); // Yellow

    public BoardPanel(ConnectFourFrame view, GameModel model) {
        this.model = model;
        this.setPreferredSize(model.getBoardDimensions().getOuterDimension());

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleBoardClick(e.getX(), e.getY());
            }
        });
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
        PlayerEnum[][] boardState = this.model.getBoard();

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
                PlayerEnum player = boardState[row][col];

                if (player == null) {
                    g2d.setColor(Color.WHITE);
                } else if (player == PlayerEnum.ONE) {
                    g2d.setColor(PLAYER1_COLOR);
                } else if (player == PlayerEnum.TWO) {
                    g2d.setColor(PLAYER2_COLOR);
                }

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

    private void handleBoardClick(int x, int y) {
        int margin = this.model.getBoardDimensions().getMargin();
        int pieceWidth = this.model.getBoardDimensions().getPieceDimensions().getDimension().width;
        int pieceHeight = this.model.getBoardDimensions().getPieceDimensions().getDimension().height;

        int col = (x - margin) / pieceWidth;
        int row = (y - margin) / pieceHeight;

        if (col >= 0 && col < this.model.getBoardDimensions().getCols() &&
                row >= 0 && row < this.model.getBoardDimensions().getRows()) {
            // Do something with the clicked cell, e.g.:
            System.out.println("Clicked cell: (" + col + ", " + row + ")");
            // Set the piece for the current player
            this.model.setBoardPiece(col);

            // Switch player identity for the next turn, HAS ERROR
            if (this.model.getBoard()[row][col] == PlayerEnum.ONE) {
                this.model.setPlayerIdentity(PlayerEnum.TWO);
            } else {
                this.model.setPlayerIdentity(PlayerEnum.ONE);
            }
            // Repaint the board to reflect the change
            repaint();
        }

        if (checkBottomRowFilled()) {
            clearBottomRow();
        }
    }

    private boolean checkBottomRowFilled() {
        PlayerEnum[][] board = this.model.getBoard();
        for (int col = 0; col < this.model.getBoardDimensions().getCols(); col++) {
            if (board[this.model.getBoardDimensions().getRows() - 1][col] == null) {
                return false; // If any cell in the bottom row is empty, return false
            }
        }
        return true;
    }

    private void clearBottomRow() {
        PlayerEnum[][] board = this.model.getBoard();
        for (int col = 0; col < this.model.getBoardDimensions().getCols(); col++) {
            board[this.model.getBoardDimensions().getRows() - 1][col] = null;
        }

        for(int col = 0; col < this.model.getBoardDimensions().getCols(); col++) {
            for(int row = this.model.getBoardDimensions().getRows() - 2; row >= 0; row--) {
                board[row + 1][col] = board[row][col]; // Shift pieces down
                board[row][col] = null; // Clear the top cell
            }
        }

        this.model.setBoard(board);
        repaint();
    }

    private PlayerEnum[][] createTestBoard() {
        PlayerEnum[][] testBoardState = new PlayerEnum[8][8];
        testBoardState[0][7] = PlayerEnum.ONE;
        testBoardState[1][1] = PlayerEnum.TWO;
        testBoardState[2][2] = PlayerEnum.ONE;
        testBoardState[3][3] = PlayerEnum.TWO;
        testBoardState[4][4] = PlayerEnum.ONE;
        testBoardState[5][5] = PlayerEnum.TWO;
        testBoardState[6][6] = PlayerEnum.ONE;
        testBoardState[7][7] = PlayerEnum.TWO;

        return testBoardState;
    }

}

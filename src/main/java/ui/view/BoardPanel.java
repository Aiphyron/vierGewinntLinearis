package ui.view;

import consumers.GameEventConsumer;
import consumers.LinetrisGameEventListener;
import models.ActionModels.NewGameAction;
import models.RequestModels.MoveGameRequestModel;
import producers.GameRequestProducer;
import ui.model.GameModel;
import ui.model.PlayerEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {

    private final GameModel model;

    private boolean waitingForGameStart = false;
    private boolean drawInitialBoardBool = true;

    private static final Color PLAYER1_COLOR = new Color(255, 0, 0); // Red
    private static final Color PLAYER2_COLOR = new Color(255, 255, 0); // Yellow

    private GameEventConsumer consumer = null;

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
        if (drawInitialBoardBool) {
            drawMessageBoard(g2d, "Welcome to Connect Four: Linetris Edition!\n To start or join a game please use the menu!");
        } else if (waitingForGameStart) {
            drawMessageBoard(g2d, "Waiting for game to start...");
        } else {
            drawBoard(g2d);
        }

        // TODO: Remove
        //drawBoard(g2d);

    }

    private void drawMessageBoard(Graphics2D g2d, String message) {
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics metrics = g2d.getFontMetrics();

        int maxWidth = getWidth() - 40; // 20px margin on each side
        java.util.List<String> lines = new java.util.ArrayList<>();
        for (String paragraph : message.split("\n")) {
            StringBuilder line = new StringBuilder();
            for (String word : paragraph.split(" ")) {
                String testLine = line.isEmpty() ? word : line + " " + word;
                if (metrics.stringWidth(testLine) > maxWidth) {
                    lines.add(line.toString());
                    line = new StringBuilder(word);
                } else {
                    if (!line.isEmpty()) line.append(" ");
                    line.append(word);
                }
            }
            lines.add(line.toString());
        }

        int lineHeight = metrics.getHeight();
        int totalHeight = lineHeight * lines.size();
        int y = (getHeight() - totalHeight) / 2 + metrics.getAscent();

        for (String l : lines) {
            int x = (getWidth() - metrics.stringWidth(l)) / 2;
            g2d.drawString(l, x, y);
            y += lineHeight;
        }
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
        if (waitingForGameStart || drawInitialBoardBool) {
            return; // Ignore clicks if waiting for game start or drawing initial board
        }
        int margin = this.model.getBoardDimensions().getMargin();
        int pieceWidth = this.model.getBoardDimensions().getPieceDimensions().getDimension().width;
        int pieceHeight = this.model.getBoardDimensions().getPieceDimensions().getDimension().height;

        int col = (x - margin) / pieceWidth;
        int row = (y - margin) / pieceHeight;

        if (col >= 0 && col < this.model.getBoardDimensions().getCols() &&
                row >= 0 && row < this.model.getBoardDimensions().getRows()) {
            // Do something with the clicked cell, e.g.:
            System.out.println("Clicked cell: (" + col + ", " + row + ")");

            sendMoveRequest(col);

            // Repaint the board to reflect the change
            repaint();
        }
    }

    public void clearRow(int row) {
        row = row -1; // Adjust row to match game masters logic (Start from 1 instead of start from 0)
        PlayerEnum[][] board = this.model.getBoard();
        // Set bottom row to null
        for (int col = 0; col < this.model.getBoardDimensions().getCols(); col++) {
            board[row][col] = null;
        }

        // Shift all rows above the cleared row down by one
        for(int col = 0; col < this.model.getBoardDimensions().getCols(); col++) {
            for(int moveRow = row - 1; moveRow >= 0; moveRow--) {
                board[moveRow + 1][col] = board[moveRow][col]; // Shift pieces down
                board[moveRow][col] = null; // Clear the top cell
            }
        }

        this.model.setBoard(board);
        repaint();
    }

    public void sendMoveRequest(int col) {
        // Adjust column to match game masters logic (Start from 1 instead of start from 0)
        col = col +1;
        MoveGameRequestModel moveRequest = new MoveGameRequestModel(
                this.model.getGameId(),
                this.model.getPlayerIdentity().toString(),
                col
        );
        GameRequestProducer producer = new GameRequestProducer();
        producer.sendGameRequest(moveRequest);
        producer.stop();
    }

    public void initializeNewGame(String gameId, PlayerEnum playerIdentity, NewGameAction newGameAction) {
        if (newGameAction == null) {
            throw new IllegalArgumentException("NewGameAction cannot be null");
        }
        model.initializeNewGame(gameId, playerIdentity, newGameAction);
        // if consumer not initialized or the gameId does not match the current consumer's gameId, start a new consumer
        // Should technically never happen, but you never know
        if (consumer == null || !consumer.getGameId().equals(gameId)) {
            startEventConsumer(gameId);
        }

        waitingForGameStart = false;
        drawInitialBoardBool = false;

        JOptionPane.showMessageDialog(this, "Initialized new game!\n" + model);
        repaint();
    }


    public void startEventConsumer(String gameId) {
        if (consumer != null) {
            stopEventConsumer();
        }
        consumer = new GameEventConsumer(gameId, new LinetrisGameEventListener(model, this, null));
        new Thread(consumer::consumeGameEvent).start();
    }

    public void stopEventConsumer() {
        if (this.consumer != null) {
            this.consumer.stop();
            this.consumer = null;
        }
    }

    /**
     * Cleans up the game by stopping the event consumer.
     * This method should be called when the game is over or when the board panel is no longer needed.
     */
    public void cleanUpGame() {
        stopEventConsumer();
        repaint();
    }
}
package ui.model;

import models.ActionModels.NewGameAction;
import ui.model.dimensions.BoardDimensions;

public class GameModel {

    private String playerName;
    private String clientName;

    // PlayerEnum [Row][Column]
    private PlayerEnum[][] board;

    // Player names and client names
    private String player1Name;
    private String client1Name;
    private String player2Name;
    private String client2Name;

    private String gameId;

    // Save which player this is
    private PlayerEnum playerIdentity = PlayerEnum.ONE;
    private PlayerEnum currentPlayer = PlayerEnum.ONE;

    // Dimensions of the board
    private final BoardDimensions boardDimensions;

    public GameModel(String playerName, String clientName) {
        if (playerName == null || playerName.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        if (clientName == null || clientName.isEmpty()) {
            throw new IllegalArgumentException("Client name cannot be null or empty");
        }
        this.playerName = playerName;
        this.clientName = clientName;
        this.boardDimensions = new BoardDimensions();
        this.board = new PlayerEnum[boardDimensions.getRows()][boardDimensions.getCols()];
    }

    public PlayerEnum[][] getBoard() {
        return board;
    }

    public void setBoardPiece(int col) {
        col = col - 1; // Adjust column to match game master's logic (Start from 1 instead of start from 0)
        if (col >= 0 && col < boardDimensions.getCols()) {
            int row = getAvailableRow(col);
            if (row == -1) {
                throw new IllegalArgumentException("Column is full");
            }
            if (this.board[row][col] != null) {
                throw new IllegalArgumentException("Cell already occupied");
            }
            this.board[row][col] = getCurrentPlayer();
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

    public PlayerEnum getPlayerIdentity() {
        return this.playerIdentity;
    }

    public PlayerEnum getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(PlayerEnum currentPlayer) {
        if (currentPlayer != PlayerEnum.ONE && currentPlayer != PlayerEnum.TWO) {
            throw new IllegalArgumentException("Invalid player identity");
        }
        this.currentPlayer = currentPlayer;
    }

    public void resetBoard() {
        PlayerEnum[][] newBoard = new PlayerEnum[getBoardDimensions().getRows()][getBoardDimensions().getCols()];
        setBoard(newBoard);
    }

    public void setGameId(String gameId) {
        if (gameId == null || gameId.isEmpty()) {
            throw new IllegalArgumentException("Game ID cannot be null or empty");
        }
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayer1Name() {
        return player1Name;
    }
    public void setPlayer1Name(String player1Name) {
        if (player1Name == null || player1Name.isEmpty()) {
            throw new IllegalArgumentException("Player 1 name cannot be null or empty");
        }
        this.player1Name = player1Name;
    }
    public String getClient1Name() {
        return client1Name;
    }
    public void setClient1Name(String client1Name) {
        if (client1Name == null || client1Name.isEmpty()) {
            throw new IllegalArgumentException("Client 1 name cannot be null or empty");
        }
        this.client1Name = client1Name;
    }
    public String getPlayer2Name() {
        return player2Name;
    }
    public void setPlayer2Name(String player2Name) {
        if (player2Name == null || player2Name.isEmpty()) {
            throw new IllegalArgumentException("Player 2 name cannot be null or empty");
        }
        this.player2Name = player2Name;
    }
    public String getClient2Name() {
        return client2Name;
    }
    public void setClient2Name(String client2Name) {
        if (client2Name == null || client2Name.isEmpty()) {
            throw new IllegalArgumentException("Client 2 name cannot be null or empty");
        }
        this.client2Name = client2Name;
    }

    public String getPlayerName() {
        return playerName;
    }
    public String getClientName() {
        return clientName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameModel:\n");
        sb.append("  Game ID: ").append(gameId).append("\n");
        sb.append("  Player Name: ").append(playerName).append("\n");
        sb.append("  Client Name: ").append(clientName).append("\n");
        sb.append("  Player 1 Name: ").append(player1Name).append("\n");
        sb.append("  Client 1 Name: ").append(client1Name).append("\n");
        sb.append("  Player 2 Name: ").append(player2Name).append("\n");
        sb.append("  Client 2 Name: ").append(client2Name).append("\n");
        sb.append("  Player Identity: ").append(playerIdentity).append("\n");
        sb.append("  Board Dimensions: ")
                .append(boardDimensions.getRows()).append("x")
                .append(boardDimensions.getCols()).append("\n");
        // Not needed right now, but can be uncommented for debugging
//        sb.append("  Board:\n");
//        for (PlayerEnum[] row : board) {
//            sb.append("    ");
//            for (PlayerEnum cell : row) {
//                sb.append(cell == null ? "." : cell.name().charAt(0)).append(" ");
//            }
//            sb.append("\n");
//        }
        return sb.toString();
    }

    public void initializeNewGame(String gameId, PlayerEnum playerIdentity, NewGameAction newGameAction) {
        if (newGameAction == null) {
            throw new IllegalArgumentException("NewGameAction cannot be null");
        }
        resetBoard();

        this.gameId = gameId;
        this.player1Name = newGameAction.getPlayer1().getName();
        this.client1Name = newGameAction.getClient1().getName();
        this.player2Name = newGameAction.getPlayer2().getName();
        this.client2Name = newGameAction.getClient2().getName();
        this.playerIdentity = playerIdentity;
        this.currentPlayer = PlayerEnum.ONE;
    }
}

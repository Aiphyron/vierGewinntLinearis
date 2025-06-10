package consumers;

import models.ActionModels.*;
import models.ActionModels.Action;
import models.ClientModel;
import models.GameEventModels.EventModel;
import models.GameEventModels.GameState;
import models.GameEventModels.SyncGameModel;
import models.PlayerModel;
import models.RequestModels.NewGameGameRequestModel;
import models.RequestModels.SyncGameRequestModel;
import models.RequestModels.SyncGameTypes;
import producers.GameRequestProducer;
import ui.model.GameModel;
import ui.model.PlayerEnum;
import ui.view.BoardPanel;

import javax.swing.*;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LinetrisGameEventListener implements GameEventListener {
    private final GameModel model;
    private final BoardPanel boardPanel;
    private final CountDownLatch latch;

    public LinetrisGameEventListener(GameModel model, BoardPanel boardPanel, CountDownLatch latch) {
        this.model = model;
        this.boardPanel = boardPanel;
        this.latch = latch;
    }

    @Override
    public void onGameEvent(EventModel event) {
        List<Action> actions = event.getActions();

        // Handle errors
        if (event.getState() == GameState.ERROR) {
            String errorMessage = event.getMessage();

            // Get player to display the error message for
            PlayerEnum currentPlayer = null;
            for (Action action: actions) {
                switch (action) {
                    case NewGameAction newGameAction -> {
                        currentPlayer = null;
                    }
                    case DeleteBottomRowAction deleteBottomRowAction -> {
                        // Do nothing because no player is set here
                    }
                    case MoveAction moveAction -> {
                        currentPlayer = PlayerEnum.fromString(moveAction.getPlayer());
                    }
                    default -> System.out.println("Unhandled action type: " + action.getClass().getSimpleName());
                }
            }

            // Only display error message to the current player or if currentPlayer is null display it to both
            if (currentPlayer == model.getPlayerIdentity() || currentPlayer == null) {
                if (errorMessage != null && !errorMessage.isEmpty()) {
                    JOptionPane.showMessageDialog(boardPanel, "Error: " + errorMessage);
                } else {
                    JOptionPane.showMessageDialog(boardPanel, "An unknown error occurred.");
                }
            }
            return;
        }

        // Handle game actions
        if (actions != null && !actions.isEmpty() && event.getState() == GameState.OK) {
            for (Action action : actions) {
                switch (action) {
                    case MoveAction moveAction -> {
                        int col = moveAction.getColumn();
                        PlayerEnum player = PlayerEnum.fromString(moveAction.getPlayer());
                        boardPanel.setBoardPiece(player, col);
                    }
                    case DeleteBottomRowAction deleteBottomRowAction -> {
                        int row = deleteBottomRowAction.getRow();
                        boardPanel.clearRow(row);
                    }
                    case WinAction winAction -> {
                        PlayerEnum winner = PlayerEnum.fromString(winAction.getPlayer());
                        // Display name of winner
                        String winnerMessage = "";
                        if (winner == model.getPlayerIdentity()) {
                            winnerMessage = "You win!";
                        } else {
                            winnerMessage = "You lose!";
                        }
                        JOptionPane.showMessageDialog(boardPanel, winnerMessage);
                        boardPanel.cleanUpGame();
                    }
                    case NewGameAction newGameAction -> {
                        String newGameId = event.getGameId();
                        boardPanel.initializeNewGame(newGameId, model.getPlayerIdentity(), newGameAction);
                    }
                    // Add cases for other action types if needed
                    default -> System.out.println("Unhandled action type: " + action.getClass().getSimpleName());
                }
            }
        }
    }

    @Override
    public void onGameSync(SyncGameModel event, SyncGameTypes wantedType) {
        // Handle game sync event
        GameRequestProducer producer = new GameRequestProducer();

        SyncGameTypes syncType = null;
        if (event != null) {
            syncType = event.getType();
        }

        if (syncType == SyncGameTypes.SEARCH_GAME && wantedType == SyncGameTypes.SEARCH_GAME) {
            if (event.getGameId().equals(model.getGameId())) {
                // If it reads the same game ID here, ignore to prevent client matching with itself
                // should technically never happen, but better safe than sorry
                System.out.println("SAME GAME ID DETECTED, IGNORING");
                return;
            }
            // Set player identity, rest is set when game master approvement is received
            model.setPlayerIdentity(PlayerEnum.TWO);
            // For starting the event consumer in GameMenu
            model.setGameId(event.getGameId());
            // Notify the other player that a player has joined his game
            SyncGameRequestModel syncRequest = new SyncGameRequestModel(
                    Instant.now().toEpochMilli(), event.getGameId(), SyncGameTypes.PLAYER_JOINED, model.getClientName(), model.getPlayerName(),model.getBoardDimensions().getRows(),model.getBoardDimensions().getCols());
            producer.sendSyncGameRequest(syncRequest);

            System.out.println("Game ID: " + event.getGameId() + " found, sending PLAYER_JOINED request");
            if (latch != null) {
                // Free up the latch to signal that the game is ready
                latch.countDown();
            }
        } else if (syncType == SyncGameTypes.PLAYER_JOINED && wantedType == SyncGameTypes.PLAYER_JOINED) {
            if (event.getGameId().equals(model.getGameId())
                    && event.getRows() == model.getBoardDimensions().getRows()
                    && event.getCols() == model.getBoardDimensions().getCols()) {
                // Set up player identity, rest is set when game master approvement is received
                model.setPlayerIdentity(PlayerEnum.ONE);
                // Send start game request
                NewGameGameRequestModel newGameRequest = new NewGameGameRequestModel(event.getGameId(), new ClientModel(model.getClientName()), new PlayerModel(model.getPlayerName()), new ClientModel(event.getClientName()), new PlayerModel(event.getPlayerName()));
                producer.sendGameRequest(newGameRequest);

                // Free up the latch to signal that the game is ready
                if (latch != null) {
                    latch.countDown();
                }
            }
        producer.stop();
        }
    }
}

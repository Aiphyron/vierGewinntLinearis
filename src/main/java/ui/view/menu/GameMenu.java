package ui.view.menu;

import consumers.GameSyncConsumer;
import consumers.LinetrisGameEventListener;
import models.RequestModels.SyncGameRequestModel;
import models.RequestModels.SyncGameTypes;
import producers.GameRequestProducer;
import ui.model.GameModel;
import ui.view.BoardPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * GameMenu is a JMenu that provides options to start a new game or connect to an existing game.
 * It handles the actions for starting a new game and connecting to an existing game.
 */
public class GameMenu extends JMenu implements ActionListener {

    public JMenuItem startGameItem;

    public JMenuItem connectGameItem;
    private GameModel gameModel;
    private BoardPanel boardPanel;


    /**
     * Constructs a GameMenu with the specified GameModel and BoardPanel.
     *
     * @param model      the GameModel that holds the game state
     * @param boardPanel the BoardPanel where the game will be displayed
     */
    public GameMenu(GameModel model, BoardPanel boardPanel) {
        gameModel = model;
        this.boardPanel = boardPanel;
        this.setText("Spiel");

        startGameItem = new JMenuItem("Neues Spiel starten");
        startGameItem.getAccessibleContext().setAccessibleDescription(
                "Startet ein neues Spiel als Spieler A"
        );
        startGameItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK
        ));
        startGameItem.addActionListener(this);
        this.add(startGameItem);

        connectGameItem = new JMenuItem("Mit Spiel verbinden");
        connectGameItem.getAccessibleContext().setAccessibleDescription(
                "Tritt einem existierenden Spiel als Spieler B bei. Dafür wird eine UUID benötigt."
        );
        connectGameItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK
        ));
        connectGameItem.addActionListener(this);
        this.add(connectGameItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == this.startGameItem) {
            String gameID = UUID.randomUUID().toString();
            gameModel.setGameId(gameID);

            SyncGameRequestModel request = new SyncGameRequestModel(Instant.now().toEpochMilli(), gameID, SyncGameTypes.SEARCH_GAME, gameModel.getClientName(), gameModel.getPlayerName(), gameModel.getBoardDimensions().getRows(), gameModel.getBoardDimensions().getCols());
            GameRequestProducer producer = new GameRequestProducer();
            producer.sendSyncGameRequest(request);
            producer.stop();

            // Create a modal waiting dialog
            JDialog waitingDialog = generateWaitWindow("Waiting for another player to join...");

            // Wait for a player joined event from consumer
            CountDownLatch latch = new CountDownLatch(1);

            GameSyncConsumer consumer = new GameSyncConsumer(new LinetrisGameEventListener(gameModel, boardPanel, latch), gameModel);

            // Start the consumer in a new thread
            new Thread(() -> {
                consumer.consumeGameSync(SyncGameTypes.PLAYER_JOINED);
            }).start();

            // Wait for the latch in a background thread, then close the dialog
            new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                waitingDialog.dispose();
                boardPanel.startEventConsumer(gameModel.getGameId());
                boardPanel.setWaitingForGameStartAndRepaint(true);
            }).start();

            // Show the dialog (this blocks the UI until dispose is called)
            waitingDialog.setVisible(true);

        } else if (source == this.connectGameItem) {
            CountDownLatch latch = new CountDownLatch(1);
            GameSyncConsumer consumer = new GameSyncConsumer(new LinetrisGameEventListener(gameModel, boardPanel, latch), gameModel);

            JDialog waitingDialog = generateWaitWindow("Searching for game to join...");

            // Start the consumer in a new thread
            new Thread(() -> {
                consumer.consumeGameSync(SyncGameTypes.SEARCH_GAME);
            }).start();

            new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                waitingDialog.dispose();
                System.out.println("Started consumer with game ID: " + gameModel.getGameId());
                boardPanel.startEventConsumer(gameModel.getGameId());
                boardPanel.setWaitingForGameStartAndRepaint(true);
            }).start();

            waitingDialog.setVisible(true);
        }

    }

    /**
     * Generates a waiting dialog with the specified display text.
     *
     * @param displayText the text to display in the waiting dialog
     * @return the JDialog waiting dialog
     */
    private JDialog generateWaitWindow(String displayText) {
        JDialog waitingDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(boardPanel), "Waiting", false);
        waitingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        waitingDialog.add(new JLabel(displayText), java.awt.BorderLayout.CENTER);
        waitingDialog.setSize(300, 100);
        waitingDialog.setLocationRelativeTo(boardPanel);

        return waitingDialog;
    }
}

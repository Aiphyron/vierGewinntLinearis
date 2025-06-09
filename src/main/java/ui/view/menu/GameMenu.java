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

public class GameMenu extends JMenu implements ActionListener {

    public JMenuItem startGameItem;

    public JMenuItem connectGameItem;
    private GameModel gameModel;
    private BoardPanel boardPanel;


    public GameMenu(GameModel model, BoardPanel boardPanel) {
        gameModel = model;
        this.boardPanel = boardPanel;
        this.setText("Spiel");

        this.startGameItem = new JMenuItem("Neues Spiel starten");
        this.startGameItem.getAccessibleContext().setAccessibleDescription(
                "Startet ein neues Spiel als Spieler A"
        );
        this.startGameItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK
        ));
        this.startGameItem.addActionListener(this);
        this.add(startGameItem);

        this.connectGameItem = new JMenuItem("Mit Spiel verbinden");
        this.connectGameItem.getAccessibleContext().setAccessibleDescription(
                "Tritt einem existierenden Spiel als Spieler B bei. Dafür wird eine UUID benötigt."
        );
        this.connectGameItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK
        ));
        this.connectGameItem.addActionListener(this);
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
            }).start();

// Show the dialog (this blocks the UI until dispose is called)
            waitingDialog.setVisible(true);
            boardPanel.startEventConsumer(gameModel.getGameId());
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
            }).start();

            waitingDialog.setVisible(true);

            boardPanel.startEventConsumer(gameModel.getGameId());
        }

    }

    private JDialog generateWaitWindow(String displayText) {
        JDialog waitingDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(boardPanel), "Waiting", true);
        waitingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        waitingDialog.add(new JLabel(displayText), java.awt.BorderLayout.CENTER);
        waitingDialog.setSize(300, 100);
        waitingDialog.setLocationRelativeTo(boardPanel);
        return waitingDialog;
    }
}

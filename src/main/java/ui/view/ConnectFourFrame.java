package ui.view;

import com.formdev.flatlaf.FlatLightLaf;
import ui.model.GameModel;
import ui.view.menu.GameMenu;
import ui.view.menu.OptionMenu;

import javax.swing.*;
import java.awt.*;

public class ConnectFourFrame {

    private final JFrame frame;

    private final BoardPanel boardPanel;

    private final ScorePanel scorePanel;

    private final GameModel model;

    public ConnectFourFrame() {

        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF: " + ex.getMessage());
        }
        // Prompt for player and client name
        String playerName = JOptionPane.showInputDialog(null, "Enter your player name:");
        if (playerName == null || playerName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Player name is required.");
            System.exit(0);
        }
        String clientName = JOptionPane.showInputDialog(null, "Enter your client name:");
        if (clientName == null || clientName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Client name is required.");
            System.exit(0);
        }

        model = new GameModel(playerName, clientName);

        frame = new JFrame("Vier Gewinnt: Linetris - Client:" + clientName + " - Player: " + playerName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardPanel = new BoardPanel(this, this.model);
        frame.add(this.boardPanel, BorderLayout.CENTER);

        scorePanel = new ScorePanel();

        JMenuBar jMenuBar = new JMenuBar();
        JMenu menu = new GameMenu(this.model, this.boardPanel);
        JMenu options = new OptionMenu();
        jMenuBar.add(menu);
        jMenuBar.add(options);
        frame.setJMenuBar(jMenuBar);

        frame.pack();
        frame.setLayout(null);
        frame.setVisible(true);

    }

}

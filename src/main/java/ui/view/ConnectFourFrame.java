package ui.view;

import com.formdev.flatlaf.FlatLightLaf;
import ui.model.GameModel;

import javax.swing.*;
import java.awt.*;

public class ConnectFourFrame {

    private final JFrame frame;

    private final MenuBar menuBar;

    private final BoardPanel boardPanel;

    private final ScorePanel scorePanel;

    private final GameModel model;

    public ConnectFourFrame() {

        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF: " + ex.getMessage());
        }
        this.model = new GameModel();

        this.frame = new JFrame("Vier Gewinnt: Linetris");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.menuBar = new MenuBar();
        this.frame.setMenuBar(this.menuBar);

        this.boardPanel = new BoardPanel(this, this.model);
        this.frame.add(this.boardPanel, BorderLayout.CENTER);

        this.scorePanel = new ScorePanel();

        this.frame.pack();
        this.frame.setLayout(null);
        this.frame.setVisible(true);

    }

}

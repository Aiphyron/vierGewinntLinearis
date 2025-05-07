package ui.view;

import com.formdev.flatlaf.FlatLightLaf;
import ui.model.GameModel;
import ui.view.menu.GameMenu;

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
        this.model = new GameModel();

        this.frame = new JFrame("Vier Gewinnt: Linetris");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar jMenuBar = new JMenuBar();
        JMenu menu = new GameMenu();
        JMenu options = new JMenu("Optionen");
        jMenuBar.add(menu);
        jMenuBar.add(options);
        this.frame.setJMenuBar(jMenuBar);

        this.boardPanel = new BoardPanel(this, this.model);
        this.frame.add(this.boardPanel, BorderLayout.CENTER);

        this.scorePanel = new ScorePanel();

        this.frame.pack();
        this.frame.setLayout(null);
        this.frame.setVisible(true);

    }

}

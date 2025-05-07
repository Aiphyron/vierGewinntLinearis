package ui.view.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.nio.charset.StandardCharsets;

public class GameMenu extends JMenu implements ActionListener {

    public JMenuItem startGameItem;

    public JMenuItem connectGameItem;

    public GameMenu() {
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
            JOptionPane.showMessageDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    "Neues Spiel als Spieler A gestartet.\nUUID: "
            );

        } else if (source == this.connectGameItem) {
            String s = (String) JOptionPane.showInputDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    "Verwende eine bekannte UUID um einem Spiel als Spieler B beizutreten.",
                    "UUID eingeben",
                    JOptionPane.PLAIN_MESSAGE
            );
            System.out.println(s);
        }

    }
}

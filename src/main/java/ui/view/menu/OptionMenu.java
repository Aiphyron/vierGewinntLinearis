package ui.view.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class OptionMenu extends JMenu implements ActionListener {
    public OptionMenu() {
        this.setText("Optionen");

        JMenuItem settingsItem = new JMenuItem("Einstellungen (Nicht implementiert)");
        settingsItem.getAccessibleContext().setAccessibleDescription(
                "Öffnet das Einstellungsmenü"
        );
        settingsItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK
        ));
        settingsItem.addActionListener(this);
        this.add(settingsItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Einstellungen sind noch nicht implementiert.");
    }
}

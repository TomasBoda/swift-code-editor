package components;

import main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Clickable extends AbstractAction
{
    private String textLink;

    public Clickable(String textLink) {
        this.textLink = textLink;
    }

    public void execute() {
        String[] values = textLink.split(":");
        int line = Integer.parseInt(values[0]);
        int position = Integer.parseInt(values[1]);

        JTextPane pane = Main.editor.editorPane;
        String code = pane.getText();
        String[] lines = code.split("\n");
        int length = lines.length;

        double ratio = Main.editor.getVerticalScrollBar().getMaximum() / length;
        int scroll = (int) ((line * ratio) - (5 * ratio));

        SwingUtilities.invokeLater(() -> {
            Main.editor.getVerticalScrollBar().setValue((scroll));
            Main.editor.colorLine(line);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        execute();
    }
}

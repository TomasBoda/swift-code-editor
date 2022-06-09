package components;

import main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;

// component used purely for clickable
// compilation errors in the console

public class Clickable extends AbstractAction
{
    private String textValue;

    public Clickable(String textValue) {
        this.textValue = textValue;
    }

    public void execute() {
        String[] values = textValue.split(":");
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

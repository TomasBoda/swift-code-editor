package gui;

import components.Clickable;
import components.StyleSet;
import main.Configuration;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Console extends JScrollPane {

    public JTextPane consolePane;

    private StyleSet normalStyleSet = new StyleSet().setFontFamily(Font.MONOSPACED).setFontSize(13).setForeground(Configuration.colorWhite).setUnderline(false);
    private StyleSet errorStyleSet = new StyleSet().setFontFamily(Font.MONOSPACED).setFontSize(13).setForeground(Configuration.colorRed).setUnderline(true);

    public Console(int width, int height) {
        consolePane = new JTextPane();
        setPreferredSize(new Dimension(width, height));
        consolePane.setMargin(new Insets(20, 20, 20, 20));
        consolePane.setBackground(new Color(30, 30, 30));
        consolePane.setForeground(new Color(230, 230, 230));
        consolePane.setEditable(false);
        consolePane.setFont(Configuration.fontConsole);
        consolePane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                Element element = consolePane.getStyledDocument().getCharacterElement(consolePane.viewToModel2D(e.getPoint()));
                Clickable clickable = (Clickable) element.getAttributes().getAttribute("clickable");
                if (clickable != null) clickable.execute();
            }
        });

        JScrollBar verticalScrollBar = getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(0, 0));
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        horizontalScrollBar.setPreferredSize(new Dimension(0, 0));

        setBorder(BorderFactory.createEmptyBorder());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(consolePane);

        getViewport().add(wrapper);
    }

    public void log(String text) {
        SwingUtilities.invokeLater(() -> {
            try {
                StyledDocument document = consolePane.getStyledDocument();
                document.insertString(document.getLength(), text, normalStyleSet);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void error(String text, int[] position) {
        SwingUtilities.invokeLater(() -> {
            try {
                StyledDocument document = consolePane.getStyledDocument();
                document.insertString(document.getLength(), text, errorStyleSet.setClickable("clickable", new Clickable(position[0] + ":" + position[1])));
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void clear() {
        consolePane.setText("");
    }
}

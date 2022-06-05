package gui;

import components.StyleSet;
import main.Configuration;
import highlighter.Highlighter;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor extends JScrollPane {

    public JTextPane lineNumbersPane;
    public JTextPane editorPane;

    public Highlighter highlighter;

    public Editor() {
        editorPane = new JTextPane();
        editorPane.setMargin(new Insets(20, 20, 20, 20));
        editorPane.setBackground(new Color(40, 40, 40));
        editorPane.setForeground(new Color(230, 230, 230));
        editorPane.setCaretColor(new Color(230, 230, 230));
        editorPane.setFont(Configuration.fontEditor);

        lineNumbersPane = new JTextPane();
        lineNumbersPane.setMargin(new Insets(20, 20, 20, 20));
        lineNumbersPane.setBackground(new Color(50, 50, 50));
        lineNumbersPane.setFont(Configuration.fontEditor);

        SimpleAttributeSet rightAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);
        lineNumbersPane.setParagraphAttributes(rightAlign, true);

        JScrollBar verticalScrollBar = getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(0, 0));
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        horizontalScrollBar.setPreferredSize(new Dimension(0, 0));
        setBorder(BorderFactory.createEmptyBorder());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(editorPane);

        getViewport().add(wrapper);
        setRowHeaderView(lineNumbersPane);

        highlighter = new Highlighter();

        editorPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                highlighter.highlight(editorPane);
                updateLineNumbering();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                highlighter.highlight(editorPane);
                updateLineNumbering();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                highlighter.highlight(editorPane);
                updateLineNumbering();
            }
        });

        editorPane.setText("import Dispatch\n" +
                "setbuf(__stdoutp, nil)\n" +
                "\n" +
                "// the above lines need to be here so that the output isn't buffered\n" +
                "// write your code here\n" +
                "\n" +
                "// run the below code to learn more about this simple Swift Code Editor\n" +
                "\n" +
                "print(\"Greetings user!\")\n" +
                "sleep(2)\n" +
                "print(\"I am a basic Swift code editor used to write single-file console scripts.\")\n" +
                "sleep(2)\n" +
                "print(\"Write some code and see with your own eyes!\")");

        updateLineNumbering();
        highlighter.highlight(editorPane);
    }

    public String getCodeContents() {
        return editorPane.getText();
    }

    public void colorLine(int lineNumber) {
        StyledDocument document = lineNumbersPane.getStyledDocument();

        StyleSet whiteSet = new StyleSet().setForeground(new Color(150, 150, 150)).setBackground(new Color(50, 50, 50));
        document.setCharacterAttributes(0, document.getLength(), whiteSet, false);

        Pattern pattern = Pattern.compile(Integer.toString(lineNumber));
        Matcher matcher = pattern.matcher(lineNumbersPane.getText());

        if (matcher.find()) {
            int offset = matcher.start();
            int length = matcher.end() - offset;

            StyleSet redSet = new StyleSet().setForeground(Color.WHITE).setBackground(Configuration.colorRed);
            document.setCharacterAttributes(offset, length, redSet, false);
        }
    }

    public void updateLineNumbering() {
        try {
            String code = editorPane.getText();

            StyleSet plainSet = new StyleSet().setFontFamily("Monospaced").setFontSize(15).setForeground(new Color(150, 150, 150));

            Document document = lineNumbersPane.getDocument();
            document.remove(0, document.getLength());
            int length = code.length() - code.replaceAll("\n", "").length() + 1;

            for (int i = 1; i <= length; i++) {
                document.insertString(document.getLength(), i + "\n", plainSet);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
package gui;

import components.StyleSet;
import main.Configuration;
import highlighter.Highlighter;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
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

        SimpleAttributeSet s = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(s, 0.35f);
        editorPane.getStyledDocument().setParagraphAttributes(0, editorPane.getStyledDocument().getLength(), s, false);

        DefaultCaret caret = new DefaultCaret() {
            @Override
            public void paint(Graphics g) {

                if (isVisible()) {
                    JTextComponent comp = getComponent();
                    if (comp == null) return;

                    Rectangle2D rectangle = null;
                    try {
                        rectangle = comp.modelToView2D(getDot());
                        if (rectangle == null) {
                            return;
                        }
                    } catch (BadLocationException e) {
                        return;
                    }

                    g.fillRect((int) rectangle.getX(), (int) rectangle.getY() + 1, 1, 16);
                }
            }
        };

        editorPane.setCaret(caret);
        editorPane.getCaret().setBlinkRate(400);

        StyleContext sc = StyleContext.getDefaultStyleContext();
        TabStop[] tabStops = new TabStop[10];
        for (int i = 0; i < 10; i++) {
            tabStops[i] = new TabStop((i + 1) * 32);
        }
        TabSet tabs = new TabSet(tabStops);
        AttributeSet paraSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabs);
        editorPane.setParagraphAttributes(paraSet, false);

        lineNumbersPane = new JTextPane();
        lineNumbersPane.setMargin(new Insets(20, 20, 20, 20));
        lineNumbersPane.setBackground(new Color(50, 50, 50));
        lineNumbersPane.setFont(Configuration.fontEditor);

        SimpleAttributeSet rightAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);
        lineNumbersPane.setParagraphAttributes(rightAlign, true);
        lineNumbersPane.getStyledDocument().setParagraphAttributes(0, lineNumbersPane.getStyledDocument().getLength(), s, false);

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
                "import Foundation\n" +
                "setbuf(__stdoutp, nil)\n" +
                "\n" +
                "let args = CommandLine.arguments\n" +
                "let index = Int(args[1]) ?? 0\n" +
                "\n" +
                "let sentences: [String] = [\n" +
                "    \"Welcome to the Swift Code Editor!\",\n" +
                "    \"Let's take a look at what you can do with it.\",\n" +
                "    \"The RUN button in the upper-left corner runs\\nthe code that is written in this editor pane.\",\n" +
                "    \"The STOP button terminates the currently\\nrunning code.\",\n" +
                "    \"The CLEAR button clears the editor pane,\\nso that you can start writing code from\\nscratch.\",\n" +
                "    \"The black input field right next to the\\nbutton determines how many times should\\nthe script be executed. It only accepts\\nwhole numbers.\",\n" +
                "    \"That's it! Have fun! Oh, one more thing.\",\n" +
                "    \"In order to show you live output of the\\nscript, the very first two lines of Swift\\ncode have to be present.\",\n" +
                "    \"They disable buffering, so the output goes\\ninstantly and directly to the console pane.\",\n" +
                "    \"Okay, time to write some code!\"\n" +
                "]\n" +
                "\n" +
                "sleep(2)\n" +
                "print(sentences[index])\n" +
                "sleep(2)");

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
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

    private JTextPane lineNumbersPane;
    public JTextPane editorPane;

    public Highlighter highlighter;

    public Editor() {
        setBorder(BorderFactory.createEmptyBorder());

        // removing scrollbars
        JScrollBar verticalScrollBar = getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(0, 0));
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        horizontalScrollBar.setPreferredSize(new Dimension(0, 0));

        // main code editor component
        editorPane = new JTextPane();
        editorPane.setMargin(new Insets(20, 20, 20, 20));
        editorPane.setBackground(new Color(40, 40, 40));
        editorPane.setForeground(new Color(230, 230, 230));
        editorPane.setCaretColor(new Color(230, 230, 230));
        editorPane.setFont(Configuration.FONT_EDITOR);

        StyledDocument editorDocument = editorPane.getStyledDocument();

        // creating default caret style - fixes visual line spacing problems
        DefaultCaret caret = new DefaultCaret() {
            @Override
            public void paint(Graphics g) {
                if (isVisible()) {
                    JTextComponent component = getComponent();
                    if (component == null) return;

                    Rectangle2D rectangle = null;
                    try {
                        rectangle = component.modelToView2D(getDot());
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

        // defining tab "\t" length
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        TabStop[] tabStops = new TabStop[10];
        for (int i = 0; i < 10; i++) tabStops[i] = new TabStop((i + 1) * 32);
        TabSet tabSet = new TabSet(tabStops);
        AttributeSet set = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabSet);
        editorPane.setParagraphAttributes(set, false);

        // adding the code syntax highlighter to the main code editor pane
        highlighter = new Highlighter();
        // handling the code syntax highlighting
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

        // line numbers component
        lineNumbersPane = new JTextPane();
        lineNumbersPane.setMargin(new Insets(20, 20, 20, 20));
        lineNumbersPane.setBackground(new Color(50, 50, 50));
        lineNumbersPane.setFont(Configuration.FONT_EDITOR);

        StyledDocument lineNumbersDocument = lineNumbersPane.getStyledDocument();

        // aligning the numbers to the right
        SimpleAttributeSet rightAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);
        lineNumbersPane.setParagraphAttributes(rightAlign, true);
        lineNumbersDocument.setParagraphAttributes(0, lineNumbersDocument.getLength(), rightAlign, false);

        // setting the code editor and line numbers pane line spacing
        StyleSet lineSpacingStyle = new StyleSet().setLineSpacing(Configuration.LINE_SPACING);
        editorDocument.setParagraphAttributes(0, editorDocument.getLength(), lineSpacingStyle, false);
        lineNumbersDocument.setParagraphAttributes(0, lineNumbersDocument.getLength(), lineSpacingStyle, false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(editorPane);
        getViewport().add(wrapper);
        setRowHeaderView(lineNumbersPane);

        editorPane.setText(Configuration.INITIAL_CODE);

        // generate line numbers and highlight initial code
        updateLineNumbering();
        highlighter.highlight(editorPane);
    }

    public String getCodeContents() {
        return editorPane.getText();
    }

    // handles line highlighting where a compilation error occurred
    public void colorLine(int lineNumber) {
        StyledDocument document = lineNumbersPane.getStyledDocument();

        StyleSet whiteSet = new StyleSet().setForeground(Configuration.COLOR_LINE_NUMBERS).setBackground(new Color(50, 50, 50));
        document.setCharacterAttributes(0, document.getLength(), whiteSet, false);

        // matches specific line number regex
        Pattern pattern = Pattern.compile(Integer.toString(lineNumber));
        Matcher matcher = pattern.matcher(lineNumbersPane.getText());

        if (matcher.find()) {
            int offset = matcher.start();
            int length = matcher.end() - offset;

            StyleSet redSet = new StyleSet().setForeground(Color.WHITE).setBackground(Configuration.COLOR_RED);
            document.setCharacterAttributes(offset, length, redSet, false);
        }
    }

    protected void updateLineNumbering() {
        try {
            String code = editorPane.getText();

            StyleSet plainSet = new StyleSet().setFontFamily("Monospaced").setFontSize(15).setForeground(Configuration.COLOR_LINE_NUMBERS);

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
package highlighter;

import components.StyleSet;
import main.Configuration;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Highlighter {

    public ArrayList<Token> tokens;

    public Highlighter() {
        tokens = new ArrayList<Token>();

        Token keywordToken = new Token(Token.TYPE.KEYWORD, "\\b(" + String.join("|", Configuration.KEYWORDS) + ")\\b", new StyleSet().setForeground(Configuration.COLOR_KEYWORD).setBold(true));
        Token numberToken = new Token(Token.TYPE.NUMBER, "(([\\+\\-]*\\d*\\.*\\d+[eE])?([\\+\\-]*\\d*\\.*\\d+))", new StyleSet().setForeground(Configuration.COLOR_NUMBER).setBold(false));
        Token stringToken = new Token(Token.TYPE.STRING, "([\"'])(?:(?=(\\\\?))\\2.)*?\\1", new StyleSet().setForeground(Configuration.COLOR_STRING).setBold(false));

        tokens.add(keywordToken);
        tokens.add(numberToken);
        tokens.add(stringToken);
    }

    public void highlight(JTextPane pane) {
        StyledDocument document = pane.getStyledDocument();

        StyleSet whiteSet = new StyleSet().setForeground(Configuration.COLOR_WHITE).setBold(false);
        document.setCharacterAttributes(0, document.getLength(), whiteSet, false);

        for (Token token : tokens) {
            String regex = token.regex;
            StyleSet style = token.style;

            // find tokens based on regex
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(pane.getText());

            while (matcher.find()) {
                int offset = matcher.start();
                int length = matcher.end() - offset;

                document.setCharacterAttributes(offset, length, style, false);
            }
        }
    }
}

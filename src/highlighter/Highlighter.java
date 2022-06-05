package highlighter;

import components.StyleSet;
import main.Configuration;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Highlighter {

    private String[] keywords = { "class", "func", "let", "public", "typealias", "deinit", "import", "operator",
        "static", "var", "enum", "init", "private", "struct", "extension", "internal",
        "protocol", "subscript", "break", "do", "if", "where", "case", "else", "in",
        "while", "continue", "fallthrough", "return", "default", "for", "switch",
        "as", "nil", "true", "dynamicType", "self", "false", "is", "super", "associativity",
        "final", "lazy", "nonmutating", "precedence", "right", "weak", "convenience",
        "get", "left", "optional", "prefix", "set", "willSet", "dynamic", "infix",
        "mutating", "override", "protocol", "type", "didSet", "inout", "none", "postfix",
        "required", "unowned", "print" };

    public Token[] tokens = new Token[3];

    public Highlighter() {
        Token stringToken = new Token(Token.TYPE.STRING, "([\"'])(?:(?=(\\\\?))\\2.)*?\\1", new StyleSet().setForeground(Configuration.colorString).setBold(false));
        Token keywordToken = new Token(Token.TYPE.KEYWORD, "\\b(" + String.join("|", keywords) + ")\\b", new StyleSet().setForeground(Configuration.colorKeyword).setBold(true));
        Token numberToken = new Token(Token.TYPE.NUMBER, "(([\\+\\-]*\\d*\\.*\\d+[eE])?([\\+\\-]*\\d*\\.*\\d+))", new StyleSet().setForeground(Configuration.colorNumber).setBold(false));

        tokens[0] = keywordToken;
        tokens[1] = numberToken;
        tokens[2] = stringToken;
    }

    public void highlight(JTextPane pane) {
        StyledDocument document = pane.getStyledDocument();

        StyleSet whiteSet = new StyleSet().setForeground(Configuration.colorWhite).setBold(false);
        document.setCharacterAttributes(0, document.getLength(), whiteSet, false);

        for (Token token : tokens) {
            String regex = token.regex;
            StyleSet style = token.style;

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

package highlighter;

import components.StyleSet;

public class Token {

    public String regex;
    public StyleSet style;

    public enum TYPE { KEYWORD, NUMBER, STRING };
    public TYPE type;

    public Token(TYPE type, String regex, StyleSet style) {
        this.type = type;
        this.regex = regex;
        this.style = style;
    }
}

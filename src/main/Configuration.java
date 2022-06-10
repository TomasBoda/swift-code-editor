package main;

import java.awt.*;

public class Configuration {

    public static final String TITLE = "Swift Code Editor";

    public static final String OUTPUT_FILENAME = "foo.swift";

    public static final int CONSOLE_WIDTH = 400;
    public static final int CONSOLE_HEIGHT = 450;

    public static Color COLOR_WHITE = new Color(230, 230, 230);
    public static Color COLOR_ORANGE = new Color(255, 149, 0);
    public static Color COLOR_RED = new Color(255, 59, 48);
    public static Color COLOR_GREEN = new Color(40, 205, 65);
    
    public static Color COLOR_KEYWORD = new Color(250, 112, 106);
    public static Color COLOR_STRING = new Color(157, 208, 253);
    public static Color COLOR_NUMBER = new Color(108, 177, 243);
    
    public static Color COLOR_TOOLBAR = new Color(50, 50, 50);
    public static Color COLOR_BUTTON_IDLE = new Color(70, 70, 70);
    public static Color COLOR_BUTTON_HOVER = new Color(60, 60, 60);
    public static Color COLOR_LINE_NUMBERS = new Color(150, 150, 150);

    public static Font FONT_EDITOR = new Font(Font.MONOSPACED, Font.PLAIN, 15);
    public static Font FONT_CONSOLE = new Font(Font.MONOSPACED, Font.PLAIN, 13);
    public static Font FONT_BUTTON = new Font(Font.MONOSPACED, Font.PLAIN, 14);

    public static final float LINE_SPACING = 0.35f;

    public static final String[] KEYWORDS = { "class", "func", "let", "public", "typealias", "deinit", "import", "operator",
            "static", "var", "enum", "init", "private", "struct", "extension", "internal",
            "protocol", "subscript", "break", "do", "if", "where", "case", "else", "in",
            "while", "continue", "fallthrough", "return", "default", "for", "switch",
            "as", "nil", "true", "dynamicType", "self", "false", "is", "super", "associativity",
            "final", "lazy", "nonmutating", "precedence", "right", "weak", "convenience",
            "get", "left", "optional", "prefix", "set", "willSet", "dynamic", "infix",
            "mutating", "override", "protocol", "type", "didSet", "inout", "none", "postfix",
            "required", "unowned", "print" };
    public static final String INITIAL_CODE = "import Dispatch\n" +
            "import Foundation\n" +
            "setbuf(__stdoutp, nil)\n" +
            "\n" +
            "let args = CommandLine.arguments\n" +
            "let index = Int(args[1]) ?? 0\n" +
            "\n" +
            "let sentences: [String] = [\n" +
            "    \"Welcome to the Swift Code Editor!\",\n" +
            "    \"Let's take a look at what you can do with it.\",\n" +
            "    \"The RUN button in the upper-left corner runs\\nthe code that is written in the editor pane.\",\n" +
            "    \"The STOP button terminates the currently\\nrunning code.\",\n" +
            "    \"The CLEAR button clears the editor pane,\\nso that you can start writing code from\\nscratch.\",\n" +
            "    \"The black input field right next to the\\nbutton determines how many times the\\nscript should be executed. It only accepts\\nwhole numbers.\",\n" +
            "    \"One more thing...\",\n" +
            "    \"In order to show the live output of the\\nscript, the first three lines of Swift\\ncode have to be present.\",\n" +
            "    \"They disable buffering, so the output goes\\ninstantly and directly to the console pane.\",\n" +
            "    \"Okay, let's write some code!\"\n" +
            "]\n" +
            "\n" +
            "sleep(1)\n" +
            "print(sentences[index])\n" +
            "sleep(1)";
}
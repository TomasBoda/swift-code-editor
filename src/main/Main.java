package main;

import builder.Builder;
import gui.Window;
import gui.Editor;
import gui.Console;
import gui.Toolbar;

import java.awt.*;

public class Main {

    public static Window window;
    public static Editor editor;
    public static Console console;
    public static Toolbar toolbar;

    public static Builder builder;

    public Main() {
        builder = new Builder();
        initComponents();
    }

    public static void run() {
        console.clear();
        console.log("Building...\n");

        String code = editor.getCodeContents();
        builder.generateCodeFile(code);
        console.log("Code successfully built, running...");
        console.log("\n\n");
        console.log("Output\n--------------------------\n");

        Thread thread = new Thread(builder);
        thread.start();
    }

    public static void stop() {
        builder.getCurrentProcess().destroy();
        console.log("\n\nProcess terminated manually...");
        toolbar.setStatus("Terminated", new Color(255, 149, 0));
    }

    private void initComponents() {
        editor = new Editor();
        console = new Console(400, 450);
        toolbar = new Toolbar();

        window = new Window();
        window.add(editor, BorderLayout.CENTER);
        window.add(console, BorderLayout.EAST);
        window.add(toolbar, BorderLayout.NORTH);
        window.init();
    }

    public static void main(String[] args) {
        new Main();
    }
}

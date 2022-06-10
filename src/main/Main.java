/*
 * Swift Code Editor desktop
 * application for creating
 * and running Swift scripts
 *
 * @author  Tomas Boda
 * @version 1.0
 * @since   2022-06-10
 */

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
    private static Thread thread;

    public Main() {
        builder = new Builder();
        initComponents();
    }

    public static void run() {
        console.clear();
        console.log("Building...\n");

        String code = editor.getCodeContents();
        builder.generateOutputFile(code);
        console.log("Code successfully built, running...\n");
        console.log("\n\n");

        int batchCount = toolbar.getBatchCount();
        toolbar.progressBar.setMax(batchCount);
        toolbar.progressBar.setValue(0);

        builder.setBatchCount(batchCount);

        thread = new Thread(builder);
        thread.start();
    }

    public static void stop() {
        thread.stop();
        builder.getCurrentProcess().destroy();
        console.log("\n\nProcess terminated manually...\n\n");
        toolbar.setStatus("Terminated", Configuration.COLOR_ORANGE);
    }

    private void initComponents() {
        editor = new Editor();
        console = new Console(Configuration.CONSOLE_WIDTH, Configuration.CONSOLE_HEIGHT);
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

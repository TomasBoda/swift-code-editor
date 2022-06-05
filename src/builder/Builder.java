package builder;

import main.Configuration;
import main.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Builder implements Runnable {

    private final String filename = "foo";

    private Process process;

    public void run() {
        try {
            process = new ProcessBuilder("/usr/bin/env", "swift", "foo.swift").start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Main.toolbar.setStatus("Running...", Configuration.colorOrange);

        try (InputStreamReader isr = new InputStreamReader(process.getInputStream())) {
            int c;
            while ((c = isr.read()) >= 0) {
                String character = Character.toString((char) c);
                Main.console.log(character);
                System.out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (InputStreamReader isr = new InputStreamReader(process.getErrorStream())) {
            String buffer = "";
            int c;

            while ((c = isr.read()) >= 0) {
                String character = Character.toString((char) c);

                if (character.equals(" ") || character.equals("\n")) {
                    if (isError(buffer)) {
                        int[] position = getErrorCaretPosition(buffer);
                        Main.console.error(buffer, position);
                    } else {
                        Main.console.log(buffer);
                    }

                    Main.console.log(character);
                    buffer = "";
                } else {
                    buffer += character;
                }

                System.out.flush();
            }

            Main.console.log(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int exitCode = 0;
        try {
            exitCode = process.waitFor();

            if (exitCode == 0) {
                Main.toolbar.setStatus("Compiled successfully", Configuration.colorGreen);
            } else {
                Main.toolbar.setStatus("Compiled with errors", Configuration.colorRed);
            }

            Main.console.log("\nExited with code " + exitCode);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isError(String text) {
        String stripped = text.replaceAll("[^a-zA-Z0-9.:]", "");
        String[] parts = stripped.split(":");
        return (parts.length == 3 && parts[0].equals("foo.swift"));
    }

    private int[] getErrorCaretPosition(String text) {
        String[] parts = text.split(":");
        return new int[] { Integer.parseInt(parts[1]), Integer.parseInt(parts[2]) };
    }

    public Process getCurrentProcess() {
        return this.process;
    }

    public void generateCodeFile(String text) {
        try {
            File file = new File(filename + ".swift");
            file.createNewFile();

            FileWriter writer = new FileWriter(filename + ".swift");
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

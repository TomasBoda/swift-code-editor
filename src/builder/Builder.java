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
    private int batchCount = 1;

    public void run() {
        Main.toolbar.setVisible(Main.toolbar.progressBar, batchCount == 1 ? false : true);
        Main.toolbar.setVisible(Main.toolbar.timeLabel, batchCount == 1 ? false : true);

        long start;
        long elapsedTime;

        long[] times = new long[batchCount];

        for (int i = 0; i < batchCount; i++) {
            start = System.nanoTime();

            String runNumber = (i + 1) + "/" + batchCount;
            double remainingTime =  getRemainingTime(i, times) / 1000;
            String remaining = i == 0 ? "" : "(remaining " + String.format("%.2f", remainingTime) + "s)";

            Main.console.log("Output " + runNumber + " " + remaining);
            Main.console.log("\n--------------------------\n");

            Main.toolbar.setRemaining(remainingTime);

            try {
                process = new ProcessBuilder("/usr/bin/env", "swift", "foo.swift", Integer.toString(i)).start();
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

                Main.console.log("\nExited with code " + exitCode + "\n\n");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            elapsedTime = System.nanoTime() - start;
            times[i] = elapsedTime;
            Main.toolbar.progressBar.setValue(Main.toolbar.progressBar.getValue() + 1);
        }

        double totalTime = getTotalRunTime(times) / 1000;
        Main.console.log("Total run time: " + String.format("%.2f", totalTime) + "s");
        Main.toolbar.setRemaining(0);

        Main.toolbar.setVisible(Main.toolbar.progressBar, false);
        Main.toolbar.setVisible(Main.toolbar.timeLabel, false);
    }

    private double getTotalRunTime(long[] times) {
        long totalTime = 0;

        for (int i = 0; i < times.length; i++) totalTime += times[i];

        return totalTime / 1000000;
    }

    private double getRemainingTime(int runNumber, long[] times) {
        if (runNumber == 0) return 0;

        long totalTime = 0;
        int totalRuns = (runNumber * (runNumber + 1)) / 2;

        for (int i = 0; i < runNumber; i++) totalTime += (i + 1) * times[i];

        double average = totalTime / totalRuns / 1000000;
        return (batchCount * average) - (runNumber * average);
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
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

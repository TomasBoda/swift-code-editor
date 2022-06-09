package builder;

import main.Configuration;
import main.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Builder implements Runnable {

    private Process process;
    private int batchCount = 1;

    public void run() {
        // show progress bar and remaining time label on run
        Main.toolbar.setVisible(Main.toolbar.progressBar, !(batchCount == 1));
        Main.toolbar.setVisible(Main.toolbar.timeLabel, !(batchCount == 1));

        long start;
        long elapsedTime;
        long[] durations = new long[batchCount];

        int lastExitCode = 0;

        // execute the code multiple times based on batchCount variable
        for (int i = 0; i < batchCount; i++) {
            start = System.nanoTime();

            double remainingTime =  getRemainingRunTime(i, durations);
            String remaining = i == 0 ? "" : "(remaining " + String.format("%.2f", remainingTime) + "s)";

            Main.console.log("Output " + (i + 1) + "/" + batchCount + " " + remaining);
            Main.console.log("\n--------------------------\n");
            Main.toolbar.setRemainingTime(remainingTime);

            // build the process to run the script
            try {
                // pass the run number as an argument to the script
                String currentRun = Integer.toString(i);
                process = new ProcessBuilder("/usr/bin/env", "swift", "foo.swift", currentRun).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Main.toolbar.setStatus("Running...", Configuration.COLOR_ORANGE);

            // read the script output character by character
            // and print it to the console
            try (InputStreamReader isr = new InputStreamReader(process.getInputStream())) {
                int c;
                while ((c = isr.read()) >= 0) {
                    String character = Character.toString((char) c);
                    Main.console.log(character);
                    System.out.flush();
                }
            } catch (IOException e) {
                //throw new RuntimeException(e);
            }

            // read the script error output
            // and print it to the console
            try (InputStreamReader isr = new InputStreamReader(process.getErrorStream())) {
                String buffer = "";
                int c;

                while ((c = isr.read()) >= 0) {
                    String character = Character.toString((char) c);

                    // if the current word has ended
                    if (character.equals(" ") || character.equals("\n")) {
                        if (isError(buffer)) {
                            // error
                            int[] position = getErrorCaretPosition(buffer);
                            Main.console.error(buffer, position);
                        } else {
                            // normal word
                            Main.console.log(buffer);
                        }

                        Main.console.log(character);
                        buffer = "";
                    } else {
                        // append new character to the current word buffer
                        buffer += character;
                    }

                    System.out.flush();
                }

                Main.console.log(buffer);
            } catch (IOException e) {
                //throw new RuntimeException(e);
            }

            // handle exit code messages
            int exitCode;

            try {
                exitCode = process.waitFor();
                lastExitCode = exitCode;

                if (exitCode != 0)
                    break;

                Main.console.log("\nExited with code " + exitCode + "\n\n");
            } catch (InterruptedException e) {
                //throw new RuntimeException(e);
            }

            // get the current run elapsed time
            elapsedTime = System.nanoTime() - start;
            durations[i] = elapsedTime;

            // move the progress bar
            int currentValue = Main.toolbar.progressBar.getValue();
            Main.toolbar.progressBar.setValue(currentValue + 1);
        }

        if (lastExitCode == 0) {
            Main.toolbar.setStatus("Compiled successfully", Configuration.COLOR_GREEN);
        } else {
            Main.toolbar.setStatus("Compiled with errors", Configuration.COLOR_RED);
        }

        double totalTime = getTotalRunTime(durations);
        Main.console.log("Total run time: " + String.format("%.2f", totalTime) + "s");
        Main.toolbar.setRemainingTime(0);

        // hide progress bar and remaining time label on end
        Main.toolbar.setVisible(Main.toolbar.progressBar, false);
        Main.toolbar.setVisible(Main.toolbar.timeLabel, false);
    }

    private double getTotalRunTime(long[] times) {
        long totalTime = 0;

        for (int i = 0; i < times.length; i++) totalTime += times[i];

        double tempTotalTime = totalTime / 1000000;
        return tempTotalTime / 1000;
    }

    private double getRemainingRunTime(int runNumber, long[] durations) {
        if (runNumber == 0) return 0;

        // the formula (triangular number) for the divergent infinite series 1 + 2 + 3 + ...
        // since each run is weighted, we need the total number
        // of weighted runs in order to obtain the overall average
        int totalRuns = (runNumber * (runNumber + 1)) / 2;
        long totalTime = 0;

        for (int i = 0; i < runNumber; i++)
            // (i + 1) is the weight for each run - the last run has the biggest weight
            totalTime += (i + 1) * durations[i];

        double average = totalTime / totalRuns / 1000000;
        double remainingTime = ((batchCount * average) - (runNumber * average)) / 1000;

        return remainingTime;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

    // checks whether the buffer word matches an error pattern
    private boolean isError(String text) {
        String stripped = text.replaceAll("[^a-zA-Z0-9.:]", "");
        String[] parts = stripped.split(":");
        return (parts.length == 3 && parts[0].equals("foo.swift"));
    }

    // retrieves the line and character number of an error (e.g. foo.swift:10:25)
    private int[] getErrorCaretPosition(String text) {
        String[] parts = text.split(":");
        return new int[] { Integer.parseInt(parts[1]), Integer.parseInt(parts[2]) };
    }

    public Process getCurrentProcess() {
        return this.process;
    }

    public void generateOutputFile(String text) {
        try {
            File file = new File(Configuration.OUTPUT_FILENAME);
            file.createNewFile();

            FileWriter writer = new FileWriter(Configuration.OUTPUT_FILENAME);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

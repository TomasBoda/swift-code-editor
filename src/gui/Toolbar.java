package gui;

import main.Configuration;
import main.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Toolbar extends JPanel {

    private JPanel leftPanel;
    private JPanel rightPanel;

    private JButton runButton;
    private JButton stopButton;
    private JButton clearButton;
    private JTextField batchField;
    public ProgressBar progressBar;
    public JLabel timeLabel;
    private JLabel statusLabel;

    public Toolbar() {
        setLayout(new BorderLayout());
        setBackground(Configuration.COLOR_TOOLBAR);

        leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        leftPanel.setBackground(Configuration.COLOR_TOOLBAR);

        runButton = createButton("Run");
        runButton.addActionListener(e -> onRun());
        leftPanel.add(runButton);

        stopButton = createButton("Stop");
        stopButton.addActionListener(e -> onStop());
        leftPanel.add(stopButton);

        clearButton = createButton("Clear");
        clearButton.addActionListener(e -> onClear());
        leftPanel.add(clearButton);

        batchField = new JTextField("10");
        batchField.setFont(Configuration.FONT_BUTTON);
        batchField.setColumns(12);
        batchField.setOpaque(true);
        batchField.setBorder(new EmptyBorder(8, 8, 8, 8));
        batchField.setBackground(Color.BLACK);
        batchField.setForeground(Configuration.COLOR_WHITE);
        batchField.setCaretColor(Configuration.COLOR_WHITE);
        batchField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char character = e.getKeyChar();
                if (((character < '0') || (character > '9')) && (character != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        leftPanel.add(batchField);

        progressBar = new ProgressBar();
        leftPanel.add(progressBar);

        timeLabel = new JLabel("");
        timeLabel.setForeground(Configuration.COLOR_WHITE);
        timeLabel.setFont(Configuration.FONT_BUTTON);
        leftPanel.add(timeLabel);

        rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(Configuration.COLOR_TOOLBAR);
        rightPanel.setBorder(new EmptyBorder(0, 0, 0, 20));

        statusLabel = new JLabel("");
        statusLabel.setForeground(Configuration.COLOR_WHITE);
        statusLabel.setFont(Configuration.FONT_BUTTON);
        rightPanel.add(statusLabel, new GridBagConstraints());

        batchField.setVisible(true);
        progressBar.setVisible(false);
        timeLabel.setVisible(false);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private void onRun() {
        if (Main.builder.getCurrentProcess() == null || !Main.builder.getCurrentProcess().isAlive()) {
            Main.run();
        }
    }

    private void onStop() {
        if (Main.builder.getCurrentProcess() != null && Main.builder.getCurrentProcess().isAlive()) {
            Main.stop();
        }
    }

    private void onClear() {
        Main.editor.editorPane.setText("import Dispatch\n" +
                "import Foundation\n" +
                "setbuf(__stdoutp, nil)\n");
        Main.editor.highlighter.highlight(Main.editor.editorPane);
        Main.editor.updateLineNumbering();
    }

    public void setStatus(String status, Color color) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
            statusLabel.setForeground(color);
        });
    }

    public void setRemainingTime(double remaining) {
        SwingUtilities.invokeLater(() -> {
            timeLabel.setText(String.format("%.2f", remaining) + "s");
        });
    }

    public int getBatchCount() {
        String text = batchField.getText();

        int count;
        try {
            count = Integer.parseInt(text);
        } catch (Exception e) {
            count = 1;
            batchField.setText("1");
        }

        return count;
    }

    public void setVisible(JComponent component, boolean visible) {
        SwingUtilities.invokeLater(() -> {
            component.setVisible(visible);
        });
    }

    private JButton createButton(String title) {
        JButton button = new JButton(title);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setForeground(Color.WHITE);
        button.setBackground(Configuration.COLOR_BUTTON_IDLE);
        button.setBorder(new EmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(Configuration.FONT_BUTTON);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Configuration.COLOR_BUTTON_HOVER);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Configuration.COLOR_BUTTON_IDLE);
            }
        });

        return button;
    }
}

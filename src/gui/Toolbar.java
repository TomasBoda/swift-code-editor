package gui;

import main.Configuration;
import main.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Toolbar extends JPanel {

    public JPanel leftPanel;
    public JPanel rightPanel;

    public JButton runButton;
    public JButton stopButton;
    public JButton clearButton;
    public JLabel statusLabel;

    public Toolbar() {
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));

        leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        leftPanel.setBackground(new Color(50, 50, 50));

        runButton = createButton("Run");
        runButton.addActionListener(e -> {
            if (Main.builder.getCurrentProcess() == null || !Main.builder.getCurrentProcess().isAlive()) {
                Main.run();
            }
        });
        leftPanel.add(runButton);

        stopButton = createButton("Stop");
        stopButton.addActionListener(e -> {
            if (Main.builder.getCurrentProcess() != null && Main.builder.getCurrentProcess().isAlive()) {
                Main.stop();
            }
        });
        leftPanel.add(stopButton);

        clearButton = createButton("Clear");
        clearButton.addActionListener(e -> {
            Main.editor.editorPane.setText("");
            Main.editor.updateLineNumbering();
        });
        leftPanel.add(clearButton);

        rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(new Color(50, 50, 50));
        rightPanel.setBorder(new EmptyBorder(0, 0, 0, 20));

        statusLabel = new JLabel("");
        statusLabel.setForeground(new Color(230, 230, 230));
        statusLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        rightPanel.add(statusLabel, new GridBagConstraints());

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    public void setStatus(String status, Color color) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
            statusLabel.setForeground(color);
        });
    }

    private JButton createButton(String title) {
        JButton button = new JButton(title);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setForeground(new Color(230, 230, 230));
        button.setBackground(new Color(70, 70, 70));
        button.setBorder(new EmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(Configuration.fontButton);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 60, 60));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 70, 70));
            }
        });

        return button;
    }
}

package gui;

import main.Configuration;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class ProgressBar extends JPanel {

    public int width = 150;
    public int height = 20;

    private int min = 0;
    private int max = 1;
    private int value = 0;
    public ProgressBar() {
        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(150, 150, 150));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int size = (int) (((double) width / (double) max) * (double) value);

        g.setColor(Configuration.COLOR_GREEN);
        g.fillRect(0, 0, size, height);

        int stroke = 2;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, stroke);
        g.fillRect(0, height - stroke, width, stroke);
        g.fillRect(0, 0, stroke, height);
        g.fillRect(width - stroke, 0, stroke, height);
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setValue(int value) {
        this.value = value;

        SwingUtilities.invokeLater(() -> {
            repaint();
        });
    }

    public int getValue() {
        return this.value;
    }
}

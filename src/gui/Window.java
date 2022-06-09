package gui;

import main.Configuration;

import javax.swing.*;
import java.awt.*;

public class Window extends JPanel {

    private JFrame frame;

    public Window() {
        frame = new JFrame(Configuration.TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setPreferredSize(new Dimension(1200, 700));
        frame.setMinimumSize(new Dimension(900, 500));

        setLayout(new BorderLayout(0, 0));
    }

    public void init() {
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

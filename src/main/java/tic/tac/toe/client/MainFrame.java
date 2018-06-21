package tic.tac.toe.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tic.tac.toe.client.board.GameTable;

import javax.swing.*;
import java.awt.*;

@Component
public class MainFrame extends JFrame {

    public static final int WIDTH = 600;

    @Autowired
    private GameTable gameTable;

    @Autowired
    private InfoPanel infoPanel;

    private String[] args;

    public MainFrame(String title) {
        super();
        setTitle(title);
        setSize(640, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(WIDTH, 680);
        setResizable(false);
        setLayout(new BorderLayout());
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public void init() {
        getContentPane().add(gameTable, BorderLayout.NORTH);
        getContentPane().add(infoPanel, BorderLayout.SOUTH);
        infoPanel.setText("Starting");
    }
}

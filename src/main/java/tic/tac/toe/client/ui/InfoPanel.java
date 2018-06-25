package tic.tac.toe.client.ui;

import tic.tac.toe.client.service.MessageListener;

import javax.swing.*;

public class InfoPanel extends JLabel implements MessageListener {

    public InfoPanel() {
    }

    public void receiveMessage(String message) {
        this.setText(message);
        validate();
        repaint();
    }
}

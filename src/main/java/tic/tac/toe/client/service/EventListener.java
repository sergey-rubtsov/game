package tic.tac.toe.client.service;

public interface EventListener {

    void drawSymbol(int x, int y, Character symbol, Character turn);

    void showWinner(Character winner);

    void startGame(Character yourSymbol, Character turn);

}

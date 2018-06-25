package tic.tac.toe.client.service;

import tic.tac.toe.server.message.CellMessage;

import java.util.List;

public interface TurnListener {

    void receiveTurn(int x, int y, Character mark);

    void highlightCells(List<CellMessage> row);
}

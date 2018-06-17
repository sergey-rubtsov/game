package tic.tac.toe.client.service;

import tic.tac.toe.model.User;

import java.util.List;

public interface EventsHandlingService {

    void addEventListeners(List<EventListener> listeners);

    User readyToPlay(String uuid);

    boolean myTurn(int x, int y);

}

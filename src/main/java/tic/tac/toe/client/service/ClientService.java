package tic.tac.toe.client.service;

import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.util.concurrent.ExecutionException;

public interface ClientService extends StompSessionHandler {

    public void sendTurn(int x, int y);

    public void initGameSession(TurnListener turnListener, MessageListener messageListener)
            throws ExecutionException, InterruptedException;

}

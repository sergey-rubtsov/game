package tic.tac.toe.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import tic.tac.toe.server.message.EndGameMessage;
import tic.tac.toe.server.message.StartGameMessage;
import tic.tac.toe.server.message.TurnMessage;
import tic.tac.toe.server.model.GameType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientServiceImpl extends StompSessionHandlerAdapter implements ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    private String url;

    private String uuid;

    private StartGameMessage status;

    private StompSession session;

    private MessageListener messageListener;

    private TurnListener turnListener;

    public ClientServiceImpl(String url, String uuid) {
        this.url = url;
        this.uuid = uuid;
    }

    public void sendTurn(int x, int y) {
        TurnMessage turn = new TurnMessage();
        turn.setX(x);
        turn.setY(y);
        this.session.send("/app/turn/" + uuid, turn);
    }

    public void initGameSession(TurnListener turnListener, MessageListener messageListener)
            throws ExecutionException, InterruptedException {
        this.turnListener = turnListener;
        this.messageListener = messageListener;
        this.messageListener.receiveMessage("Game starting");
        this.session = stompClient().connect(url, this).get();
    }

    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        //subscribe on start game events
        session.subscribe("/topic/status/" + uuid, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return StartGameMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers,
                                    Object payload) {
                LOGGER.info("got message {}", payload);
                status = (StartGameMessage) payload;
                if (status.getGameType() == GameType.NEW) {
                    messageListener.receiveMessage("New game created, your mark is " + status.getMark());
                } else if (status.getGameType() == GameType.ACTIVE) {
                    messageListener.receiveMessage("Game started, your mark is " + status.getMark());
                }
            }
        });
        //run game trigger
        session.send("/app/user/" + uuid, uuid);
        //subscribe on turn events
        session.subscribe("/topic/" + uuid, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return TurnMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers,
                                    Object payload) {
                LOGGER.info("got message {}", payload);
                TurnMessage turnMessage = (TurnMessage) payload;
                turnListener.receiveTurn(turnMessage.getX(), turnMessage.getY(), turnMessage.getMark());
                messageListener.receiveMessage("Next turn " + turnMessage.getNext());
            }
        });
        //end game handling
        session.subscribe("/topic/end" + uuid, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return EndGameMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers,
                                    Object payload) {
                LOGGER.info("got message {}", payload);
                EndGameMessage endGameMessage = (EndGameMessage) payload;
                if (endGameMessage.getDeadHeat()) {
                    messageListener.receiveMessage("Game over, Dead Heat");

                } else {
                    messageListener.receiveMessage("Game over, winner " + endGameMessage.getWinner());
                    turnListener.highlightCells(endGameMessage.getRow());
                }
            }
        });
    }

    public void handleException(StompSession session, StompCommand command, StompHeaders headers,
                                byte[] payload, Throwable exception) {
    }

    public void handleTransportError(StompSession session, Throwable exception) {
    }

    public Type getPayloadType(StompHeaders headers) {
        return null;
    }

    public void handleFrame(StompHeaders headers, Object payload) {
    }

    private WebSocketStompClient stompClient() {
        WebSocketClient simpleWebSocketClient =
                new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(simpleWebSocketClient));

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient =
                new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }

}

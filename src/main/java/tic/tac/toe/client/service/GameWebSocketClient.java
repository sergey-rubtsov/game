package tic.tac.toe.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import tic.tac.toe.client.repository.UserRepository;
import tic.tac.toe.message.ServerMessage;
import tic.tac.toe.model.User;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class GameWebSocketClient implements EventsHandlingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameWebSocketClient.class);

    private String serverUrl;

    private StompSession session;

    @Autowired
    private WebSocketStompClient stompClient;

    @Autowired
    private StompSessionHandler sessionHandler;

    @Autowired
    private UserRepository userRepository;

    public GameWebSocketClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void initSession() throws ExecutionException, InterruptedException {
        this.session = stompClient.connect(serverUrl, sessionHandler).get();
        this.session.subscribe("/topic/messages", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ServerMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers,
                                    Object payload) {
                //ServerMessage here
                LOGGER.info(payload.toString());
            }
        });
    }

    public void addEventListeners(List<EventListener> listeners) {
        LOGGER.info("add event listeners");
    }

    public User readyToPlay(String uuid) {
        //ClientMessage msg = new ClientMessage(userId, "ready to play");
        //session.send("/app/chat/java", msg);
        return userRepository.getCurrentUser();
    }

    public boolean myTurn(int x, int y) {
        //ClientMessage msg = new ClientMessage(userId, "my turn");
        //session.send("/app/chat/java", msg);
        return true;
    }
}

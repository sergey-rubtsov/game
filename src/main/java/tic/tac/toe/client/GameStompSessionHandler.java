package tic.tac.toe.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GameStompSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameStompSessionHandler.class);

    private String userId;

    private GameTable table;

    public GameStompSessionHandler(String userId, GameTable table) {
        this.userId = userId;
        this.table = table;
    }

    private void showHeaders(StompHeaders headers) {
        for (Map.Entry<String, List<String>> e : headers.entrySet()) {
            LOGGER.info("  " + e.getKey() + ": ");
            for (String v : e.getValue()) {
                LOGGER.info(v);
            }
        }
    }

    private void sendJsonMessage(StompSession session) {
        ClientMessage msg = new ClientMessage(userId,
                "hello from spring");
        session.send("/app/chat/java", msg);
    }

    private void subscribeTopic(String topic, StompSession session) {
        session.subscribe(topic, new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ServerMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers,
                                    Object payload) {
                LOGGER.info(payload.toString());
            }
        });
    }

    @Override
    public void afterConnected(StompSession session,
                               StompHeaders connectedHeaders) {
        LOGGER.info("Connected! Headers:");
        showHeaders(connectedHeaders);

        subscribeTopic("/topic/messages", session);
        sendJsonMessage(session);
    }
}

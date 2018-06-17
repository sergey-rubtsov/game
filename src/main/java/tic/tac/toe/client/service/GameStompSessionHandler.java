package tic.tac.toe.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import tic.tac.toe.client.repository.UserRepository;
import tic.tac.toe.message.ClientMessage;
import tic.tac.toe.message.ServerMessage;
import tic.tac.toe.model.User;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Component
public class GameStompSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameStompSessionHandler.class);

    private String userId;

    @Autowired
    private UserRepository userRepository;

    public GameStompSessionHandler(String userId) {
        User user = new User();
        user.setSymbol(' ');
        user.setUuid(userId);
        //userRepository.saveCurrentUser(user);
        this.userId = userId;
    }

    @Override
    public void afterConnected(StompSession session,
                               StompHeaders connectedHeaders) {
        for (Map.Entry<String, List<String>> e : connectedHeaders.entrySet()) {
            LOGGER.info("  " + e.getKey() + ": ");
            for (String v : e.getValue()) {
                LOGGER.info(v);
            }
        }
/*        session.subscribe("/topic/messages", new StompFrameHandler() {

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
        });*/
/*        ClientMessage msg = new ClientMessage(userId,
                "hello from spring");
        session.send("/app/chat/java", msg);*/
    }
}

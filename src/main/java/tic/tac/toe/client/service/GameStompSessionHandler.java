package tic.tac.toe.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import tic.tac.toe.client.InfoPanel;
import tic.tac.toe.client.board.GameTable;
import tic.tac.toe.client.repository.UserRepository;
import tic.tac.toe.server.message.ClientMessage;
import tic.tac.toe.server.message.ClientStatusMessage;
import tic.tac.toe.server.message.GameStatusMessage;
import tic.tac.toe.server.message.ServerMessage;
import tic.tac.toe.server.message.TurnMessage;
import tic.tac.toe.server.message.UserMessage;
import tic.tac.toe.server.model.Game;
import tic.tac.toe.server.model.GameType;
import tic.tac.toe.server.model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class GameStompSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameStompSessionHandler.class);

    public static String gameUuid;

    public static GameType gameType;

    private boolean subsrcibed = false;

    public static String next;

    public static Character mark;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameTable gameTable;

    @Autowired
    private InfoPanel infoPanel;

    @Autowired
    private StompSession session;

    public GameStompSessionHandler() {
    }

    @Override
    public void afterConnected(StompSession session,
                               StompHeaders connectedHeaders) {
        for (Map.Entry<String, List<String>> e : connectedHeaders.entrySet()) {
            for (String v : e.getValue()) {
                LOGGER.info(v);
            }
        }
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setSymbol(' ');
        user.setUuid(uuid.toString());
        userRepository.saveCurrentUser(user);
        session.subscribe("/topic/status", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return GameStatusMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers,
                                    Object payload) {
                LOGGER.error("got message {}", payload);
                GameStatusMessage status = (GameStatusMessage) payload;
                gameUuid = status.getUuid();
                gameType = status.getGameType();
                List<UserMessage> userMessages = status.getUsers();
                Optional<UserMessage> me = userMessages.stream().filter(user -> user.getUuid().equals(userRepository.getCurrentUser().getUuid()))
                        .findFirst();
                me.ifPresent(userMessage -> userRepository.getCurrentUser().setSymbol(userMessage.getMark()));

                User user = userRepository.getCurrentUser();
                Game game = new Game();
                user.setGame(game);
                if (!subsrcibed) {
                    session.subscribe("/topic/" + gameUuid, new StompFrameHandler() {
                        @Override
                        public Type getPayloadType(StompHeaders headers) {
                            return TurnMessage.class;
                        }

                        @Override
                        public void handleFrame(StompHeaders headers,
                                                Object payload) {
                            LOGGER.error("got message {}", payload);
                            TurnMessage turnMessage = (TurnMessage) payload;
                            gameTable.drawMark(turnMessage.getX(), turnMessage.getY(), turnMessage.getMark());
                            next = turnMessage.getNext();
                        }
                    });
                    subsrcibed = true;
                }
            }
        });
        ClientStatusMessage msg = new ClientStatusMessage(userRepository.getCurrentUser().getUuid());
        session.send("/app/user/" + userRepository.getCurrentUser().getUuid(), msg);



    }
}

package tic.tac.toe.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import tic.tac.toe.server.message.ClientMessage;
import tic.tac.toe.server.message.ClientStatusMessage;
import tic.tac.toe.server.message.GameStatusMessage;
import tic.tac.toe.server.message.ServerMessage;
import tic.tac.toe.server.message.TurnMessage;
import tic.tac.toe.server.message.UserMessage;
import tic.tac.toe.server.model.Game;
import tic.tac.toe.server.model.GameType;
import tic.tac.toe.server.model.Turn;
import tic.tac.toe.server.model.User;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameService gameService;

    @MessageMapping("/chat/{topic}")
    @SendTo("/topic/messages")
    public ServerMessage send(@DestinationVariable("topic") String topic,
                              ClientMessage message) {
        LOGGER.info("got request from " + message.getFrom() + " " + message.getText());
        return new ServerMessage(message.getFrom(), message.getText(), topic);
    }

    @MessageMapping("/user/{uuid}")
    @SendTo("/topic/status")
    public GameStatusMessage status(@DestinationVariable("uuid") String uuid,
                                    ClientStatusMessage message) {
        LOGGER.info("got user request " + uuid + " " + message);
        Game game = gameService.processUserStatusRequest(uuid);
        List<User> users = game.getUsers();
        List<UserMessage> userMessages = new ArrayList<>();
        users.forEach(user -> userMessages.add(new UserMessage(user.getUuid(), user.getSymbol())));
        return new GameStatusMessage(game.getUuid(), GameType.NEW, userMessages);
    }


    @MessageMapping("/game/{gameUuid}")
    @SendTo("/topic/{gameUuid}")
    public TurnMessage turn(@DestinationVariable String gameUuid,
                            TurnMessage userTurn) {
        LOGGER.info("got turn request " + gameUuid + " " + userTurn);
        Turn turn = gameService.processUserTurn(gameUuid, userTurn.getX(), userTurn.getY(), userTurn.getMark());
        TurnMessage turnMessage = new TurnMessage();
        turnMessage.setX(turn.getX());
        turnMessage.setY(turn.getY());
        turnMessage.setNext(turn.getNext());
        turnMessage.setMark(turn.getMark());
        return turnMessage;
    }


}

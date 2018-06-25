package tic.tac.toe.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import tic.tac.toe.server.message.CellMessage;
import tic.tac.toe.server.message.StartGameMessage;
import tic.tac.toe.server.model.Game;
import tic.tac.toe.server.model.User;
import tic.tac.toe.server.service.GameServiceImpl;

@Controller
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameServiceImpl gameService;

    @MessageMapping("/user/{uuid}")
    @SendTo("/topic/status/{uuid}")
    public StartGameMessage status(@DestinationVariable String uuid) {
        LOGGER.info("got game status request " + uuid);
        Game game = gameService.processUserStatusRequest(uuid);
        User user = gameService.findUserByUuid(uuid);
        return new StartGameMessage(game.getUuid(), game.getGameType(), user.getMark());
    }


    @MessageMapping("/turn/{uuid}")
    public void turn(@DestinationVariable("uuid") String uuid,
                            CellMessage userTurn) {
        LOGGER.info("got turn request " + uuid + " " + userTurn);
        gameService.processUserTurn(uuid, userTurn.getX(), userTurn.getY());
    }


}

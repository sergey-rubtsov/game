package tic.tac.toe.server.service;

import tic.tac.toe.server.model.Game;
import tic.tac.toe.server.model.User;

import javax.validation.constraints.NotNull;

public interface GameService {

    Game processUserStatusRequest(String uuid);

    User findUserByUuid(String uuid);

    void processUserTurn(@NotNull String uuid, int x, int y);

}

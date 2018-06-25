package tic.tac.toe.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import tic.tac.toe.server.model.GameType;

@Data
@AllArgsConstructor
public class StartGameMessage {

    private String gameUuid;

    private GameType gameType;

    private Character mark;

}

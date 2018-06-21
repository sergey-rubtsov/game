package tic.tac.toe.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import tic.tac.toe.server.model.GameType;

import java.util.List;

@Data
@AllArgsConstructor
public class GameStatusMessage {

    private String uuid;

    private GameType gameType;

    private List<UserMessage> users;

}

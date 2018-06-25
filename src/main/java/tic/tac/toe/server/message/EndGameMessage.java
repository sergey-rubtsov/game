package tic.tac.toe.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndGameMessage {

    private Boolean deadHeat;

    private Character winner;

    private List<CellMessage> row = new ArrayList<>();

}

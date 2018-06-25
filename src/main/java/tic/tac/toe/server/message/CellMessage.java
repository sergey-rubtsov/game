package tic.tac.toe.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CellMessage {

    private int x;

    private int y;

}

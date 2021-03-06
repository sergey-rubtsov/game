package tic.tac.toe.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnMessage {

    private Character next;

    private int x;

    private int y;

    private Character mark;

}

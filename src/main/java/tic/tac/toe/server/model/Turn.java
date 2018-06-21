package tic.tac.toe.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Turn {

    private String next;

    private int x;

    private int y;

    private Character mark;

}

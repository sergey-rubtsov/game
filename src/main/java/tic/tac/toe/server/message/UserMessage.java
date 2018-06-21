package tic.tac.toe.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserMessage {

    private String uuid;

    private Character mark;

}

package tic.tac.toe.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientStatusMessage {

    private String uuid;

}

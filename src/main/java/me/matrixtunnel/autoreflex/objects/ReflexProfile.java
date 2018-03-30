package me.matrixtunnel.autoreflex.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by MatrixTunnel on 3/12/2018.
 */
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ReflexProfile {

    private UUID uuid;
    private long firstDetectedTime;

    private int kickPoints;
    private String lastKickId, lastCheat;
    //TODO Add list of all cheat types they were caught for

    public void addKickPoint() {
        kickPoints += 1;
    }
}

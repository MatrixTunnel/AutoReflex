package me.matrixtunnel.autoreflex.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by MatrixTunnel on 3/12/2018.
 */
@Getter
public class ReflexAutoBanEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private String lastKickId, lastCheat;

    @Setter private boolean cancelled;

    public ReflexAutoBanEvent(final Player player, final String lastKickId, final String lastCheat) {
        super(false);

        this.player = player;
        this.lastKickId = lastKickId;
        this.lastCheat = lastCheat;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

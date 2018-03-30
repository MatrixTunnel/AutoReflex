package me.matrixtunnel.autoreflex.reflex;

import me.matrixtunnel.autoreflex.AutoReflex;
import me.matrixtunnel.autoreflex.events.ReflexAutoBanEvent;
import me.matrixtunnel.autoreflex.objects.ReflexProfile;
import me.matrixtunnel.autoreflex.utils.Project;
import me.matrixtunnel.autoreflex.utils.StringProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rip.reflex.TA;
import rip.reflex.api.ReflexAPIProvider;
import rip.reflex.api.event.ReflexCheckEvent;
import rip.reflex.api.event.ReflexLoadEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by MatrixTunnel on 3/12/2018.
 */
public class ReflexHandler implements Listener, Project {

    private int maxKickPoints = config.getMinimumKickPoints(); // 3
    private long resetDataInterval = config.getResetDataInterval(); // 8400 = 7 Minutes
    private long resetDataTaskInterval = config.getResetDataTaskInterval(); // 20 = 1 Second

    private Collection<Integer> taskIds = new ArrayList<>();

    private boolean enabled = false;

    public ReflexHandler() {
        plugin.registerEvents(this);

        taskIds.add(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> { // Clears kick points data
            if (profileManager.getReflexProfiles().isEmpty()) return; // Micro optimization :bloblul:

            List<UUID> removeProfiles = new ArrayList<>();

            profileManager.getReflexProfiles().forEach((uuid, profile) -> {
                if (System.currentTimeMillis() - profile.getFirstDetectedTime() > resetDataInterval)
                    removeProfiles.add(uuid);
            });

            removeProfiles.forEach(uuid -> {
                profileManager.removeReflexProfile(uuid);

                if (enabled) AutoReflex.get().getReflexAPI().log(TA.DEBUG, "[AutoReflex] Reset check data for player " + uuid.toString());
            });
            removeProfiles.clear();
        }, resetDataTaskInterval, resetDataTaskInterval).getTaskId());
    }

    public void close() {
        enabled = false;

        taskIds.forEach(id -> Bukkit.getScheduler().cancelTask(id));
        taskIds.clear();
    }

    @EventHandler
    public void onLoad(ReflexLoadEvent event) {
        plugin.setReflexAPI(ReflexAPIProvider.getAPI());
        enabled = true;

        plugin.getLogger().info("Hooked into Reflex successfully");
    }

    @EventHandler
    public void onCheck(ReflexCheckEvent event) {
        if (!enabled || event.isCancelled()) return;

        if (event.getResult().isCheckFailed()) {
            ReflexProfile profile = profileManager.getReflexProfile(event.getPlayer());

            profile.setLastKickId(event.getViolationId());
            profile.setLastCheat(event.getCheat().name());
        }
    }

    public void trigger(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (!enabled || player == null || !player.isOnline()) return;

        ReflexProfile profile = profileManager.getReflexProfile(player);
        profile.addKickPoint();

        if (config.isAutoBanEventEnabled()) {
            ReflexAutoBanEvent autoBanEvent = new ReflexAutoBanEvent(player, profile.getLastKickId(), profile.getLastCheat());
            Bukkit.getPluginManager().callEvent(autoBanEvent);

            if (autoBanEvent.isCancelled()) return;
        }

        // This looks weird but it gets the job done
        Bukkit.getScheduler().runTask(plugin, () -> { // Run commands sync
            if (config.isTriggerCommandsEnabled())
                config.getTriggerCommands().forEach(command ->
                        handleCommand(profile, command));

            if (profile.getKickPoints() >= maxKickPoints) {
                if (config.isBeforeCommandsEnabled())
                    config.getBeforeCommands().forEach(command ->
                            handleCommand(profile, command));

                if (config.isCheatCommandsEnabled())
                    config.getConfig().getStringList("commands." + profile.getLastCheat().toLowerCase()).forEach(command ->
                            handleCommand(profile, command));

                if (config.isAfterCommandsEnabled())
                    config.getAfterCommands().forEach(command ->
                            handleCommand(profile, command));
            }
        });
    }

    private void handleCommand(ReflexProfile profile, String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.toLowerCase().startsWith("reflex delay") ? // Ignore formatting and let Reflex handle it :)
                command : StringProcessor.commandFilter(profile, command));
    }

}

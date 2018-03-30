package me.matrixtunnel.autoreflex;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * Created by MatrixTunnel on 3/14/2018.
 */
@Getter
public final class Config {

    private final FileConfiguration config;

    private final boolean paperOptimizationEnabled;

    private final int minimumKickPoints;
    private final int resetDataInterval;
    private final long resetDataTaskInterval;
    private final boolean autoBanEventEnabled;

    private final boolean triggerCommandsEnabled;
    private final boolean beforeCommandsEnabled;
    private final boolean afterCommandsEnabled;
    private final boolean cheatCommandsEnabled;

    private final List<String> triggerCommands;
    private final List<String> beforeCommands;
    private final List<String> afterCommands;

    Config() {
        AutoReflex.get().saveDefaultConfig();
        AutoReflex.get().reloadConfig();

        config = AutoReflex.get().getConfig();

        paperOptimizationEnabled = config.getBoolean("settings.paper_optimizations", false);

        minimumKickPoints = config.getInt("settings.min_kick_points", 3);
        resetDataInterval = config.getInt("settings.reset_data_interval", 8400);
        resetDataTaskInterval = config.getLong("settings.reset_data_task_interval", 20);
        autoBanEventEnabled = config.getBoolean("settings.enable_autoban_event", true);

        triggerCommandsEnabled = config.getBoolean("commands.enable_trigger_commands", true);
        beforeCommandsEnabled = config.getBoolean("commands.enable_global_before_commands", true);
        afterCommandsEnabled = config.getBoolean("commands.enable_global_after_commands", true);
        cheatCommandsEnabled = config.getBoolean("commands.enable_cheat_commands", true);

        triggerCommands = config.getStringList("commands.trigger");
        beforeCommands = config.getStringList("commands.global_before");
        afterCommands = config.getStringList("commands.global_after");
    }

}

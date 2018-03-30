package me.matrixtunnel.autoreflex;

import lombok.Getter;
import lombok.Setter;
import me.matrixtunnel.autoreflex.commands.AutoReflexCommand;
import me.matrixtunnel.autoreflex.reflex.ReflexHandler;
import me.matrixtunnel.autoreflex.utils.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import rip.reflex.api.ReflexAPI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class AutoReflex extends JavaPlugin {

    private static AutoReflex instance;

    private Config pluginConfig;
    private ProfileManager profileManager;
    private ReflexHandler reflexHandler;

    @Setter private ReflexAPI reflexAPI;

    private List<Command> commands = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        init();
    }

    private void init() {
        pluginConfig = new Config();

        profileManager = new ProfileManager();
        reflexHandler = new ReflexHandler();

        registerCommands();
    }

    public void reload() {
        onDisable();

        init();
    }

    @Override
    public void onDisable() {
        commands.clear();

        profileManager.clear();
        reflexHandler.close();
    }

    private void registerCommands() {
        commands.add(new AutoReflexCommand());

        if (pluginConfig.isPaperOptimizationEnabled()) {
            Bukkit.getServer().getCommandMap().registerAll(getName().toLowerCase(), commands);
        } else {
            try {
                Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
                commandMap.registerAll(getName().toLowerCase(), commands);
                commandMapField.setAccessible(false);
            } catch (Exception ignored) {}
        }
    }

    public void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
    }

    public void unregisterEvents(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public static AutoReflex get() {
        return instance;
    }

}

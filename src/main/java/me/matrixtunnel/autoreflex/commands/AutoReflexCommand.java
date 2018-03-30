package me.matrixtunnel.autoreflex.commands;

import me.matrixtunnel.autoreflex.AutoReflex;
import me.matrixtunnel.autoreflex.utils.Project;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.reflex.api.Cheat;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by MatrixTunnel on 3/12/2018.
 */
public class AutoReflexCommand extends Command implements Project {

    public AutoReflexCommand() {
        super("autoreflex", "Command used to trigger the auto ban system", ChatColor.RED + "/autoreflex <help|version|trigger|reset|reload>", Collections.singletonList("ar"));
        setPermissionMessage("No perms! You need '<permission>' to use that command :o");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(usageMessage);
            return false;
        } else if (!hasPermission(sender, args[0].toLowerCase())) { //TODO Work on this
            testPermission(sender);
            //sender.spigot().sendMessage(new ComponentBuilder("No perms! You need '<permission>' to use that command :o").color(ChatColor.RED).create());
            return true;
        }

        if (args.length == 2) {
            String name = args[1];
            Player player = Bukkit.getPlayer(name);

            switch (args[0].toLowerCase()) {
                case "trigger":
                    if (player == null || !player.isOnline()) {
                        sendFailMessage(sender, "Failed to trigger for player '" + name + "'");
                        return true;
                    }

                    AutoReflex.get().getReflexHandler().trigger(player.getUniqueId());
                    return true;
                case "reset":
                    if (player == null || !player.isOnline()) {
                        sendFailMessage(sender, "Failed to reset player '" + name + "'");
                        return true;
                    }

                    profileManager.removeReflexProfile(player.getUniqueId());
                    return true;
                case "softreset":
                    if (player == null || !player.isOnline()) {
                        sendFailMessage(sender, "Failed to soft-reset player '" + name + "'");
                        return true;
                    }

                    Arrays.stream(Cheat.values()).forEach(cheat -> AutoReflex.get().getReflexAPI().setViolations(player, cheat, 0));
                    return true;
                case "reload":
                    sender.sendMessage(ChatColor.DARK_AQUA + "Reloading...");
                    AutoReflex.get().reload();
                    sender.sendMessage(ChatColor.GREEN + "Plugin successfully reloaded!");
                    return true;
            }
        }

        sender.sendMessage(usageMessage);
        return false;
    }

    private boolean hasPermission(CommandSender sender, String subCommand) {
        return subCommand.matches("help|version") || sender.isOp() || sender.hasPermission("autoreflex.command." + subCommand);
    }

    private void sendFailMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.spigot().sendMessage(new ComponentBuilder(message).color(ChatColor.RED).create());
        }

        plugin.getLogger().warning(message); // Always log the warning
    }

}

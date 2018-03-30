package me.matrixtunnel.autoreflex.utils;

import me.matrixtunnel.autoreflex.objects.ReflexProfile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

/**
 * Created by MatrixTunnel on 3/12/2018.
 */
public class StringProcessor implements Project {

    public static String commandFilter(ReflexProfile profile, String message) { //TODO Switch to player and get profile
        return ChatColor.translateAlternateColorCodes('&', message)
                .replace("%break%", "\n")
                .replace("%player%", Bukkit.getPlayer(profile.getUuid()) == null ? "null" : Bukkit.getPlayer(profile.getUuid()).getName())
                .replace("%kick-points%", String.valueOf(profile.getKickPoints()))
                .replace("%id%", profile.getLastKickId())
                .replace("%clean-id%", profile.getLastKickId()).replace("#", "")
                .replace("%cheat%", profile.getLastCheat())

                .replace("%max-kick-points%", String.valueOf(config.getMinimumKickPoints()));
    }

}

package me.matrixtunnel.autoreflex.utils;

import lombok.Getter;
import me.matrixtunnel.autoreflex.objects.ReflexProfile;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by MatrixTunnel on 3/12/2018.
 */
@Getter
public class ProfileManager {

    private static final Map<UUID, ReflexProfile> profiles = new HashMap<>();

    public Map<UUID, ReflexProfile> getReflexProfiles() {
        return profiles;
    }

    public ReflexProfile getReflexProfile(Player player) {
        if (player == null) return null;

        return getReflexProfile(player.getUniqueId());
    }

    public ReflexProfile getReflexProfile(UUID uuid) {
        return profiles.getOrDefault(uuid, profiles.putIfAbsent(uuid, new ReflexProfile(uuid, System.currentTimeMillis(), 0, null, null)));
    }

    public void removeReflexProfile(ReflexProfile profile) {
        profiles.remove(profile.getUuid());
    }

    public void removeReflexProfile(UUID uuid) {
        profiles.remove(uuid);
    }

    public void clear() {
        profiles.clear();
    }

}

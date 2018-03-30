package me.matrixtunnel.autoreflex.utils;

import me.matrixtunnel.autoreflex.AutoReflex;
import me.matrixtunnel.autoreflex.Config;
import me.matrixtunnel.autoreflex.reflex.ReflexHandler;
import rip.reflex.api.ReflexAPI;

/**
 * Created by MatrixTunnel on 3/14/2018.
 */
public interface Project {

    AutoReflex plugin = AutoReflex.get();

    Config config = plugin.getPluginConfig();
    ProfileManager profileManager = plugin.getProfileManager();
    ReflexHandler reflexHandler = plugin.getReflexHandler();
    ReflexAPI reflexAPI = plugin.getReflexAPI();

}

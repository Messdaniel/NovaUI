package me.messdaniel.novaui;

import me.messdaniel.novaui.menus.BaseMenu;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class NovaUI {

    private static Plugin PLUGIN = null;

    private NovaUI() {}

    public static void init(final @NotNull Plugin plugin) {
        PLUGIN = plugin;
    }

    public static @NotNull Plugin getPlugin() {
        if (PLUGIN == null) init(JavaPlugin.getProvidingPlugin(BaseMenu.class));
        return PLUGIN;
    }
}

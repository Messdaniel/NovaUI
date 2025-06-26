package me.messdaniel.novaui.utils;

import com.google.common.primitives.Ints;
import me.messdaniel.novaui.exception.MenuException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionHelper {

    private static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    // Unbreakable change
    private static final int V1_11 = 1110;
    // Material and components on items change
    private static final int V1_13 = 1130;
    // PDC and customModelData
    private static final int V1_14 = 1140;
    // Paper adventure changes
    private static final int V1_16_5 = 1165;
    // SkullMeta#setOwningPlayer was added
    private static final int V1_12_1 = 1121;
    // PlayerProfile API
    private static final int V1_20_1 = 1201;
    private static final int V1_20_5 = 1205;

    private static final int CURRENT_VERSION = getCurrentVersion();

    public static final boolean IS_COMPONENT_LEGACY = CURRENT_VERSION < V1_16_5;

    public static final boolean IS_ITEM_LEGACY = CURRENT_VERSION < V1_13;

    public static final boolean IS_UNBREAKABLE_LEGACY = CURRENT_VERSION < V1_11;

    public static final boolean IS_PDC_VERSION = CURRENT_VERSION >= V1_14;

    public static final boolean IS_SKULL_OWNER_LEGACY = CURRENT_VERSION < V1_12_1;

    public static final boolean IS_CUSTOM_MODEL_DATA = CURRENT_VERSION >= V1_14;

    public static final boolean IS_PLAYER_PROFILE_API = CURRENT_VERSION >= V1_20_1;

    public static final boolean IS_ITEM_NAME_COMPONENT = CURRENT_VERSION >= V1_20_5;
    private static final boolean IS_PAPER = checkPaper();
    public static final boolean IS_FOLIA = checkFolia();

    private static boolean checkPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private static boolean checkFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private static int getCurrentVersion() {
        final Matcher matcher = Pattern.compile("(?<version>\\d+\\.\\d+)(?<patch>\\.\\d+)?").matcher(Bukkit.getBukkitVersion());

        final StringBuilder stringBuilder = new StringBuilder();
        if (matcher.find()) {
            stringBuilder.append(matcher.group("version").replace(".", ""));
            final String patch = matcher.group("patch");
            if (patch == null) stringBuilder.append("0");
            else stringBuilder.append(patch.replace(".", ""));
        }

        //noinspection UnstableApiUsage
        final Integer version = Ints.tryParse(stringBuilder.toString());

        // Should never fail
        if (version == null) throw new MenuException("Could not retrieve server version!");

        return version;
    }

    public static Class<?> craftClass(@NotNull final String name) throws ClassNotFoundException {
        return Class.forName(CRAFTBUKKIT_PACKAGE + "." + name);
    }
}

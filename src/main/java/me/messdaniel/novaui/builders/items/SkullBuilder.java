package me.messdaniel.novaui.builders.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

public class SkullBuilder extends BaseItemBuilder<SkullBuilder> {

    SkullBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    @NotNull
    @Contract("_ -> this")
    public SkullBuilder owner(@NotNull final OfflinePlayer owner) {
        if (getItemStack().getType() != Material.PLAYER_HEAD) return this;

        final SkullMeta skullMeta = (SkullMeta) getMeta();

        skullMeta.setOwningPlayer(owner);

        setMeta(skullMeta);

        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public SkullBuilder texture(@NotNull final String textureUrl) {
        return texture(textureUrl, UUID.randomUUID());
    }

    @NotNull
    @Contract("_ -> this")
    public SkullBuilder texture(@NotNull final String textureUrl, @NotNull final UUID uuid) {
        if (getItemStack().getType() != Material.PLAYER_HEAD) return this;

        final SkullMeta skullMeta = (SkullMeta) getMeta();
        skullMeta.setPlayerProfile(createProfile(uuid, textureUrl));
        setMeta(skullMeta);

        return this;
    }

    @NotNull
    private PlayerProfile createProfile(@NotNull final UUID uuid, @NotNull final String url) {
        PlayerProfile profile = Bukkit.createProfile(uuid);
        setSkinTexture(url,profile);

        return profile;
    }

    @NotNull
    private void setSkinTexture(@NotNull final String url, @NotNull final PlayerProfile profile) {
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(URI.create(url).toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        profile.setTextures(textures);
    }
}

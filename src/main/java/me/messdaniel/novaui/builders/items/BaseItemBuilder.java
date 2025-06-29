package me.messdaniel.novaui.builders.items;

import me.messdaniel.novaui.utils.VersionHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BaseItemBuilder<B extends BaseItemBuilder<B>> {

    private static final EnumSet<Material> LEATHER_ARMOR = EnumSet.of(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS
    );

    private ItemStack itemStack;
    private ItemMeta meta;

    public BaseItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;

        meta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
    }

    @NotNull
    @Contract("_ -> this")
    public B name(@NotNull final Component displayName) {
        if (meta == null) return (B) this;

        meta.displayName(displayName);
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B amount(final int amount) {
        itemStack.setAmount(amount);
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B lore(@NotNull final List<Component> lore) {
        meta.lore(lore);
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B lore(@NotNull final Component... lore) {
        meta.lore(Arrays.asList(lore));
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B enchant(@NotNull final Enchantment enchantment, final int level, final boolean ignoreLevelRestriction) {
        meta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B enchant(@NotNull final Enchantment enchantment, final int level) {
        return enchant(enchantment, level, true);
    }

    @NotNull
    @Contract("_ -> this")
    public B enchant(@NotNull final Enchantment enchantment) {
        return enchant(enchantment, 1, true);
    }

    @NotNull
    @Contract("_ -> this")
    public B enchant(@NotNull final Map<Enchantment, Integer> enchantments, final boolean ignoreLevelRestriction) {
        enchantments.forEach((enchantment, level) -> this.enchant(enchantment, level, ignoreLevelRestriction));
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B enchant(@NotNull final Map<Enchantment, Integer> enchantments) {
        return enchant(enchantments, true);
    }

    @NotNull
    @Contract("_ -> this")
    public B disenchant(@NotNull final Enchantment enchantment) {
        itemStack.removeEnchantment(enchantment);
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B flags(@NotNull final ItemFlag... flags) {
        meta.addItemFlags(flags);
        return (B) this;
    }


    @NotNull
    @Contract("_ -> this")
    public B unbreakable() {
        return unbreakable(true);
    }

    @NotNull
    @Contract("_ -> this")
    public B unbreakable(final boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B glow() {
        return glow(true);
    }

    @NotNull
    @Contract("_ -> this")
    public B glow(final boolean glow) {
        if (glow) {
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            return (B) this;
        }

        for (Enchantment enchantment : meta.getEnchants().keySet()) {
            meta.removeEnchant(enchantment);
        }

        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B pdc(@NotNull final Consumer<PersistentDataContainer> consumer) {
        consumer.accept(meta.getPersistentDataContainer());
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B model(@NotNull final int modelData) {
        if (VersionHelper.IS_CUSTOM_MODEL_DATA) {
            meta.setCustomModelData(modelData);
        }

        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public B color(@NotNull final Color color) {
        if (LEATHER_ARMOR.contains(itemStack.getType())) {
            final LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) getMeta();

            leatherArmorMeta.setColor(color);
            setMeta(leatherArmorMeta);
        }

        return (B) this;
    }

    @NotNull
    public ItemStack build() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    protected void setItemStack(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @NotNull
    protected ItemStack getItemStack() {
        return itemStack;
    }

    protected void setMeta(@NotNull ItemMeta meta) {
        this.meta = meta;
    }

    @NotNull
    protected ItemMeta getMeta() {
        return meta;
    }
}

package me.messdaniel.novaui.builders.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemBuilder extends BaseItemBuilder<ItemBuilder> {

    ItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public static ItemBuilder item(Material material) {
        return new ItemBuilder(new ItemStack(material));
    }

    public static ItemBuilder item(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static SkullBuilder skull() {
        return new SkullBuilder(new ItemStack(Material.PLAYER_HEAD));
    }

    public static SkullBuilder skull(ItemStack itemStack) {
        return new SkullBuilder(itemStack);
    }
}

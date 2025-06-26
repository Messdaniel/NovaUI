package me.messdaniel.novaui.itembuilder;

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
}

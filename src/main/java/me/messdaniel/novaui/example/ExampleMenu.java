package me.messdaniel.novaui.example;

import me.messdaniel.novaui.itembuilder.ItemBuilder;
import me.messdaniel.novaui.menus.Menu;
import me.messdaniel.novaui.menutypes.MenuType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExampleMenu {

    public ExampleMenu(Player player) {
        Menu menu = Menu.menu()
                .title(Component.text("Example menu"))
                .type(MenuType.CHEST)
                .rows(5)
                .build();

        ItemStack item = ItemBuilder.item(Material.DIRT)
                .name(Component.text("Example item"))
                .amount(10)
                        .glow().build();

        menu.setItem(10,item);

        menu.addCloseMenuAction(event -> {
            player.sendMessage("You closed the menu");
        });

        menu.addOpenAction(event -> {
            player.sendMessage("You opened the menu");
        });

        menu.addSlotAction(10, event -> {
            player.sendMessage("You clicked on the example item");
        });

        menu.open(player);
    }
}

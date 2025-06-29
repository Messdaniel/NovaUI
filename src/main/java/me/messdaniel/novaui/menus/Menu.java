package me.messdaniel.novaui.menus;

import me.messdaniel.novaui.builders.menu.PaginatedMenuBuilder;
import me.messdaniel.novaui.menutypes.MenuType;
import me.messdaniel.novaui.builders.menu.MenuBuilder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class Menu extends BaseMenu {

    private Component title;
    private int rows = 1;
    private MenuType type = MenuType.CHEST;

    public Menu(Component title, int rows) {
        super(title, rows);
        this.title = title;
        this.rows = rows;
    }

    public Menu(Component title, MenuType type) {
        super(title, type);
        this.title = title;
        this.type = type;
    }

    @NotNull
    public static MenuBuilder menu() {
        return new MenuBuilder();
    }

    @NotNull
    public static PaginatedMenuBuilder paginated() {
        return new PaginatedMenuBuilder();
    }
}

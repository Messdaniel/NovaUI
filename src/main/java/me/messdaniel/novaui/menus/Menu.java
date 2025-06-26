package me.messdaniel.novaui.menus;

import me.messdaniel.novaui.menutypes.MenuType;
import me.messdaniel.novaui.menubuilders.MenuBuilder;
import net.kyori.adventure.text.Component;

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

    public static MenuBuilder menu() {
        return new MenuBuilder();
    }
}

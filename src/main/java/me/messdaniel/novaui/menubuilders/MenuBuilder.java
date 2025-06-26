package me.messdaniel.novaui.menubuilders;

import me.messdaniel.novaui.menus.Menu;
import me.messdaniel.novaui.menutypes.MenuType;
import net.kyori.adventure.text.Component;

public class MenuBuilder {

    private Component title;
    private int rows = 1;
    private MenuType type;

    public MenuBuilder() {

    }

    public Component title() {
        return title;
    }

    public MenuBuilder title(Component title) {
        this.title = title;
        return this;
    }

    public int rows() {
        return rows;
    }

    public MenuBuilder rows(int rows) {
        this.rows = rows;
        return this;
    }

    public MenuType type() {
        return type;
    }

    public MenuBuilder type(MenuType type) {
        this.type = type;
        return this;
    }

    public Menu build() {
        if (type == null || type == MenuType.CHEST) {
            return new Menu(title(), rows());
        }
        return new Menu(title(), type());
    }
}

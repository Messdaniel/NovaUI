package me.messdaniel.novaui.builders.menu;

import me.messdaniel.novaui.menus.Menu;
import me.messdaniel.novaui.menutypes.MenuType;

public class MenuBuilder extends BaseMenuBuilder<MenuBuilder> {

    public Menu build() {
        if (type() == MenuType.CHEST) {
            return new Menu(title(), rows());
        }

        return new Menu(title(), type());
    }
}

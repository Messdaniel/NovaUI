package me.messdaniel.novaui.builders.menu;

import me.messdaniel.novaui.menus.PaginatedMenu;
import me.messdaniel.novaui.menutypes.MenuType;
import org.jetbrains.annotations.NotNull;

public class PaginatedMenuBuilder extends BaseMenuBuilder<PaginatedMenuBuilder> {

    private int pageSize = 0;

    @NotNull
    public PaginatedMenuBuilder pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int pageSize() {
        return pageSize;
    }

    public PaginatedMenu build() {
        if (type() == MenuType.CHEST) {
            return new PaginatedMenu(title(), rows(), pageSize());
        }

        return new PaginatedMenu(title(), type(), pageSize());
    }
}

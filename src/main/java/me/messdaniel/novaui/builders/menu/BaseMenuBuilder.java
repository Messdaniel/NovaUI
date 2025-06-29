package me.messdaniel.novaui.builders.menu;

import me.messdaniel.novaui.menutypes.MenuType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BaseMenuBuilder<B extends BaseMenuBuilder<B>> {

    private Component title;
    private int rows = 1;
    private MenuType type = MenuType.CHEST;

    public Component title() {
        return title;
    }

    @NotNull
    @Contract("_ -> this")
    public B title(Component title) {
        this.title = title;
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public int rows() {
        return rows;
    }

    @NotNull
    @Contract("_ -> this")
    public B rows(int rows) {
        this.rows = rows;
        return (B) this;
    }

    @NotNull
    @Contract("_ -> this")
    public MenuType type() {
        return type;
    }

    @NotNull
    @Contract("_ -> this")
    public B type(MenuType type) {
        this.type = type;
        return (B) this;
    }
}

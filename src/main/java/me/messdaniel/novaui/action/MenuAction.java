package me.messdaniel.novaui.action;

import org.bukkit.event.Event;

public interface MenuAction<T extends Event> {

    void execute(final T event);
}

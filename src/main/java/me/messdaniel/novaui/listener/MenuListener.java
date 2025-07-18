package me.messdaniel.novaui.listener;

import me.messdaniel.novaui.action.MenuAction;
import me.messdaniel.novaui.menus.BaseMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

import java.util.Set;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof BaseMenu menu)) return;
        if (event.getClickedInventory() == null) return;

        final Set<MenuAction<InventoryClickEvent>> clickActions = menu.getClickActions();
        if (clickActions != null) {
            clickActions.forEach(action -> action.execute(event));
        }

        final MenuAction<InventoryClickEvent> slotClickActions = menu.getSlotAction(event.getSlot());
        if (slotClickActions != null) {
            slotClickActions.execute(event);
        }
    }

    @EventHandler
    public void onMenuDrag(InventoryDragEvent event) {
        if (!(event.getInventory().getHolder() instanceof BaseMenu menu)) return;

        final Set<MenuAction<InventoryDragEvent>> actions = menu.getDragActions();
        if (actions == null || actions.isEmpty()) return;

        actions.forEach(action -> action.execute(event));
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof BaseMenu menu)) return;

        final Set<MenuAction<InventoryCloseEvent>> actions = menu.getCloseMenuActions();
        if (actions == null || actions.isEmpty() || menu.isUpdating() || !menu.shouldRunCloseAction()) return;

        actions.forEach(action -> action.execute(event));
    }

    @EventHandler
    public void onMenuOpen(InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof BaseMenu menu)) return;

        final Set<MenuAction<InventoryOpenEvent>> actions = menu.getOpenMenuActions();
        if (actions == null || actions.isEmpty() || menu.isUpdating() || !menu.shouldRunOpenAction()) return;

        actions.forEach(action -> action.execute(event));
    }
}

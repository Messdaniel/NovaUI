package me.messdaniel.novaui.menutypes;

import org.bukkit.event.inventory.InventoryType;

public enum MenuType {
    CHEST(InventoryType.CHEST, 53),
    WORKBENCH(InventoryType.WORKBENCH, 8),
    HOPPER(InventoryType.HOPPER, 4),
    DISPENSER(InventoryType.DISPENSER, 8),
    BREWING(InventoryType.BREWING, 4),
    SMITHING(InventoryType.SMITHING, 4);

    final InventoryType inventoryType;
    final int maxSize;
    MenuType(InventoryType inventoryType, int maxSize) {
        this.inventoryType = inventoryType;
        this.maxSize = maxSize;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public int getMaxSize() {
        return maxSize;
    }
}

package me.messdaniel.novaui.menus;

import me.messdaniel.novaui.NovaUI;
import me.messdaniel.novaui.action.MenuAction;
import me.messdaniel.novaui.listener.MenuListener;
import me.messdaniel.novaui.menutypes.MenuType;
import me.messdaniel.novaui.exception.MenuException;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BaseMenu implements InventoryHolder {

    private static final Plugin plugin = NovaUI.getPlugin();

    static {
        Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
        //Bukkit.getPluginManager().registerEvents(new InteractionModifierListener(), plugin);
    }

    private Inventory inventory;
    private Component title;
    private int rows = 1;
    private MenuType type = MenuType.CHEST;
    private final Map<Integer, ItemStack> invItems;
    private boolean updating = false;

    private Map<Integer, MenuAction<InventoryClickEvent>> slotClickActions;
    private Set<MenuAction<InventoryClickEvent>> clickActions;
    private Set<MenuAction<InventoryDragEvent>> dragActions;
    private Set<MenuAction<InventoryCloseEvent>> closeMenuActions;
    private Set<MenuAction<InventoryOpenEvent>> openMenuActions;

    private boolean runCloseAction = true;
    private boolean runOpenAction = true;

    public BaseMenu(Component title, int rows) {
        this.rows = rows;
        this.title = title;
        int inventorySize = rows * 9;
        this.inventory = Bukkit.createInventory(this, inventorySize, title);
        invItems = new LinkedHashMap<>(inventorySize);
        slotClickActions = new LinkedHashMap<>(inventorySize);
        clickActions = new HashSet<>(inventorySize);
        dragActions = new HashSet<>(inventorySize);
        closeMenuActions = new HashSet<>(inventorySize);
        openMenuActions = new HashSet<>(inventorySize);
    }

    public BaseMenu(Component title, MenuType type) {
        this.title = title;
        this.type = type;
        int inventorySize = type.getMaxSize();
        this.inventory = Bukkit.createInventory(this, type.getInventoryType(), title);
        invItems = new LinkedHashMap<>(inventorySize);
        slotClickActions = new LinkedHashMap<>(inventorySize);
        clickActions = new HashSet<>(inventorySize);
        dragActions = new HashSet<>(inventorySize);
        closeMenuActions = new HashSet<>(inventorySize);
        openMenuActions = new HashSet<>(inventorySize);
    }

    public Component title() {
        return title;
    }

    public void setItem(final int slot, @NotNull final ItemStack item) {
        validateSlot(slot);
        invItems.put(slot, item);
    }

    public void setItem(@NotNull final List<Integer> slots, @NotNull final ItemStack item) {
        for (final int slot : slots) {
            setItem(slot, item);
        }
    }

    public void removeItem(@NotNull final ItemStack item) {
        final Optional<Map.Entry<Integer, ItemStack>> entry = invItems.entrySet()
                .stream()
                .filter(it -> it.getValue().equals(item))
                .findFirst();

        entry.ifPresent(it -> {
            invItems.remove(it.getKey());
            inventory.remove(it.getValue());
        });
    }

    public void removeItem(final int slot) {
        invItems.remove(slot);
    }

    public void addItem(@NotNull final ItemStack... items) {
        this.addItem(false, items);
    }

    public void addItem(final boolean expandIfFull, @NotNull final ItemStack... items) {
        final List<ItemStack> notAddedItems = new ArrayList<>();

        for (final ItemStack item : items) {
            for (int slot = 0; slot < type.getMaxSize() + 1; slot++) {
                if (invItems.get(slot) != null) {
                    if (slot == type.getMaxSize() - 1) {
                        notAddedItems.add(item);
                    }
                    continue;
                }

                invItems.put(slot, item);
                break;
            }
        }

        if (!expandIfFull || this.rows >= 6 || notAddedItems.isEmpty() || (this.type != null && this.type != MenuType.CHEST)) {
            return;
        }

        this.rows++;
        this.inventory = Bukkit.createInventory(this, this.rows * 9, this.title);
        this.update();
        this.addItem(true, notAddedItems.toArray(new ItemStack[0]));
    }

    public ItemStack getItem(int slot) {
        return invItems.get(slot);
    }

    public boolean isUpdating() {
        return updating;
    }

    public void setUpdating(final boolean updating) {
        this.updating = updating;
    }

    public void open(HumanEntity player) {
        inventory.clear();
        populateGui();
        player.openInventory(inventory);
    }

    public void close(@NotNull final HumanEntity player) {
        close(player, true);
    }

    public void close(@NotNull final HumanEntity player, final boolean runCloseAction) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            this.runCloseAction = runCloseAction;
            player.closeInventory();
            this.runCloseAction = true;
        }, 2L);
    }

    public void populateGui() {
        for (final Map.Entry<Integer, ItemStack> entry : invItems.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue());
        }
    }

    public void update() {
        inventory.clear();
        populateGui();
        for (HumanEntity viewer : new ArrayList<>(inventory.getViewers())) ((Player) viewer).updateInventory();
    }

    @NotNull
    public BaseMenu updateTitle(@NotNull final Component title) {
        updating = true;

        final List<HumanEntity> viewers = new ArrayList<>(inventory.getViewers());

        inventory = Bukkit.createInventory(this, inventory.getSize(), title);

        for (final HumanEntity player : viewers) {
            open(player);
        }

        updating = false;
        this.title = title;
        return this;
    }

    public void updateItem(final int slot, @NotNull final ItemStack item) {
        invItems.put(slot, item);
        inventory.setItem(slot, item);
    }

    @Nullable
    public MenuAction<InventoryClickEvent> getSlotAction(final int slot) {
        return slotClickActions.get(slot);
    }

    public void addSlotAction(final int slot,@Nullable final MenuAction<@NotNull InventoryClickEvent> slotClickAction) {
        this.slotClickActions.put(slot, slotClickAction);
    }

    @Nullable
    public Set<MenuAction<InventoryClickEvent>> getClickActions() {
        return clickActions;
    }

    public void addClickAction(@Nullable final MenuAction<@NotNull InventoryClickEvent> clickAction) {
        this.clickActions.add(clickAction);
    }

    @Nullable
    public Set<MenuAction<InventoryDragEvent>> getDragActions() {
        return dragActions;
    }

    public void addDragAction(@Nullable final MenuAction<@NotNull InventoryDragEvent> dragAction) {
        this.dragActions.add(dragAction);
    }

    @Nullable
    public Set<MenuAction<InventoryCloseEvent>> getCloseMenuActions() {
        return closeMenuActions;
    }

    public void addCloseMenuAction(@Nullable final MenuAction<@NotNull InventoryCloseEvent> closeGuiAction) {
        this.closeMenuActions.add(closeGuiAction);
    }

    @Nullable
    public Set<MenuAction<InventoryOpenEvent>> getOpenMenuActions() {
        return openMenuActions;
    }

    public void addOpenAction(@Nullable final MenuAction<@NotNull InventoryOpenEvent> openGuiAction) {
        this.openMenuActions.add(openGuiAction);
    }

    public boolean shouldRunCloseAction() {
        return runCloseAction;
    }

    public boolean shouldRunOpenAction() {
        return runOpenAction;
    }

    private void validateSlot(final int slot) {
        final int maxSize = type.getMaxSize();

        if (type == MenuType.CHEST) {
            if (slot < 0 || slot >= maxSize) throwInvalidSlot(slot);
            return;
        }

        if (slot < 0 || slot > maxSize) throwInvalidSlot(slot);
    }

    private void throwInvalidSlot(final int slot) {
        if (type == MenuType.CHEST) {
            throw new MenuException("Slot " + slot + " is not valid for the gui type - " + type.name() + " with rows - " + rows + "!");
        }

        throw new MenuException("Slot " + slot + " is not valid for the gui type - " + type.name() + "!");
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void setInventory(@NotNull final Inventory inventory) {
        this.inventory = inventory;
    }

    public int getRows() {
        return rows;
    }

    @NotNull
    public MenuType guiType() {
        return type;
    }
}

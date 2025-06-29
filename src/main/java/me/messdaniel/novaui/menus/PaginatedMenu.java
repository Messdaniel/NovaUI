package me.messdaniel.novaui.menus;

import me.messdaniel.novaui.menutypes.MenuType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PaginatedMenu extends BaseMenu {

    private final List<ItemStack> pageItems = new ArrayList<>();
    private final Map<Integer, ItemStack> currentPage;

    private int pageSize;
    private int pageNum = 1;

    public PaginatedMenu(Component title, int rows, int pageSize) {
        super(title, rows);
        this.pageSize = pageSize;
        int inventorySize = rows * 9;
        currentPage = new LinkedHashMap<>(inventorySize);
    }

    public PaginatedMenu(Component title, MenuType type, int pageSize) {
        super(title, type);
        this.pageSize = pageSize;
        int inventorySize = type.getMaxSize();
        currentPage = new LinkedHashMap<>(inventorySize);
    }

    public PaginatedMenu setPageSize(final int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public void addItem(@NotNull final ItemStack item) {
        pageItems.add(item);
    }

    @Override
    public void addItem(@NotNull final ItemStack... items) {
        pageItems.addAll(Arrays.asList(items));
    }

    @Override
    public void update() {
        getInventory().clear();
        populateGui();

        updatePage();
    }

    public void updatePageItem(final int slot, @NotNull final ItemStack item) {
        if (!currentPage.containsKey(slot)) return;
        final int index = pageItems.indexOf(currentPage.get(slot));

        currentPage.put(slot, item);
        pageItems.set(index, item);
        getInventory().setItem(slot, item);
    }

    public void removePageItem(@NotNull final ItemStack item) {
        pageItems.remove(item);
        updatePage();
    }

    @Override
    public void open(@NotNull final HumanEntity player) {
        open(player, 1);
    }

    public void open(@NotNull final HumanEntity player, final int openPage) {
        if (player.isSleeping()) return;
        if (openPage <= getPagesNum() || openPage > 0) pageNum = openPage;

        getInventory().clear();
        currentPage.clear();

        populateGui();

        if (pageSize == 0) pageSize = calculatePageSize();

        populatePage();

        player.openInventory(getInventory());
    }

    @Override
    @NotNull
    public PaginatedMenu updateTitle(@NotNull final Component title) {
        setUpdating(true);

        final List<HumanEntity> viewers = new ArrayList<>(getInventory().getViewers());

        setInventory(Bukkit.createInventory(this, getInventory().getSize(), title));

        for (final HumanEntity player : viewers) {
            open(player, getPageNum());
        }

        setUpdating(false);

        return this;
    }

    @NotNull
    public Map<@NotNull Integer, @NotNull ItemStack> getCurrentPageItems() {
        return Collections.unmodifiableMap(currentPage);
    }

    @NotNull
    public List<@NotNull ItemStack> getPageItems() {
        return Collections.unmodifiableList(pageItems);
    }

    public int getCurrentPageNum() {
        return pageNum;
    }

    public int getNextPageNum() {
        if (pageNum + 1 > getPagesNum()) return pageNum;
        return pageNum + 1;
    }

    public int getPrevPageNum() {
        if (pageNum - 1 == 0) return pageNum;
        return pageNum - 1;
    }

    public boolean next() {
        if (pageNum + 1 > getPagesNum()) return false;

        pageNum++;
        updatePage();
        return true;
    }

    public boolean previous() {
        if (pageNum - 1 == 0) return false;

        pageNum--;
        updatePage();
        return true;
    }

    ItemStack getPageItem(final int slot) {
        return currentPage.get(slot);
    }

    @NotNull
    private List<ItemStack> getPageNum(final int givenPage) {
        final int page = givenPage - 1;

        final List<ItemStack> guiPage = new ArrayList<>();

        int max = ((page * pageSize) + pageSize);
        if (max > pageItems.size()) max = pageItems.size();

        for (int i = page * pageSize; i < max; i++) {
            guiPage.add(pageItems.get(i));
        }

        return guiPage;
    }

    public int getPagesNum() {
        if (pageSize == 0) pageSize = calculatePageSize();
        return (int) Math.ceil((double) pageItems.size() / pageSize);
    }

    private void populatePage() {
        int slot = 0;
        final int inventorySize = getInventory().getSize();
        for (ItemStack itemStack : getPageNum(pageNum)) {
            if (slot >= inventorySize) {
                break;
            }

            if (getPageItem(slot) != null || getInventory().getItem(slot) != null) {
                slot++;
                continue;
            }

            final ItemStack item = itemStack;

            currentPage.put(slot, item);
            getInventory().setItem(slot, item);
            slot++;
        }
    }

    @Nullable
    Map<Integer, ItemStack> getMutableCurrentPageItems() {
        return currentPage;
    }

    public void clearPage() {
        for (Map.Entry<Integer, ItemStack> entry : currentPage.entrySet()) {
            getInventory().setItem(entry.getKey(), null);
        }
    }

    public void clearPageItems(final boolean update) {
        pageItems.clear();
        if (update) update();
    }

    public void clearPageItems() {
        clearPageItems(false);
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(final int pageNum) {
        this.pageNum = pageNum;
    }

    public void updatePage() {
        clearPage();
        populatePage();
    }

    public int calculatePageSize() {
        int counter = 0;

        for (int slot = 0; slot < getRows() * 9; slot++) {
            if (getPageItem(slot) == null) counter++;
        }

        if (counter == 0) return 1;
        return counter;
    }

}

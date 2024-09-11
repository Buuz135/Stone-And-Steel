package com.buuz135.stoneandsteel.container;

import com.buuz135.stoneandsteel.SnSContent;
import com.buuz135.stoneandsteel.recipe.DesignerRecipe;
import com.buuz135.stoneandsteel.tile.DesignerTile;
import com.google.common.collect.Lists;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.List;


public class DesignerContainer extends BasicContainer{

    private final DesignerTile tile;
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    private List<RecipeHolder<DesignerRecipe>> recipes = Lists.newArrayList();

    public DesignerContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, (DesignerTile) inventory.player.getCommandSenderWorld().getBlockEntity(buffer.readBlockPos()));
    }

    public DesignerContainer(int id, Inventory inventory, DesignerTile tile) {
        super(SnSContent.MenuTypes.DESIGNER.get(), id);
        this.tile = tile;


        createPlayerInventory(inventory);

        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(tile.getInput(), i, (i % 2) * 18 + 10, (i / 2) * 18 + 16));
        }

        addSlot(new SlotItemHandler(this.tile.getOutput(), 0, 143, 18 + 15));
        setupRecipeList();
    }

    private void createPlayerInventory(Inventory player) {
        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(player, k, 8 + k * 18, 142));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(tile.getLevel(),tile.getBlockPos()),  playerIn, SnSContent.Blocks.DESIGNER.get());
    }

    private void setupRecipeList() {
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.recipes = tile.getLevel().getRecipeManager().getAllRecipesFor((RecipeType<DesignerRecipe>) SnSContent.RecipeTypes.DESIGNER.get());
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<RecipeHolder<DesignerRecipe>> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public DesignerTile getTile() {
        return tile;
    }
}

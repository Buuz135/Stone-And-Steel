package com.buuz135.stoneandsteel.tile;

import com.buuz135.stoneandsteel.SnSContent;
import com.buuz135.stoneandsteel.container.DesignerContainer;
import com.buuz135.stoneandsteel.inventory.InventoryComponent;
import com.buuz135.stoneandsteel.recipe.DesignerRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class DesignerTile extends BlockEntity implements MenuProvider {

    private InventoryComponent input;
    private InventoryComponent output;

    public DesignerTile(BlockPos pos, BlockState blockState) {
        super(SnSContent.TileEntities.DESIGNER.get(), pos, blockState);
        this.input = new InventoryComponent(6);
        this.output = new InventoryComponent(1);
    }

    @Override
    public Component getDisplayName() {
        return SnSContent.Blocks.DESIGNER.get().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new DesignerContainer(i, inventory, this);
    }

    public void openGui(Player player) {
        if (player instanceof ServerPlayer) {
            player.openMenu( this, packetBuffer -> {
                packetBuffer.writeBlockPos(worldPosition);
            });
        }
    }

    public void craft(ResourceLocation resourceLocation, int amount) {
        var recipes = level.getRecipeManager().getAllRecipesFor((RecipeType<DesignerRecipe>) SnSContent.RecipeTypes.DESIGNER.get());
        for (RecipeHolder<DesignerRecipe> recipe : recipes) {
            if (recipe.id().equals(resourceLocation)) {
                while (hasAllItems(recipe.value(), getInput()) && amount > 0 && ItemHandlerHelper.insertItem(getOutput(), recipe.value().getOutput().copy(), true).isEmpty()){
                    ItemHandlerHelper.insertItem(getOutput(), recipe.value().getOutput().copy(), false);
                    --amount;
                    reduce(recipe.value(), getInput());
                }
                return;
            }
        }
    }

    public static void reduce(DesignerRecipe recipe, IItemHandler itemHandler) {
        for (int inputIndex = 0; inputIndex < recipe.getInput().size(); inputIndex++) {
            var sizedIngredient = recipe.getInput().get(inputIndex);
            var remainingAmount = sizedIngredient.count();
            for (int checkingIndex = 0; checkingIndex < itemHandler.getSlots(); checkingIndex++) {
                var tileStack = itemHandler.getStackInSlot(checkingIndex);
                if (sizedIngredient.ingredient().test(tileStack)){
                    var toReduce = Math.min(remainingAmount, tileStack.getCount());
                    remainingAmount -= toReduce;
                    tileStack.shrink(toReduce);
                }
                if (remainingAmount <= 0){
                    break;
                }
            }
        }
    }

    public static boolean hasAllItems(DesignerRecipe recipe, IItemHandler itemHandler) {
        for (int inputIndex = 0; inputIndex < recipe.getInput().size(); inputIndex++) {
            var sizedIngredient = recipe.getInput().get(inputIndex);
            var remainingAmount = sizedIngredient.count();
            for (int checkingIndex = 0; checkingIndex < itemHandler.getSlots(); checkingIndex++) {
                var tileStack = itemHandler.getStackInSlot(checkingIndex);
                if (sizedIngredient.ingredient().test(tileStack)){
                    remainingAmount -= tileStack.getCount();
                }
                if (remainingAmount <= 0){
                    break;
                }
            }
            if (remainingAmount > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.input.deserializeNBT(registries, tag.getCompound("input"));
        this.output.deserializeNBT(registries, tag.getCompound("output"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("input", this.input.serializeNBT(registries));
        tag.put("output", this.output.serializeNBT(registries));
    }

    public InventoryComponent getInput() {
        return input;
    }

    public InventoryComponent getOutput() {
        return output;
    }
}

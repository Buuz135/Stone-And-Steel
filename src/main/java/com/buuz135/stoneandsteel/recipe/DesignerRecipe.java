package com.buuz135.stoneandsteel.recipe;

import com.buuz135.stoneandsteel.SnSContent;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

public class DesignerRecipe implements Recipe<CraftingInput> {

    public static final MapCodec<DesignerRecipe> CODEC = RecordCodecBuilder.mapCodec(in -> in.group(
            SizedIngredient.FLAT_CODEC.listOf().fieldOf("input").forGetter(DesignerRecipe::getInput),
            ItemStack.CODEC.fieldOf("output").forGetter(DesignerRecipe::getOutput)
    ).apply(in, DesignerRecipe::new));


    private List<SizedIngredient> input;
    private ItemStack output;

    public DesignerRecipe(List<SizedIngredient> input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SnSContent.RecipeSerializers.DESIGNER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SnSContent.RecipeTypes.DESIGNER.get();
    }

    public List<SizedIngredient> getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

}

package com.buuz135.stoneandsteel.datagen;


import com.buuz135.stoneandsteel.StoneAndSteel;
import com.buuz135.stoneandsteel.recipe.DesignerRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SnSRecipeProvider extends RecipeProvider {

    public SnSRecipeProvider(DataGenerator generatorIn, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(generatorIn.getPackOutput(), lookupProvider);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {

        designer(consumer, "stone", new ItemStack(Blocks.STONE), new SizedIngredient(Ingredient.of(Blocks.STONE), 1), new SizedIngredient(Ingredient.of(Items.COAL), 2), new SizedIngredient(Ingredient.of(Tags.Items.CROPS), 2));
    }

    public static void designer(RecipeOutput recipeOutput, String name, ItemStack output, SizedIngredient... inputs) {
        var recipe = new DesignerRecipe(List.of(inputs), output);
        recipeOutput.accept(ResourceLocation.fromNamespaceAndPath(StoneAndSteel.MODID, name), recipe, null);
    }
}

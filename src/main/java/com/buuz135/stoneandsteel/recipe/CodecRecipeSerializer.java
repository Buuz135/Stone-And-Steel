package com.buuz135.stoneandsteel.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class CodecRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
    private final Class<T> recipeClass;
    private final Supplier<RecipeType<?>> recipeTypeSupplier;
    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public CodecRecipeSerializer(Class<T> recipeClass, Supplier<RecipeType<?>> recipeTypeSupplier, MapCodec<T> codec) {
        this.recipeClass = recipeClass;
        this.recipeTypeSupplier = recipeTypeSupplier;
        this.codec = codec;
        this.streamCodec = StreamCodec.ofMember((value, buff) -> buff.writeJsonWithCodec(this.codec().codec(), value),
                registryFriendlyByteBuf -> registryFriendlyByteBuf.readJsonWithCodec(this.codec.codec()));
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }

    @Override
    public MapCodec<T> codec() {
        return codec;
    }
}


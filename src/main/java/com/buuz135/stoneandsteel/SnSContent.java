package com.buuz135.stoneandsteel;

import com.buuz135.stoneandsteel.block.DesignerBlock;
import com.buuz135.stoneandsteel.container.DesignerContainer;
import com.buuz135.stoneandsteel.tile.DesignerTile;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class SnSContent {

    public static <T extends Block> DeferredHolder<Block, T> block(String id, Supplier<T> block) {
        return Blocks.REGISTRY.register(id, block);
    }

    public static DeferredHolder<Item, Item> item(String id, Supplier<Item> item) {
        return Items.REGISTRY.register(id, () -> {
            var i = item.get();
            //SushiGoCrafting.TAB.getTabList().add(i);
            return i;
        });
    }

    public static DeferredHolder<Item, Item> basicItem(String id) {
        return Items.REGISTRY.register(id, () -> new Item(new Item.Properties()));
    }


    public static DeferredHolder<Item, BlockItem> blockItem(String id, Supplier<? extends Block> sup) {
        return Items.REGISTRY.register(id, () -> {
            var blockItem = new BlockItem(sup.get(), new Item.Properties());
            //SushiGoCrafting.TAB.getTabList().add(blockItem);
            return blockItem;
        });
    }

    public static DeferredHolder<Item, BlockItem> blockItem(String id, Supplier<? extends Block> sup, Item.Properties properties) {
        return Items.REGISTRY.register(id, () -> {
            var blockItem = new BlockItem(sup.get(), properties);
            //SushiGoCrafting.TAB.getTabList().add(blockItem);
            return blockItem;
        });
    }

    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> tile(String id, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block> sup) {
        return TileEntities.REGISTRY.register(id, () -> BlockEntityType.Builder.of(supplier, sup.get()).build(null));
    }

    public static DeferredHolder<MobEffect, MobEffect> effect(String id, Supplier<MobEffect> supplier) {
        return Effects.REGISTRY.register(id, supplier);
    }

    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> entity(String id, Supplier<EntityType<T>> supplier) {
        return EntityTypes.REGISTRY.register(id, supplier);
    }

    public static <T extends IGlobalLootModifier> DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<T>> lootSerializer(String id, Supplier<MapCodec<T>> supplier) {
        return LootSerializers.REGISTRY.register(id, supplier);
    }

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> recipeSerializer(String id, Supplier<RecipeSerializer<?>> supplier) {
        return RecipeSerializers.REGISTRY.register(id, supplier);
    }

    public static DeferredHolder<RecipeType<?>, RecipeType<?>> recipeType(String id, Supplier<RecipeType<?>> supplier) {
        return RecipeTypes.REGISTRY.register(id, supplier);
    }

    public static class Blocks {

        public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(Registries.BLOCK, StoneAndSteel.MODID);

        public static final DeferredHolder<Block, DesignerBlock> DESIGNER = block("designer", DesignerBlock::new);

    }

    public static class Items {

        public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, StoneAndSteel.MODID);

        public static final DeferredHolder<Item, BlockItem> DESIGNER = blockItem("designer", Blocks.DESIGNER);

    }

    public static class TileEntities {

        public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, StoneAndSteel.MODID);

        public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DesignerTile>> DESIGNER = tile("designer", DesignerTile::new, Blocks.DESIGNER);
    }

    public static class Effects {

        public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, StoneAndSteel.MODID);


    }

    public static class EntityTypes {

        public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, StoneAndSteel.MODID);


    }

    public static class LootSerializers {

        public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, StoneAndSteel.MODID);



    }

    public static class RecipeSerializers {

        public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, StoneAndSteel.MODID);


    }

    public static class RecipeTypes {

        public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_TYPE, StoneAndSteel.MODID);



    }

    public static class MenuTypes{

        public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, StoneAndSteel.MODID);

        public static final DeferredHolder<MenuType<?>, MenuType<DesignerContainer>> DESIGNER = REGISTRY.register("designer", () -> IMenuTypeExtension.create(DesignerContainer::new));

    }

}

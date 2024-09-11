package com.buuz135.stoneandsteel.datagen;

import com.buuz135.stoneandsteel.StoneAndSteel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Arrays;
import java.util.List;

public class SnSItemModelProvider extends ItemModelProvider {


    public SnSItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (DeferredBlock<Block> testBlock : StoneAndSteel.TEST_BLOCKS) {
            getBuilder(BuiltInRegistries.BLOCK.getKey(testBlock.get()).getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + BuiltInRegistries.BLOCK.getKey(testBlock.get()).getPath())));
        }
    }

}

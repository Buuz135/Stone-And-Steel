package com.buuz135.stoneandsteel.datagen;

import com.buuz135.stoneandsteel.StoneAndSteel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SnSModelProvider extends BlockModelProvider {

    public SnSModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (DeferredHolder<Block, Block> testBlock : StoneAndSteel.TEST_BLOCKS) {
            this.withExistingParent(BuiltInRegistries.BLOCK.getKey(testBlock.get()).getPath(), "block/cube_all").texture("all", ResourceLocation.parse("block/stone"));
        }
    }




}

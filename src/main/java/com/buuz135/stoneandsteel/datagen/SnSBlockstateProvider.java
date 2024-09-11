package com.buuz135.stoneandsteel.datagen;

import com.buuz135.stoneandsteel.StoneAndSteel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;


public class SnSBlockstateProvider extends BlockStateProvider {

    public SnSBlockstateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen.getPackOutput(), modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (DeferredBlock<Block> testBlock : StoneAndSteel.TEST_BLOCKS) {
            simpleBlockUn(testBlock.get());
        }
    }

    private void simpleBlockUn(Block block) {
        simpleBlock(block, new ModelFile.UncheckedModelFile(modLoc("block/" + BuiltInRegistries.BLOCK.getKey(block).getPath())));
    }


}

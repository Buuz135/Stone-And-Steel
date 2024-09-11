package com.buuz135.stoneandsteel.datagen;

import com.buuz135.stoneandsteel.StoneAndSteel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.text.WordUtils;

public class SnSLangProvider extends LanguageProvider {

    public SnSLangProvider(DataGenerator gen, String modid, String locale) {
        super(gen.getPackOutput(), modid, locale);
    }

    @Override
    protected void addTranslations() {
        for (DeferredBlock<Block> testBlock : StoneAndSteel.TEST_BLOCKS) {
            add(testBlock.get(), WordUtils.capitalize(BuiltInRegistries.ITEM.getKey(testBlock.get().asItem()).getPath().replaceAll("_", " ")));
        }
    }

}

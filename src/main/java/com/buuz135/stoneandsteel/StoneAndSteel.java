package com.buuz135.stoneandsteel;

import com.buuz135.stoneandsteel.datagen.*;
import com.buuz135.stoneandsteel.network.DesignerCraftMessage;
import com.buuz135.stoneandsteel.screen.DesignerScreen;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(StoneAndSteel.MODID)
public class StoneAndSteel {

    public static final String MODID = "stoneandsteel";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final PayloadRegistrar NETWORK = new PayloadRegistrar(MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static List<DeferredHolder<Block, Block>> TEST_BLOCKS = new ArrayList<DeferredHolder<Block, Block>>();

    // Creates a creative tab with the id "stoneandsteel:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.stoneandsteel")).withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> new ItemStack(Blocks.STONE)).displayItems((parameters, output) -> {
         // Add the example item to the tab. For your own tabs, this method is preferred over the event
    }).build());


    public StoneAndSteel(IEventBus modEventBus, ModContainer modContainer, Dist dist) {
        SnSContent.MenuTypes.REGISTRY.register(modEventBus);
        SnSContent.Blocks.REGISTRY.register(modEventBus);
        SnSContent.Items.REGISTRY.register(modEventBus);
        SnSContent.TileEntities.REGISTRY.register(modEventBus);
        SnSContent.RecipeSerializers.REGISTRY.register(modEventBus);
        SnSContent.RecipeTypes.REGISTRY.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        CREATIVE_MODE_TABS.register(modEventBus);

        NETWORK.playToServer(DesignerCraftMessage.TYPE, DesignerCraftMessage.CODEC, DesignerCraftMessage::handle);


        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::dataGen);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        for (int i = 0; i < 50; i++) {
            var block = SnSContent.Blocks.REGISTRY.register("example_block_" + i, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
            TEST_BLOCKS.add(block);
            SnSContent.Items.REGISTRY.register("example_block_" + i, () -> new BlockItem(block.get(), new Item.Properties()));
        }

        if (dist.isClient()){
            modEventBus.addListener((final RegisterMenuScreensEvent event) -> {
                event.register(SnSContent.MenuTypes.DESIGNER.get(), DesignerScreen::new);
            });
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == EXAMPLE_TAB.get()) {
            event.acceptAll(TEST_BLOCKS.stream().map(DeferredHolder::get).map(ItemStack::new).toList());
            event.accept(new ItemStack(SnSContent.Blocks.DESIGNER.get()));
        }
    }

    public void dataGen(GatherDataEvent event) {
        event.getGenerator().addProvider(true, new SnSModelProvider(event.getGenerator(), MODID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new SnSBlockstateProvider(event.getGenerator(), MODID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new SnSItemModelProvider(event.getGenerator(), MODID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new SnSLangProvider(event.getGenerator(), MODID, "en_us"));
        event.getGenerator().addProvider(true, new SnSRecipeProvider(event.getGenerator(), event.getLookupProvider()));
    }

}

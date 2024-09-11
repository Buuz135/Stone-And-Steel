package com.buuz135.stoneandsteel.screen;

import com.buuz135.stoneandsteel.SnSContent;
import com.buuz135.stoneandsteel.StoneAndSteel;
import com.buuz135.stoneandsteel.container.DesignerContainer;
import com.buuz135.stoneandsteel.network.DesignerCraftMessage;
import com.buuz135.stoneandsteel.recipe.DesignerRecipe;
import com.buuz135.stoneandsteel.tile.DesignerTile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.neoforged.neoforge.network.PacketDistributor;

import java.awt.*;
import java.util.List;

public class DesignerScreen extends AbstractContainerScreen<DesignerContainer> {

    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.fromNamespaceAndPath(StoneAndSteel.MODID,"textures/gui/designer/scroller.png");
    private static final ResourceLocation SCROLLER_DISABLED_SPRITE = ResourceLocation.fromNamespaceAndPath(StoneAndSteel.MODID,"textures/gui/designer/scroller_disabled.png");
    private static final ResourceLocation RECIPE_SELECTED_SPRITE = ResourceLocation.fromNamespaceAndPath(StoneAndSteel.MODID,"textures/gui/designer/recipe_selected.png");
    private static final ResourceLocation RECIPE_HIGHLIGHTED_SPRITE = ResourceLocation.fromNamespaceAndPath(StoneAndSteel.MODID,"textures/gui/designer/recipe_highlighted.png");
    private static final ResourceLocation RECIPE_SPRITE = ResourceLocation.fromNamespaceAndPath(StoneAndSteel.MODID,"textures/gui/designer/recipe.png");

    private static final int SCROLLER_WIDTH = 12;
    private static final int SCROLLER_HEIGHT = 15;
    private static final int RECIPES_COLUMNS = 4;
    private static final int RECIPES_ROWS = 3;
    private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
    private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
    private static final int SCROLLER_FULL_HEIGHT = 54;
    private static final int RECIPES_X = 52;
    private static final int RECIPES_Y = 14;
    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    private boolean displayRecipes;

    public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(StoneAndSteel.MODID,"textures/gui/designer.png");

    public DesignerScreen(DesignerContainer menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.displayRecipes = true;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(BACKGROUND, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int k = (int)(41.0F * this.scrollOffs);
        ResourceLocation resourcelocation = this.isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
        guiGraphics.blit(resourcelocation, i + 119, j + 15 + k,0,0, 12, 15, 12,15);
        int l = this.leftPos + 52;
        int i1 = this.topPos + 14;
        int j1 = this.startIndex + 12;
        this.renderButtons(guiGraphics, mouseX, mouseY, l, i1, j1);
        this.renderRecipes(guiGraphics, l, i1, j1);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        if (this.displayRecipes) {
            int i = this.leftPos + 52;
            int j = this.topPos + 14;
            int k = this.startIndex + 12;
            List<RecipeHolder<DesignerRecipe>> list = this.menu.getRecipes();

            for(int index = this.startIndex; index < k && index < this.menu.getNumRecipes(); ++index) {
                int i1 = index - this.startIndex;
                int j1 = i + i1 % 4 * 16;
                int k1 = j + i1 / 4 * 18 + 2;
                if (x >= j1 && x < j1 + 16 && y >= k1 && y < k1 + 18) {
                    var recipe = list.get(index);
                    var result = recipe.value().getOutput();
                    var lines = DesignerScreen.getTooltipFromItem(Minecraft.getInstance(), result);
                    lines.add(Component.literal("Requires: ").withStyle(ChatFormatting.GRAY));
                    lines.add(Component.empty());
                    lines.add(Component.empty());
                    guiGraphics.renderComponentTooltip(this.font, lines ,x, y);
                    guiGraphics.pose().popPose();
                    guiGraphics.pose().translate(0,0,1000);
                    for (int inputIndex = 0; inputIndex < recipe.value().getInput().size(); inputIndex++) {
                        var sizedIngredient = recipe.value().getInput().get(inputIndex);
                        var stack = sizedIngredient.getItems()[(int) ((Minecraft.getInstance().level.getGameTime() / 20) % sizedIngredient.getItems().length)];
                        var remainingAmount = sizedIngredient.count();
                        for (int checkingIndex = 0; checkingIndex < this.menu.getTile().getInput().getSlots(); checkingIndex++) {
                            var tileStack = this.menu.getTile().getInput().getStackInSlot(checkingIndex);
                            if (sizedIngredient.ingredient().test(tileStack)){
                                remainingAmount -= tileStack.getCount();
                            }
                            if (remainingAmount <= 0){
                                break;
                            }
                        }
                        if (remainingAmount > 0) {
                            DesignerScreen.renderSlotHighlight(guiGraphics, x + 18 * inputIndex + 12, y + 10 * (lines.size() - 3)+ 1, 0, FastColor.ARGB32.color(0x33, Color.RED.getRGB()));
                        }
                        guiGraphics.renderItem(stack, x + 18 * inputIndex + 12, y + 10 * (lines.size() - 3)+ 1 );
                        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, x + 18 * inputIndex + 12, y + 10 * (lines.size() - 3)+ 1);
                    }
                    guiGraphics.pose().pushPose();
                }
            }
        }

    }

    private void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int lastVisibleElementIndex) {
        for(int i = this.startIndex; i < lastVisibleElementIndex && i < this.menu.getNumRecipes(); ++i) {
            int j = i - this.startIndex;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int i1 = y + l * 18 + 2;
            ResourceLocation resourcelocation;
            var recipe = this.menu.getRecipes().get(i);
            if (i == this.menu.getSelectedRecipeIndex() || !DesignerTile.hasAllItems(recipe.value(), this.menu.getTile().getInput())) {
                resourcelocation = RECIPE_SELECTED_SPRITE;
            } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
                resourcelocation = RECIPE_HIGHLIGHTED_SPRITE;
            } else {
                resourcelocation = RECIPE_SPRITE;
            }

            guiGraphics.blit(resourcelocation, k, i1 - 1, 0,0,16, 18, 16,18);
        }

    }

    private void renderRecipes(GuiGraphics guiGraphics, int x, int y, int startIndex) {
        List<RecipeHolder<DesignerRecipe>> list = this.menu.getRecipes();

        for(int i = this.startIndex; i < startIndex && i < this.menu.getNumRecipes(); ++i) {
            int j = i - this.startIndex;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int i1 = y + l * 18 + 2;
            var recipe = list.get(i);
            var result = recipe.value().getOutput();
            guiGraphics.renderItem(result, k, i1);
        }

    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;
        if (this.displayRecipes) {
            int i = this.leftPos + 52;
            int j = this.topPos + 14;
            int k = this.startIndex + 12;

            for(int index = this.startIndex; index < k; ++index) {
                int i1 = index - this.startIndex;
                double d0 = mouseX - (double)(i + i1 % 4 * 16);
                double d1 = mouseY - (double)(j + i1 / 4 * 18);


                if (d0 >= 0.0 && d1 >= 0.0 && d0 < 16.0 && d1 < 18.0 ) {
                    var recipe = this.menu.getRecipes().get(index);
                    if (DesignerTile.hasAllItems(recipe.value(), this.menu.getTile().getInput())){
                        PacketDistributor.sendToServer(new DesignerCraftMessage(recipe.id(), this.menu.getTile().getBlockPos(), Screen.hasShiftDown() ? 64 : 1));
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
                    }
                    return true;
                }
            }

            i = this.leftPos + 119;
            j = this.topPos + 9;
            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }


    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + 14;
            int j = i + 54;
            this.scrollOffs = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.isScrollBarActive()) {
            int i = this.getOffscreenRows();
            float f = (float)scrollY / (float)i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5) * 4;
        }

        return true;
    }

    private boolean isScrollBarActive() {
        return this.displayRecipes && this.menu.getNumRecipes() > 12;
    }

    protected int getOffscreenRows() {
        return (this.menu.getNumRecipes() + 4 - 1) / 4 - 3;
    }
}

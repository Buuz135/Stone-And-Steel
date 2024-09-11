package com.buuz135.stoneandsteel.network;

import com.buuz135.stoneandsteel.SnSContent;
import com.buuz135.stoneandsteel.StoneAndSteel;
import com.buuz135.stoneandsteel.recipe.DesignerRecipe;
import com.buuz135.stoneandsteel.tile.DesignerTile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class DesignerCraftMessage implements CustomPacketPayload{

    public static CustomPacketPayload.Type<DesignerCraftMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(StoneAndSteel.MODID, "design_craft"));
    public static StreamCodec<? super RegistryFriendlyByteBuf, DesignerCraftMessage> CODEC = new StreamCodec<>() {
        @Override
        public DesignerCraftMessage decode(RegistryFriendlyByteBuf object) {
            return new DesignerCraftMessage(object.readResourceLocation(), object.readBlockPos(), object.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf registryFriendlyByteBuf, DesignerCraftMessage sealRequestMessage) {
            registryFriendlyByteBuf.writeResourceLocation(sealRequestMessage.recipe);
            registryFriendlyByteBuf.writeBlockPos(sealRequestMessage.pos);
            registryFriendlyByteBuf.writeInt(sealRequestMessage.amount);
        }
    };

    private ResourceLocation recipe;
    private BlockPos pos;
    private int amount;

    public DesignerCraftMessage(ResourceLocation seal, BlockPos pos, int amount) {
        this.recipe = seal;
        this.pos = pos;
        this.amount = amount;
    }

    public DesignerCraftMessage() {

    }

    public void handle(IPayloadContext contextSupplier) {
        contextSupplier.enqueueWork(() -> {
            Player entity = contextSupplier.player();
            if (entity instanceof ServerPlayer serverPlayer && entity.level() instanceof ServerLevel serverLevel) {
                var tile = serverLevel.getBlockEntity(pos);
                if (tile instanceof DesignerTile designerTile){
                    designerTile.craft(recipe, amount);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

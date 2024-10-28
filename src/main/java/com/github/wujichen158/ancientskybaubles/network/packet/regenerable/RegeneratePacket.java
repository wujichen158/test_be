package com.github.wujichen158.ancientskybaubles.network.packet.regenerable;

import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class RegeneratePacket extends HarvestStatusResponsePacket {

    public RegeneratePacket(boolean harvestedStatus, BlockPos regenerableBlockPos) {
        super(harvestedStatus, regenerableBlockPos);
    }

    public RegeneratePacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(this.regenerableBlockPos) instanceof RegenerableBlockEntity blockEntity) {
                blockEntity.harvestStatus = false;
            }
        });
        ctx.setPacketHandled(true);
    }
}

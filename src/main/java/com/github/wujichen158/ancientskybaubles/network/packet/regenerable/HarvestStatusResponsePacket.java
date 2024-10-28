package com.github.wujichen158.ancientskybaubles.network.packet.regenerable;

import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import com.github.wujichen158.ancientskybaubles.network.packet.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class HarvestStatusResponsePacket extends Packet {
    protected boolean harvestedStatus;
    protected BlockPos regenerableBlockPos;

    public HarvestStatusResponsePacket(boolean harvestedStatus, BlockPos regenerableBlockPos) {
        this.harvestedStatus = harvestedStatus;
        this.regenerableBlockPos = regenerableBlockPos;
    }

    public HarvestStatusResponsePacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    public void decode(FriendlyByteBuf packetBuffer) {
        this.harvestedStatus = packetBuffer.readBoolean();
        this.regenerableBlockPos = packetBuffer.readBlockPos();
    }

    @Override
    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBoolean(this.harvestedStatus);
        packetBuffer.writeBlockPos(this.regenerableBlockPos);
    }

    @Override
    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(this.regenerableBlockPos) instanceof RegenerableBlockEntity blockEntity) {
                blockEntity.harvestStatus = this.harvestedStatus;
            }
        });
        ctx.setPacketHandled(true);
    }
}

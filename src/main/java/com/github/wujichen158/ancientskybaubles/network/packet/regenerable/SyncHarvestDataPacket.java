package com.github.wujichen158.ancientskybaubles.network.packet.regenerable;

import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import com.github.wujichen158.ancientskybaubles.network.packet.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.Optional;

public class SyncHarvestDataPacket extends Packet {
    private boolean harvestStatus;
    private BlockPos blockPos;

    public SyncHarvestDataPacket(boolean harvested, BlockPos blockPos) {
        this.harvestStatus = harvested;
        this.blockPos = blockPos;
    }

    public SyncHarvestDataPacket(FriendlyByteBuf packetBuffer) {
        super(packetBuffer);
    }


    @Override
    public void decode(FriendlyByteBuf packetBuffer) {
        this.harvestStatus = packetBuffer.readBoolean();
        this.blockPos = packetBuffer.readBlockPos();
    }

    @Override
    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBoolean(this.harvestStatus);
        packetBuffer.writeBlockPos(this.blockPos);
    }

    @Override
    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> Optional.ofNullable(Minecraft.getInstance().level)
                .map(level -> (RegenerableBlockEntity) level.getBlockEntity(this.blockPos))
                .ifPresent(blockEntity -> blockEntity.harvestStatus = this.harvestStatus));
        ctx.setPacketHandled(true);
    }
}

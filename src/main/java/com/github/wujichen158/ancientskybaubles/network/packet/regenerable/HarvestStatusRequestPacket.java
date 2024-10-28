package com.github.wujichen158.ancientskybaubles.network.packet.regenerable;

import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import com.github.wujichen158.ancientskybaubles.network.AncientSkyBaublesNetwork;
import com.github.wujichen158.ancientskybaubles.network.packet.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;
import java.util.UUID;

public class HarvestStatusRequestPacket extends Packet {
    private UUID playerUuid;
    private BlockPos regenerableBlockPos;

    public HarvestStatusRequestPacket(UUID playerUuid, BlockPos regenerableBlockPos) {
        this.playerUuid = playerUuid;
        this.regenerableBlockPos = regenerableBlockPos;
    }

    public HarvestStatusRequestPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    public void decode(FriendlyByteBuf packetBuffer) {
        this.playerUuid = packetBuffer.readUUID();
        this.regenerableBlockPos = packetBuffer.readBlockPos();
    }

    @Override
    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeUUID(this.playerUuid);
        packetBuffer.writeBlockPos(this.regenerableBlockPos);
    }

    @Override
    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            Optional.ofNullable(ctx.getSender()).ifPresent(player -> {
                Optional.ofNullable((RegenerableBlockEntity) player.level().getBlockEntity(this.regenerableBlockPos))
                        .ifPresent(blockEntity -> AncientSkyBaublesNetwork.INSTANCE.send(
                                new HarvestStatusResponsePacket(blockEntity.hasHarvested(player), this.regenerableBlockPos),
                                PacketDistributor.PLAYER.with(player)));
            });
        });
        ctx.setPacketHandled(true);
    }
}

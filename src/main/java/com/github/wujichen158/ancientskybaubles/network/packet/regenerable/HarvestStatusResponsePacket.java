package com.github.wujichen158.ancientskybaubles.network.packet.regenerable;

import com.github.wujichen158.ancientskybaubles.client.cache.RegenerableBlockEntityCache;
import com.github.wujichen158.ancientskybaubles.network.packet.Packet;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class HarvestStatusResponsePacket extends Packet {
    protected boolean harvestedStatus;
    protected GlobalPos regenerableGlobalPos;

    public HarvestStatusResponsePacket(boolean harvestedStatus, GlobalPos regenerableGlobalPos) {
        this.harvestedStatus = harvestedStatus;
        this.regenerableGlobalPos = regenerableGlobalPos;
    }

    public HarvestStatusResponsePacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    public void decode(FriendlyByteBuf packetBuffer) {
        this.harvestedStatus = packetBuffer.readBoolean();
        this.regenerableGlobalPos = packetBuffer.readGlobalPos();
    }

    @Override
    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBoolean(this.harvestedStatus);
        packetBuffer.writeGlobalPos(this.regenerableGlobalPos);
    }

    @Override
    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> RegenerableBlockEntityCache.addHarvestStatus(this.regenerableGlobalPos, this.harvestedStatus));
        ctx.setPacketHandled(true);
    }
}

package com.github.wujichen158.ancientskybaubles.network.packet.regenerable;

import com.github.wujichen158.ancientskybaubles.client.cache.RegenerableBlockEntityCache;
import com.github.wujichen158.ancientskybaubles.network.packet.Packet;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class GenerableOnBreakPacket extends Packet {
    protected GlobalPos regenerableGlobalPos;

    public GenerableOnBreakPacket(GlobalPos regenerableGlobalPos) {
        this.regenerableGlobalPos = regenerableGlobalPos;
    }

    public GenerableOnBreakPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    public void decode(FriendlyByteBuf packetBuffer) {
        this.regenerableGlobalPos = packetBuffer.readGlobalPos();
    }

    @Override
    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeGlobalPos(this.regenerableGlobalPos);
    }

    @Override
    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> RegenerableBlockEntityCache.onBreak(this.regenerableGlobalPos));
        ctx.setPacketHandled(true);
    }
}

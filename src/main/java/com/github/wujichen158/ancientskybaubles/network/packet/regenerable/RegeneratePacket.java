package com.github.wujichen158.ancientskybaubles.network.packet.regenerable;

import com.github.wujichen158.ancientskybaubles.client.cache.RegenerableBlockEntityCache;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class RegeneratePacket extends HarvestStatusResponsePacket {

    public RegeneratePacket(boolean harvestedStatus, GlobalPos regenerableGlobalPos) {
        super(harvestedStatus, regenerableGlobalPos);
    }

    public RegeneratePacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> RegenerableBlockEntityCache.regenerate(this.regenerableGlobalPos, this.harvestedStatus));
        ctx.setPacketHandled(true);
    }
}

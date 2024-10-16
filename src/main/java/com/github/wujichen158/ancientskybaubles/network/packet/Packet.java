package com.github.wujichen158.ancientskybaubles.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public abstract class Packet {
    public abstract void decode(FriendlyByteBuf packetBuffer);

    public abstract void encode(FriendlyByteBuf packetBuffer);

    public abstract void handle(CustomPayloadEvent.Context ctx);

    public Packet() {
    }

    public Packet(FriendlyByteBuf buffer) {
        decode(buffer);
    }

}

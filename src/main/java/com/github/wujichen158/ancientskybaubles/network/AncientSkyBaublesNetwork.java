package com.github.wujichen158.ancientskybaubles.network;

import com.github.wujichen158.ancientskybaubles.util.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class AncientSkyBaublesNetwork {
    public static final String CHANNEL = "main";
    private static final int PROTOCOL_VERSION = ModList.get().getModFileById(Reference.MOD_ID).versionString().hashCode();

    private static final Channel.VersionTest ACCEPT_SEPARATED = (status, version) -> version == PROTOCOL_VERSION;

    public static final SimpleChannel INSTANCE = ChannelBuilder
            .named(new ResourceLocation(Reference.MOD_ID, CHANNEL))
            .clientAcceptedVersions(ACCEPT_SEPARATED)
            .serverAcceptedVersions(ACCEPT_SEPARATED)
            .networkProtocolVersion(PROTOCOL_VERSION)
            .simpleChannel();
}

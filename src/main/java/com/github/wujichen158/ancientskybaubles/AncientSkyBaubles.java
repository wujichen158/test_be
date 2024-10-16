package com.github.wujichen158.ancientskybaubles;

import com.github.wujichen158.ancientskybaubles.listener.ClientRenderListener;
import com.github.wujichen158.ancientskybaubles.network.AncientSkyBaublesNetwork;
import com.github.wujichen158.ancientskybaubles.network.packet.regenerable.SyncHarvestDataPacket;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesBlockEntities;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesBlocks;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesCreativeTabs;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesItems;
import com.github.wujichen158.ancientskybaubles.util.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class AncientSkyBaubles {
    private static final Logger LOGGER = LogManager.getLogger();

    public AncientSkyBaubles() {
        // 注册 mod 加载时的设置
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // 建立网络通信
        bus.addListener(this::setupNetwork);

        // 注册物品
        AncientSkyBaublesItems.init(bus);
        // 注册方块
        AncientSkyBaublesBlocks.init(bus);
        // 注册 BE
        AncientSkyBaublesBlockEntities.init(bus);
        // 注册创造模式物品栏
        AncientSkyBaublesCreativeTabs.init(bus);

        // 监听渲染相关事件
        bus.register(ClientRenderListener.class);

        // 注册 mod 本身以供服务器与其他游戏事件使用
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setupNetwork(final FMLCommonSetupEvent event) {
        int messageNumber = 0;
        AncientSkyBaublesNetwork.INSTANCE.messageBuilder(SyncHarvestDataPacket.class, messageNumber++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncHarvestDataPacket::encode)
                .decoder(SyncHarvestDataPacket::new)
                .consumerNetworkThread(SyncHarvestDataPacket::handle)
                .add();
    }
}

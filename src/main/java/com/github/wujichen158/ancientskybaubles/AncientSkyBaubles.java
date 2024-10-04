package com.github.wujichen158.ancientskybaubles;

import com.github.wujichen158.ancientskybaubles.listener.ClientRenderListener;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesCreativeTabs;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesItems;
import com.github.wujichen158.ancientskybaubles.util.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class AncientSkyBaubles {
    private static final Logger LOGGER = LogManager.getLogger();

    public AncientSkyBaubles() {
        // 注册 mod 加载时的设置
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册物品
        AncientSkyBaublesItems.init(bus);
        // 注册创造模式物品栏
        AncientSkyBaublesCreativeTabs.init(bus);

        // 监听渲染相关事件
        bus.register(ClientRenderListener.class);

        // 注册 mod 本身以供服务器与其他游戏事件使用
        MinecraftForge.EVENT_BUS.register(this);
    }
}

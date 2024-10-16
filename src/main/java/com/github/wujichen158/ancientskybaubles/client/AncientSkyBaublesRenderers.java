package com.github.wujichen158.ancientskybaubles.client;

import com.github.wujichen158.ancientskybaubles.client.renderer.ShoalSaltBlockEntityRenderer;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesBlockEntities;
import com.github.wujichen158.ancientskybaubles.util.Reference;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AncientSkyBaublesRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
//        event.registerBlockEntityRenderer(AncientSkyBaublesBlockEntities.SHOAL_SALT_BLOCK_ENTITY.get(),
//                ShoalSaltBlockEntityRenderer::new);
    }
}
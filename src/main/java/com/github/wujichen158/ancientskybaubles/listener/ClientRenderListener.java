package com.github.wujichen158.ancientskybaubles.listener;

import com.github.wujichen158.ancientskybaubles.client.renderer.MedalCuriosRenderer;
import com.github.wujichen158.ancientskybaubles.client.renderer.RegenerableBlockEntityRenderer;
import com.github.wujichen158.ancientskybaubles.client.renderer.RegenerableModels;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesBlockEntities;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesCuriosRenderers;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


public class ClientRenderListener {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            if (ModList.get().isLoaded("curios")) {
                AncientSkyBaublesCuriosRenderers.registerCuriosRenderers();
            }
        });
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MedalCuriosRenderer.MEDAL,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.0F),
                        64, 64));
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(AncientSkyBaublesBlockEntities.SHOAL_SALT_BLOCK_ENTITY.get(),
                RegenerableBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        RegenerableModels.registerAllModels(event);
    }

}

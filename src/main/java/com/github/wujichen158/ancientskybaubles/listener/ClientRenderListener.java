package com.github.wujichen158.ancientskybaubles.listener;

import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesRenderers;
import com.github.wujichen158.ancientskybaubles.renderer.MedalCuriosRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


public class ClientRenderListener {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            if (ModList.get().isLoaded("curios")) {
                AncientSkyBaublesRenderers.registerRenderers();
            }
        });
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MedalCuriosRenderer.MEDAL,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.0F),
                        64, 64));
    }
}

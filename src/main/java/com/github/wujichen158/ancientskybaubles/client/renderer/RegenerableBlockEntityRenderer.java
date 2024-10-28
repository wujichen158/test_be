package com.github.wujichen158.ancientskybaubles.client.renderer;

import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import com.github.wujichen158.ancientskybaubles.network.AncientSkyBaublesNetwork;
import com.github.wujichen158.ancientskybaubles.network.packet.regenerable.HarvestStatusRequestPacket;
import com.github.wujichen158.ancientskybaubles.util.GlobalPosUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;

@OnlyIn(Dist.CLIENT)
public class RegenerableBlockEntityRenderer implements BlockEntityRenderer<RegenerableBlockEntity> {

    public RegenerableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(RegenerableBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (blockEntity.harvestStatus != null) {
            // 命中
            RegenerableModels model = RegenerableModels.valueOf(BuiltInRegistries.BLOCK.getKey(
                    blockEntity.getBlockState().getBlock()).getPath());
            renderModel(model.getModel(blockEntity.harvestStatus), poseStack, bufferSource,
                    combinedLight, combinedOverlay);
        } else {
            // 未命中，发包给服务端查询
            if (!blockEntity.packetedFlag) {
                blockEntity.packetedFlag = true;
                GlobalPos globalPos = GlobalPosUtil.constructGlobalPos(blockEntity);
                AncientSkyBaublesNetwork.INSTANCE.send(new HarvestStatusRequestPacket(
                                Minecraft.getInstance().player.getUUID(), globalPos),
                        Minecraft.getInstance().getConnection().getConnection());
            }
        }
    }

    private void renderModel(ResourceLocation modelRl, PoseStack ms, MultiBufferSource bufferSource,
                             int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        BakedModel bakedModel = mc.getModelManager().getModel(modelRl);
        RenderType blockRenderType = RenderType.translucent();
        RenderType beRenderType = RenderTypeHelper.getEntityRenderType(blockRenderType, true);
        mc.getBlockRenderer().getModelRenderer().renderModel(ms.last(), bufferSource.getBuffer(beRenderType),
                null, bakedModel,
                1, 1, 1, combinedLightIn, combinedOverlayIn, ModelData.EMPTY, blockRenderType);
    }
}

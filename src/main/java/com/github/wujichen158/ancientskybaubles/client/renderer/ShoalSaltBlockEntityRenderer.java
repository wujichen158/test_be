package com.github.wujichen158.ancientskybaubles.client.renderer;

import com.github.wujichen158.ancientskybaubles.block.entity.ShoalSaltBlockEntity;
import com.github.wujichen158.ancientskybaubles.client.cache.RegenerableBlockEntityCache;
import com.github.wujichen158.ancientskybaubles.network.AncientSkyBaublesNetwork;
import com.github.wujichen158.ancientskybaubles.network.packet.regenerable.HarvestStatusRequestPacket;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class ShoalSaltBlockEntityRenderer implements BlockEntityRenderer<ShoalSaltBlockEntity> {

    // 定义两种状态常量：未开采和已开采
//    private static final BlockState UNHARVESTED_STATE = AncientSkyBaublesBlocks.RegenerableBlocks.SHOAL_SALT_BLOCK
//            .get().defaultBlockState().setValue(ShoalSaltBlock.HARVESTED, false);
    private static final BlockState HARVESTED_STATE = AncientSkyBaublesBlocks.RegenerableBlocks.SHOAL_SALT_BLOCK
            .get().defaultBlockState();

    public ShoalSaltBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ShoalSaltBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (RegenerableBlockEntityCache.hasHarvestStatus(blockEntity)) {
            if (RegenerableBlockEntityCache.queryHarvestStatus(blockEntity)) {
                // 渲染已开采的模型
                renderModel("shoal_salt_mined", poseStack, bufferSource, combinedLight, combinedOverlay);
            } else {
                // 渲染未开采的模型
                renderModel("shoal_salt", poseStack, bufferSource, combinedLight, combinedOverlay);
            }
        } else {
            // 发包给服务端查询
            AncientSkyBaublesNetwork.INSTANCE.send(new HarvestStatusRequestPacket(
                            Minecraft.getInstance().player.getUUID(),
                            RegenerableBlockEntityCache.constructGlobalPos(blockEntity)),
                    Minecraft.getInstance().getConnection().getConnection());
        }
    }

    // 辅助方法用于渲染不同的模型
    private void renderModel(String modelName, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        // 使用 ResourceLocation 加载模型
        ResourceLocation modelLocation = new ResourceLocation("ancientskybaubles", "block/" + modelName);
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelLocation);

        if (model == Minecraft.getInstance().getModelManager().getMissingModel()) {
            System.out.println("模型未找到，使用了缺失模型");
        }

//        renderModel(p_110914_.last(),
//                p_110915_.getBuffer(renderType != null ? renderType : RenderTypeHelper.getEntityRenderType(rt, false)),
//                p_110913_, bakedmodel, f, f1, f2, p_110916_, p_110917_, modelData, rt);

        // 渲染模型
        RenderType renderType = RenderType.translucent();
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(),
                bufferSource.getBuffer(renderType),
                null, model, 1.0F, 1.0F, 1.0F, combinedLight, combinedOverlay,
                ModelData.EMPTY, renderType);
    }
}

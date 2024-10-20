package com.github.wujichen158.ancientskybaubles.client.renderer;

import com.github.wujichen158.ancientskybaubles.block.RegenerableBlock;
import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import com.github.wujichen158.ancientskybaubles.client.cache.RegenerableBlockEntityCache;
import com.github.wujichen158.ancientskybaubles.network.AncientSkyBaublesNetwork;
import com.github.wujichen158.ancientskybaubles.network.packet.regenerable.HarvestStatusRequestPacket;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;

public class ShoalSaltBlockEntityRenderer implements BlockEntityRenderer<RegenerableBlockEntity> {

    // TODO: 寻找其他无需定义BlockState的方式
    // 定义两种状态常量：未开采和已开采
    private static final BlockState UNHARVESTED_STATE = AncientSkyBaublesBlocks.RegenerableBlocks.SHOAL_SALT_BLOCK
            .get().defaultBlockState().setValue(RegenerableBlock.HARVESTED, false);
    private static final BlockState HARVESTED_STATE = AncientSkyBaublesBlocks.RegenerableBlocks.SHOAL_SALT_BLOCK
            .get().defaultBlockState().setValue(RegenerableBlock.HARVESTED, true);

    public ShoalSaltBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(RegenerableBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (RegenerableBlockEntityCache.hasHarvestStatus(blockEntity)) {
            // 命中
            Boolean res = RegenerableBlockEntityCache.queryHarvestStatus(blockEntity);
            if (res != null) {
                BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
                BlockState blockState = res ? HARVESTED_STATE : UNHARVESTED_STATE;
                renderModel(blockState, blockRenderDispatcher, poseStack, bufferSource, combinedLight, combinedOverlay);
            }
        } else {
            // 未命中，发包给服务端查询
            GlobalPos globalPos = RegenerableBlockEntityCache.constructGlobalPos(blockEntity);
            AncientSkyBaublesNetwork.INSTANCE.send(new HarvestStatusRequestPacket(
                            Minecraft.getInstance().player.getUUID(), globalPos),
                    Minecraft.getInstance().getConnection().getConnection());
            // 仅发一次即可
            RegenerableBlockEntityCache.addHarvestStatus(globalPos, null);
        }
    }

    private void renderModel(BlockState blockState, BlockRenderDispatcher blockRenderDispatcher, PoseStack poseStack,
                             MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        BakedModel bakedModel = blockRenderDispatcher.getBlockModel(blockState);
        for (RenderType blockRenderType : bakedModel.getRenderTypes(blockState, RandomSource.create(42), ModelData.EMPTY)) {
            RenderType entityRenderType = RenderTypeHelper.getEntityRenderType(blockRenderType, true);
            blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(),
                    bufferSource.getBuffer(entityRenderType), blockState, bakedModel,
                    1, 1, 1, combinedLight, combinedOverlay, ModelData.EMPTY, blockRenderType);
        }

    }
}

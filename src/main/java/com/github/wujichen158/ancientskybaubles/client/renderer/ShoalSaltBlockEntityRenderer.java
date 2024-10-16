package com.github.wujichen158.ancientskybaubles.client.renderer;

import com.github.wujichen158.ancientskybaubles.block.entity.ShoalSaltBlockEntity;
import com.github.wujichen158.ancientskybaubles.register.AncientSkyBaublesBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        if (blockEntity.harvestStatus) {
//            System.out.println("has harvested!");
            // 渲染已开采的模型
//            renderHarvestedModel(blockRenderDispatcher, poseStack, bufferSource, combinedLight, combinedOverlay);
        } else {
//            System.out.println("unharvested");
            // 渲染未开采的模型
//            renderUnharvestedModel(blockRenderDispatcher, poseStack, bufferSource, combinedLight, combinedOverlay);
        }
    }

    private void renderHarvestedModel(BlockRenderDispatcher blockRenderDispatcher, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        blockRenderDispatcher.renderSingleBlock(HARVESTED_STATE, poseStack, bufferSource, combinedLight, combinedOverlay,
                ModelData.EMPTY, null);
    }

    private void renderUnharvestedModel(BlockRenderDispatcher blockRenderDispatcher, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        // TODO: 更改渲染方法
        blockRenderDispatcher.renderSingleBlock(HARVESTED_STATE, poseStack, bufferSource, combinedLight, combinedOverlay,
                ModelData.EMPTY, null);
    }
}

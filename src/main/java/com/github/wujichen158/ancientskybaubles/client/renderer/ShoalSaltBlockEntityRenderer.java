package com.github.wujichen158.ancientskybaubles.client.renderer;

import com.github.wujichen158.ancientskybaubles.block.ShoalSaltBlock;
import com.github.wujichen158.ancientskybaubles.block.entity.ShoalSaltBlockEntity;
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
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class ShoalSaltBlockEntityRenderer implements BlockEntityRenderer<ShoalSaltBlockEntity> {

    // TODO: 寻找其他无需定义BlockState的方式
    // 定义两种状态常量：未开采和已开采
    private static final BlockState UNHARVESTED_STATE = AncientSkyBaublesBlocks.RegenerableBlocks.SHOAL_SALT_BLOCK
            .get().defaultBlockState().setValue(ShoalSaltBlock.HARVESTED, false);
    private static final BlockState HARVESTED_STATE = AncientSkyBaublesBlocks.RegenerableBlocks.SHOAL_SALT_BLOCK
            .get().defaultBlockState().setValue(ShoalSaltBlock.HARVESTED, true);

    public ShoalSaltBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ShoalSaltBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (RegenerableBlockEntityCache.hasHarvestStatus(blockEntity)) {
            // 命中
            BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
            BlockState blockState = RegenerableBlockEntityCache.queryHarvestStatus(blockEntity) ? HARVESTED_STATE : UNHARVESTED_STATE;
            renderModel(blockState, blockRenderDispatcher, poseStack, bufferSource, combinedLight, combinedOverlay);
        } else {
            // 未命中，发包给服务端查询
            AncientSkyBaublesNetwork.INSTANCE.send(new HarvestStatusRequestPacket(
                            Minecraft.getInstance().player.getUUID(),
                            RegenerableBlockEntityCache.constructGlobalPos(blockEntity)),
                    Minecraft.getInstance().getConnection().getConnection());
        }
    }

    private void renderModel(BlockState blockState, BlockRenderDispatcher blockRenderDispatcher, PoseStack poseStack,
                             MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
//        blockRenderDispatcher.renderSingleBlock(blockState, poseStack, bufferSource, combinedLight, combinedOverlay,
//                ModelData.EMPTY, RenderType.translucent());
        BakedModel bakedmodel = blockRenderDispatcher.getBlockModel(blockState);
        RenderType renderType = RenderType.translucent();
        int i = Minecraft.getInstance().getBlockColors().getColor(blockState, null, null, 0);
        float f = (float)(i >> 16 & 255) / 255.0F;
        float f1 = (float)(i >> 8 & 255) / 255.0F;
        float f2 = (float)(i & 255) / 255.0F;
        for (RenderType rt : bakedmodel.getRenderTypes(blockState, RandomSource.create(42), ModelData.EMPTY)) {
            blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(),
                    bufferSource.getBuffer(renderType), blockState, bakedmodel,
                    1, 1, 1, combinedLight, combinedOverlay, ModelData.EMPTY, rt);
        }

    }
}

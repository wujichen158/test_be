package com.github.wujichen158.ancientskybaubles.client.renderer;

import com.github.wujichen158.ancientskybaubles.block.entity.RegenerableBlockEntity;
import com.github.wujichen158.ancientskybaubles.network.AncientSkyBaublesNetwork;
import com.github.wujichen158.ancientskybaubles.network.packet.regenerable.HarvestStatusRequestPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class RegenerableBlockEntityRenderer implements BlockEntityRenderer<RegenerableBlockEntity> {

    /**
     * 5 个对象分别为北、南、西、东、无
     */
    private static final Quaternionf[] HORIZONTAL_ROTATIONS = {
            new Quaternionf().rotateY(0),
            new Quaternionf().rotateY((float) Math.PI),
            new Quaternionf().rotateY((float) Math.PI / 2),
            new Quaternionf().rotateY(-(float) Math.PI / 2),
            new Quaternionf()
    };

    public RegenerableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(RegenerableBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (blockEntity.harvestStatus != null) {
            // 命中
            renderModel(blockEntity, poseStack, bufferSource, combinedLight, combinedOverlay);
        } else {
            // 未命中，发包给服务端查询
            if (!blockEntity.packetedFlag) {
                blockEntity.packetedFlag = true;
                AncientSkyBaublesNetwork.INSTANCE.send(new HarvestStatusRequestPacket(
                                Minecraft.getInstance().player.getUUID(), blockEntity.getBlockPos()),
                        Minecraft.getInstance().getConnection().getConnection());
            }
        }
    }

    private void renderModel(RegenerableBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource,
                             int combinedLightIn, int combinedOverlayIn) {
        BlockState state = blockEntity.getBlockState();

        // 将 state 中的朝向处理为旋转
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        // 平移旋转中心至方块中心、应用旋转并复原旋转中心
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(calHorizontalRot(facing));
        poseStack.translate(-0.5, 0, -0.5);

        // 通过 state 中的注册名来获取模型
        RegenerableModels model = RegenerableModels.valueOf(BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath());

        Minecraft mc = Minecraft.getInstance();
        BakedModel bakedModel = mc.getModelManager().getModel(model.getModel(blockEntity.harvestStatus));

        // 自动获取渲染类型防止类型不匹配
        RenderType beRenderType = RenderTypeHelper.getEntityRenderType(RenderType.translucent(), true);

        // 渲染模型
        mc.getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(beRenderType),
                null, bakedModel,
                1, 1, 1, combinedLightIn, combinedOverlayIn, ModelData.EMPTY, null);
    }

    /**
     * 可尝试使用以下方式，但不一定稳定：<br>
     * return HORIZONTAL_ROTATIONS[direction.get2DDataValue()];
     *
     * @param direction
     * @return
     */
    public Quaternionf calHorizontalRot(Direction direction) {
        return switch (direction) {
            case NORTH -> HORIZONTAL_ROTATIONS[0];
            case SOUTH -> HORIZONTAL_ROTATIONS[1];
            case WEST -> HORIZONTAL_ROTATIONS[2];
            case EAST -> HORIZONTAL_ROTATIONS[3];
            default -> HORIZONTAL_ROTATIONS[4];
        };
    }
}

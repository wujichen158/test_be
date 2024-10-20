package com.github.wujichen158.ancientskybaubles.client.renderer;

import com.github.wujichen158.ancientskybaubles.item.MedalCuriosItem;
import com.github.wujichen158.ancientskybaubles.util.Reference;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Matrix4f;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

@OnlyIn(Dist.CLIENT)
public class MedalCuriosRenderer implements ICurioRenderer {
    public static final ModelLayerLocation MEDAL = new ModelLayerLocation(
            new ResourceLocation(Reference.MOD_ID, "medal"), "main");

    private static final int LEFT_CHEST_ID = 0;
    private static final int MIDDLE_CHEST_ID = 1;
    private static final int RIGHT_CHEST_ID = 2;

    // 固定缩放，使渲染大小保持一致（无论图片大小如何）

    private static final float SCALE_WIDTH = 0.2f;
    private static final float SCALE_HEIGHT = 0.2f;
    private static final float SCALE_Z = 1f;

    private final HumanoidModel<LivingEntity> model;

    public MedalCuriosRenderer() {
        this.model = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(MEDAL));
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext,
                                                                          PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent,
                                                                          MultiBufferSource renderTypeBuffer, int light,
                                                                          float limbSwing, float limbSwingAmount,
                                                                          float partialTicks, float ageInTicks,
                                                                          float netHeadYaw, float headPitch) {
        if (stack.getItem() instanceof MedalCuriosItem medalCuriosItem) {
            // 跟随玩家身体旋转，保持徽章位置固定
            ICurioRenderer.followBodyRotations(slotContext.entity(), this.model);

            matrixStack.pushPose();

            // 移动矩阵到玩家左胸前，可以根据需要进行微调
            // 基于第一人称视角：
            // x 以身体中线为 0，左加右减
            // y 以肩膀为 0，下加上减
            // z 以身体中线为 0，后加前减
            switch (slotContext.index()) {
                case LEFT_CHEST_ID:
                    // 左胸
                    matrixStack.translate(0.2D, 0.15D, -0.2D);
                    break;
                case RIGHT_CHEST_ID:
                    // 右胸
                    matrixStack.translate(-0.2D, 0.15D, -0.2D);
                    break;
                case MIDDLE_CHEST_ID:
                default:
                    // 前胸，默认
                    matrixStack.translate(0D, 0.25D, -0.2D);
                    break;
            }

            // 确保徽章按比例显示
            matrixStack.scale(SCALE_WIDTH, SCALE_HEIGHT, SCALE_Z);

            // 渲染徽章
            VertexConsumer vertexConsumer = renderTypeBuffer.getBuffer(RenderType.entityTranslucent(new ResourceLocation(
                    Reference.MOD_ID, "textures/item/" + ForgeRegistries.ITEMS.getKey(medalCuriosItem).getPath() + ".png"
            )));
            renderBadgeQuad(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);

            matrixStack.popPose();
        }
    }

    /**
     * 顶点渲染，定义一个正方形
     *
     * @param matrixStack
     * @param vertexConsumer
     * @param light
     * @param overlay
     */
    private void renderBadgeQuad(PoseStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay) {
        matrixStack.pushPose();

        Matrix4f matrix = matrixStack.last().pose();
        vertexConsumer.vertex(matrix, -0.5F, -0.5F, 0.0F)
                .color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 0.0F)
                .overlayCoords(overlay).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix, 0.5F, -0.5F, 0.0F)
                .color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 0.0F)
                .overlayCoords(overlay).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix, 0.5F, 0.5F, 0.0F)
                .color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 1.0F)
                .overlayCoords(overlay).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix, -0.5F, 0.5F, 0.0F)
                .color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 1.0F)
                .overlayCoords(overlay).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        matrixStack.popPose();
    }
}

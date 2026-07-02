package net.quedoon.giant_potato.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.quedoon.giant_potato.block.entity.custom.MashTankBlockEntity;

// Credits to TurtyWurty
// Under MIT-License: https://github.com/DaRealTurtyWurty/1.20-Tutorial-Mod?tab=MIT-1-ov-file#readme
// Major Rewrites for Fabric
public class MashTankBlockEntityRenderer implements BlockEntityRenderer<MashTankBlockEntity> {
    public MashTankBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(MashTankBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        FluidVariant fluidStack = entity.getFluid();
        if (fluidStack.isBlank())
            return;

        Level level = entity.getLevel();
        if (level == null)
            return;

        final TextureAtlasSprite sprite = FluidVariantRendering.getSprite(fluidStack);
        int color = FluidVariantRendering.getColor(fluidStack);
        FluidState state = fluidStack.getFluid().defaultFluidState();

        float height = (((float) entity.fluidStorage.getAmount() / entity.fluidStorage.getCapacity()) * 0.625f) + 0.25f;

        VertexConsumer builder = vertexConsumers.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));

        // Top Texture
        drawQuad(builder, matrices, 0.1f, height, 0.1f, 0.9f, height, 0.9f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), light, color);
        drawQuad(builder, matrices, 0.1f, 0, 0.1f, 0.9f, height, 0.1f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), light, color);

        matrices.pushPose();
        matrices.mulPose(Axis.XP.rotationDegrees(180));
        matrices.translate(0, 0f, -1f);
        drawQuad(builder, matrices, 0.1f, -0.01f, 0.1f, 0.9f, -0.01f, 0.9f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), light, color);
        matrices.popPose();

        matrices.pushPose();
        matrices.mulPose(Axis.YP.rotationDegrees(180));
        matrices.translate(-1f, 0, -1.8f);
        drawQuad(builder, matrices, 0.1f, 0, 0.9f, 0.9f, height, 0.9f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), light, color);
        matrices.popPose();

        matrices.pushPose();
        matrices.mulPose(Axis.YP.rotationDegrees(90));
        matrices.translate(-1f, 0, 0);
        drawQuad(builder, matrices, 0.1f, 0, 0.1f, 0.9f, height, 0.1f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), light, color);
        matrices.popPose();

        matrices.pushPose();
        matrices.mulPose(Axis.YN.rotationDegrees(90));
        matrices.translate(0, 0, -1f);
        drawQuad(builder, matrices, 0.1f, 0, 0.1f, 0.9f, height, 0.1f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), light, color);
        matrices.popPose();
    }

    private static void drawVertex(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int packedLight, int color) {
        builder.addVertex(poseStack.last().copy(), x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setLight(packedLight)
                .setNormal(1, 0, 0);
    }

    private static void drawQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int packedLight, int color) {
        drawVertex(builder, poseStack, x0, y0, z0, u0, v0, packedLight, color);
        drawVertex(builder, poseStack, x0, y1, z1, u0, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y1, z1, u1, v1, packedLight, color);
        drawVertex(builder, poseStack, x1, y0, z0, u1, v0, packedLight, color);
    }
}

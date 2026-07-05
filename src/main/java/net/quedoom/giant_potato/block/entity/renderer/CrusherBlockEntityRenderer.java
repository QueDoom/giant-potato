package net.quedoom.giant_potato.block.entity.renderer;

import com.jcraft.jorbis.Block;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.quedoom.giant_potato.block.entity.custom.CrusherBlockEntity;
import net.quedoom.giant_potato.util.ModTags;
import org.joml.Matrix4f;

public class CrusherBlockEntityRenderer implements BlockEntityRenderer<CrusherBlockEntity> {
    public CrusherBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(CrusherBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = blockEntity.getItem(0);

        poseStack.pushPose();

        poseStack.translate(blockEntity.getXZPosForRenderItem().x, blockEntity.getYPosForRenderItem(), blockEntity.getXZPosForRenderItem().y);

        poseStack.scale(0.75f, blockEntity.getScaleForRenderItem(), 0.75f);

        poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRotationForRenderItem()));

        int light = getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos());

        if (stack.getItem() instanceof BlockItem blockItem && !stack.is(ModTags.Items.CRUSHER_RENDER_BLOCK_AS_ITEM)) {
            blockRenderer.renderSingleBlock(blockItem.getBlock().defaultBlockState(), poseStack, multiBufferSource, light, OverlayTexture.NO_OVERLAY);
        } else {
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light,
                    OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, blockEntity.getLevel(), 1);
        }
        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}

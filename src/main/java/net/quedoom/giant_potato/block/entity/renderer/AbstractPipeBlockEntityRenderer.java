package net.quedoom.giant_potato.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.quedoom.giant_potato.block.entity.util.block_entity.pipe.AbstractPipeBlockEntity;
import net.quedoom.giant_potato.mixin.access.DebugRendererAccess;
import net.quedoom.giant_potato.util.ModTags;
import org.joml.Vector3f;

public class AbstractPipeBlockEntityRenderer implements BlockEntityRenderer<AbstractPipeBlockEntity> {
    public AbstractPipeBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }
    @Override
    public void render(AbstractPipeBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Minecraft client = Minecraft.getInstance();
        if (client == null || client.level == null) return;
        if (((DebugRendererAccess) client.debugRenderer).showChunkBorder()) {
            renderInteractionBoxes(entity, matrices, vertexConsumers);
        }
    }

    private void renderInteractionBoxes(AbstractPipeBlockEntity entity, PoseStack matrices, MultiBufferSource vertexConsumers) {
        BlockState state = entity.getBlockState();
        if (!state.is(ModTags.Blocks.PIPES)) return;
        entity.getHitBoxes().forEach((identifier, hitBox) -> {
            Vector3f color = hitBox.getDebugColor();
            LevelRenderer.renderLineBox(matrices, vertexConsumers.getBuffer(RenderType.LINES), hitBox.getRotatedBox(Direction.NORTH),
                    color.x, color.y, color.z, 1f);
        });
    }
}

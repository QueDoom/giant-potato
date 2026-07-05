package net.quedoom.giant_potato.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.quedoom.giant_potato.block.entity.custom.MashBowlBlockEntity;

public class MashBowlBlockEntityRenderer implements BlockEntityRenderer<MashBowlBlockEntity> {
    public MashBowlBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(MashBowlBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = blockEntity.getItem(0);
        poseStack.pushPose();
//        poseStack.translate();

    }
}

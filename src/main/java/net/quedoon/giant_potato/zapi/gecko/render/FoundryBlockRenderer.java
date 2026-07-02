package net.quedoon.giant_potato.zapi.gecko.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.quedoon.giant_potato.block.entity.custom.FoundryBlockEntity;
import net.quedoon.giant_potato.zapi.gecko.model.FoundryBlockModel;

import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FoundryBlockRenderer extends GeoBlockRenderer<FoundryBlockEntity> {
    public FoundryBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new FoundryBlockModel());
    }
}

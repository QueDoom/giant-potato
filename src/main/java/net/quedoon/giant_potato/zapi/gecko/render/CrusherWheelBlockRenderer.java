package net.quedoon.giant_potato.zapi.gecko.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.quedoon.giant_potato.block.entity.custom.CrusherWheelBlockEntity;
import net.quedoon.giant_potato.zapi.gecko.model.CrusherWheelBlockModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CrusherWheelBlockRenderer extends GeoBlockRenderer<CrusherWheelBlockEntity> {
    public CrusherWheelBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new CrusherWheelBlockModel());
    }
}

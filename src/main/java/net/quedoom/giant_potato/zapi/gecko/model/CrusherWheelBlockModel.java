package net.quedoom.giant_potato.zapi.gecko.model;

import net.minecraft.resources.ResourceLocation;
import net.quedoom.giant_potato.block.entity.custom.CrusherWheelBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class CrusherWheelBlockModel extends GeoModel<CrusherWheelBlockEntity> {
    @Override
    public ResourceLocation getModelResource(CrusherWheelBlockEntity crusherWheelBlockEntity) {
        return GeoModelIdentifier.blockModel("crusher_wheel");
    }

    @Override
    public ResourceLocation getTextureResource(CrusherWheelBlockEntity crusherWheelBlockEntity) {
        return GeoModelIdentifier.blockTexture("crusher_wheel");
    }

    @Override
    public ResourceLocation getAnimationResource(CrusherWheelBlockEntity crusherWheelBlockEntity) {
        return GeoModelIdentifier.blockAnimation("crusher_wheel");
    }
}

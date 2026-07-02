package net.quedoom.giant_potato.zapi.gecko.model;

import net.minecraft.resources.ResourceLocation;
import net.quedoom.giant_potato.block.entity.custom.FoundryBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class FoundryBlockModel extends GeoModel<FoundryBlockEntity> {
    @Override
    public ResourceLocation getModelResource(FoundryBlockEntity foundryBlockEntity) {
        return GeoModelIdentifier.blockModel("foundry");
    }

    @Override
    public ResourceLocation getTextureResource(FoundryBlockEntity foundryBlockEntity) {
        return GeoModelIdentifier.blockTexture("foundry");
    }

    @Override
    public ResourceLocation getAnimationResource(FoundryBlockEntity foundryBlockEntity) {
        return GeoModelIdentifier.blockAnimation("foundry");
    }
}

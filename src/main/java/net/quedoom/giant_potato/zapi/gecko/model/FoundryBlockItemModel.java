package net.quedoom.giant_potato.zapi.gecko.model;

import net.minecraft.resources.ResourceLocation;
import net.quedoom.giant_potato.block.item.FoundryBlockItem;
import software.bernie.geckolib.model.GeoModel;

public class FoundryBlockItemModel extends GeoModel<FoundryBlockItem> {
    @Override
    public ResourceLocation getModelResource(FoundryBlockItem foundryBlockItem) {
        return GeoModelIdentifier.itemModel("foundry");
    }

    @Override
    public ResourceLocation getTextureResource(FoundryBlockItem foundryBlockItem) {
        return GeoModelIdentifier.blockTexture("foundry");
    }

    @Override
    public ResourceLocation getAnimationResource(FoundryBlockItem foundryBlockItem) {
        return GeoModelIdentifier.itemAnimation("foundry");
    }
}

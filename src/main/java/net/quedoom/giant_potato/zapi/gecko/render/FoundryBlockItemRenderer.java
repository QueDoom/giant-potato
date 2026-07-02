package net.quedoom.giant_potato.zapi.gecko.render;

import net.quedoom.giant_potato.block.item.FoundryBlockItem;
import net.quedoom.giant_potato.zapi.gecko.model.FoundryBlockItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FoundryBlockItemRenderer extends GeoItemRenderer<FoundryBlockItem> {
    public FoundryBlockItemRenderer() {
        super(new FoundryBlockItemModel());
    }
}

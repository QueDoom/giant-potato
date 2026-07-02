package net.quedoon.giant_potato.fluid.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public interface EmptyableItemContainer {
    public default Item getEmptyItem() {
        return Items.BUCKET;
    }
}

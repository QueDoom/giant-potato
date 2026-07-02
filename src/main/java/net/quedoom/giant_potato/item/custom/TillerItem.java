package net.quedoom.giant_potato.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TillerItem extends Item {
    public TillerItem(Properties settings) {
        super(settings);
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        if (stack.getDamageValue() < stack.getMaxDamage() - 1) {
            ItemStack moreDamaged = stack.copy();
            moreDamaged.setDamageValue(stack.getDamageValue() + 1);
            return moreDamaged;
        }

        return ItemStack.EMPTY;
    }
}

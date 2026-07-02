package net.quedoom.giant_potato.recipe;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record FoundryRecipeInput(List<ItemStack> input) implements RecipeInput {
    @Override
    public ItemStack getItem(int slot) {
        return input.get(slot);
    }

    @Override
    public int size() {
        return 3;
    }
}

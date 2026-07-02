package net.quedoom.giant_potato.zapi.emi.recipes;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.quedoom.giant_potato.recipe.CrusherRecipe;
import net.quedoom.giant_potato.zapi.emi.GiantPotatoEmiPlugin;

public class CrusherRecipeEmiRecipe extends BasicEmiRecipe {

    public CrusherRecipeEmiRecipe(RecipeHolder<CrusherRecipe> recipe) {
        super(GiantPotatoEmiPlugin.CRUSHER_CATEGORY, recipe.id(), 76, 18);
        this.inputs.add(EmiIngredient.of(recipe.value().getIngredients().get(0)));
        this.outputs.add(EmiStack.of(recipe.value().getResultItem(null)));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        widgetHolder.addSlot(inputs.get(0), 0, 0);
        widgetHolder.addSlot(outputs.get(0), 58, 0).recipeContext(this);
    }

    @Override
    public boolean supportsRecipeTree() {
        return true;
    }
}

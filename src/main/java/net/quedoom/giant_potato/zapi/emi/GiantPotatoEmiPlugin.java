package net.quedoom.giant_potato.zapi.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.quedoom.giant_potato.GiantPotato;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.recipe.CrusherRecipe;
import net.quedoom.giant_potato.recipe.ModRecipes;
import net.quedoom.giant_potato.zapi.emi.recipes.CrusherRecipeEmiRecipe;

public class GiantPotatoEmiPlugin implements EmiPlugin {
    public static final ResourceLocation CRUSHER_SPRITES = ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "textures/gui/crusher/crusher_emi");
    public static final EmiStack CRUSHER_WORKSTATION = EmiStack.of(ModBlocks.CRUSHER);
    public static final EmiRecipeCategory CRUSHER_CATEGORY =
            new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "crusher_workstation"),
                    CRUSHER_WORKSTATION, new EmiTexture(CRUSHER_SPRITES, 0, 0, 82, 32));



    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addCategory(CRUSHER_CATEGORY);
        emiRegistry.addWorkstation(CRUSHER_CATEGORY, CRUSHER_WORKSTATION);

        RecipeManager manager = emiRegistry.getRecipeManager();

        for (RecipeHolder<CrusherRecipe> recipe : manager.getAllRecipesFor(ModRecipes.CRUSHER_TYPE)) {
            emiRegistry.addRecipe(new CrusherRecipeEmiRecipe(recipe));
        }
    }
}

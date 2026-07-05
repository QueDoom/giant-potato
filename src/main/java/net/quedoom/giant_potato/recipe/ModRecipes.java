package net.quedoom.giant_potato.recipe;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.quedoom.giant_potato.GiantPotato;

public class ModRecipes {

    public static final RecipeSerializer<FoundryRecipe> FOUNDRY_SERIALIZER = registerRecipeSerializer("alloying", new FoundryRecipe.Serializer());
    public static final RecipeType<FoundryRecipe>FOUNDRY_TYPE = registerRecipeType("alloying");

    public static final RecipeSerializer<CrusherRecipe> CRUSHER_SERIALIZER = registerRecipeSerializer("crushing", new CrusherRecipe.Serializer());
    public static final RecipeType<CrusherRecipe> CRUSHER_TYPE = registerRecipeType("crushing");



    private static <T extends Recipe<?>> RecipeSerializer<T> registerRecipeSerializer(String path, RecipeSerializer<T> serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, path), serializer);
    }

    private static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String path) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, path), new RecipeType<>() {
            @Override
            public String toString() {
                return path;
            }
        });
    }

    public static void registerRecipes() {
        GiantPotato.LOGGER.info("Registering Mod Recipes for " + GiantPotato.MOD_ID);
    }
}

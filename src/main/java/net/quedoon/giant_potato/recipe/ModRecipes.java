package net.quedoon.giant_potato.recipe;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.quedoon.giant_potato.GiantPotato;
import net.quedoon.giant_potato.GiantPotatoClient;

public class ModRecipes {

    public static final RecipeSerializer<FoundryRecipe> FOUNDRY_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "alloying"), new FoundryRecipe.Serializer());
    public static final RecipeType<FoundryRecipe> FOUNDRY_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "alloying"), new RecipeType<>() {
                @Override
                public String toString() {
                    return "alloying";
                }
            });
    public static final RecipeSerializer<CrusherRecipe> CRUSHER_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "crushing"), new CrusherRecipe.Serializer());
    public static final RecipeType<CrusherRecipe> CRUSHER_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "crushing"), new RecipeType<>() {
                @Override
                public String toString() {
                    return "crushing";
                }
            });



    public static final ResourceLocation FOUNDRY_ID = ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "alloying");
    public static final ResourceLocation CRUSHER_ID = ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "crushing");
    public static final ResourceLocation ASSEMBLER_ID = ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "assembling");
    public static final ResourceLocation PRESS_ID = ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "pressing");


    public static void registerRecipes() {
        GiantPotato.LOGGER.info("Registering Mod Recipes for " + GiantPotato.MOD_ID);
    }
}

package net.quedoom.giant_potato.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeExporter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.TILLER)
                .requires(Items.DIAMOND_HOE)
                .requires(Items.POISONOUS_POTATO)
                .unlockedBy(getHasName(Items.POTATO), has(Items.POTATO))
                .group("tiller")
                .save(recipeExporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MASH_BOWL)
                .pattern("   ")
                .pattern("S S")
                .pattern("SSS")
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy(getHasName(Items.OAK_SLAB), has(ItemTags.WOODEN_SLABS))
                .group("mash_bowl")
                .save(recipeExporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FERTILIZER_DIRT)
                .requires(ModItems.TILLER)
                .requires(Items.DIRT)
                .requires(Items.POISONOUS_POTATO)
                .unlockedBy(getHasName(ModItems.TILLER), has(ModItems.TILLER))
                .group("fertilizer_dirt")
                .save(recipeExporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SMOOTH_POTATOES, 8)
                .pattern("SSS")
                .pattern("SFS")
                .pattern("SSS")
                .define('S', Blocks.SMOOTH_STONE)
                .define('F', ModItems.FERTILIZER_DIRT)
                .unlockedBy(getHasName(Blocks.SMOOTH_STONE), has(Blocks.SMOOTH_STONE))
                .group("smooth_potatoes")
                .save(recipeExporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.FOUNDRY)
                .pattern("III")
                .pattern("IFI")
                .pattern("SSS")
                .define('S', ModBlocks.SMOOTH_POTATOES)
                .define('F', ModItems.FERTILIZER_DIRT)
                .define('I', Items.IRON_INGOT)
                .unlockedBy(getHasName(ModItems.FERTILIZER_DIRT), has(ModItems.FERTILIZER_DIRT))
                .group("foundry")
                .save(recipeExporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MIDAS_HAND)
                .pattern(" GG")
                .pattern(" GG")
                .pattern("L  ")
                .define('G', Blocks.GOLD_BLOCK)
                .define('L', Items.LEATHER)
                .unlockedBy(getHasName(Blocks.GOLD_BLOCK), has(Blocks.GOLD_BLOCK))
                .group("midas_hand")
                .save(recipeExporter);

        List<ItemLike> input_charred_potato = List.of(Items.POTATO);
        oreBlasting(recipeExporter, input_charred_potato, RecipeCategory.MISC, ModItems.CHARRED_POTATO, 0.2f, 200, "charred_potato");
    }
}

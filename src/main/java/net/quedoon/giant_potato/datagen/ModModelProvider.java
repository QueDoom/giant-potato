package net.quedoon.giant_potato.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.quedoon.giant_potato.block.ModBlocks;
import net.quedoon.giant_potato.fluid.ModFluids;
import net.quedoon.giant_potato.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.family(ModBlocks.SMOOTH_POTATOES);
//        BlockStateModelGenerator.createSlabBlockState(ModBlocks.SMOOTH_POTATOES_SLAB, Identifier.of(GiantPotato.MOD_ID, "smooth_potatoes_slab_bottom"),
//                Identifier.of(GiantPotato.MOD_ID, "smooth_potatoes_slab_top"), Identifier.of(GiantPotato.MOD_ID, "smooth_potatoes_slab_full"));
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.TILLER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.WRENCH, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.CRUSHER_WHEEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModFluids.MASH_BUCKET, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModFluids.POISONOUS_MASH_BUCKET, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModFluids.SEWER_WATER_BUCKET, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(ModItems.CHARRED_POTATO, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.BIOSTEEL_ALLOY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.POTATO_ALLOY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.POISONOUS_POTATO_ALLOY, ModelTemplates.FLAT_ITEM);
    }
}

package net.quedoom.giant_potato.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.fluid.ModFluids;
import net.quedoom.giant_potato.item.ModItems;
import net.quedoom.giant_potato.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModLangGenerator extends FabricLanguageProvider {
    public ModLangGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
        // Blocks
        translationBuilder.add(ModBlocks.MASH_BOWL, "Mash Bowl");
        translationBuilder.add(ModBlocks.MASH_TANK, "Mash Tank");
        translationBuilder.add(ModBlocks.MASH_PIPE, "Mash Pipe");
        translationBuilder.add(ModBlocks.SMOOTH_POTATOES, "Smooth Potatoes");
        translationBuilder.add(ModItems.FOUNDRY, "Foundry");
        translationBuilder.add(ModItems.CRUSHER_WHEEL, "Crushing Wheel");
        translationBuilder.add(ModBlocks.CRUSHER, "Crusher");
        translationBuilder.add(ModBlocks.SEWER_DRAIN, "Sewer Drain");
        translationBuilder.add(ModBlocks.SEWER, "Sewer");

        // Items
        translationBuilder.add(ModItems.TILLER, "Tiller");
        translationBuilder.add(ModItems.FERTILIZER_DIRT, "Fertilizer Dirt");
        translationBuilder.add(ModItems.CHARRED_POTATO, "Charred Potato");
        translationBuilder.add(ModItems.BIOSTEEL_ALLOY, "Biosteel Alloy");
        translationBuilder.add(ModItems.POTATO_ALLOY, "Potato Alloy");
        translationBuilder.add(ModItems.POISONOUS_POTATO_ALLOY, "Poisonous Potato Alloy");
        translationBuilder.add(ModItems.WRENCH, "Wrench");
        translationBuilder.add(ModItems.MIDAS_HAND, "Midas's Hand");
        translationBuilder.add(ModFluids.MASH_BUCKET, "Mash Bucket");
        translationBuilder.add(ModFluids.SEWER_WATER_BUCKET, "Sewer Water Bucket");



        // Tags
        translationBuilder.add(ModTags.Items.MASH_BOWL_POTATO, "Mash Bowl: Potato");
        translationBuilder.add(ModTags.Items.MASH_BOWL_POISONOUS_POTATO, "Mash Bowl: Poisonous Potato");
        translationBuilder.add(ModTags.Fluid.MASH, "Mash");
        translationBuilder.add(ModTags.Fluid.POISONOUS_MASH, "Poisonous Mash");
        translationBuilder.add(ModTags.Fluid.MASH_FLUIDS, "Mash Fluids");

        // Fluids
        translationBuilder.add(ModFluids.MASH_BLOCK, "Mash");
        translationBuilder.add("block.giant_potato.mash", "Mash");
        translationBuilder.add("block.giant_potato.sewer_water", "Sewer Water");
        translationBuilder.add("block.giant_potato.sewer_water_block", "Sewer Water");

        // GUI
        translationBuilder.add("gui.giant_potato.foundry", "Alloying");
        translationBuilder.add("gui.giant_potato.crusher", "Crushing");
        translationBuilder.add("emi.category.giant_potato.crusher_workstation", "Crushing");
        translationBuilder.add("gui.giant_potato.mash_tank", "Mash Tank");

        // Jade
        translationBuilder.add("component.giant_potato.mash", "%s / %s Mash");

        // EMI
        translationBuilder.add("giant_potato.emi.info.mash_buckets", "Mash can be obtained by throwing potatoes on a Mash Bowl and then jumping on it.");

        // Other
        translationBuilder.add("itemGroup.giant_potato", "Giant Potato");

        translationBuilder.add("giant_potato.tooltip.liquid.amount.with.capacity", "%s / %s mB");
        translationBuilder.add("giant_potato.tooltip.liquid.amount", "%s mB");
        translationBuilder.add("block.minecraftDir.empty","Empty");
    }
}

package net.quedoom.giant_potato.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.FluidTags;
import net.quedoom.giant_potato.fluid.ModFluids;
import net.quedoom.giant_potato.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModFluidTagProvider extends FabricTagProvider.FluidTagProvider {
    public ModFluidTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        getOrCreateTagBuilder(FluidTags.WATER)
                .add(ModFluids.MASH).add(ModFluids.MASH_FLOWING_UNUSED)
                .add(ModFluids.POISONOUS_MASH).add(ModFluids.POISONOUS_MASH_FLOWING_UNUSED);

        getOrCreateTagBuilder(ModTags.Fluid.MASH)
                .add(ModFluids.MASH).add(ModFluids.MASH_FLOWING_UNUSED);
        getOrCreateTagBuilder(ModTags.Fluid.POISONOUS_MASH)
                .add(ModFluids.POISONOUS_MASH).add(ModFluids.POISONOUS_MASH_FLOWING_UNUSED);
        getOrCreateTagBuilder(ModTags.Fluid.MASH_FLUIDS)
                .addTag(ModTags.Fluid.MASH).addTag(ModTags.Fluid.POISONOUS_MASH);
    }
}

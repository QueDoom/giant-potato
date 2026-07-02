package net.quedoom.giant_potato.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        // getOrCreateTagBuilder()

        getOrCreateTagBuilder(ModTags.Blocks.ELEMENTAL_SMOOTH_POTATOES).add(ModBlocks.SMOOTH_POTATOES);

        getOrCreateTagBuilder(ModTags.Blocks.MASH_BOWLS).add(ModBlocks.MASH_BOWL);

        getOrCreateTagBuilder(ModTags.Blocks.MASH_PIPE_CONNECT_TO)
                .addTag(ModTags.Blocks.MASH_PIPES)
                .addTag(ModTags.Blocks.MASH_MACHINES)
        ;

        getOrCreateTagBuilder(ModTags.Blocks.MIDAS_HAND_WORKS_ON)
                .add(Blocks.MUSHROOM_STEM).add(Blocks.RED_MUSHROOM_BLOCK).add(Blocks.BROWN_MUSHROOM_BLOCK);

        getOrCreateTagBuilder(ModTags.Blocks.PIPES)
                .addTag(ModTags.Blocks.MASH_PIPES)
        ;

        getOrCreateTagBuilder(ModTags.Blocks.MASH_PIPES)
                .add(ModBlocks.MASH_PIPE)
                .add(ModBlocks.MASH_PIPE_OUTPUT)
        ;

        getOrCreateTagBuilder(ModTags.Blocks.MASH_MACHINES)
                .add(ModBlocks.FOUNDRY)
                .add(ModBlocks.CRUSHER)
                .add(ModBlocks.MASH_TANK)
                .addTag(ModTags.Blocks.MASH_BOWLS)
        ;

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.SMOOTH_POTATOES);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.FOUNDRY).add(ModBlocks.SEWER_DRAIN).add(ModBlocks.SEWER);
    }
}

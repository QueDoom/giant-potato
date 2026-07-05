package net.quedoom.giant_potato.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.quedoom.giant_potato.GiantPotato;
import net.quedoom.giant_potato.block.custom.*;

public class ModBlocks {

    public static final Block MASH_BOWL = registerBlock("mash_bowl",
            new MashBowlBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD)
                    .strength(1.0f).explosionResistance(1.0f)));
    public static final Block MASH_TANK = registerBlock("mash_tank",
            new MashTankBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD)
                    .strength(1.0F).explosionResistance(1.0F).noOcclusion()));
    public static final Block MASH_PIPE = registerBlock("mash_pipe",
            new MashPipeBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD)
                    .strength(1.0F).explosionResistance(1.0F).noOcclusion()));
    public static final Block MASH_PIPE_OUTPUT = registerBlock("mash_pipe_output",
            new MashPipeOutputBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD)
                    .strength(1.0F).explosionResistance(1.0F).noOcclusion()));
    public static final Block SMOOTH_POTATOES = registerBlock("smooth_potatoes",
            new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE)
                    .strength(3.0F).explosionResistance(3.0F).requiresCorrectToolForDrops()));
    public static final Block SMOOTH_POTATOES_SLAB = registerBlock("smooth_potatoes_slab",
            new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE)
                    .strength(3.0F).explosionResistance(3.0F).requiresCorrectToolForDrops()));

    // Sewer
    public static final Block SEWER_DRAIN = registerBlock("sewer_drain",
            new SewerDrainBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL)
                    .strength(4.0F).explosionResistance(3.0F).requiresCorrectToolForDrops().noOcclusion()));
    public static final Block SEWER = registerBlock("sewer",
            new SewerBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL)
                    .strength(4.0F).explosionResistance(3.0F).requiresCorrectToolForDrops()));

    // Machines
    public static final Block FOUNDRY = registerBlockWithoutItem("foundry",
            new FoundryBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE)
                    .strength(3.0F).explosionResistance(3.0F).requiresCorrectToolForDrops()));

    public static final Block CRUSHER = registerBlock("crusher",
            new CrusherBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE)
                    .strength(3.0F).explosionResistance(3.0F).requiresCorrectToolForDrops().noOcclusion()));

    // TEST
    public static final Block MASH_TEST = registerBlockWithoutItem("mash_test", new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, name), block);
    }

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, name),
                new BlockItem(block, new Item.Properties()));
    }

    public static void registerModBlocks() {
        GiantPotato.LOGGER.info("Registering Mod Blocks for " + GiantPotato.MOD_ID);
    }
}

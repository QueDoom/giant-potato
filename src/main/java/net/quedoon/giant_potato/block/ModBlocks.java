package net.quedoon.giant_potato.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.quedoon.giant_potato.GiantPotato;
import net.quedoon.giant_potato.block.custom.*;

public class ModBlocks {

    public static final Block MASH_BOWL = registerBlock("mash_bowl",
            new MashBowlBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)
                    .strength(1.0f).resistance(1.0f)));
    public static final Block MASH_TANK = registerBlock("mash_tank",
            new MashTankBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)
                    .strength(1.0F).resistance(1.0F).nonOpaque()));
    public static final Block MASH_PIPE = registerBlock("mash_pipe",
            new MashPipeBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)
                    .strength(1.0F).resistance(1.0F).nonOpaque()));
    public static final Block MASH_PIPE_OUTPUT = registerBlock("mash_pipe_output",
            new MashPipeOutputBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)
                    .strength(1.0F).resistance(1.0F).nonOpaque()));
    public static final Block SMOOTH_POTATOES = registerBlock("smooth_potatoes",
            new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
                    .strength(3.0F).resistance(3.0F).requiresTool()));
    public static final Block SMOOTH_POTATOES_SLAB = registerBlock("smooth_potatoes_slab",
            new SlabBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
                    .strength(3.0F).resistance(3.0F).requiresTool()));

    // Sewer
    public static final Block SEWER_DRAIN = registerBlock("sewer_drain",
            new SewerDrainBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.METAL)
                    .strength(4.0F).resistance(3.0F).requiresTool().nonOpaque()));
    public static final Block SEWER = registerBlock("sewer",
            new SewerBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.METAL)
                    .strength(4.0F).resistance(3.0F).requiresTool()));

    // Machines
    public static final Block FOUNDRY = registerBlockWithoutItem("foundry",
            new FoundryBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
                    .strength(3.0F).resistance(3.0F).requiresTool()));
    public static final Block CRUSHER = registerBlock("crusher",
            new CrusherBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
                    .strength(3.0f).resistance(3.0f).requiresTool()));
    public static final Block CRUSHER_WHEEL = registerBlockWithoutItem("crusher_wheel",
            new CrusherWheelBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
                    .strength(3.0f).resistance(3.0f).requiresTool()));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(GiantPotato.MOD_ID, name), block);
    }

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(GiantPotato.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(GiantPotato.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        GiantPotato.LOGGER.info("Registering Mod Blocks for " + GiantPotato.MOD_ID);
    }
}

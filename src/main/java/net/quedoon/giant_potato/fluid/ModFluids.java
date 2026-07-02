package net.quedoon.giant_potato.fluid;

import net.minecraft.world.level.block.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.quedoon.giant_potato.GiantPotato;
import net.quedoon.giant_potato.fluid.fluid.MashFluid;
import net.quedoon.giant_potato.fluid.fluid.PoisonousMashFluid;
import net.quedoon.giant_potato.fluid.fluid.SewerWaterFluid;
import net.quedoon.giant_potato.fluid.item.MashBucketItem;

public class ModFluids {

    public static final FlowingFluid MASH = Registry.register(BuiltInRegistries.FLUID,
            ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "mash"), new MashFluid.Still());
    public static final FlowingFluid MASH_FLOWING_UNUSED = Registry.register(BuiltInRegistries.FLUID,
            ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "flowing_mash"), new MashFluid.Flowing());

    public static final FlowingFluid POISONOUS_MASH = Registry.register(BuiltInRegistries.FLUID,
            ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "poisonous_mash"), new PoisonousMashFluid.Still());
    public static final FlowingFluid POISONOUS_MASH_FLOWING_UNUSED = Registry.register(BuiltInRegistries.FLUID,
            ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "flowing_poisonous_mash"), new PoisonousMashFluid.Flowing());

    public static final Block MASH_BLOCK = Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID,
            "mash_block"), new LiquidBlock(ModFluids.MASH, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
    public static final Item MASH_BUCKET = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID,
            "mash_bucket"), new MashBucketItem(ModFluids.MASH,
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final Block POISONOUS_MASH_BLOCK = Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID,
            "poisonous_mash_block"), new LiquidBlock(ModFluids.POISONOUS_MASH, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
    public static final Item POISONOUS_MASH_BUCKET = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID,
            "poisonous_mash_bucket"), new MashBucketItem(ModFluids.POISONOUS_MASH,
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));



    public static final FlowingFluid SEWER_WATER = Registry.register(BuiltInRegistries.FLUID,
            ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "sewer_water"), new SewerWaterFluid.Still());
    public static final FlowingFluid FLOWING_SEWER_WATER = Registry.register(BuiltInRegistries.FLUID,
            ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "flowing_sewer_water"), new SewerWaterFluid.Flowing());

    public static final Block SEWER_WATER_BLOCK = Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID,
            "sewer_water_block"), new LiquidBlock(ModFluids.SEWER_WATER, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
    public static final Item SEWER_WATER_BUCKET = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID,
            "sewer_water_bucket"), new BucketItem(ModFluids.SEWER_WATER,
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static void registerFluids() {
        GiantPotato.LOGGER.info("Registering Mod Fluids for " + GiantPotato.MOD_ID);
    }
}

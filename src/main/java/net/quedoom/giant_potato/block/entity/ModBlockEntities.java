package net.quedoom.giant_potato.block.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.quedoom.giant_potato.GiantPotato;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.block.entity.custom.*;


public class ModBlockEntities {
    public static final BlockEntityType<FoundryBlockEntity> FOUNDRY_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "foundry_be"),
                    BlockEntityType.Builder.of(FoundryBlockEntity::new, ModBlocks.FOUNDRY).build(null));

    public static final BlockEntityType<CrusherBlockEntity> CRUSHER_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "crusher_be"),
                    BlockEntityType.Builder.of(CrusherBlockEntity::new, ModBlocks.CRUSHER).build(null));

    public static final BlockEntityType<CrusherWheelBlockEntity> CRUSHER_WHEEL_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "crusher_wheel_be"),
                    BlockEntityType.Builder.of(CrusherWheelBlockEntity::new, ModBlocks.CRUSHER_WHEEL).build(null));

    public static final BlockEntityType<MashTankBlockEntity> MASH_TANK_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "mash_tank_be"),
                    BlockEntityType.Builder.of(MashTankBlockEntity::new, ModBlocks.MASH_TANK).build(null));

    public static final BlockEntityType<MashPipeBlockEntity> MASH_PIPE_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "mash_pipe_be"),
                    BlockEntityType.Builder.of(MashPipeBlockEntity::new, ModBlocks.MASH_PIPE).build(null));

    public static final BlockEntityType<MashPipeOutputBlockEntity> MASH_PIPE_OUTPUT_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "mash_pipe_output_be"),
                    BlockEntityType.Builder.of(MashPipeOutputBlockEntity::new, ModBlocks.MASH_PIPE_OUTPUT).build(null));



    public static void registerBlockEntities() {
        GiantPotato.LOGGER.info("Registering Mod Block Entities for " + GiantPotato.MOD_ID);
    }
}

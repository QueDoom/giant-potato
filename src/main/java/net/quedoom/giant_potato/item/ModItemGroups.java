package net.quedoom.giant_potato.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.quedoom.giant_potato.GiantPotato;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.fluid.ModFluids;

public class ModItemGroups {


    public static final CreativeModeTab GIANT_POTATO_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(net.quedoom.giant_potato.GiantPotato.MOD_ID, "giant_potato"),
            FabricItemGroup.builder().title(Component.translatable("itemGroup.giant_potato"))
                    .icon(() -> new ItemStack(ModItems.TILLER)).displayItems((displayContext, entries) -> {
                        entries.accept(ModBlocks.MASH_BOWL);
                        entries.accept(ModBlocks.MASH_PIPE);
                        entries.accept(ModItems.WRENCH);
                        entries.accept(ModBlocks.MASH_TANK);
                        entries.accept(ModFluids.MASH_BUCKET);
                        entries.accept(ModFluids.POISONOUS_MASH_BUCKET);

                        entries.accept(ModItems.TILLER);
                        entries.accept(ModItems.FERTILIZER_DIRT);
                        entries.accept(ModBlocks.SMOOTH_POTATOES);

                        entries.accept(ModItems.FOUNDRY);
                        entries.accept(ModItems.CHARRED_POTATO);
                        entries.accept(ModItems.BIOSTEEL_ALLOY);
                        entries.accept(ModItems.POTATO_ALLOY);
                        entries.accept(ModItems.POISONOUS_POTATO_ALLOY);

                        entries.accept(ModBlocks.CRUSHER);
                        entries.accept(ModItems.CRUSHER_WHEEL);

                        entries.accept(ModBlocks.SEWER_DRAIN);
                        entries.accept(ModBlocks.SEWER);
                        entries.accept(ModFluids.SEWER_WATER_BUCKET);



                    }).build());

    public static void registerModItemGroups() {
        GiantPotato.LOGGER.info("Registering Mod Item Groups for " + GiantPotato.MOD_ID);
    }
}

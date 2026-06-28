package net.quedoon.giant_potato.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.quedoon.giant_potato.GiantPotato;
import net.quedoon.giant_potato.block.ModBlocks;
import net.quedoon.giant_potato.fluid.ModFluids;

public class ModItemGroups {


    public static final ItemGroup GIANT_POTATO_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(net.quedoon.giant_potato.GiantPotato.MOD_ID, "giant_potato"),
            FabricItemGroup.builder().displayName(Text.translatable("itemGroup.giant_potato"))
                    .icon(() -> new ItemStack(ModItems.TILLER)).entries((displayContext, entries) -> {
                        entries.add(ModBlocks.MASH_BOWL);
                        entries.add(ModBlocks.MASH_PIPE);
                        entries.add(ModItems.WRENCH);
                        entries.add(ModBlocks.MASH_TANK);
                        entries.add(ModFluids.MASH_BUCKET);
                        entries.add(ModFluids.POISONOUS_MASH_BUCKET);

                        entries.add(ModItems.TILLER);
                        entries.add(ModItems.FERTILIZER_DIRT);
                        entries.add(ModBlocks.SMOOTH_POTATOES);

                        entries.add(ModItems.FOUNDRY);
                        entries.add(ModItems.CHARRED_POTATO);
                        entries.add(ModItems.BIOSTEEL_ALLOY);
                        entries.add(ModItems.POTATO_ALLOY);
                        entries.add(ModItems.POISONOUS_POTATO_ALLOY);

                        entries.add(ModBlocks.CRUSHER);
                        entries.add(ModItems.CRUSHER_WHEEL);

                        entries.add(ModBlocks.SEWER_DRAIN);



                    }).build());

    public static void registerModItemGroups() {
        GiantPotato.LOGGER.info("Registering Mod Item Groups for " + GiantPotato.MOD_ID);
    }
}

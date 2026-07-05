package net.quedoom.giant_potato.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.quedoom.giant_potato.fluid.ModFluids;
import net.quedoom.giant_potato.item.ModItems;
import net.quedoom.giant_potato.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Items.MASH_BOWL_POTATO).add(Items.POTATO);
        getOrCreateTagBuilder(ModTags.Items.MASH_BOWL_POISONOUS_POTATO).add(Items.POISONOUS_POTATO);
        getOrCreateTagBuilder(ModTags.Items.MASH_BOWL_POTATOES).add(Items.POTATO).add(Items.POISONOUS_POTATO);

        getOrCreateTagBuilder(ModTags.Items.MASH_BUCKETS).add(ModFluids.MASH_BUCKET);
        getOrCreateTagBuilder(ModTags.Items.POISONOUS_MASH_BUCKETS).add(ModFluids.POISONOUS_MASH_BUCKET);

        getOrCreateTagBuilder(ModTags.Items.POISONOUS_MASH_BUCKETS).add(ModItems.WRENCH);

        getOrCreateTagBuilder(ModTags.Items.CRUSHER_RENDER_BLOCK_AS_ITEM)
                .addTag(ItemTags.DOORS)
                .addTag(ItemTags.BEDS)
                .addTag(ItemTags.BANNERS)
        ;

    }
}

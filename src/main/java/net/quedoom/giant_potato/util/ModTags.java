package net.quedoom.giant_potato.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.quedoom.giant_potato.GiantPotato;

public class ModTags {
    public static class Fluid {

        public static final TagKey<net.minecraft.world.level.material.Fluid> MASH = createTag("mash");
        public static final TagKey<net.minecraft.world.level.material.Fluid> POISONOUS_MASH = createTag("poisonous_mash");
        public static final TagKey<net.minecraft.world.level.material.Fluid> MASH_FLUIDS = createTag("mash_fluids");

        private static TagKey<net.minecraft.world.level.material.Fluid> createTag(String name) {
            return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, name));
        }
    }
    public static class Blocks {

            public static final TagKey<Block> ELEMENTAL_SMOOTH_POTATOES = createTag("elemental_smooth_potatoes");

            public static final TagKey<Block> MASH_PIPE_CONNECT_TO = createTag("mash_pipe_connect_to");
            public static final TagKey<Block> MASH_BOWLS = createTag("mash_bowls");

            public static final TagKey<Block> MIDAS_HAND_WORKS_ON = createTag("midas_hand_works_on");

            public static final TagKey<Block> PIPES = createTag("pipes");
            public static final TagKey<Block> MASH_PIPES = createTag("mash_pipes");
            public static final TagKey<Block> MASH_MACHINES = createTag("mash_machines");


        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, name));
        }
    }
    public static class Items {

        public static final TagKey<Item> MASH_BOWL_POTATO = createTag("mash_bowl_potato");
        public static final TagKey<Item> MASH_BOWL_POISONOUS_POTATO = createTag("mash_bowl_poisonous_potato");
        public static final TagKey<Item> MASH_BOWL_POTATOES = createTag("mash_bowl_potatoes");

        public static final TagKey<Item> MASH_BUCKETS = createTag("mash_buckets");
        public static final TagKey<Item> POISONOUS_MASH_BUCKETS = createTag("poisonous_mash_buckets");

        public static final TagKey<Item> WRENCHES = createTag("wrenches");


        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, name));
        }
    }
}

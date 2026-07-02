package net.quedoon.giant_potato.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.quedoon.giant_potato.GiantPotato;
import net.quedoon.giant_potato.block.ModBlocks;
import net.quedoon.giant_potato.block.item.FoundryBlockItem;
import net.quedoon.giant_potato.item.custom.MidasHandItem;
import net.quedoon.giant_potato.item.custom.TillerItem;
import net.quedoon.giant_potato.item.custom.WrenchItem;

public class ModItems {
    public static Item TILLER = registerItem("tiller", new TillerItem(new Item.Properties().stacksTo(1).durability(500)));
    public static Item FERTILIZER_DIRT = registerItem("fertilizer_dirt", new Item(new Item.Properties()));
    public static Item FOUNDRY = registerItem("foundry", new FoundryBlockItem(new Item.Properties()));
    public static Item CRUSHER_WHEEL = registerItem("crusher_wheel", new BlockItem(ModBlocks.CRUSHER_WHEEL, new Item.Properties()));

    public static Item CHARRED_POTATO = registerItem("charred_potato", new Item(new Item.Properties()));
    public static Item BIOSTEEL_ALLOY = registerItem("biosteel_alloy", new Item(new Item.Properties()));
    public static Item POTATO_ALLOY = registerItem("potato_alloy", new Item(new Item.Properties()));
    public static Item POISONOUS_POTATO_ALLOY = registerItem("poisonous_potato_alloy", new Item(new Item.Properties()));

    public static Item WRENCH = registerItem("wrench", new WrenchItem(new Item.Properties().stacksTo(1)));

    public static final Item MIDAS_HAND = registerItem("midas_hand", new MidasHandItem(new Item.Properties().stacksTo(1).durability(100)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, name), item);
    }

    public static void registerModItems() {
        GiantPotato.LOGGER.info("Registering Mod Items for " + GiantPotato.MOD_ID);
    }
}

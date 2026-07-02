package net.quedoon.giant_potato.zapi.gecko.model;

import net.minecraft.resources.ResourceLocation;
import net.quedoon.giant_potato.GiantPotato;

public class GeoModelIdentifier {
    public static ResourceLocation itemModel(String name) {
        return ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "geo/item/" + name + ".geo.json");
    }
    public static ResourceLocation blockModel(String name) {
        return ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "geo/block/" + name + ".geo.json");
    }

    public static ResourceLocation itemTexture(String name) {
        return ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "textures/item/" + name + ".png");
    }
    public static ResourceLocation blockTexture(String name) {
        return ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "textures/block/" + name + ".png");
    }

    public static ResourceLocation itemAnimation(String name) {
        return ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "animations/item/" + name + ".animation.json");
    }
    public static ResourceLocation blockAnimation(String name) {
        return ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "animations/block/" + name + ".animation.json");
    }
}

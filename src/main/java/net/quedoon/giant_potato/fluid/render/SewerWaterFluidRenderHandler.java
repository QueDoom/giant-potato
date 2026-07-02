package net.quedoon.giant_potato.fluid.render;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.quedoon.giant_potato.GiantPotato;
import org.jetbrains.annotations.Nullable;

public class SewerWaterFluidRenderHandler implements FluidRenderHandler {
    private TextureAtlasSprite[] sprites = new TextureAtlasSprite[2];

    @Override
    public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        return sprites;
    }

    public void updateSprites(TextureAtlasSprite[] customSprites) {
        sprites = customSprites;
    }

    @Override
    public void reloadTextures(TextureAtlas textureAtlas) {
        sprites[0] = textureAtlas.getSprite(ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "block/sewer_water"));
        sprites[1] = textureAtlas.getSprite(ResourceLocation.fromNamespaceAndPath(GiantPotato.MOD_ID, "block/flowing_sewer_water"));
    }
}

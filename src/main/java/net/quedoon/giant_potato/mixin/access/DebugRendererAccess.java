package net.quedoon.giant_potato.mixin.access;

import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DebugRenderer.class)
public interface DebugRendererAccess {
    @Accessor("renderChunkborder")
    boolean showChunkBorder();
}

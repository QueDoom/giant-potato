package net.quedoom.giant_potato.block.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.zapi.gecko.render.FoundryBlockItemRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class FoundryBlockItem extends BlockItem implements GeoItem {
    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    RawAnimation IDLE = RawAnimation.begin().thenLoop("is_open");

    public FoundryBlockItem(Properties settings) {
        super(ModBlocks.FOUNDRY, settings);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, this::isOpenAnimController));
    }

    protected <E extends FoundryBlockItem>PlayState isOpenAnimController(final AnimationState<E> state) {
        return state.setAndContinue(IDLE);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private FoundryBlockItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new FoundryBlockItemRenderer();

                return GeoRenderProvider.super.getGeoItemRenderer();
            }
        });
    }
}

package net.quedoom.giant_potato.fluid.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.quedoom.giant_potato.fluid.util.EmptyableItemContainer;
import net.quedoom.giant_potato.util.ModTags;
import org.jetbrains.annotations.Nullable;

public class MashBucketItem extends BucketItem implements EmptyableItemContainer {
    private final Fluid fluid;

    public MashBucketItem(Fluid fluid, Properties settings) {
        super(fluid, settings);
        this.fluid = fluid;
    }

    @Override
    protected void playEmptySound(@Nullable Player player, LevelAccessor world, BlockPos pos) {
        SoundEvent soundEvent = this.fluid.is(ModTags.Fluid.MASH_FLUIDS) ? SoundEvents.HONEY_BLOCK_BREAK : SoundEvents.BUCKET_EMPTY;
        world.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        world.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }

    @Override
    public boolean emptyContents(@Nullable Player player, Level world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        return super.emptyContents(player, world, pos, hitResult);
    }


}

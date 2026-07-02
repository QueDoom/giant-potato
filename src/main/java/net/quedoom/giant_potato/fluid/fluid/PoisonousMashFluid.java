package net.quedoom.giant_potato.fluid.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.quedoom.giant_potato.fluid.ModFluids;

import java.util.Optional;

public abstract class PoisonousMashFluid extends FlowingFluid {
    @Override
    public Fluid getFlowing() {
        return ModFluids.POISONOUS_MASH_FLOWING_UNUSED;
    }

    @Override
    public Fluid getSource() {
        return ModFluids.POISONOUS_MASH;
    }

    @Override
    protected boolean canConvertToSource(Level world) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockEntity);
    }

    @Override
    protected int getDropOff(LevelReader world) {
        return 15;
    }

    @Override
    public Item getBucket() {
        return ModFluids.POISONOUS_MASH_BUCKET;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public int getTickDelay(LevelReader world) {
        return 99999;
    }

    @Override
    protected float getExplosionResistance() {
        return 6f;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.ofNullable(SoundEvents.HONEY_BLOCK_BREAK);
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return ModFluids.POISONOUS_MASH_BLOCK.defaultBlockState().setValue(BlockStateProperties.LEVEL, getLegacyLevel(state));
    }


    @Override
    public boolean isSource(FluidState state) {
        return true;
    }

    public static class Flowing extends PoisonousMashFluid {

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        protected int getSlopeFindDistance(LevelReader world) {
            return 0;
        }

        @Override
        public int getAmount(FluidState state) {
            return 0;
        }
    }

    public static class Still extends PoisonousMashFluid {

        @Override
        protected int getSlopeFindDistance(LevelReader world) {
            return 0;
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }
    }
}

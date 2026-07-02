package net.quedoon.giant_potato.fluid.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.quedoon.giant_potato.fluid.ModFluids;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SewerWaterFluid extends FlowingFluid {
    public SewerWaterFluid() {
    }

    public Fluid getFlowing() {
        return ModFluids.FLOWING_SEWER_WATER;
    }

    public Fluid getSource() {
        return ModFluids.SEWER_WATER;
    }

    public Item getBucket() {
        return Items.WATER_BUCKET;
    }

    public void animateTick(Level world, BlockPos pos, FluidState state, RandomSource random) {
        if (!state.isSource() && !(Boolean)state.getValue(FALLING)) {
            if (random.nextInt(64) == 0) {
                world.playLocalSound((double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }
        } else if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.UNDERWATER, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), (double)0.0F, (double)0.0F, (double)0.0F);
        }

    }

    @Nullable
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }

    protected boolean canConvertToSource(Level world) {
        return false;
    }

    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockEntity);
    }

    public int getSlopeFindDistance(LevelReader world) {
        return 4;
    }

    public BlockState createLegacyBlock(FluidState state) {
        return (BlockState) ModFluids.SEWER_WATER_BLOCK.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    public boolean isSame(Fluid fluid) {
        return fluid == ModFluids.SEWER_WATER || fluid == ModFluids.FLOWING_SEWER_WATER;
    }

    public int getDropOff(LevelReader world) {
        return 1;
    }

    @Override
    public int getAmount(FluidState state) {
        return 0;
    }

    public int getTickDelay(LevelReader world) {
        return 5;
    }

    public boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.defaultFluidState().is(ModFluids.SEWER_WATER);
    }

    protected float getExplosionResistance() {
        return 100.0F;
    }

    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    public static class Still extends SewerWaterFluid {
        public Still() {
        }

        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

    public static class Flowing extends SewerWaterFluid {
        public Flowing() {
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(new Property[]{LEVEL});
        }

        public int getAmount(FluidState state) {
            return (Integer)state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }
}

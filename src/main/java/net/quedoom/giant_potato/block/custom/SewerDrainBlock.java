package net.quedoom.giant_potato.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.quedoom.giant_potato.GiantPotato;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.block.util.ModProperties;
import net.quedoom.giant_potato.block.util.SewerDrainShape;
import net.quedoom.giant_potato.fluid.ModFluids;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.random.RandomGenerator;

public class SewerDrainBlock extends Block {
    public static final EnumProperty<SewerDrainShape> DRAIN_SHAPE = ModProperties.DRAIN_SHAPE;

    public static final MapCodec<SewerDrainBlock> CODEC = simpleCodec(SewerDrainBlock::new);

    public static final VoxelShape shape_drain = Block.box(0, 14, 0, 16, 16, 16);
    public static final VoxelShape shape_drain_low = Block.box(0, 0, 0, 16, 2, 16);
    public static final VoxelShape shape_tunnel = Shapes.or(
            Block.box(0, 0, 0, 16, 16, 3),
            Block.box(0, 0, 0, 3, 16, 16),
            Block.box(0, 0, 13, 16, 16, 16),
            Block.box(13, 0, 0, 16, 16, 16)
    );

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(DRAIN_SHAPE)) {
            case SewerDrainShape.DRAIN_FALL -> shape_drain_low;
            case SewerDrainShape.DRAIN_TUNNEL -> Shapes.or(shape_drain, shape_tunnel);
            case SewerDrainShape.TUNNEL -> shape_tunnel;
            default -> shape_drain;
        };
    }

    public @Nullable BlockPos findBottomDrain(Level world, BlockPos pos) {
        int MAX_RANGE = 10;
        for (int i = 0; i < MAX_RANGE; i++) {
            BlockPos negY = pos.below(i);
            if (!world.getBlockState(negY).is(ModBlocks.SEWER_DRAIN)) {
                return i == 1 ? null : world.getBlockState(negY).is(BlockTags.AIR) ? negY : null;
            }
        }
        BlockPos negYMAX = pos.below(MAX_RANGE++);
        if (world.getBlockState(negYMAX).is(BlockTags.AIR)) {
            return negYMAX;
        } else {
            return null;
        }
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (!(world.isClientSide())) {
            if (!(world instanceof ServerLevel serverWorld)) return;
            @Nullable BlockPos outputPos = findBottomDrain(world, pos);
            if (entity instanceof ItemEntity item && outputPos != null) {
                item.teleportTo(serverWorld, Math.floor(entity.getBlockX()) + 0.5, outputPos.getY() + 0.9, entity.getBlockZ() + 0.5, RelativeMovement.ALL, 0, 0);
                entity.setDeltaMovement(new Vec3(0,0,0));
            }
        }


        super.stepOn(world, pos, state, entity);
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return getCollisionShape(state, world, pos, CollisionContext.empty());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getCollisionShape(state, world, pos, context);
    }

    public SewerDrainBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DRAIN_SHAPE, SewerDrainShape.DRAIN_ONLY));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DRAIN_SHAPE);
    }

    @Override
    public void handlePrecipitation(BlockState state, Level world, BlockPos pos, Biome.Precipitation precipitation) {
        GiantPotato.LOGGER.info("PRECIPITATION");
        if (world.isClientSide()) {
            GiantPotato.LOGGER.info("PRECIPITATION CLIENT");
            if (shouldRain(world, precipitation)) {
                GiantPotato.LOGGER.info("PRECIPITATION SHOULD RAIN");
                if (precipitation == Biome.Precipitation.RAIN) {
                    createParticle(world, pos, ParticleTypes.DRIPPING_WATER);
                    GiantPotato.LOGGER.info("PRECIPITATION DRIP");
                } else {
                    createParticle(world, pos, ParticleTypes.SNOWFLAKE);
                }
            }
        } else {
            GiantPotato.LOGGER.info("PRECIPITATION SURVUR");
            if (precipitation == Biome.Precipitation.RAIN) {
                GiantPotato.LOGGER.info("PRECIPITATION water");
                placeSewerWater(state, world, pos);
            }
        }
    }

    private void placeSewerWater(BlockState state, Level world, BlockPos pos) {
        BlockPos belowBottomDrain = findBottomDrain(world, pos);
        if (belowBottomDrain == null) return;
        BlockPos neg1 = belowBottomDrain.below(1);
        BlockPos neg2 = belowBottomDrain.below(2);
        if (world.getBlockState(neg1).is(BlockTags.AIR) && !world.getBlockState(neg2).is(BlockTags.AIR)) {
            world.setBlockAndUpdate(neg1, ModFluids.SEWER_WATER_BLOCK.defaultBlockState());
        }

    }

    private static void createParticle(Level world, BlockPos pos, Fluid fluid) {
        Random random = Random.from(RandomGenerator.getDefault());
        Vec3 vec3d = new Vec3(random.nextGaussian(), 0, random.nextGaussian());
        double x = (double)pos.getX() + (double)0.5F + vec3d.x;
        double y = (float)pos.getY() + 0.94;
        double z = (double)pos.getZ() + (double)0.5F + vec3d.z;
        Fluid fluid2 = getDripFluid(world, fluid);
        ParticleOptions particleEffect = fluid2.defaultFluidState().getDripParticle();
        world.addParticle(particleEffect, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
    }

    private static void createParticle(Level world, BlockPos pos, ParticleOptions effect) {
        Random random = Random.from(RandomGenerator.getDefault());
        Vec3 vec3d = new Vec3(random.nextGaussian(), 0, random.nextGaussian());
        double x = (double)pos.getX() + (double)0.5F + vec3d.x;
        double y = (float)pos.getY() + 0.94;
        double z = (double)pos.getZ() + (double)0.5F + vec3d.z;
        world.addParticle(effect, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
    }

    private static Fluid getDripFluid(Level world, Fluid fluid) {
        if (fluid.isSame(Fluids.EMPTY)) {
            return world.dimensionType().ultraWarm() ? Fluids.LAVA : Fluids.WATER;
        } else {
            return fluid;
        }
    }

    private boolean shouldRain(Level world, Biome.Precipitation precipitation) {
        if (precipitation == Biome.Precipitation.RAIN) {
            return world.getRandom().nextFloat() < 0.25F;
        } else if (precipitation == Biome.Precipitation.SNOW) {
            return world.getRandom().nextFloat() < 0.1F;
        } else {
            return false;
        }
    }


    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return getBlockState(ctx.getLevel(), ctx.getClickedPos());
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return getBlockState(world, pos);
    }

    public static BlockState getBlockState(LevelAccessor world, BlockPos pos) {
        BlockState up = world.getBlockState(pos.above());
        BlockState down = world.getBlockState(pos.below());
        if (up.is(ModBlocks.SEWER_DRAIN)) {
            return ModBlocks.SEWER_DRAIN.defaultBlockState().setValue(DRAIN_SHAPE, SewerDrainShape.TUNNEL);
        } else if (down.is(ModBlocks.SEWER_DRAIN)) {
            return ModBlocks.SEWER_DRAIN.defaultBlockState().setValue(DRAIN_SHAPE, SewerDrainShape.DRAIN_TUNNEL);
        } else if (down.is(BlockTags.AIR)) {
            return ModBlocks.SEWER_DRAIN.defaultBlockState().setValue(DRAIN_SHAPE, SewerDrainShape.DRAIN_ONLY);
        } else {
            return ModBlocks.SEWER_DRAIN.defaultBlockState().setValue(DRAIN_SHAPE, SewerDrainShape.DRAIN_FALL);
        }
    }
}

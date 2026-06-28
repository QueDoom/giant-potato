package net.quedoon.giant_potato.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.quedoon.giant_potato.block.ModBlocks;
import net.quedoon.giant_potato.block.util.ModProperties;
import net.quedoon.giant_potato.block.util.SewerDrainShape;
import net.quedoon.giant_potato.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.random.RandomGenerator;

public class SewerDrainBlock extends Block {
    public static final EnumProperty<SewerDrainShape> DRAIN_SHAPE = ModProperties.DRAIN_SHAPE;

    public static final MapCodec<SewerDrainBlock> CODEC = createCodec(SewerDrainBlock::new);

    public static final VoxelShape shape_drain = Block.createCuboidShape(0, 14, 0, 16, 16, 16);
    public static final VoxelShape shape_drain_low = Block.createCuboidShape(0, 0, 0, 16, 2, 16);
    public static final VoxelShape shape_tunnel = VoxelShapes.union(
            Block.createCuboidShape(0, 0, 0, 16, 16, 3),
            Block.createCuboidShape(0, 0, 0, 3, 16, 16),
            Block.createCuboidShape(0, 0, 13, 16, 16, 16),
            Block.createCuboidShape(13, 0, 0, 16, 16, 16)
    );

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(DRAIN_SHAPE)) {
            case SewerDrainShape.DRAIN_FALL -> shape_drain_low;
            case SewerDrainShape.DRAIN_TUNNEL -> VoxelShapes.union(shape_drain, shape_tunnel);
            case SewerDrainShape.TUNNEL -> shape_tunnel;
            default -> shape_drain;
        };
    }

    public @Nullable BlockPos findBottomDrain(World world, BlockPos pos) {
        int MAX_RANGE = 10;
        for (int i = 0; i < MAX_RANGE; i++) {
            BlockPos negY = pos.down(i);
            if (!world.getBlockState(negY).isOf(ModBlocks.SEWER_DRAIN)) {
                return i == 1 ? null : world.getBlockState(negY).isIn(BlockTags.AIR) ? negY : null;
            }
        }
        BlockPos negYMAX = pos.down(MAX_RANGE++);
        if (world.getBlockState(negYMAX).isIn(BlockTags.AIR)) {
            return negYMAX;
        } else {
            return null;
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!(world.isClient())) {
            if (!(world instanceof ServerWorld serverWorld)) return;
            @Nullable BlockPos outputPos = findBottomDrain(world, pos);
            if (entity instanceof ItemEntity item && outputPos != null) {
                item.teleport(serverWorld, Math.floor(entity.getBlockX()) + 0.5, outputPos.getY() + 0.9, entity.getBlockZ() + 0.5, PositionFlag.VALUES, 0, 0);
                entity.setVelocity(new Vec3d(0,0,0));
            }
        }


        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    protected VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return getCollisionShape(state, world, pos, ShapeContext.absent());
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getCollisionShape(state, world, pos, context);
    }

    public SewerDrainBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(DRAIN_SHAPE, SewerDrainShape.DRAIN_ONLY));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DRAIN_SHAPE);
    }

    @Override
    public void precipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation) {
        if (!world.isClient()) return;
        if (shouldRain(world, precipitation)) {
            if (precipitation == Biome.Precipitation.RAIN) {
                createParticle(world, pos, ParticleTypes.DRIPPING_WATER);
            } else {
                createParticle(world, pos, ParticleTypes.SNOWFLAKE);
            }
        }
    }

    private static void createParticle(World world, BlockPos pos, Fluid fluid) {
        Random random = Random.from(RandomGenerator.getDefault());
        Vec3d vec3d = new Vec3d(random.nextGaussian(), 0, random.nextGaussian());
        double x = (double)pos.getX() + (double)0.5F + vec3d.x;
        double y = (float)pos.getY() + 0.94;
        double z = (double)pos.getZ() + (double)0.5F + vec3d.z;
        Fluid fluid2 = getDripFluid(world, fluid);
        ParticleEffect particleEffect = fluid2.getDefaultState().getParticle();
        world.addParticle(particleEffect, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
    }

    private static void createParticle(World world, BlockPos pos, ParticleEffect effect) {
        Random random = Random.from(RandomGenerator.getDefault());
        Vec3d vec3d = new Vec3d(random.nextGaussian(), 0, random.nextGaussian());
        double x = (double)pos.getX() + (double)0.5F + vec3d.x;
        double y = (float)pos.getY() + 0.94;
        double z = (double)pos.getZ() + (double)0.5F + vec3d.z;
        world.addParticle(effect, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
    }

    private static Fluid getDripFluid(World world, Fluid fluid) {
        if (fluid.matchesType(Fluids.EMPTY)) {
            return world.getDimension().ultrawarm() ? Fluids.LAVA : Fluids.WATER;
        } else {
            return fluid;
        }
    }

    private boolean shouldRain(World world, Biome.Precipitation precipitation) {
        if (precipitation == Biome.Precipitation.RAIN) {
            return world.getRandom().nextFloat() < 0.25F;
        } else if (precipitation == Biome.Precipitation.SNOW) {
            return world.getRandom().nextFloat() < 0.1F;
        } else {
            return false;
        }
    }


    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getBlockState(ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return getBlockState(world, pos);
    }

    public static BlockState getBlockState(WorldAccess world, BlockPos pos) {
        BlockState up = world.getBlockState(pos.up());
        BlockState down = world.getBlockState(pos.down());
        if (up.isOf(ModBlocks.SEWER_DRAIN)) {
            return ModBlocks.SEWER_DRAIN.getDefaultState().with(DRAIN_SHAPE, SewerDrainShape.TUNNEL);
        } else if (down.isOf(ModBlocks.SEWER_DRAIN)) {
            return ModBlocks.SEWER_DRAIN.getDefaultState().with(DRAIN_SHAPE, SewerDrainShape.DRAIN_TUNNEL);
        } else if (down.isIn(BlockTags.AIR)) {
            return ModBlocks.SEWER_DRAIN.getDefaultState().with(DRAIN_SHAPE, SewerDrainShape.DRAIN_ONLY);
        } else {
            return ModBlocks.SEWER_DRAIN.getDefaultState().with(DRAIN_SHAPE, SewerDrainShape.DRAIN_FALL);
        }
    }
}

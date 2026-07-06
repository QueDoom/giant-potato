package net.quedoom.giant_potato.block.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.quedoom.giant_potato.GiantPotato;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.block.util.ModProperties;
import net.quedoom.giant_potato.block.util.PosDirection;
import net.quedoom.giant_potato.block.util.SewerStates;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SewerBlock extends Block {
    public static final EnumProperty<SewerStates> SEWER_STATE = ModProperties.SEWER_STATES;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public SewerBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(getStateDefinition().any().setValue(SEWER_STATE, SewerStates.DO_NOT_CONNECT).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SEWER_STATE, FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        Level world = ctx.getLevel();
        return this.defaultBlockState().setValue(SEWER_STATE, getState(this.defaultBlockState(), world, pos)).setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.setBlock(pos, getBlockState(state, level, pos), 1);
    }

    private BlockState getBlockState(BlockState state, LevelAccessor world, BlockPos pos) {
        return state.setValue(SEWER_STATE, getState(state, world, pos));
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return getBlockState(state, world, pos);
    }

    private SewerStates getState(BlockState state, LevelAccessor level, BlockPos pos) {
        Direction facing = state.getValue(FACING);

        BlockState forwardState = level.getBlockState(pos.relative(facing));
        BlockState leftState = level.getBlockState(pos.relative(facing.getCounterClockWise()));
        BlockState rightState = level.getBlockState(pos.relative(facing.getClockWise()));
        BlockState backState = level.getBlockState(pos.relative(facing.getOpposite()));


        boolean backConnected = checkBackwardsForSewer(backState, facing);
        boolean forwardConnected = forwardState.is(ModBlocks.SEWER) && forwardState.getValue(FACING) == facing;
        boolean leftConnected = leftState.is(ModBlocks.SEWER) && leftState.getValue(FACING) == facing.getCounterClockWise();
        boolean rightConnected = rightState.is(ModBlocks.SEWER) && rightState.getValue(FACING) == facing.getClockWise();


        if (!hasSewerShape(state, facing, (Level) level, pos)) return SewerStates.NO_SHAPE;
        SewerStates sewerState;


        if (backConnected) {
            GiantPotato.LOGGER.info("back");
            if (forwardConnected) {
                sewerState = SewerStates.CONNECT_FORWARD;
            } else if (rightConnected) {
                sewerState = SewerStates.CONNECT_RIGHT;
            } else if (leftConnected) {
                sewerState = SewerStates.CONNECT_LEFT;
            } else {
                sewerState = SewerStates.BACKWARDS_ONLY;
            }
        } else {
            GiantPotato.LOGGER.info("not back");
            if (forwardConnected) {
                sewerState = SewerStates.FORWARD_ONLY;
            } else if (rightConnected) {
                sewerState = SewerStates.RIGHT_ONLY;
            } else if (leftConnected) {
                sewerState = SewerStates.LEFT_ONLY;
            } else {
                sewerState = SewerStates.DO_NOT_CONNECT;
            }
        }

        return sewerState;
    }

    private boolean checkBackwardsForSewer(BlockState state, Direction facing) {
        if (!state.is(ModBlocks.SEWER)) return false;
        SewerStates sewerState = state.getValue(SEWER_STATE);
        Direction backFacing = state.getValue(FACING);
        return switch (sewerState) {
            case SewerStates.CONNECT_FORWARD, SewerStates.FORWARD_ONLY -> backFacing == facing;
            case SewerStates.CONNECT_RIGHT, SewerStates.RIGHT_ONLY -> backFacing.getClockWise() == facing;
            case SewerStates.CONNECT_LEFT, SewerStates.LEFT_ONLY -> backFacing.getCounterClockWise() == facing;
            default -> false;
        };
    }

    public boolean hasSewerShape(BlockState state, Direction facing, Level level, BlockPos pos) {
        return true;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {




    }

    private void displayParticleStair(Level level, BlockPos pos, Direction direction) {

    }

    private void displayParticleCube(Level level, BlockPos pos) {
        VoxelShape shape = Shapes.block();
        shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {

        });
    }
}


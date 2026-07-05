package net.quedoom.giant_potato.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
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

    private SewerStates getState(BlockState state, LevelAccessor world, BlockPos pos) {
        Direction facing = state.getValue(FACING);

        PosDirection forward = new PosDirection(pos, facing);
        PosDirection left = new PosDirection(pos, facing.getCounterClockWise());
        PosDirection right = new PosDirection(pos, facing.getClockWise());
        PosDirection back = new PosDirection(pos, facing.getOpposite());

        BlockState backState = back.getState((Level) world);


        if (!hasSewerShape(state, facing, (Level) world, pos)) return SewerStates.NO_SHAPE;

        SewerStates sewerState = SewerStates.DO_NOT_CONNECT;
        if (backState.is(ModBlocks.SEWER) && backState.getValue(FACING) == facing) {
            if (isConnected((Level) world, forward)) {
                sewerState = SewerStates.CONNECT_FORWARD;
            } else if (isConnected((Level) world, left)) {
                sewerState = SewerStates.CONNECT_LEFT;
            } else if (isConnected((Level) world, right)) {
                sewerState = SewerStates.CONNECT_RIGHT;
            } else {
                sewerState = SewerStates.BACKWARDS_ONLY;
            }
        } else {
            if (isConnected((Level) world, forward)) {
                sewerState = SewerStates.FORWARD_ONLY;
            } else if (isConnected((Level) world, left)) {
                sewerState = SewerStates.LEFT_ONLY;
            } else if (isConnected((Level) world, right)) {
                sewerState = SewerStates.RIGHT_ONLY;
            }
        }
        return sewerState;
    }

    public boolean hasSewerShape(BlockState state, Direction facing, Level world, BlockPos pos) {
        return true;
    }

    private boolean isConnected(Level world, PosDirection posDir) {
        BlockState state = posDir.getState(world);
        return state.is(ModBlocks.SEWER) && state.getValue(FACING) == posDir.getDirection();
    }
}


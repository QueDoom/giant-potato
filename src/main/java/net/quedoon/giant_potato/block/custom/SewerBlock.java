package net.quedoon.giant_potato.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.quedoon.giant_potato.block.ModBlocks;
import net.quedoon.giant_potato.block.util.ModProperties;
import net.quedoon.giant_potato.block.util.PosDirection;
import net.quedoon.giant_potato.block.util.SewerStates;
import org.jetbrains.annotations.Nullable;

public class SewerBlock extends Block {
    public static final EnumProperty<SewerStates> SEWER_STATE = ModProperties.SEWER_STATES;
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public SewerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState().with(SEWER_STATE, SewerStates.DO_NOT_CONNECT).with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SEWER_STATE, FACING);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        if (!world.isClient()) return this.getDefaultState();
        BlockState facingState = this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
        return facingState.with(SEWER_STATE, getState(facingState, world, pos));
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return neighborState.isOf(ModBlocks.SEWER) ? state.with(SEWER_STATE, getState(state, world, pos)) : state;
    }

    private SewerStates getState(BlockState state, WorldAccess world, BlockPos pos) {
        Direction facing = state.get(FACING);

        PosDirection forward = new PosDirection(pos, facing);
        PosDirection left = new PosDirection(pos, facing.rotateYCounterclockwise());
        PosDirection right = new PosDirection(pos, facing.rotateYClockwise());
        PosDirection back = new PosDirection(pos, facing.getOpposite());

        BlockState backState = back.getState((World) world);


        if (!hasSewerShape(state, facing, (World) world, pos)) return SewerStates.NO_SHAPE;

        SewerStates sewerState = SewerStates.DO_NOT_CONNECT;
        if (backState.isOf(ModBlocks.SEWER) && backState.get(FACING) == facing && backState.isOf(ModBlocks.SEWER)) {
            if (isConnected((World) world, forward)) {
                sewerState = SewerStates.CONNECT_FORWARD;
            } else if (isConnected((World) world, left)) {
                sewerState = SewerStates.CONNECT_LEFT;
            } else if (isConnected((World) world, right)) {
                sewerState = SewerStates.CONNECT_RIGHT;
            }
        } else {
            if (isConnected((World) world, forward)) {
                sewerState = SewerStates.FORWARD_ONLY;
            } else if (isConnected((World) world, left)) {
                sewerState = SewerStates.LEFT_ONLY;
            } else if (isConnected((World) world, right)) {
                sewerState = SewerStates.RIGHT_ONLY;
            }
        }
        return sewerState;
    }

    public boolean hasSewerShape(BlockState state, Direction facing, World world, BlockPos pos) {
        return true;
    }

    private boolean isConnected(World world, PosDirection posDir) {
        BlockState state = posDir.getState(world);
        return state.isOf(ModBlocks.SEWER) && state.get(FACING) == posDir.getDirection();
    }
}


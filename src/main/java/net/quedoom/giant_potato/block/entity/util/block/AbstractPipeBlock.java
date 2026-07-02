package net.quedoom.giant_potato.block.entity.util.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.quedoom.giant_potato.block.entity.util.block_entity.pipe.AbstractPipeBlockEntity;
import net.quedoom.giant_potato.block.util.ModBlockHitboxes;
import net.quedoom.giant_potato.block.util.ModProperties;
import net.quedoom.giant_potato.block.util.PipeShape;
import net.quedoom.giant_potato.util.ModTags;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPipeBlock extends BaseEntityBlock {
    public static final EnumProperty<PipeShape.PipeShapes> NORTH = ModProperties.NORTH_PIPE_SHAPE;
    public static final EnumProperty<PipeShape.PipeShapes> SOUTH = ModProperties.SOUTH_PIPE_SHAPE;
    public static final EnumProperty<PipeShape.PipeShapes> EAST = ModProperties.EAST_PIPE_SHAPE;
    public static final EnumProperty<PipeShape.PipeShapes> WEST = ModProperties.WEST_PIPE_SHAPE;
    public static final EnumProperty<PipeShape.PipeShapes> UP = ModProperties.UP_PIPE_SHAPE;
    public static final EnumProperty<PipeShape.PipeShapes> DOWN = ModProperties.DOWN_PIPE_SHAPE;
    
    public AbstractPipeBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.getStateDefinition().any().setValue(NORTH, PipeShape.PipeShapes.NONE).setValue(SOUTH, PipeShape.PipeShapes.NONE).setValue(EAST, PipeShape.PipeShapes.NONE).setValue(WEST, PipeShape.PipeShapes.NONE).setValue(UP, PipeShape.PipeShapes.NONE).setValue(DOWN, PipeShape.PipeShapes.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH,SOUTH,EAST,WEST,UP,DOWN);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return ModBlockHitboxes.Pipe.getShape(state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return ModBlockHitboxes.Pipe.getShape(state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        return getPipeStates(world, pos);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof AbstractPipeBlockEntity blockEntity) {
            blockEntity.setPipeStates(((Level) world), pos);
        }
        return state;
        //        return getPipeStates((World) world, pos);
    }

    private @Nullable BlockState getPipeStates(Level world, BlockPos pos) {
        PipeShape.PipeShapes north = world.getBlockState(pos.north()).is(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? PipeShape.PipeShapes.TRUE : PipeShape.PipeShapes.NONE;
        PipeShape.PipeShapes south = world.getBlockState(pos.south()).is(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? PipeShape.PipeShapes.TRUE : PipeShape.PipeShapes.NONE;
        PipeShape.PipeShapes east = world.getBlockState(pos.east()).is(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? PipeShape.PipeShapes.TRUE : PipeShape.PipeShapes.NONE;
        PipeShape.PipeShapes west = world.getBlockState(pos.west()).is(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? PipeShape.PipeShapes.TRUE : PipeShape.PipeShapes.NONE;
        PipeShape.PipeShapes up = world.getBlockState(pos.above()).is(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? PipeShape.PipeShapes.TRUE : PipeShape.PipeShapes.NONE;
        PipeShape.PipeShapes down = world.getBlockState(pos.below()).is(ModTags.Blocks.MASH_PIPE_CONNECT_TO) ? PipeShape.PipeShapes.TRUE : PipeShape.PipeShapes.NONE;

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof AbstractPipeBlockEntity blockEntity)) return this.defaultBlockState();
        blockEntity.initializeSides(north, south, east, west, up, down);

        return this.defaultBlockState().setValue(NORTH, north).setValue(SOUTH, south).setValue(EAST, east).setValue(WEST, west).setValue(UP, up).setValue(DOWN, down);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (hit == null) return InteractionResult.PASS;
        AbstractPipeBlockEntity blockEntity = getBlockEntity(world, pos);
        if (blockEntity == null) return InteractionResult.PASS;
        return blockEntity.attemptInteraction(player, InteractionHand.MAIN_HAND);
    }

    public static @Nullable AbstractPipeBlockEntity getBlockEntity(BlockGetter world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof AbstractPipeBlockEntity blockEntity) return blockEntity;
        if (!(world.getBlockEntity(pos) instanceof AbstractPipeBlockEntity abstractPipeBlockEntity)) return null;
        return abstractPipeBlockEntity;
    }
    
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}

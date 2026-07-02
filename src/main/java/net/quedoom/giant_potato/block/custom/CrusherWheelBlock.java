package net.quedoom.giant_potato.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.block.entity.custom.CrusherWheelBlockEntity;
import net.quedoom.giant_potato.block.util.ModBlockHitboxes;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CrusherWheelBlock extends BaseEntityBlock {
    public static final MapCodec<CrusherWheelBlock> CODEC = CrusherWheelBlock.simpleCodec(CrusherWheelBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;


    public CrusherWheelBlock(Properties settings) {
        super(settings.noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Map<Integer, Direction> directionMap = Map.of(0, Direction.NORTH, 1, Direction.SOUTH, 2, Direction.EAST, 3, Direction.WEST);
        Map<Integer, Direction> directionMapCrusher = Map.of(0, Direction.EAST, 1, Direction.EAST, 2, Direction.NORTH, 3, Direction.NORTH);
        BlockPos pos = ctx.getClickedPos();
        Level world = ctx.getLevel();
        BlockState state = null;
        Direction dirWheel;
        Direction dirCrusher;
        for(int i = 0; i <= 3; i++) {
            dirWheel = directionMap.get(i);
            dirCrusher = directionMapCrusher.get(i);
            switch (i) {
                case 0 -> state = world.getBlockState(pos.north(1));
                case 1 -> state = world.getBlockState(pos.south(1));
                case 2 -> state = world.getBlockState(pos.east(1));
                case 3 -> state = world.getBlockState(pos.west(1));
            }
            if (state != null && state.is(ModBlocks.CRUSHER) &&
                    (state.getValue(BlockStateProperties.HORIZONTAL_FACING) == dirCrusher || state.getValue(BlockStateProperties.HORIZONTAL_FACING) == dirCrusher.getOpposite() ))
                    return this.defaultBlockState().setValue(FACING, dirWheel);
        }

        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrusherWheelBlockEntity(pos, state);
    }

    private VoxelShape getShape(BlockState state) {
        return ModBlockHitboxes.getCrusherWheelHitbox(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShape(state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShape(state);
    }
}

package net.quedoom.giant_potato.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.block.entity.ModBlockEntities;
import net.quedoom.giant_potato.block.entity.custom.CrusherBlockEntity;
import net.quedoom.giant_potato.block.util.ModProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class CrusherBlock extends BaseEntityBlock {
    public static final MapCodec<CrusherBlock> CODEC = simpleCodec(CrusherBlock::new);

    public static final IntegerProperty FIRST = ModProperties.CRUSHER_FIRST;
    public static final IntegerProperty SECOND = ModProperties.CRUSHER_SECOND;

    public CrusherBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FIRST, 0).setValue(SECOND, 0));
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return this.getCollisionShape(blockState, blockGetter, blockPos, CollisionContext.empty());
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape bottom = Block.box(0, 0, 0, 16, 2, 16);

        int crushProgress = blockState.getValue(FIRST);
        VoxelShape top = Block.box(0, 14 - crushProgress, 0, 16, 16 - crushProgress, 16);

        return Shapes.or(top, bottom);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FIRST, SECOND);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float f) {
        super.fallOn(level, blockState, blockPos, entity, f);
        level.setBlock(blockPos, advanceState(level, blockState, blockPos, entity), 1);
    }

//    @Override
//    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
//        level.setBlock(blockPos, advanceState(level, blockState, blockPos), 1);
//        return InteractionResult.SUCCESS;
//    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!(level.getBlockEntity(blockPos) instanceof CrusherBlockEntity blockEntity)) return ItemInteractionResult.FAIL;
        if (blockEntity.getItem(0).isEmpty() && itemStack.isEmpty()) return ItemInteractionResult.FAIL;

        if (blockEntity.isEmpty() && !itemStack.isEmpty()) {
            blockEntity.setItem(0, itemStack);
            blockEntity.randomizeRotation();

            if (level instanceof ServerLevel serverLevel) resetState(serverLevel, blockState, blockPos, null);
            level.playSound(player, blockPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 2f);
            itemStack.shrink(1);
            return ItemInteractionResult.SUCCESS;
        } else if (itemStack.isEmpty()) {
            player.setItemInHand(InteractionHand.MAIN_HAND, blockEntity.getItem(0));
            level.playSound(player, blockPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1f);
            blockEntity.clearContent();
            return ItemInteractionResult.SUCCESS;
        } else if (itemStack.is(blockEntity.getItem(0).getItem())){
            itemStack.grow(1);
            level.playSound(player, blockPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1f);
            blockEntity.clearContent();
            return ItemInteractionResult.SUCCESS;
        } else {
            player.addItem(blockEntity.getItem(0));
            level.playSound(player, blockPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1f);
            blockEntity.clearContent();
            return ItemInteractionResult.SUCCESS;
        }
    }



    @Override
    protected void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getBlock() != blockState2.getBlock()) {
            if (!(level.getBlockEntity(blockPos) instanceof CrusherBlockEntity blockEntity)) return;
            Containers.dropContents(level, blockPos, blockEntity);
            level.updateNeighbourForOutputSignal(blockPos, this);

            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }
    }

    private BlockState advanceState(Level level, BlockState state, BlockPos pos, Entity entity) {
        int first = state.getValue(FIRST);
        int second = state.getValue(SECOND);

        if (second < 2) {
            return state.setValue(SECOND, second + 1);
        } else {
            if (first < 10) {
                return state.setValue(FIRST, first + 1).setValue(SECOND, 0);
            } else {
                return resetState(level, state, pos, entity);
            }
        }
    }

    private void teleportEntitySoTheyDontGoIntoTheBlock(ServerLevel level, BlockPos blockPos, @NotNull Entity entity) {
        entity.teleportTo(level, entity.getX(), blockPos.above().getY(), entity.getZ(), Set.of(), entity.getYRot(), entity.getXRot());
    }

    private BlockState resetState(Level level, BlockState state, BlockPos pos, @Nullable Entity entity) {
        if (!(level.getBlockEntity(pos) instanceof CrusherBlockEntity blockEntity)) return state;
        if (entity != null && level instanceof ServerLevel serverLevel) {
            teleportEntitySoTheyDontGoIntoTheBlock(serverLevel, pos, entity);
        }
        if (state.getValue(FIRST) == 10 && state.getValue(SECOND) == 2) blockEntity.doRecipe();
        return state.setValue(FIRST, 0).setValue(SECOND, 0);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CrusherBlockEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}

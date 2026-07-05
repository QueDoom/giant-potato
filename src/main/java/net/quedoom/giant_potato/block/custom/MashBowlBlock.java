package net.quedoom.giant_potato.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.BlockHitResult;
import net.quedoom.giant_potato.GiantPotato;
import net.quedoom.giant_potato.block.entity.ModBlockEntities;
import net.quedoom.giant_potato.block.entity.util.ImplementedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.quedoom.giant_potato.block.entity.custom.MashBowlBlockEntity;
import net.quedoom.giant_potato.block.util.ModBlockHitboxes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MashBowlBlock extends BaseEntityBlock {
    public static final MapCodec<MashBowlBlock> CODEC = simpleCodec(MashBowlBlock::new);

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);
    private static final VoxelShape SHAPE = ModBlockHitboxes.getMashBowlHitbox();

    public MashBowlBlock(Properties properties) {
        super(properties);
    }


    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (level.isClientSide()) return InteractionResult.FAIL;
        if (level.getBlockEntity(blockPos) instanceof MashBowlBlockEntity entity) {
            ItemStack stack = entity.getItem(0);
            player.sendSystemMessage(Component.literal(stack.toString()));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        //if (fallDistance >= 0.5F) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (!(blockEntity instanceof MashBowlBlockEntity)) return;
            ((MashBowlBlockEntity) blockEntity).jumpedOn(world, state, pos, entity);
        //}
        entity.causeFallDamage(fallDistance, 1.0F - (float) state.getValue(STAGE) / 3.0F, entity.damageSources().fall());
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClientSide) return; // server


        BlockEntity blockEntity = world.getBlockEntity(pos);

        // checking
        if (!(blockEntity instanceof MashBowlBlockEntity mashBowlBlockEntity)) return;
        if (!(entity instanceof ItemEntity item)) return;
        ItemStack itemEntityStack = item.getItem();
        GiantPotato.LOGGER.info("MASH BOWL: ITEM");

        // logic
        int placeItem = mashBowlBlockEntity.placeItem(itemEntityStack);
        if (placeItem != 0) {
            if (itemEntityStack.getCount() - placeItem <= 0) {
                item.discard();
            } else {
                item.setItem(itemEntityStack.copyWithCount(itemEntityStack.getCount() - placeItem));
            }
            mashBowlBlockEntity.setChanged();
            GiantPotato.LOGGER.info("MASH BOWL: set");
        }
    }

    private static boolean canMergeItems(ItemStack first, ItemStack second) {
        return first.getCount() <= first.getMaxStackSize() && ItemStack.isSameItemSameComponents(first, second);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MashBowlBlockEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}

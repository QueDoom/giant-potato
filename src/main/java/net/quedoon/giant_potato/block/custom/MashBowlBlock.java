package net.quedoon.giant_potato.block.custom;

import net.quedoon.giant_potato.block.entity.util.ImplementedInventory;
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
import net.quedoon.giant_potato.block.entity.custom.MashBowlBlockEntity;
import net.quedoon.giant_potato.block.util.ModBlockHitboxes;

public class MashBowlBlock extends Block {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);
    private static final VoxelShape SHAPE = ModBlockHitboxes.getMashBowlHitbox();


    public MashBowlBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof MashBowlBlockEntity)) return;
        ((MashBowlBlockEntity) blockEntity).jumpedOn(world, state, pos, entity, fallDistance);
        super.fallOn(world, state, pos, entity, fallDistance);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClientSide) return;
        System.out.println("I got stepped on, ouch!");
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof MashBowlBlockEntity mashBowlBlockEntity)) return;
        if (!(entity instanceof ItemEntity item)) return;
        ItemStack itemEntityStack = item.getItem();
        NonNullList<ItemStack> inventory = mashBowlBlockEntity.getItems();
        ItemStack itemStack = inventory.getFirst();
        if (canInsert(inventory, itemEntityStack)) {
            if (itemStack.isEmpty()) {
                inventory.set(0, itemEntityStack);
                item.setItem(ItemStack.EMPTY);
            } else if (canMergeItems(itemEntityStack, itemStack)) {
                int i = itemEntityStack.getMaxStackSize() - itemEntityStack.getCount();
                int j = Math.min(itemEntityStack.getCount(), i);
                itemEntityStack.shrink(j);
                itemStack.grow(j);
            }
            mashBowlBlockEntity.setChanged();
        }
        super.stepOn(world, pos, state, entity);
    }

    private static boolean canInsert(NonNullList<ItemStack> inventory, ItemStack stack) {
        boolean var10000;
        if (inventory instanceof ImplementedInventory implementedInventory) {
            if (!implementedInventory.canPlaceItemThroughFace(0, stack, null)) {
                var10000 = false;
                return var10000;
            }
        }
        var10000 = true;
        return var10000;

    }
    private static boolean canMergeItems(ItemStack first, ItemStack second) {
        return first.getCount() <= first.getMaxStackSize() && ItemStack.isSameItemSameComponents(first, second);
    }
}

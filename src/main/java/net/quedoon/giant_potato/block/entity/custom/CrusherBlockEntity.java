package net.quedoon.giant_potato.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.quedoon.giant_potato.GiantPotato;
import net.quedoon.giant_potato.block.ModBlocks;
import net.quedoon.giant_potato.block.entity.ModBlockEntities;
import net.quedoon.giant_potato.block.entity.util.block_entity.mashines.AbstractMashMachineBlockEntity;
import net.quedoon.giant_potato.recipe.CrusherRecipe;
import net.quedoon.giant_potato.recipe.CrusherRecipeInput;
import net.quedoon.giant_potato.recipe.ModRecipes;
import net.quedoon.giant_potato.screen.custom.CrusherScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrusherBlockEntity extends AbstractMashMachineBlockEntity<CrusherRecipe> {
    public CrusherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUSHER_BE, pos, state, 2, 8000);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui."+ GiantPotato.MOD_ID +".crusher");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new CrusherScreenHandler(syncId, playerInventory, worldPosition);
    }

    // // // // // // // // //

    @Override
    public int getMaxProgress() {
        return 72;
    }

    @Override
    public String getName() {
        return "crusher";
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if (!(world.isClientSide())) return;
        GiantPotato.LOGGER.info("We are in the server!");

        int hasCrusherWheels = getCrusherWheels(world, pos, state);
        if (hasCrusherWheels < 2) return;
        GiantPotato.LOGGER.info("Wheels are all here: {}", hasCrusherWheels);

        boolean hasRec = hasRecipe();
        GiantPotato.LOGGER.info("Has recipe = {}", hasRec);
        GiantPotato.LOGGER.info("Can instert? " + (canInsertIntoOutputSlot() ? "yes" : "no"));
        if (hasRecipe() && canInsertIntoOutputSlot()) {
            GiantPotato.LOGGER.info("Got it!");
            setCrusherWheelState(world, pos, state, true);
            increaseCraftingProgress();
            this.active = true;
            setChanged(world, pos, state);

            if (hasCraftingFinished()) {
                GiantPotato.LOGGER.info("Crafted");
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();

            setCrusherWheelState(world, pos, state, false);
            this.active = false;
            setChanged(world, pos, state);
        }
    }


//    protected void craftItem() {
//        ItemStack output = getCurrentRecipe().get().value().output();
//
//        this.removeStack(INPUT_SLOT, 1);
//        this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(),
//                this.getStack(OUTPUT_SLOT).getCount() + output.getCount()));
//    }
//
//
//    protected boolean hasRecipe() {
//        Optional<RecipeEntry<CrusherRecipe>> recipe = getCurrentRecipe();
//        if (recipe.isEmpty()) {
//            return false;
//        }
//
//        ItemStack output = recipe.get().value().getResult(null);
//        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
//    }

    protected Optional<RecipeHolder<CrusherRecipe>> getCurrentRecipe() {
        return this.getLevel().getRecipeManager()
                .getRecipeFor(ModRecipes.CRUSHER_TYPE, new CrusherRecipeInput(inventory.get(0)), this.getLevel());
    }

    @Override
    protected ItemStack getRecipeOutput() {
        return getCurrentRecipe().get().value().output();
    }


    private void setCrusherWheelState(Level world, BlockPos pos, BlockState state, boolean value) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockEntity north = world.getBlockEntity(pos.north(1));
        BlockEntity south = world.getBlockEntity(pos.south(1));
        BlockEntity east = world.getBlockEntity(pos.east(1));
        BlockEntity west = world.getBlockEntity(pos.west(1));

        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            if (east instanceof CrusherWheelBlockEntity eastWheel && west instanceof CrusherWheelBlockEntity westWheel) {
                eastWheel.setActive(value);
                westWheel.setActive(value);
            }
        } else {
            if (north instanceof CrusherWheelBlockEntity northWheel && south instanceof CrusherWheelBlockEntity southWheel) {
                northWheel.setActive(value);
                southWheel.setActive(value);
            }
        }
    }

    private int getCrusherWheels(Level world, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        int wheels = 0;
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            if (world.getBlockState(pos.east(1)).is(ModBlocks.CRUSHER_WHEEL)
                    && world.getBlockState(pos.east(1)).getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.WEST) wheels += 1;
            if (world.getBlockState(pos.west(1)).is(ModBlocks.CRUSHER_WHEEL)
                    && world.getBlockState(pos.west(1)).getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.EAST) wheels += 1;
        } else {
            if (world.getBlockState(pos.north(1)).is(ModBlocks.CRUSHER_WHEEL)
                    && world.getBlockState(pos.north(1)).getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.SOUTH) wheels += 1;
            if (world.getBlockState(pos.south(1)).is(ModBlocks.CRUSHER_WHEEL)
                    && world.getBlockState(pos.south(1)).getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.NORTH) wheels += 1;
        }
        return wheels;
    }

}

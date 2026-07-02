package net.quedoom.giant_potato.block.entity.util.block_entity.mashines;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.quedoom.giant_potato.block.entity.util.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractMachineBlockEntity<T extends Recipe<?>> extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    protected final NonNullList<ItemStack> inventory;
    protected boolean active = Boolean.FALSE;

    protected static final int INPUT_SLOT = 0;
    protected static final int OUTPUT_SLOT = 1;

    protected int progress = 0;
    protected int maxProgress = 72;

    public AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state);
        this.inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }

    public abstract int getMaxProgress();

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        String name = getName();
        super.saveAdditional(nbt, registryLookup);
        ContainerHelper.saveAllItems(nbt, inventory, registryLookup);
        nbt.putInt("giant_potato." + name + ".progress", progress);
        nbt.putInt("giant_potato." + name + ".max_progress", maxProgress);
        nbt.putBoolean("giant_potato." + name + ".active", active);
    }

    public abstract String getName();

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        String name = getName();
        ContainerHelper.loadAllItems(nbt, inventory, registryLookup);
        progress = nbt.getInt("giant_potato." + name + ".progress");
        maxProgress = nbt.getInt("giant_potato." + name + ".max_progress");
        active = nbt.getBoolean("giant_potato." + name + ".active");
        super.loadAdditional(nbt, registryLookup);
    }

    // Tick

    public abstract void tick(Level world, BlockPos pos, BlockState state);



    protected void resetProgress() {
        this.progress = 0;
        this.maxProgress = getMaxProgress();
    }

    protected boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    protected void increaseCraftingProgress() {
        this.progress++;
    }

    protected boolean canInsertIntoOutputSlot() {
        return this.getItem(OUTPUT_SLOT).isEmpty() ||
                this.getItem(OUTPUT_SLOT).getCount() < this.getItem(OUTPUT_SLOT).getMaxStackSize();
    }

//    protected abstract void craftItem();

    protected void craftItem() {
        ItemStack output = getRecipeOutput();

        this.removeItem(INPUT_SLOT, 1);
        this.setItem(OUTPUT_SLOT, new ItemStack(output.getItem(),
                this.getItem(OUTPUT_SLOT).getCount() + output.getCount()));
    }

//    protected abstract boolean hasRecipe();

    protected boolean hasRecipe() {
        Optional<RecipeHolder<T>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().getResultItem(null);
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    protected abstract Optional<RecipeHolder<T>> getCurrentRecipe();
    protected abstract ItemStack getRecipeOutput();


    protected boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getItem(OUTPUT_SLOT).isEmpty() || this.getItem(OUTPUT_SLOT).getItem() == output.getItem();
    }

    protected boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = this.getItem(OUTPUT_SLOT).isEmpty() ? 64 : this.getItem(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = this.getItem(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }
}

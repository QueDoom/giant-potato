package net.quedoon.giant_potato.block.entity.util.block_entity.mashines;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.quedoon.giant_potato.block.entity.util.ImplementedInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractMachineBlockEntity<T extends Recipe<?>> extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    protected final DefaultedList<ItemStack> inventory;
    protected boolean active = Boolean.FALSE;

    protected static final int INPUT_SLOT = 0;
    protected static final int OUTPUT_SLOT = 1;

    protected int progress = 0;
    protected int maxProgress = 72;

    public AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state);
        this.inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    public abstract int getMaxProgress();

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        String name = getName();
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("giant_potato." + name + ".progress", progress);
        nbt.putInt("giant_potato." + name + ".max_progress", maxProgress);
        nbt.putBoolean("giant_potato." + name + ".active", active);
    }

    public abstract String getName();

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        String name = getName();
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("giant_potato." + name + ".progress");
        maxProgress = nbt.getInt("giant_potato." + name + ".max_progress");
        active = nbt.getBoolean("giant_potato." + name + ".active");
        super.readNbt(nbt, registryLookup);
    }

    // Tick

    public abstract void tick(World world, BlockPos pos, BlockState state);



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
        return this.getStack(OUTPUT_SLOT).isEmpty() ||
                this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount();
    }

//    protected abstract void craftItem();

    protected void craftItem() {
        ItemStack output = getRecipeOutput();

        this.removeStack(INPUT_SLOT, 1);
        this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(),
                this.getStack(OUTPUT_SLOT).getCount() + output.getCount()));
    }

//    protected abstract boolean hasRecipe();

    protected boolean hasRecipe() {
        Optional<RecipeEntry<T>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().getResult(null);
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    protected abstract Optional<RecipeEntry<T>> getCurrentRecipe();
    protected abstract ItemStack getRecipeOutput();


    protected boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
    }

    protected boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = this.getStack(OUTPUT_SLOT).isEmpty() ? 64 : this.getStack(OUTPUT_SLOT).getMaxCount();
        int currentCount = this.getStack(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }
}

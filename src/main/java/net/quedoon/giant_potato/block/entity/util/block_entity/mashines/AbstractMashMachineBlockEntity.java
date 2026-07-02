package net.quedoon.giant_potato.block.entity.util.block_entity.mashines;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.quedoon.giant_potato.block.entity.util.ImplementedMashTank;
import net.quedoon.giant_potato.fluid.ModFluids;

public abstract class AbstractMashMachineBlockEntity<T extends Recipe<?>> extends AbstractMachineBlockEntity<T> implements ImplementedMashTank {

    private int MAX_MASH;

    protected final ContainerData propertyDelegate;

    public AbstractMashMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize, int maxMash) {
        super(type, pos, state, inventorySize);
        this.MAX_MASH = maxMash;
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return (int) switch (index) {
                    case 0 -> AbstractMashMachineBlockEntity.this.progress;
                    case 1 -> AbstractMashMachineBlockEntity.this.maxProgress;
                    case 2 -> AbstractMashMachineBlockEntity.this.fluidStorage.getAmount();
                    case 3 -> AbstractMashMachineBlockEntity.this.fluidStorage.getCapacity();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AbstractMashMachineBlockEntity.this.progress = value;
                    case 1 -> AbstractMashMachineBlockEntity.this.maxProgress = value;
                    default -> {
                        return;
                    }
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public SingleVariantStorage<FluidVariant> getMashStorage() {
        return fluidStorage;
    }

    public final SingleVariantStorage<FluidVariant> fluidStorage = ImplementedMashTank.of(MAX_MASH, this);
    public final SingleVariantStorage<FluidVariant> fluidStorageHalf = ImplementedMashTank.of(MAX_MASH, this);

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        String name = getName();
        super.saveAdditional(nbt, registryLookup);
        ContainerHelper.saveAllItems(nbt, inventory, registryLookup);
        nbt.putInt("giant_potato." + name + ".progress", progress);
        nbt.putInt("giant_potato." + name + ".max_progress", maxProgress);
        nbt.putBoolean("giant_potato." + name + ".active", active);
        SingleVariantStorage.writeNbt(this.fluidStorage, FluidVariant.CODEC, nbt, registryLookup);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        String name = getName();
        super.loadAdditional(nbt, registryLookup);
        ContainerHelper.loadAllItems(nbt, inventory, registryLookup);
        progress = nbt.getInt("giant_potato." + name + ".progress");
        maxProgress = nbt.getInt("giant_potato." + name + ".max_progress");
        active = nbt.getBoolean("giant_potato." + name + ".active");
        SingleVariantStorage.readNbt(fluidStorage, FluidVariant.CODEC, FluidVariant::blank, nbt, registryLookup);
    }
}

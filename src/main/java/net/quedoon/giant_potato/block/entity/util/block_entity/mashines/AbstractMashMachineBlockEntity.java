package net.quedoon.giant_potato.block.entity.util.block_entity.mashines;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.math.BlockPos;
import net.quedoon.giant_potato.block.entity.util.ImplementedMashTank;
import net.quedoon.giant_potato.fluid.ModFluids;

public abstract class AbstractMashMachineBlockEntity<T extends Recipe<?>> extends AbstractMachineBlockEntity<T> implements ImplementedMashTank {

    private int MAX_MASH;

    protected final PropertyDelegate propertyDelegate;

    public AbstractMashMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize, int maxMash) {
        super(type, pos, state, inventorySize);
        this.MAX_MASH = maxMash;
        this.propertyDelegate = new PropertyDelegate() {
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
            public int size() {
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
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        String name = getName();
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("giant_potato." + name + ".progress", progress);
        nbt.putInt("giant_potato." + name + ".max_progress", maxProgress);
        nbt.putBoolean("giant_potato." + name + ".active", active);
        SingleVariantStorage.writeNbt(this.fluidStorage, FluidVariant.CODEC, nbt, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        String name = getName();
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("giant_potato." + name + ".progress");
        maxProgress = nbt.getInt("giant_potato." + name + ".max_progress");
        active = nbt.getBoolean("giant_potato." + name + ".active");
        SingleVariantStorage.readNbt(fluidStorage, FluidVariant.CODEC, FluidVariant::blank, nbt, registryLookup);
    }
}

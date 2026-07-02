package net.quedoon.giant_potato.block.entity.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.quedoon.giant_potato.block.entity.util.ImplementedInventory;
import net.quedoon.giant_potato.block.entity.ModBlockEntities;
import net.quedoon.giant_potato.block.entity.fluid.FluidUtils;
import net.quedoon.giant_potato.block.entity.util.ImplementedMashTank;
import net.quedoon.giant_potato.fluid.ModFluids;
import net.quedoon.giant_potato.fluid.util.EmptyableItemContainer;
import net.quedoon.giant_potato.screen.custom.MashTankScreenHandler;
import net.quedoon.giant_potato.util.ModTags;
import org.jetbrains.annotations.Nullable;

public class MashTankBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory, ImplementedMashTank {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);

    protected final ContainerData propertyDelegate;

    public MashTankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MASH_TANK_BE, pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return (int) switch (index) {
                    case 0 -> MashTankBlockEntity.this.fluidStorage.getAmount();
                    case 1 -> MashTankBlockEntity.this.fluidStorage.getCapacity();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public final SingleVariantStorage<FluidVariant> fluidStorage = ImplementedMashTank.of(64, this);
    public final SingleVariantStorage<FluidVariant> fluidStorageHalf = ImplementedMashTank.of(64, this);
    public final SingleVariantStorage<FluidVariant> fluidStorageHalfMinus = ImplementedMashTank.of(64, this);

    @Override
    public SingleVariantStorage<FluidVariant> getMashStorage() {
        return fluidStorage;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Mash Tank");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new MashTankScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    public void tick(Level world1, BlockPos pos, BlockState state1) {
        if (hasFluidStackInFirstSlot()) {
            if (this.fluidStorage.getAmount() < this.fluidStorage.getCapacity() )
                transferFluidToTank();
        }

        if(hasFluidHandlerInSecondSlot()) {
            transferFluidFromTankToHandler();
        }
    }

    private void transferFluidFromTankToHandler() {
        if(inventory.get(1).is(Items.BUCKET)) {
            try(Transaction transaction = Transaction.openOuter()) {
                FluidVariant variant = fluidStorage.variant;
                this.fluidStorage.extract(variant, 1000, transaction);
                setFluidStorages();
                if(variant.isOf(ModFluids.MASH)) {
                    inventory.set(1, new ItemStack(ModFluids.MASH_BUCKET));
                }
                if(variant.isOf(ModFluids.POISONOUS_MASH)) {
                    inventory.set(1, new ItemStack(ModFluids.POISONOUS_MASH_BUCKET));
                }

                transaction.commit();
            }
        }
    }

    private boolean hasFluidHandlerInSecondSlot() {
        return !inventory.get(1).isEmpty() && FluidUtils.isContainer(inventory.get(1))
                && (FluidUtils.isContainerEmpty(inventory.get(1)) || FluidUtils.doesContainerStillHaveSpace(inventory.get(1))) && !fluidStorage.isResourceBlank();
    }

    private void transferFluidToTank() {
       if(inventory.get(0).is(ModTags.Items.MASH_BUCKETS) && (fluidStorage.variant.isOf(ModFluids.MASH) || fluidStorage.isResourceBlank())) {
            try (Transaction transaction = Transaction.openOuter()) {
                this.fluidStorage.insert(FluidVariant.of(ModFluids.MASH), 1000, transaction);
                setFluidStorages();
                setInputSlotToEmptyContainer();
                transaction.commit();
            }
        }
       if(inventory.get(0).is(ModTags.Items.POISONOUS_MASH_BUCKETS) && (fluidStorage.variant.isOf(ModFluids.POISONOUS_MASH) || fluidStorage.isResourceBlank())) {
            try (Transaction transaction = Transaction.openOuter()) {
                this.fluidStorage.insert(FluidVariant.of(ModFluids.POISONOUS_MASH), 1000, transaction);
                setFluidStorages();
                setInputSlotToEmptyContainer();
                transaction.commit();
            }
        }
    }

    private void setFluidStorages() {
        this.fluidStorageHalf.variant = this.fluidStorage.variant;
        this.fluidStorageHalf.amount = this.fluidStorage.amount / 2;
        this.fluidStorageHalfMinus.variant = this.fluidStorage.variant;
        this.fluidStorageHalfMinus.amount = this.fluidStorageHalf.amount - 16000;
        if (this.fluidStorageHalfMinus.amount < 0) {
            this.fluidStorageHalfMinus.amount = 0;
        }
        if (this.fluidStorageHalf.amount > 16000) {
            this.fluidStorageHalf.amount = 16000;
        }
        setChanged();
    }


    private void setInputSlotToEmptyContainer() {
        Item item = this.inventory.get(0).getItem();
        if (item instanceof EmptyableItemContainer itemContainer) {
            this.inventory.set(0, itemContainer.getEmptyItem().getDefaultInstance());
        }
    }


    private boolean hasFluidStackInFirstSlot() {
        return !inventory.get(0).isEmpty() && inventory.get(0).is(ModTags.Items.MASH_BUCKETS) || inventory.get(0).is(ModTags.Items.POISONOUS_MASH_BUCKETS);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        ContainerHelper.saveAllItems(nbt, inventory, registryLookup);
        SingleVariantStorage.writeNbt(this.fluidStorage, FluidVariant.CODEC, nbt, registryLookup);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        ContainerHelper.loadAllItems(nbt, inventory, registryLookup);
        SingleVariantStorage.readNbt(fluidStorage, FluidVariant.CODEC, FluidVariant::blank, nbt, registryLookup);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    public FluidVariant getFluid() {
        return fluidStorage.variant;
    }
}

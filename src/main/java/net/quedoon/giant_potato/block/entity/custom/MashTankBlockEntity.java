package net.quedoon.giant_potato.block.entity.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;

    public MashTankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MASH_TANK_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
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
            public int size() {
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
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Mash Tank");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MashTankScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public void tick(World world1, BlockPos pos, BlockState state1) {
        if (hasFluidStackInFirstSlot()) {
            if (this.fluidStorage.getAmount() < this.fluidStorage.getCapacity() )
                transferFluidToTank();
        }

        if(hasFluidHandlerInSecondSlot()) {
            transferFluidFromTankToHandler();
        }
    }

    private void transferFluidFromTankToHandler() {
        if(inventory.get(1).isOf(Items.BUCKET)) {
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
       if(inventory.get(0).isIn(ModTags.Items.MASH_BUCKETS) && (fluidStorage.variant.isOf(ModFluids.MASH) || fluidStorage.isResourceBlank())) {
            try (Transaction transaction = Transaction.openOuter()) {
                this.fluidStorage.insert(FluidVariant.of(ModFluids.MASH), 1000, transaction);
                setFluidStorages();
                setInputSlotToEmptyContainer();
                transaction.commit();
            }
        }
       if(inventory.get(0).isIn(ModTags.Items.POISONOUS_MASH_BUCKETS) && (fluidStorage.variant.isOf(ModFluids.POISONOUS_MASH) || fluidStorage.isResourceBlank())) {
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
        markDirty();
    }


    private void setInputSlotToEmptyContainer() {
        Item item = this.inventory.get(0).getItem();
        if (item instanceof EmptyableItemContainer itemContainer) {
            this.inventory.set(0, itemContainer.getEmptyItem().getDefaultStack());
        }
    }


    private boolean hasFluidStackInFirstSlot() {
        return !inventory.get(0).isEmpty() && inventory.get(0).isIn(ModTags.Items.MASH_BUCKETS) || inventory.get(0).isIn(ModTags.Items.POISONOUS_MASH_BUCKETS);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        SingleVariantStorage.writeNbt(this.fluidStorage, FluidVariant.CODEC, nbt, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
        SingleVariantStorage.readNbt(fluidStorage, FluidVariant.CODEC, FluidVariant::blank, nbt, registryLookup);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    public FluidVariant getFluid() {
        return fluidStorage.variant;
    }
}

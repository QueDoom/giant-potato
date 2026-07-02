package net.quedoom.giant_potato.screen.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.quedoom.giant_potato.block.entity.custom.FoundryBlockEntity;
import net.quedoom.giant_potato.screen.ModScreenHandlers;

public class FoundryScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    private final FoundryBlockEntity blockEntity;

    public FoundryScreenHandler(int syncId, Inventory inventory, BlockPos pos) {
        this(syncId, inventory, inventory.player.level().getBlockEntity(pos), new SimpleContainerData(3));
    }

    public FoundryScreenHandler(int syncId, Inventory playerInventory,
                                BlockEntity blockEntity, ContainerData arrayPropertyDelegate) {
        super(ModScreenHandlers.FOUNDRY_SCREEN_HANDLER, syncId);
        checkContainerSize((Container) blockEntity, 4);
        this.inventory = (Container) blockEntity;
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((FoundryBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 42, 34));
        this.addSlot(new Slot(inventory, 1, 60, 34));
        this.addSlot(new Slot(inventory, 2, 78, 34));
        this.addSlot(new Slot(inventory, 3, 139, 35));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledArrowProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1); // Max Progress
        int arrowPixelSize = 24; // This is the width in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    public int getScaledMashDisplay() {
        int mash = this.propertyDelegate.get(2);// Mash
        int maxMash = this.blockEntity.getMaxMash();
        int mashDisplayPixelSize = 12; // This is the width in pixels of your bar

        return mash != 0 ? mash * mashDisplayPixelSize / maxMash : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return newStack;
    }


    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.mayPlace(stack) && slot.getContainerSlot() != 3;
    }
}

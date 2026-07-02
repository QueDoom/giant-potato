package net.quedoon.giant_potato.block.entity.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.quedoon.giant_potato.GiantPotato;
import net.quedoon.giant_potato.block.entity.util.ImplementedInventory;
import net.quedoon.giant_potato.block.entity.ModBlockEntities;
import net.quedoon.giant_potato.recipe.FoundryRecipe;
import net.quedoon.giant_potato.recipe.FoundryRecipeInput;
import net.quedoon.giant_potato.recipe.ModRecipes;
import net.quedoon.giant_potato.screen.custom.FoundryScreenHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FoundryBlockEntity extends BlockEntity implements GeoBlockEntity, ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    protected boolean active = Boolean.FALSE;
    private boolean open = Boolean.FALSE;
    private int openTimer = -1;

    private static final int INPUT_SLOT_0 = 0;
    private static final int INPUT_SLOT_1 = 1;
    private static final int INPUT_SLOT_2 = 2;
    private static final int INPUT_SLOTS = 3;
    private static final int OUTPUT_SLOT = 3;

    protected final ContainerData propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;
    private final int DEFAULT_MAX_PROGRESS = 72;
    private int mash = 0;
    private final int MAX_MASH = 200;

    public int getMaxMash() {
        return MAX_MASH;
    }

    protected static final RawAnimation IDLE_CLOSED = RawAnimation.begin().thenLoop("idle_closed");
    protected static final RawAnimation IDLE_OPEN = RawAnimation.begin().thenLoop("idle_open");
    protected static final RawAnimation OPEN = RawAnimation.begin().thenPlay("open").thenLoop("idle_open");
    protected static final RawAnimation ACTIVE = RawAnimation.begin().thenLoop("active");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FoundryBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FOUNDRY_BE, pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FoundryBlockEntity.this.progress;
                    case 1 -> FoundryBlockEntity.this.maxProgress;
                    case 2 -> FoundryBlockEntity.this.mash;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FoundryBlockEntity.this.progress = value;
                    case 1 -> FoundryBlockEntity.this.maxProgress = value;
                    case 2 -> FoundryBlockEntity.this.mash = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        ContainerHelper.saveAllItems(nbt, inventory, registryLookup);
        nbt.putInt("giant_potato.foundry.progress", progress);
        nbt.putInt("giant_potato.foundry.maxProgress", maxProgress);
        nbt.putInt("giant_potato.foundry.mash", mash);
        nbt.putBoolean("giant_potato.foundry.open", open);
        nbt.putBoolean("giant_potato.foundry.active", active);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        ContainerHelper.loadAllItems(nbt, inventory, registryLookup);
        progress = nbt.getInt("giant_potato.foundry.progress");
        maxProgress = nbt.getInt("giant_potato.foundry.maxProgress");
        mash = nbt.getInt("giant_potato.foundry.mash");
        open = nbt.getBoolean("giant_potato.foundry.open");
        active = nbt.getBoolean("giant_potato.foundry.active");

        super.loadAdditional(nbt, registryLookup);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer serverPlayerEntity) {
        return this.worldPosition;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui." + GiantPotato.MOD_ID + ".foundry");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new FoundryScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    public void setOpen() {
        this.setOpenTimer();
        setChanged();
    }


    public boolean getOpen() {
        return this.open;
    }

    public void setOpenTimer(int val) {
        this.openTimer = val;
    }
    public void setOpenTimer() {
        this.openTimer = 30;
    }

    /*
        GECKOLIB STUFF

         */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add((new AnimationController<>(this, "open", 0, state -> PlayState.STOP)).triggerableAnim("open", OPEN));
        controllerRegistrar.add(new AnimationController<>(this, "active", 0, animationState -> {
            if (this.openTimer > 0) return PlayState.STOP;
            if (this.open) {
                if (this.active) {
                    return animationState.setAndContinue(ACTIVE);
                } else {
                    return animationState.setAndContinue(IDLE_OPEN);
                }
            } else {
                return animationState.setAndContinue(IDLE_CLOSED);
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    // // // // // // // // //

    public void tick(Level world, BlockPos pos, BlockState state) {
        if (this.openTimer >= 0) {
            this.open = this.openTimer == 0;
            setChanged();
            this.openTimer--;
        }

        if (!this.open) return;
        if (hasRecipe() && canInsertIntoOutputSlot()) {
            increaseCraftingProgress();
            this.active = true;
            setChanged(world, pos, state);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
            this.active = false;
            setChanged(world, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = DEFAULT_MAX_PROGRESS;
    }

    private void craftItem() {
        Optional<RecipeHolder<FoundryRecipe>> recipe = getCurrentRecipe();

        for (int i = 0; i < INPUT_SLOTS; i++) {
            this.removeItem(i, 1);
        }
        this.setItem(OUTPUT_SLOT, new ItemStack(recipe.get().value().output().getItem(),
                this.getItem(OUTPUT_SLOT).getCount() + recipe.get().value().output().getCount()));
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private boolean canInsertIntoOutputSlot() {
        return this.getItem(OUTPUT_SLOT).isEmpty() ||
                this.getItem(OUTPUT_SLOT).getCount() < this.getItem(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<FoundryRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output();
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    private Optional<RecipeHolder<FoundryRecipe>> getCurrentRecipe() {
        List<ItemStack> input = new ArrayList<>();
        for (int i = 0; i < INPUT_SLOTS; i++) {
            input.add(inventory.get(i));
        }

        return this.getLevel().getRecipeManager()
                .getRecipeFor(ModRecipes.FOUNDRY_TYPE, new FoundryRecipeInput(input), this.getLevel());
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getItem(OUTPUT_SLOT).isEmpty() || this.getItem(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = this.getItem(OUTPUT_SLOT).isEmpty() ? 64 : this.getItem(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = this.getItem(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}

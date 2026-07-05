package net.quedoom.giant_potato.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.quedoom.giant_potato.block.custom.CrusherBlock;
import net.quedoom.giant_potato.block.entity.ModBlockEntities;
import net.quedoom.giant_potato.block.util.ModProperties;
import net.quedoom.giant_potato.recipe.CrusherRecipe;
import net.quedoom.giant_potato.recipe.ModRecipes;
import net.quedoom.giant_potato.util.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import java.util.Optional;

public class CrusherBlockEntity extends BlockEntity implements Container {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    private float randomItemRotation = 180.0f;

    public CrusherBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CRUSHER_BE, blockPos, blockState);
    }

    public void doRecipe() {
        Level level = this.getLevel();
        if (level == null) return;
        BlockPos pos = this.getBlockPos();
        ItemStack stack = inventory.getFirst();


        Optional<RecipeHolder<CrusherRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            if (!stack.isEmpty()) {
                inventory.clear();
                Containers.dropItemStack(level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
            }
        } else {
            ItemStack output = recipe.get().value().getResultItem(null);

            inventory.clear();
            Containers.dropItemStack(level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, output);
        }
    }

    private Optional<RecipeHolder<CrusherRecipe>> getCurrentRecipe() {
        return this.getLevel().getRecipeManager().getRecipeFor(ModRecipes.CRUSHER_TYPE, new SingleRecipeInput(inventory.getFirst()), this.getLevel());
    }

    public float getScaleForRenderItem() {
        Level level = this.getLevel();
        BlockPos pos =  this.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof CrusherBlock)) return 0.125f;
        float crushProgress = state.getValue(ModProperties.CRUSHER_FIRST) / 10f;
        float crushMax = 1f;
        float defaultScale = 0.74f;
        return (defaultScale * (crushMax - crushProgress)) + 0.01f;
    }


    public float getYPosForRenderItem() {
        Level level = this.getLevel();
        BlockPos pos =  this.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof CrusherBlock)) return 0.125f;
        float crushProgress = state.getValue(ModProperties.CRUSHER_FIRST) / 10f;
        float crushMax = 1f;
        float defaultYPos =  0.425f - 0.0625f;
        return (inventory.getFirst().getItem() instanceof BlockItem && !inventory.getFirst().is(ModTags.Items.CRUSHER_RENDER_BLOCK_AS_ITEM)) ? 0.0625f : 0.425f - defaultYPos * crushProgress;
    }

    public Vector2f getXZPosForRenderItem() {
        if (inventory.get(0).getItem() instanceof BlockItem && !inventory.getFirst().is(ModTags.Items.CRUSHER_RENDER_BLOCK_AS_ITEM)) {
            return getXZBlock();
        }
        return new Vector2f(0.5f, 0.5f);
    }

    private Vector2f getXZBlock() {
        if (randomItemRotation == 0.0f) {
            return new Vector2f(0.125f, 0.125f);
        } else if (randomItemRotation == 90.0f) {
            return new Vector2f(0.125F, 0.875F);
        } else if (randomItemRotation == 270.0f) {
            return new Vector2f(0.875F, 0.125F);
        } else {
            return new Vector2f(0.875F, 0.875F);
        }
    }

    public void randomizeRotation() {
        double random = Math.random();
        if  (random <= 0.25) {
            randomItemRotation = 0.0F;
        } else if (random <= 0.5) {
            randomItemRotation = 90.0F;
        } else if (random <= 0.75) {
            randomItemRotation = 180.0F;
        } else {
            randomItemRotation = 270.0F;
        }
    }

    public float getRotationForRenderItem() {
        return randomItemRotation;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        setChanged();
        return inventory.get(i);
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int j) {
        setChanged();
        ItemStack stack = inventory.get(i);
        stack.shrink(j);
        return inventory.set(i, stack);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        setChanged();
        return ContainerHelper.takeItem(inventory, i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        setChanged();
        inventory.set(i, itemStack.copyWithCount(1));
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        setChanged();
        inventory.clear();
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ContainerHelper.saveAllItems(compoundTag, inventory, provider);
        compoundTag.putFloat("crusher_item_rotation", randomItemRotation);

        super.saveAdditional(compoundTag, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ContainerHelper.loadAllItems(compoundTag, inventory, provider);
        randomItemRotation = compoundTag.getFloat("crusher_item_rotation");

        super.loadAdditional(compoundTag, provider);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }



}

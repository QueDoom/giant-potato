package net.quedoom.giant_potato.block.entity.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.quedoom.giant_potato.GiantPotato;
import net.quedoom.giant_potato.block.ModBlocks;
import net.quedoom.giant_potato.block.entity.ModBlockEntities;
import net.quedoom.giant_potato.block.entity.util.ImplementedInventory;
import net.quedoom.giant_potato.block.entity.util.ImplementedMashTank;
import net.quedoom.giant_potato.fluid.ModFluids;
import net.quedoom.giant_potato.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.random.RandomGenerator;

public class MashBowlBlockEntity extends BlockEntity implements ImplementedInventory, ImplementedMashTank {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

    private static final int MASH_PER_POTATO = 4;
    private static final int MASH_PER_POISONOUS_POTATO = 2;


    public MashBowlBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MASH_BOWL_BE, pos, state);
    }

    public final SingleVariantStorage<FluidVariant> fluidStorage = ImplementedMashTank.of(1, this);

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    public void jumpedOn(Level world, BlockState state, BlockPos pos, Entity entity) {
        if (!(entity.isAlive() || entity instanceof IronGolem)) return;
        if (!world.isClientSide()) {
            System.out.println("I got jumpied on, ouch!");
            if (this.inventory.getFirst().is(ModTags.Items.MASH_BOWL_POTATO) || this.inventory.getFirst().is(ModTags.Items.MASH_BOWL_POISONOUS_POTATO)) {
                ItemStack stack = this.inventory.getFirst();
                Fluid mashType = this.getFluidFromPotato(stack);
                int mashAmount = this.getFluidAmountFromPotato(stack);
                if (mashType != null && this.fluidStorage.amount + (stack.is(ModTags.Items.MASH_BUCKETS) ? MASH_PER_POTATO : MASH_PER_POISONOUS_POTATO) <= this.fluidStorage.getCapacity()) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        stack.shrink(1);
                        this.fluidStorage.insert(FluidVariant.of(mashType), mashAmount, transaction);
                        transaction.commit();
                    }

                }
                world.playLocalSound(pos, SoundEvents.SLIME_JUMP_SMALL, SoundSource.BLOCKS, 1, 1, true);
            }
        } else {
            spawnSplashParticles(world,  pos, true);
        }
    }

    private int getFluidAmountFromPotato(ItemStack stack) {
        if (stack.is(ModTags.Items.MASH_BOWL_POTATO)) {
            return MASH_PER_POTATO;
        } else if (stack.is(ModTags.Items.MASH_BOWL_POISONOUS_POTATO)) {
            return MASH_PER_POISONOUS_POTATO;
        }
        return 0;
    }

    private void spawnSplashParticles(Level world, BlockPos pos, boolean big) {
        double xPos = pos.getX() + 0.5f;
        double yPos = pos.getY() + 0.4f;
        double zPos = pos.getZ() + 0.5f;
        double offset;
        double xVel;
        double yVel;
        double zVel;
        for (int i = big ? 12 : 4; i > 0; i--) {
            offset = RandomGenerator.getDefault().nextDouble() * 0.6 - 0.3;
            xVel = RandomGenerator.getDefault().nextDouble() * 0.4 - 0.1;
            yVel = RandomGenerator.getDefault().nextDouble() * 0.4 - 0.1;
            zVel = RandomGenerator.getDefault().nextDouble() * 0.4 - 0.1;

            world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.MASH_TEST.defaultBlockState()), xPos + offset, yPos + offset, zPos + offset, xVel, yVel, zVel);
        }
        if (big) {
            for (int i = 4; i > 0; i--) {
                offset = RandomGenerator.getDefault().nextDouble() * 0.6 - 0.3;
                xVel = RandomGenerator.getDefault().nextDouble() * 0.4;
                yVel = RandomGenerator.getDefault().nextDouble() * 0.4;
                zVel = RandomGenerator.getDefault().nextDouble() * 0.4;

                world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.MASH_TEST.defaultBlockState()), xPos + offset, yPos + offset, zPos + offset, xVel, yVel, zVel);
            }
        }


    }

    public boolean canPlaceItemC(ItemStack stack) {
        GiantPotato.LOGGER.info("MASH BOWL: can place start");
        ItemStack itemStack = inventory.getFirst();
        ItemStack stackNormalized = stack.copyWithCount(1);
        ItemStack itemStackNormalized = itemStack.copyWithCount(1);
        GiantPotato.LOGGER.info("MASH BOWL: is same? : {}", ItemStack.isSameItemSameComponents(stackNormalized, itemStackNormalized)  || itemStack.isEmpty());
        return ItemStack.isSameItemSameComponents(stackNormalized, itemStackNormalized) || itemStack.isEmpty();
    }

    public int placeItem(ItemStack stack) {
        ItemStack itemStack = inventory.getFirst();
        int invAmount = itemStack.getCount();
        int stackAmount = stack.getCount();
        int maxCount = itemStack.getMaxStackSize();
        int stackAndInv = stackAmount + invAmount;
        int mathSlot = stackAndInv - maxCount;
        GiantPotato.LOGGER.info("MASH BOWL: LOGIC START");

        if (canPlaceItemC(stack)) {
            if (mathSlot <= 0) {
                inventory.set(0, stack.copyWithCount(stackAndInv));
                GiantPotato.LOGGER.info("MASH BOWL: {}", stackAmount);
                return stackAmount;
            } else {
                inventory.set(0, stack.copyWithCount(maxCount));
                GiantPotato.LOGGER.info("MASH BOWL: 64");
                return stackAmount - mathSlot;
            }
        }
        GiantPotato.LOGGER.info("MASH BOWL: 0");
        return 0;
    }


    @Nullable
    private Fluid getFluidFromPotato(ItemStack stack) {
        if (stack.is(ModTags.Items.MASH_BOWL_POTATO)) {
            return ModFluids.MASH;
        } else if (stack.is(ModTags.Items.MASH_BOWL_POISONOUS_POTATO)) {
            return ModFluids.POISONOUS_MASH;
        }
        return null;
    }


    private void transferFluidToTank(int amount, Fluid type) {
        if((fluidStorage.variant.isOf(type) || fluidStorage.isResourceBlank())) {
            try (Transaction transaction = Transaction.openOuter()) {
                this.fluidStorage.insert(FluidVariant.of(type), amount, transaction);
                transaction.commit();
            }
        }
    }



    @Override
    public SingleVariantStorage<FluidVariant> getMashStorage() {
        return fluidStorage;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ContainerHelper.saveAllItems(compoundTag, inventory, provider);
        SingleVariantStorage.writeNbt(fluidStorage, FluidVariant.CODEC, compoundTag, provider);

        super.saveAdditional(compoundTag, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ContainerHelper.loadAllItems(compoundTag, inventory, provider);
        SingleVariantStorage.readNbt(fluidStorage, FluidVariant.CODEC, FluidVariant::blank, compoundTag, provider);

        super.loadAdditional(compoundTag, provider);
    }
}

package net.quedoon.giant_potato.block.entity.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.quedoon.giant_potato.block.entity.util.ImplementedInventory;
import net.quedoon.giant_potato.fluid.ModFluids;
import net.quedoon.giant_potato.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.random.RandomGenerator;

public class MashBowlBlockEntity extends BlockEntity implements ImplementedInventory {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

    private static final int MASH_PER_POTATO = 4;
    private static final int MASH_PER_POISONOUS_POTATO = 2;


    public MashBowlBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return (FluidConstants.BUCKET / 81); // 1 Bucket = 81000 Droplets = 1000mB || * 64 ==> 64,000mB = 64 Buckets
        }

        @Override
        protected void onFinalCommit() {
            setChanged();
            getLevel().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    };

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    public void jumpedOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
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

            world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, ModFluids.MASH_BLOCK.defaultBlockState()), xPos + offset, yPos + offset, zPos + offset, xVel, yVel, zVel);
        }
        if (big) {
            for (int i = 4; i > 0; i--) {
                offset = RandomGenerator.getDefault().nextDouble() * 0.6 - 0.3;
                xVel = RandomGenerator.getDefault().nextDouble() * 0.4;
                yVel = RandomGenerator.getDefault().nextDouble() * 0.4;
                zVel = RandomGenerator.getDefault().nextDouble() * 0.4;

                world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, ModFluids.MASH_BLOCK.defaultBlockState()), xPos + offset, yPos + offset, zPos + offset, xVel, yVel, zVel);
            }
        }


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
}

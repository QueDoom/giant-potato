package net.quedoon.giant_potato.block.entity.util;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.quedoon.giant_potato.fluid.ModFluids;
import net.quedoon.giant_potato.util.ModTags;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ImplementedMashTank {

    SingleVariantStorage<FluidVariant> getMashStorage();

    default FluidVariant getFluid() {
        return getMashStorage().variant;
    }
    default long getCapacity() {
        return getMashStorage().getCapacity();
    }

    static SingleVariantStorage<FluidVariant> of(int CAPACITY, @NotNull BlockEntity blockEntity) {
        return new SingleVariantStorage<FluidVariant>() {
            @Override
            protected FluidVariant getBlankVariant() {
                return FluidVariant.blank();
            }

            @Override
            protected long getCapacity(FluidVariant variant) {
                return (FluidConstants.BUCKET / 81) * CAPACITY; // 1 Bucket = 81000 Droplets = 1000mB || * 64 ==> 64,000mB = 64 Buckets
            }

            @Override
            protected void onFinalCommit() {
                blockEntity.markDirty();
                blockEntity.getWorld().updateListeners(blockEntity.getPos(), blockEntity.getCachedState(), blockEntity.getCachedState(), 3);
            }

            @Override
            protected boolean canInsert(FluidVariant variant) {
                return variant.isOf(ModFluids.MASH) || variant.isOf(ModFluids.POISONOUS_MASH);
            }
        };
    }


}

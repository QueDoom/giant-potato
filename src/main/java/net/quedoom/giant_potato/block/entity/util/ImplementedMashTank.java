package net.quedoom.giant_potato.block.entity.util;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.quedoom.giant_potato.fluid.ModFluids;
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
                blockEntity.setChanged();
                blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
            }

            @Override
            protected boolean canInsert(FluidVariant variant) {
                return variant.isOf(ModFluids.MASH) || variant.isOf(ModFluids.POISONOUS_MASH);
            }
        };
    }


}

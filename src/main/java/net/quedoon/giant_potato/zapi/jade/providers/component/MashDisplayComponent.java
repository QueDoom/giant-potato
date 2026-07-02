package net.quedoon.giant_potato.zapi.jade.providers.component;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.quedoon.giant_potato.block.entity.custom.MashTankBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.text.NumberFormat;

public class MashDisplayComponent implements IBlockComponentProvider {
    private static final NumberFormat nf = NumberFormat.getIntegerInstance();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockEntity blockEntity = blockAccessor.getBlockEntity();
//        if (blockEntity instanceof ImplementedInventory blockEntityWithImplementedInventory) {
//            if (blockEntityWithImplementedInventory.getStack(0).)
        if (blockEntity instanceof MashTankBlockEntity mashTankBlockEntity)
            iTooltip.add(Component.translatable("component.giant_potato.mash", nf.format(mashTankBlockEntity.fluidStorage.getAmount()), nf.format(mashTankBlockEntity.fluidStorage.getCapacity())));
//        }
    }

    @Override
    public ResourceLocation getUid() {
        return null;
    }
}

package net.quedoom.giant_potato.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.quedoom.giant_potato.util.ModTags;

public class MidasHandItem extends Item {
    public MidasHandItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();
        if (world.isClientSide()) return InteractionResult.FAIL;
        if (player == null) return InteractionResult.FAIL;
        if (state.is(ModTags.Blocks.MIDAS_HAND_WORKS_ON)) {
            world.setBlockAndUpdate(pos, Blocks.GOLD_BLOCK.defaultBlockState());
            stack.hurtAndBreak(1, ((ServerLevel) world), ((ServerPlayer) player), item -> {
                player.onEquippedItemBroken(stack.getItem(), EquipmentSlot.MAINHAND);
            });
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}

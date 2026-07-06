package net.quedoom.giant_potato.block.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record PosDirection(BlockPos originPos, Direction direction) {

    public BlockState getOrigin(Level world) {
        return world.getBlockState(originPos);
    }

    public BlockState getState(Level world) {
        return world.getBlockState(originPos.relative(direction));
    }

}

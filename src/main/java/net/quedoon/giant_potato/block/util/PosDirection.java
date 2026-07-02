package net.quedoon.giant_potato.block.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PosDirection {
    private final BlockPos POS;
    private final Direction DIRECTION;


    public PosDirection(BlockPos posOrigin, Direction direction) {
        this.POS = posOrigin;
        this.DIRECTION = direction;
    }

    public BlockPos getOriginPosition() {
        return POS;
    }

    public BlockState getOrigin(Level world) {
        return world.getBlockState(POS);
    }

    public Direction getDirection() {
        return DIRECTION;
    }

    public BlockState getState(Level world) {
        return world.getBlockState(POS.relative(DIRECTION));
    }

}
